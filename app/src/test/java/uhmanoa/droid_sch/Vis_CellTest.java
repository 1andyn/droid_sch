package uhmanoa.droid_sch;

import junit.framework.TestCase;

import java.util.ArrayList;

public class Vis_CellTest extends TestCase {

    public void testGetTop() throws Exception {
        Vis_Cell vc = new Vis_Cell(1,2,3);
        assertEquals(1,vc.getTop());
    }

    public void testGetMid() throws Exception {
        Vis_Cell vc = new Vis_Cell(3,5,9);
        assertEquals(5,vc.getMid());
    }

    public void testGetBot() throws Exception {
        Vis_Cell vc = new Vis_Cell(7,1,4);
        assertEquals(4,vc.getBot());
    }

    public void testSetTopWeight() throws Exception {
        Vis_Cell vc = new Vis_Cell(1,1,1);
        vc.setTopWeight(0);
        assertEquals(0, vc.getTop());
    }

    public void testSetMidWeight() throws Exception {
        Vis_Cell vc = new Vis_Cell(3,5,9);
        vc.setMidWeight(2);
        assertEquals(2,vc.getMid());
    }

    public void testSetBotWeight() throws Exception {
        Vis_Cell vc = new Vis_Cell(7,1,4);
        vc.setBotWeight(11);
        assertEquals(11,vc.getBot());
    }

    public void testGetWeights() throws Exception {
        Vis_Cell vc = new Vis_Cell(11,12,13);
        ArrayList<Integer> wts = vc.getWeights();
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