package uhmanoa.droid_sch;

import java.util.ArrayList;

/**
 * Created by supah_000 on 3/3/2015.
 *
 * used to store weights used by views in the visualize activity
 *
 * */

public class Vis_Cell {
    private ArrayList<Integer> weight_container;

    public Vis_Cell(int t, int m, int b) {
        weight_container = new ArrayList<Integer>();
        weight_container.add(t);
        weight_container.add(m);
        weight_container.add(b);
    }

    public int getTop() {
        return weight_container.get(0);
    }

    public int getMid() {
        return weight_container.get(1);
    }

    public int getBot() {
        return weight_container.get(2);
    }

    public void setTopWeight(int input) {
        if(weight_container != null && weight_container.get(0) != null) {
            weight_container.set(0, input);
        }
    }

    public void setMidWeight(int input) {
        if(weight_container != null && weight_container.get(1) != null) {
            weight_container.set(1, input);
        }
    }

    public void setBotWeight(int input) {
        if(weight_container != null && weight_container.get(2) != null) {
            weight_container.set(2, input);
        }
    }

    public ArrayList<Integer> getWeights() {
        return weight_container;
    }

}


