package tracks.singlePlayer.advanced.Agente;

import ontology.Types;
import tools.Direction;
import tools.Vector2d;


/**
 * Created by dperez on 13/01/16.
 */
public class Node implements Comparable<Node> {

    public double totalCost;
    public double estimatedCost;
    public Node parent;
    public Types.ACTIONS accion;
    public Vector2d position;
    public Node comingFrom;
    public int id;
    public int orientacion;

    public Node(Vector2d pos, int orien, double coste_est, double coste_total ,Types.ACTIONS acc)
    {
        estimatedCost = coste_est;
        totalCost = coste_total;
        parent = null;
        position = pos;
        id = ((int)(position.x) * 100 + (int)(position.y));
        orientacion = orien;
        accion = acc;
    }

    public double getCost(){
        return totalCost;
    }

    public boolean tienePadre(){
        if (parent == null)
            return false;
        return true;
    }

    @Override
    public int compareTo(Node n) {
        if(this.estimatedCost + this.totalCost < n.estimatedCost + n.totalCost)
            return -1;
        if(this.estimatedCost + this.totalCost > n.estimatedCost + n.totalCost)
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o)
    {
        if (((Node)o).position.equals(this.position) && ((Node)o).orientacion == orientacion)
            return true;
        return false;
    }


    public void setMoveDir(Node pre) {

        //TODO: New types of actions imply a change in this method.
        Direction action = Types.DNONE;

        if(pre.position.x < this.position.x)
            action = Types.DRIGHT;
            orientacion = 1;
        if(pre.position.x > this.position.x)
            action = Types.DLEFT;
            orientacion = 3;

        if(pre.position.y < this.position.y)
            action = Types.DDOWN;
            orientacion =2;
        if(pre.position.y > this.position.y)
            action = Types.DUP;
            orientacion = 0;

        this.comingFrom = pre;
        parent = pre;
    }
}
