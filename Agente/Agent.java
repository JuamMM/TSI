package tracks.singlePlayer.advanced.Agente;

import java.awt.desktop.SystemSleepEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.DeflaterInputStream;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;
import tools.Pair;
import tools.Vector2d;

public class Agent extends AbstractPlayer {

    Vector2d abajo = new Vector2d(0,1);
    Vector2d arriba = new Vector2d(0,-1);
    Vector2d derecha = new Vector2d(1,0);
    Vector2d izquierda = new Vector2d(-1,0);
    int gemas=0;


    Vector2d coordenadas_objetivo;

    Vector2d fescala;
    Vector2d portal;
    Vector2d avatar;

    ArrayList<Integer> recorrer_diamantes = new ArrayList<>();
    ArrayList<NodoDiamante> AbiertosDiamantes = new ArrayList<>();
    ArrayList<NodoDiamante> CerradosDiamantes = new ArrayList<>();

    ArrayList<Observation>[] Diamantes;
    ArrayList<Node> Abiertos = new ArrayList<Node>();
    ArrayList<Node> Cerrados = new ArrayList<Node>();
    ArrayList<Observation> mapa[][];


    /**
     * initialize all variables for the agent
     *
     * @param stateObs     Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     */

    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){

        mapa = stateObs.getObservationGrid();
        fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length ,stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);
        ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
        //Seleccionamos el portal mas proximo
        portal = posiciones[0].get(0).position;

        coordenadas_objetivo = portal;
        coordenadas_objetivo.x = portal.x;
        coordenadas_objetivo.y = portal.y;

        Diamantes = stateObs.getResourcesPositions(stateObs.getAvatarPosition());
        
        if(Diamantes != null){
            recorrer_diamantes = OrdenDiamantes(stateObs,elapsedTimer);
        }
        else{
            gemas = 20;
        }
    }

    public void init (StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {}

    public ArrayList<Integer> OrdenDiamantes(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
        ArrayList<Integer> vector = new ArrayList<>();
        vector.add(0);
        double Distancia_acumulada = (Math.abs(Diamantes[0].get(0).position.x - stateObs.getAvatarPosition().x) + Math.abs(Diamantes[0].get(0).position.y - stateObs.getAvatarPosition().y));
        Boolean noTerminado = true;

        NodoDiamante nodo = new NodoDiamante(vector,Distancia_acumulada);

        ArrayList<Integer> solucion = new ArrayList<>();

        AbiertosDiamantes.add(nodo);

        while(noTerminado){

            NodoDiamante nodo_explorado = AbiertosDiamantes.get(0);

            AbiertosDiamantes.remove(0);

            CerradosDiamantes.add(nodo_explorado);

            if(nodo_explorado.getVector().size() == 10){
                noTerminado = false;
                solucion = nodo_explorado.getVector();
            }

            generarSolucionesDiamantes(nodo_explorado);
        }

        return solucion;
    }

    public Boolean CompruebaObstaculo(int pox, int poy){

        if(pox<0 || poy>=mapa.length) return true;
        if(poy<0 || poy>=mapa[pox].length) return true;

        for(Observation obs : mapa[pox][poy])
        {
            if(obs.itype == 0) {
                return true;
            }
        }

        return false;
    }

    private void Introduce(ArrayList<Node> lista, Node introducido){
        if (lista.contains(introducido)){
            if(lista.get(lista.indexOf(introducido)).getCost() > introducido.getCost()){
                lista.remove(introducido);
                lista.add(introducido);
            }
        }
        else{
            lista.add(introducido);
        }
    }

    private void generarSolucionesDiamantes(NodoDiamante explorado){
        double coste_base = explorado.getCost();
        int indice_ultimo =  explorado.getVector().get(explorado.getVector().size()-1);
        ArrayList<Integer> vector_nodo;

        ArrayList<Integer> DiamantesNoAsignados = new ArrayList<>();

        for(int i=0; i<Diamantes[0].size(); i++){
            Boolean introducir = true;
            for(int e=0; e<explorado.getVector().size(); e++){
                if(i == explorado.getVector().get(e)){
                    introducir=false;
                }
            }
            if(introducir){
                DiamantesNoAsignados.add(i);
            }
        }

        for(int i=0; i<DiamantesNoAsignados.size(); i++){
            vector_nodo = (ArrayList<Integer>) explorado.getVector().clone();
            int indice_aniadido = DiamantesNoAsignados.get(i);
            vector_nodo.add(indice_aniadido);
            double coste_nuevo = Math.abs(Diamantes[0].get(indice_aniadido).position.x - Diamantes[0].get(indice_ultimo).position.x) + Math.abs(Diamantes[0].get(indice_aniadido).position.y - Diamantes[0].get(indice_ultimo).position.y);
            NodoDiamante introducir = new NodoDiamante(vector_nodo, (coste_base + coste_nuevo)/explorado.getVector().size());

            AbiertosDiamantes.add(introducir);
        }

        AbiertosDiamantes.sort(Comparator.comparing(NodoDiamante::getCost));

    }

    private void generarSoluciones(StateObservation stateObs, Node explorado){
        Vector2d casilla = explorado.position;

        if(!CompruebaObstaculo((int)casilla.x,(int)casilla.y-1)){
            //Comprobar Arriba
            Vector2d coordenadasNodo = new Vector2d(casilla.x, casilla.y-1);
            double coste = (Math.abs(coordenadasNodo.x - coordenadas_objetivo.x) + Math.abs(coordenadasNodo.y-coordenadas_objetivo.y)) +1;

            if(explorado.orientacion != 0){
                coste+=1;
            }

            Node nodo = new Node(coordenadasNodo, 0,Math.abs(coordenadasNodo.x - coordenadas_objetivo.x) + Math.abs(coordenadasNodo.y-coordenadas_objetivo.y), coste, ACTIONS.ACTION_UP);
            nodo.setMoveDir(explorado);

            Introduce(Abiertos, nodo);
        }
        if(!CompruebaObstaculo((int)casilla.x,(int)casilla.y+1)){
            //Comprobar Abajo
            Vector2d coordenadasNodo = new Vector2d(casilla.x, casilla.y+1);
            double coste = (Math.abs(coordenadasNodo.x - coordenadas_objetivo.x) + Math.abs(coordenadasNodo.y-coordenadas_objetivo.y)) +1;

            if(explorado.orientacion != 2){
                coste+=1;
            }

            Node nodo = new Node(coordenadasNodo, 2,Math.abs(coordenadasNodo.x - coordenadas_objetivo.x) + Math.abs(coordenadasNodo.y-coordenadas_objetivo.y), coste, ACTIONS.ACTION_DOWN);
            nodo.setMoveDir(explorado);

            Introduce(Abiertos, nodo);

        }
        if(!CompruebaObstaculo((int)casilla.x-1,(int)casilla.y)){
            //Comprobar Izquierda
            Vector2d coordenadasNodo = new Vector2d(casilla.x-1, casilla.y);
            double coste = (Math.abs(coordenadasNodo.x - coordenadas_objetivo.x) + Math.abs(coordenadasNodo.y-coordenadas_objetivo.y)) + 1;

            if(explorado.orientacion != 3){
                coste+=1;
            }

            Node nodo = new Node(coordenadasNodo, 3, Math.abs(coordenadasNodo.x - coordenadas_objetivo.x) + Math.abs(coordenadasNodo.y-coordenadas_objetivo.y), coste, ACTIONS.ACTION_LEFT);
            nodo.setMoveDir(explorado);

            Introduce(Abiertos, nodo);

        }
        if(!CompruebaObstaculo((int)casilla.x+1,(int)casilla.y)){
            //Comprobar Derecha
            Vector2d coordenadasNodo = new Vector2d(casilla.x+1, casilla.y-1);
            double coste = (Math.abs(coordenadasNodo.x - coordenadas_objetivo.x) + Math.abs(coordenadasNodo.y-coordenadas_objetivo.y)) + 1;

            if(explorado.orientacion != 1){
                coste+=1;
            }

            Node nodo = new Node(coordenadasNodo, 1, Math.abs(coordenadasNodo.x - coordenadas_objetivo.x) + Math.abs(coordenadasNodo.y-coordenadas_objetivo.y), coste, ACTIONS.ACTION_RIGHT);
            nodo.setMoveDir(explorado);

            Introduce(Abiertos, nodo);

        }

        Abiertos.sort(Comparator.comparing(Node::getCost));


    }

    @Override
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        Boolean ObjetivoNoEncontrado = true;

        Types.ACTIONS accion_devolver = ACTIONS.ACTION_NIL;

        avatar =  new Vector2d(stateObs.getAvatarPosition().x / fescala.x,stateObs.getAvatarPosition().y / fescala.y);

       if(gemas < 10){
            coordenadas_objetivo = Diamantes[0].get(0).position;
            coordenadas_objetivo.x = Math.floor(coordenadas_objetivo.x / fescala.x);
            coordenadas_objetivo.y = Math.floor(coordenadas_objetivo.y / fescala.y);

            if(avatar.x == coordenadas_objetivo.x && avatar.y == coordenadas_objetivo.y){
                gemas++;
            }

        }
        else {
            ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
            //Seleccionamos el portal mas proximo
            portal = posiciones[0].get(0).position;
            portal.x = Math.floor(portal.x / fescala.x);
            portal.y = Math.floor(portal.y / fescala.y);

            coordenadas_objetivo = portal;
            coordenadas_objetivo.x = portal.x;
            coordenadas_objetivo.x = portal.x;
        }

        int orien = 0;
        if(stateObs.getAvatarOrientation() == abajo){
            orien = 2;
        }
        else if(stateObs.getAvatarOrientation() == derecha){
            orien = 1;
        }
        else if(stateObs.getAvatarOrientation() == izquierda){
            orien = 3;
        }

        Node nodo = new Node(avatar, orien, Math.abs(avatar.x - portal.x) + Math.abs(avatar.y-portal.y), Math.abs(avatar.x - portal.x) + Math.abs(avatar.y-portal.y), ACTIONS.ACTION_NIL);
        Node explorado = nodo;

        Cerrados.clear();

        Abiertos.add(nodo);
        while(ObjetivoNoEncontrado){

            explorado = Abiertos.get(0);

            Abiertos.remove(0);

            if(Cerrados.contains(explorado)){
                
            }
            else {

                Cerrados.add(explorado);

                if (explorado.position.x == coordenadas_objetivo.x && explorado.position.y == coordenadas_objetivo.y) {
                    ObjetivoNoEncontrado = false;
                }

                generarSoluciones(stateObs, explorado);

            }

        }

        while(explorado.tienePadre()){
            System.out.println(accion_devolver);
            accion_devolver = explorado.accion;
            explorado = explorado.parent;
        }

        Abiertos.clear();

        return accion_devolver;

    }

}
