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

import uhmanoa.droid_sch.Visualize;

import static org.hamcrest.core.IsEqual.equalTo;
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


}
