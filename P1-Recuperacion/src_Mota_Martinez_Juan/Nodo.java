package tracks.singlePlayer.src_Mota_Martinez_Juan;

import core.game.StateObservation;
import ontology.Types;
import tools.Direction;
import tools.Vector2d;

import java.lang.reflect.Type;
import java.util.*;

public class Nodo implements Comparable<Nodo>{
    public double totalCost;
    public double estimatedCost;
    public Nodo padre;
    public Vector2d pos;
    public int ori = -1;
    public Types.ACTIONS acc;

    public Nodo(Vector2d coord){
        totalCost = 0.0f;
        estimatedCost = 0.0f;
        padre = null;
        pos = coord;
        pos.x = coord.x;
        pos.y = coord.y;
        acc = Types.ACTIONS.ACTION_NIL;
    }

    public Nodo(Nodo otro)
    {
        estimatedCost = otro.estimatedCost;
        totalCost = otro.totalCost;
        padre= null;
        pos = new Vector2d( otro.pos);
        ori = otro.ori;
        acc = Types.ACTIONS.ACTION_NIL;
    }

    @Override
    public int compareTo(Nodo n) {
        if(this.estimatedCost + this.totalCost < n.estimatedCost + n.totalCost)
            return -1;
        if(this.estimatedCost + this.totalCost > n.estimatedCost + n.totalCost)
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o)
    {
        return this.pos.equals(((Nodo) o).pos) && ((Nodo) o).ori == this.ori;
    }

    public boolean mismasCoor(Vector2d o){
        return pos.x == o.x && pos.y == o.y;
    }

}
