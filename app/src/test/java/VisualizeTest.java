import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;

import java.util.ArrayList;

import uhmanoa.droid_sch.Course;
import uhmanoa.droid_sch.Schedule;
import uhmanoa.droid_sch.Vis_Cell;
import uhmanoa.droid_sch.Vis_CellRow;
import uhmanoa.droid_sch.Visualize;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by supah_000 on 3/3/2015.
 */
@Config(emulateSdk = 18)
@RunWith(CustomRobolectricRunner.class)
public class VisualizeTest {

    @Test
    public void testHourArray() throws Exception {
        Activity activity = Robolectric.setupActivity(Visualize.class);
        Visualize vs = (Visualize)activity;
        ArrayList<String> hours = vs.DEBUG_getTimeValues();
        assertThat(hours.get(0), equalTo("12:00a"));
        assertThat(hours.get(1), equalTo("1:00a"));
        assertThat(hours.get(2), equalTo("2:00a"));
        assertThat(hours.get(3), equalTo("3:00a"));
        assertThat(hours.get(4), equalTo("4:00a"));
        assertThat(hours.get(5), equalTo("5:00a"));
        assertThat(hours.get(6), equalTo("6:00a"));
        assertThat(hours.get(7), equalTo("7:00a"));
        assertThat(hours.get(8), equalTo("8:00a"));
        assertThat(hours.get(9), equalTo("9:00a"));
        assertThat(hours.get(10), equalTo("10:00a"));
        assertThat(hours.get(11), equalTo("11:00a"));
        assertThat(hours.get(12), equalTo("12:00p"));
        assertThat(hours.get(13), equalTo("1:00p"));
        assertThat(hours.get(14), equalTo("2:00p"));
        assertThat(hours.get(15), equalTo("3:00p"));
        assertThat(hours.get(16), equalTo("4:00p"));
        assertThat(hours.get(17), equalTo("5:00p"));
        assertThat(hours.get(18), equalTo("6:00p"));
        assertThat(hours.get(19), equalTo("7:00p"));
        assertThat(hours.get(20), equalTo("8:00p"));
        assertThat(hours.get(21), equalTo("9:00p"));
        assertThat(hours.get(22), equalTo("10:00p"));
        assertThat(hours.get(23), equalTo("11:00p"));
    }

    @Test
    public void testHeightCalculations() throws Exception {
        Activity activity = Robolectric.setupActivity(Visualize.class);
        Visualize vs = (Visualize)activity;
        int test_end = 910;
        assertEquals(50, vs.DEBUG_getTopHeight(test_end));
        int test_start = 950;
        assertEquals(50, vs.DEBUG_getBotHeight(test_start));

        test_end = 830;
        assertEquals(150, vs.DEBUG_getTopHeight(test_end));
        test_start = 1045;
        assertEquals(75, vs.DEBUG_getBotHeight(test_start));
    }



    @Test
    public void testWeightCalculation() {
        Activity activity = Robolectric.setupActivity(Visualize.class);
        Visualize vs = (Visualize)activity;

        ArrayList<Character> days1 = new ArrayList<Character>();
        days1.add('M');
        days1.add('W');
        days1.add('F');

        Schedule sch = new Schedule();
        Course crs = new Course("ICS 314", "Software Engineering I", 51804, 3,
                "B Auernheimer", days1, 830, 920, "SAKAM D101", 1, 10, 0, 10, "3/3 to 4/27",
                "MATH CLASS ");
        sch.addCourse(crs);

        ArrayList<Vis_CellRow> visual_weights = vs.DEBUG_getHeights(sch);
        Vis_CellRow vcr1 = visual_weights.get(8);
        Vis_CellRow vcr2 = visual_weights.get(9);
        Vis_Cell mon = vcr1.getVisCell(1);
        Vis_Cell mon2 = vcr2.getVisCell(1);
        System.out.println("Checking Case Values");
        assertEquals(2,mon.getTimeCase()); // one course starts in time block
        assertEquals(1,mon2.getTimeCase()); // one course ends in time block
        System.out.println("Checking Height Values");
        assertEquals(150, mon.getBot()); // Since 30 minutes is half hour, half max height is 150
        assertEquals(150, mon.getMid()); // remaining space is 150
        assertEquals(0, mon.getTop()); // remaining space is 150
        assertEquals(100, mon2.getTop()); // Since 20 minutes is 2/3 hr, 300(2/3) = 100
        assertEquals(200, mon2.getMid()); // remaining space is 200
        assertEquals(0, mon2.getBot()); // bottom is empty
        mon = vcr1.getVisCell(3);
        mon2 = vcr2.getVisCell(3);
        System.out.println("Checking Case Values");
        assertEquals(2,mon.getTimeCase()); // one course starts in time block
        assertEquals(1,mon2.getTimeCase()); // one course ends in time block
        System.out.println("Checking Height Values");
        assertEquals(150, mon.getBot()); // Since 30 minutes is half hour, half max height is 150
        assertEquals(150, mon.getMid()); // remaining space is 150
        assertEquals(0, mon.getTop()); // remaining space is 150
        assertEquals(100, mon2.getTop()); // Since 20 minutes is 2/3 hr, 300(2/3) = 100
        assertEquals(200, mon2.getMid()); // remaining space is 200
        assertEquals(0, mon2.getBot()); // bottom is empty
        mon = vcr1.getVisCell(5);
        mon2 = vcr2.getVisCell(5);
        System.out.println("Checking Case Values");
        assertEquals(2,mon.getTimeCase()); // one course starts in time block
        assertEquals(1,mon2.getTimeCase()); // one course ends in time block
        System.out.println("Checking Height Values");
        assertEquals(150, mon.getBot()); // Since 30 minutes is half hour, half max height is 150
        assertEquals(150, mon.getMid()); // remaining space is 150
        assertEquals(0, mon.getTop()); // remaining space is 150
        assertEquals(100, mon2.getTop()); // Since 20 minutes is 2/3 hr, 300(2/3) = 100
        assertEquals(200, mon2.getMid()); // remaining space is 200
        assertEquals(0, mon2.getBot()); // bottom is empty
    }

}
