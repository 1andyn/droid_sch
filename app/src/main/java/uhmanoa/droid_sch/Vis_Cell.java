package uhmanoa.droid_sch;

import java.util.ArrayList;

/**
 * Created by supah_000 on 3/3/2015.
 *
 * used to store heights used by views in the visualize activity
 *
 * */

public class Vis_Cell {
    private ArrayList<Integer> height_container;
    private ArrayList<Long> color_container;
    private int time_case = 0;

    public Vis_Cell(int t, int m, int b, long x, long y, long z, int vcase) {
        height_container = new ArrayList<Integer>();
        color_container = new ArrayList<Long>();
        height_container.add(t);
        height_container.add(m);
        height_container.add(b);
        color_container.add(x);
        color_container.add(y);
        color_container.add(z);
        setTimeCase(vcase);
    }

    public void setTimeCase(int timecase) {
        time_case = timecase;
    }

    public int getTimeCase() {
        return time_case;
    }

    public int getTop() {
        return height_container.get(0);
    }

    public int getMid() {
        return height_container.get(1);
    }

    public int getBot() {
        return height_container.get(2);
    }

    public void setTopHeight(int input) {
        if(height_container != null && height_container.get(0) != null) {
            height_container.set(0, input);
        }
    }

    public void setMidHeight(int input) {
        if(height_container != null && height_container.get(1) != null) {
            height_container.set(1, input);
        }
    }

    public void setBotHeight(int input) {
        if(height_container != null && height_container.get(2) != null) {
            height_container.set(2, input);
        }
    }

    public long getTopColor() {
        return color_container.get(0);
    }

    public long getMidColor() {
        return color_container.get(1);
    }

    public long getBotColor() {
        return color_container.get(2);
    }

    public void setTopColor(long input) {
        if(color_container != null && color_container.get(0) != null) {
            color_container.set(0, input);
        }
    }

    public void setMidColor(long input) {
        if(color_container != null && color_container.get(1) != null) {
            color_container.set(1, input);
        }
    }

    public void setBotColor(long input) {
        if(color_container != null && color_container.get(2) != null) {
            color_container.set(2, input);
        }
    }

    public ArrayList<Integer> getHeights() {
        return height_container;
    }

    public ArrayList<Long> getColors() {
        return color_container;
    }

}


