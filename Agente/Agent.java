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
    int gemas=20;

    Boolean sobrevivir = false;

    Boolean gemas_sobrevivir = false;

    Vector2d coordenadas_objetivo;

    Vector2d fescala;
    Vector2d portal;
    Vector2d avatar;

    ArrayList<Vector2d> recorrer_diamantes = new ArrayList<>();

    ArrayList<Observation>[] enemigos;

    ArrayList<Vector2d> Diamantes = new ArrayList<>();
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
        
        if(stateObs.getResourcesPositions(stateObs.getAvatarPosition()) != null){
            OrdenDiamantes(stateObs,elapsedTimer);
            gemas = 0;

            coordenadas_objetivo = recorrer_diamantes.get(gemas);
            coordenadas_objetivo.x = Math.floor(coordenadas_objetivo.x / fescala.x);
            coordenadas_objetivo.y = Math.floor(coordenadas_objetivo.y / fescala.y);
        }
        else{
            gemas = 20;
        }

        if(stateObs.getNPCPositions() != null){
            sobrevivir = true;
        }

        if(gemas == 0 && sobrevivir){
            gemas_sobrevivir = true;
        }
    }

    public void init (StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {}

    public void OrdenDiamantes(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
        Vector2d posicion1 = new Vector2d();
        Vector2d posicion2 = new Vector2d();
        double distancia_minima = 10000000;
        ArrayList<Observation>[] distanciasDia;
        for(int i=0; i< stateObs.getResourcesPositions(stateObs.getAvatarPosition())[0].size(); i++){
            Diamantes.add(stateObs.getResourcesPositions(stateObs.getAvatarPosition())[0].get(i).position);
        }
        //Buscamos la pareja de diamantes mas cercana entre ellos
        for(int i=0; i<Diamantes.size(); i++){
            for(int e=i+1; e<Diamantes.size(); e++){
                if(distancia_minima > (Math.abs(Diamantes.get(i).x - Diamantes.get(e).x) + Math.abs(Diamantes.get(i).y - Diamantes.get(e).y))){
                    posicion1 = Diamantes.get(i);
                    posicion2 = Diamantes.get(e);
                    distancia_minima = (Math.abs(Diamantes.get(i).x - Diamantes.get(e).x) + Math.abs(Diamantes.get(i).y - Diamantes.get(e).y));
                }
            }
        }
        //Continuamos las asignaciones
        recorrer_diamantes.add(posicion1);
        recorrer_diamantes.add(posicion2);

        Diamantes.remove(posicion1);
        Diamantes.remove(posicion2);

        while(recorrer_diamantes.size() < 10){
            Boolean aniadirFinal = false;
            Vector2d primeaposicion = new Vector2d();
            Vector2d ultimaposicion = new Vector2d();
            double distancia_minima_ini = 100000;
            double distancia_minima_fin = 100000;
            posicion1 = recorrer_diamantes.get(0);
            posicion2 = recorrer_diamantes.get(recorrer_diamantes.size()-1);

            for(int i=0; i<Diamantes.size(); i++){

                if((Math.abs(Diamantes.get(i).x - posicion1.x) + Math.abs(Diamantes.get(i).y - posicion1.y)) < distancia_minima_ini){
                    distancia_minima_ini= (Math.abs(Diamantes.get(i).x - posicion1.x) + Math.abs(Diamantes.get(i).y - posicion1.y));
                    primeaposicion = Diamantes.get(i);
                }

                if((Math.abs(Diamantes.get(i).x - posicion2.x) + Math.abs(Diamantes.get(i).y - posicion2.y)) < distancia_minima_fin){
                    distancia_minima_fin= (Math.abs(Diamantes.get(i).x - posicion2.x) + Math.abs(Diamantes.get(i).y - posicion2.y));
                    ultimaposicion = Diamantes.get(i);
                }

            }

            if(distancia_minima_fin < distancia_minima_ini){
                recorrer_diamantes.add(ultimaposicion);
                Diamantes.remove(ultimaposicion);
            }
            else{
                recorrer_diamantes.add(0,primeaposicion);
                Diamantes.remove(primeaposicion);
            }

        }
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

    public int Peligro(StateObservation stateObs, Vector2d coord){
        int gradoPeligro = 0;
        enemigos = stateObs.getNPCPositions();
        for(int a=0; a<enemigos[0].size(); a++){
            Vector2d enemigo = enemigos[0].get(a).position;
            enemigo.x = Math.floor(enemigo.x / fescala.x);
            enemigo.y = Math.floor(enemigo.y / fescala.y);
            enemigo.x -= 5;
            enemigo.y -= 5;

            for(int i=0; i<=10; i++){
                for(int e=0; e<=10; e++){

                    if(enemigo.x+i == coord.x && enemigo.y+e == coord.y){
                        if(i == 0 || i == 10 || e == 0 || e == 10){
                            gradoPeligro+=2;
                        }
                        else if(i == 1 || i == 9 || e == 1 || e == 9){
                            gradoPeligro+=8;
                        }
                        else if(i==2 || i ==8 || e == 2 || e== 8){
                            gradoPeligro+=32;
                        }
                        else if( i== 3 || i == 7 || e == 3 || e == 7){
                            gradoPeligro+=128;
                        }
                        else  if( i == 4 || i == 6 || e == 4 || e == 6){
                            gradoPeligro+=512;
                        }
                        else{
                            gradoPeligro+=1000;
                        }
                    }
                }
            }

        }
        return gradoPeligro;
    }

    double CercanoAlMuro(Vector2d casilla) {
        double devolver = 0;

        if (CompruebaObstaculo((int)casilla.x + 1, (int)casilla.y)) {
            devolver+=1;
        }

        if (CompruebaObstaculo((int)casilla.x - 1, (int)casilla.y)) {
            devolver+=1;
        }

        if (CompruebaObstaculo((int)casilla.x, (int)casilla.y + 1)){
            devolver+=1;
        }

        if(CompruebaObstaculo((int)casilla.x,(int)casilla.y - 1)) {
            devolver+=1;
        }


        return devolver;
    }

    @Override
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        Boolean ObjetivoNoEncontrado = true;

        Types.ACTIONS accion_devolver = ACTIONS.ACTION_NIL;

        avatar =  new Vector2d(stateObs.getAvatarPosition().x / fescala.x,stateObs.getAvatarPosition().y / fescala.y);

        int orien = 0;

        if (stateObs.getAvatarOrientation() == abajo) {
            orien = 2;
        } else if (stateObs.getAvatarOrientation() == derecha) {
            orien = 1;
        } else if (stateObs.getAvatarOrientation() == izquierda) {
            orien = 3;
        }

        

        if(gemas_sobrevivir){

            if(Peligro(stateObs,avatar) == 0) {
                sobrevivir = false;
            }
            else{
                sobrevivir = true;
            }

        }
        else if(stateObs.getNPCPositions() == null){
            sobrevivir = false;
        }
        else{
            sobrevivir = true;
        }

        if(!sobrevivir) {

            if (avatar.x == coordenadas_objetivo.x && avatar.y == coordenadas_objetivo.y) {
                gemas++;

                if (gemas < 10) {
                    coordenadas_objetivo = recorrer_diamantes.get(gemas);
                    coordenadas_objetivo.x = Math.floor(coordenadas_objetivo.x / fescala.x);
                    coordenadas_objetivo.y = Math.floor(coordenadas_objetivo.y / fescala.y);
                }
            }

            if (gemas >= 10) {

                ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
                //Seleccionamos el portal mas proximo
                portal = posiciones[0].get(0).position;
                portal.x = Math.floor(portal.x / fescala.x);
                portal.y = Math.floor(portal.y / fescala.y);

                coordenadas_objetivo = portal;
                coordenadas_objetivo.x = portal.x;
                coordenadas_objetivo.x = portal.x;
            }

            Node nodo = new Node(avatar, orien, Math.abs(avatar.x - coordenadas_objetivo.x) + Math.abs(avatar.y - coordenadas_objetivo.y), Math.abs(avatar.x - coordenadas_objetivo.x) + Math.abs(avatar.y - coordenadas_objetivo.y), ACTIONS.ACTION_NIL);
            Node explorado = nodo;

            Cerrados.clear();

            Abiertos.add(nodo);
            while (ObjetivoNoEncontrado) {

                explorado = Abiertos.get(0);

                Abiertos.remove(0);

                if (Cerrados.contains(explorado)) {

                } else {

                    Cerrados.add(explorado);

                    if (explorado.position.x == coordenadas_objetivo.x && explorado.position.y == coordenadas_objetivo.y) {
                        ObjetivoNoEncontrado = false;
                    }

                    generarSoluciones(stateObs, explorado);

                }

            }

            while (explorado.tienePadre()) {
                accion_devolver = explorado.accion;
                explorado = explorado.parent;
            }

            Abiertos.clear();

            return accion_devolver;

        }
        else{
            double peligro_minimo = 10000;

            if(Peligro(stateObs,avatar) + CercanoAlMuro(avatar) > 0){
                double peligro;

                if(!CompruebaObstaculo((int)avatar.x,(int)avatar.y-1)){
                    //Comprobar Arriba
                    Vector2d coordenadasNodo = new Vector2d(avatar.x, avatar.y-1);
                    peligro = Peligro(stateObs,coordenadasNodo);

                    peligro += CercanoAlMuro(coordenadasNodo);
                    
                    if(peligro_minimo > peligro){
                        peligro_minimo = peligro;
                        accion_devolver = ACTIONS.ACTION_UP;
                    }
                    
                }
                if(!CompruebaObstaculo((int)avatar.x,(int)avatar.y+1)){
                    //Comprobar Abajo
                    Vector2d coordenadasNodo = new Vector2d(avatar.x, avatar.y+1);
                    peligro = Peligro(stateObs,coordenadasNodo);

                    peligro += CercanoAlMuro(coordenadasNodo);

                    if(peligro_minimo > peligro){
                        peligro_minimo = peligro;
                        accion_devolver = ACTIONS.ACTION_DOWN;
                    }


                }
                if(!CompruebaObstaculo((int)avatar.x-1,(int)avatar.y)){
                    //Comprobar Izquierda
                    Vector2d coordenadasNodo = new Vector2d(avatar.x-1, avatar.y);
                    peligro = Peligro(stateObs,coordenadasNodo);

                    peligro += CercanoAlMuro(coordenadasNodo);

                    if(peligro_minimo > peligro){
                        peligro_minimo = peligro;
                        accion_devolver = ACTIONS.ACTION_LEFT;
                    }


                }
                if(!CompruebaObstaculo((int)avatar.x+1,(int)avatar.y)){
                    //Comprobar Derecha
                    Vector2d coordenadasNodo = new Vector2d(avatar.x+1, avatar.y-1);
                    peligro = Peligro(stateObs,coordenadasNodo);

                    peligro += CercanoAlMuro(coordenadasNodo);

                    if(peligro_minimo > peligro){
                        accion_devolver = ACTIONS.ACTION_RIGHT;
                    }

                }

                return accion_devolver;
                
            }

            return accion_devolver;
        }
    }

}
