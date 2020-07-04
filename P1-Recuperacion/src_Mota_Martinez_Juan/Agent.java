package tracks.singlePlayer.src_Mota_Martinez_Juan;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;
import tools.com.google.gson.internal.bind.ArrayTypeAdapter;


import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;

public class Agent extends AbstractPlayer{

    boolean PortalEncontrado = false;
    Vector2d fescala;
    Vector2d portal;
    Vector2d avatar;
    Stack<Types.ACTIONS> plan = new Stack<>();
    ArrayList<Types.ACTIONS> acciones;

    ArrayList<Observation>[][] grid;


    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length,
                stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);

        ArrayList<Observation>[] salida = stateObs.getPortalsPositions(stateObs.getAvatarPosition());

        portal = salida[0].get(0).position;
        portal = Ajuste(portal);
        avatar = stateObs.getAvatarPosition();
        avatar = Ajuste(avatar);

        acciones = new ArrayList<>();
        acciones.add(Types.ACTIONS.ACTION_LEFT);
        acciones.add(Types.ACTIONS.ACTION_RIGHT);
        acciones.add(Types.ACTIONS.ACTION_UP);
        acciones.add(Types.ACTIONS.ACTION_DOWN);

        grid = stateObs.getObservationGrid();

    }

    private Vector2d Ajuste( Vector2d coordenadas){
        return  new Vector2d(coordenadas.x / fescala.x, coordenadas.y / fescala.y);
    }

    public void init (StateObservation stateObs, ElapsedCpuTimer elapsedTimer){

    }

    public Types.ACTIONS act (StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        Types.ACTIONS paso = Types.ACTIONS.ACTION_NIL;

        if(PortalEncontrado) {
            paso = plan.pop();
        } else{
            AEstrella(stateObs);
            paso = plan.pop();
        }

        return paso;
    }

    public boolean Muro(StateObservation stateObs, Nodo casilla){

        for(Observation obs : grid[(int)casilla.pos.x][ (int)casilla.pos.y]) {
            if(obs.itype == 0)
                return true;
        }

        return false;
    }

    public double heuristica(Vector2d inicio, Vector2d meta){
        double valor = 0;
        valor = Math.abs(inicio.x - meta.x)*2;
        if(inicio.y < meta.y){
            valor += Math.abs(inicio.y- meta.y)*3;
        }
        else{
            valor += Math.abs(inicio.y - meta.y);
        }
        return valor;
    }

    private void AEstrella (StateObservation stateObs){
        Nodo ini = new Nodo(avatar);
        Nodo n_final = new Nodo(portal);
        ini.ori = 2;
        ini.totalCost = 0;
        ini.estimatedCost = heuristica(ini.pos, portal);

        PriorityQueue<Nodo> abiertos = new PriorityQueue<>();
        PriorityQueue<Nodo> cerrados = new PriorityQueue<>();

        abiertos.add(ini);
        Nodo explorar = ini;

        while(!PortalEncontrado) {
            explorar =  abiertos.poll();
            cerrados.add(explorar);
            
            if (explorar.mismasCoor(portal)) {
                PortalEncontrado = true;
                n_final = explorar;
            } else {
                for (Types.ACTIONS acc : acciones) {
                    Nodo hijo = new Nodo(explorar);

                    if (acc == Types.ACTIONS.ACTION_LEFT) {
                        hijo.pos.x = hijo.pos.x-1;
                        hijo.ori = 4;
                        hijo.totalCost = explorar.totalCost + 2.0;
                        hijo.acc = Types.ACTIONS.ACTION_LEFT;
                    } else if (acc == Types.ACTIONS.ACTION_RIGHT) {
                        hijo.pos.x = hijo.pos.x+1;
                        hijo.ori = 2;
                        hijo.totalCost = explorar.totalCost + 2.0;
                        hijo.acc = Types.ACTIONS.ACTION_RIGHT;
                    } else if (acc == Types.ACTIONS.ACTION_UP) {
                        hijo.pos.y = hijo.pos.y-1;
                        hijo.ori = 1;
                        hijo.totalCost = explorar.totalCost + 3.0;
                        hijo.acc = Types.ACTIONS.ACTION_UP;
                    } else if (acc == Types.ACTIONS.ACTION_DOWN) {
                        hijo.pos.y = hijo.pos.y+1;
                        hijo.ori = 3;
                        hijo.totalCost = explorar.totalCost + 1.0;
                        hijo.acc = Types.ACTIONS.ACTION_DOWN;
                    }

                    if (!Muro(stateObs,hijo)) {
                        hijo.padre = explorar;

                        hijo.estimatedCost = heuristica(hijo.pos,portal);

                        if (hijo.ori != explorar.ori) {
                            hijo.totalCost = hijo.totalCost + 1.0;
                        }

                        if (!abiertos.contains(hijo) && !cerrados.contains(hijo)) {
                            abiertos.add(hijo);
                        } else if (abiertos.contains(hijo)) {
                            ArrayList<Nodo> abiertos_array = new ArrayList<Nodo>(Arrays.asList(abiertos.toArray(new Nodo[abiertos.size()])));
                            Nodo existe = abiertos_array.get(abiertos_array.indexOf(hijo));
                            if (existe.totalCost > hijo.totalCost){
                                abiertos.remove(existe);
                                abiertos.add(hijo);
                            }
                        }
                    }
                }
            }
        }

        while(n_final.acc != Types.ACTIONS.ACTION_NIL){

            plan.push(n_final.acc);

            n_final = n_final.padre;
        }

    }
}
