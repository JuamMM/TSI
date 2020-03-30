package tracks.singlePlayer.advanced.Agente;

import java.util.ArrayList;

public class NodoDiamante {
    public ArrayList<Integer> vector = new ArrayList<>();
    public double precio;

    public NodoDiamante(ArrayList<Integer> datos, double coste)
    {
        precio = coste;
        vector = datos;
    }

    public double getCost(){
        return precio;
    }

    public int getTam(){
        return vector.size();
    }

    public ArrayList<Integer> getVector(){
        return vector;
    }

    public int compareTo(NodoDiamante n) {
        if(this.precio < n.precio)
            return -1;
        if(this.precio > n.precio)
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o)
    {
        if (((NodoDiamante)o).vector.equals(this.vector))
            return true;
        return false;
    }

}
