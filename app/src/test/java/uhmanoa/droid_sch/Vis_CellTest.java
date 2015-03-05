package uhmanoa.droid_sch;

import junit.framework.TestCase;

import java.util.ArrayList;

public class Vis_CellTest extends TestCase {

    public void testGetTop() throws Exception {
        Vis_Cell vc = new Vis_Cell(1,2,3,0,0,0);
        assertEquals(1,vc.getTop());
    }

    public void testGetMid() throws Exception {
        Vis_Cell vc = new Vis_Cell(3,5,9,0,0,0);
        assertEquals(5,vc.getMid());
    }

    public void testGetBot() throws Exception {
        Vis_Cell vc = new Vis_Cell(7,1,4,0,0,0);
        assertEquals(4,vc.getBot());
    }

    public void testSetTopheight() throws Exception {
        Vis_Cell vc = new Vis_Cell(1,1,1,0,0,0);
        vc.setTopHeight(0);
        assertEquals(0, vc.getTop());
    }

    public void testSetMidheight() throws Exception {
        Vis_Cell vc = new Vis_Cell(3,5,9,0,0,0);
        vc.setMidHeight(2);
        assertEquals(2,vc.getMid());
    }

    public void testSetBotheight() throws Exception {
        Vis_Cell vc = new Vis_Cell(7,1,4,0,0,0);
        vc.setBotHeight(11);
        assertEquals(11,vc.getBot());
    }

    public void testGetheights() throws Exception {
        Vis_Cell vc = new Vis_Cell(11,12,13,0,0,0);
        ArrayList<Integer> wts = vc.getHeights();
        int x = vc.getBot();
        int y = wts.get(2);
        assertEquals(x,y);

        x = vc.getMid();
        y = wts.get(1);
        assertEquals(x,y);

        x = vc.getTop();
        y = wts.get(0);
        assertEquals(x,y);
    }
}