package uhmanoa.droid_sch;

import java.util.ArrayList;

/**
 * Created by supah_000 on 3/3/2015.
 */

public class Vis_CellRow {
    ArrayList<Vis_Cell> row_heights;

    public Vis_CellRow() {
        row_heights = new ArrayList<Vis_Cell>();
    }

    public void addVisCell(Vis_Cell vc) {
        row_heights.add(vc);
    }

    public int size() {
        return row_heights.size();
    }

    public ArrayList<Vis_Cell> getRowHeights() {
        return row_heights;
    }

    public Vis_Cell getVisCell(int index) {
        return row_heights.get(index);
    }

}
