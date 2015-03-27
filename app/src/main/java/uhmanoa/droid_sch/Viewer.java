package uhmanoa.droid_sch;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;


public class Viewer extends ActionBarActivity implements OnViewButtonPress {

    private Drawable drw_bg;
    private Resources res_srch;
    private Point pt_resolution;
    private ArrayList<Schedule> al_sched;
    private ViewStub empty_sched;
    private ListView lv_sched;
    private SchListAdapter sch_adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        al_sched = new ArrayList<Schedule>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        sch_adp = new SchListAdapter(this, R.layout.sch_view, al_sched, this);
        pt_resolution = new Point();
        loadImageResources();
        configureListView();
        configureViewStubs();
        toggle_ViewStub();
        setupDebugSchedules();
    }

    //DEBUG
    private void debugVisualize() {
        Intent i;
        i = new Intent(this, Visualize.class);
        startActivity(i);
    }


    private void setupDebugSchedules() {
        Schedule sch = new Schedule();
        sch.setID(0);
        ArrayList<Character> days1 = new ArrayList<Character>();
        days1.add('M');
        days1.add('W');
        days1.add('F');

        ArrayList<Character> days2 = new ArrayList<Character>();
        days2.add('T');

        ArrayList<Character> days4 = new ArrayList<Character>();
        days4.add('T');

        ArrayList<String> fr = new ArrayList<String>();
        fr.add("NI");

        Course crt = new Course ("EE 160", "Programming for Engineers", 82496, 4,
                "T Dobry", days1, days2, 830,730, 920, 1015, "PHYSCI 217", "POST 214", 1, 20, 0,
                5, "01/12-05/15", "MAJOR");
        crt.setID(0);
        sch.addCourse(crt);

        ArrayList<Character> days3 = new ArrayList<Character>();
        days3.add('M');
        days3.add('W');
        Course crt2 = new Course ("EE 205", "Object Oriented Programming", 85518, 3,
                "R Zhang", days3, days4, 1130, 300, 1220, 545, "POST 214", "POST 214", 1, 20, 0,
                5, "01/12-05/15", "MAJOR");
        crt2.setID(1);
        crt2.setFocusReqs(fr);


        sch.addCourse(crt2);
        al_sched.add(sch);

        mandatoryDataChange();
    }

    private void configureListView() {
        lv_sched = (ListView) findViewById(R.id.lv_sched);
        lv_sched.setAdapter(sch_adp);
    }

    private void configureViewStubs() {
        empty_sched = (ViewStub) findViewById(R.id.empty_sched);
    }

    protected void acquireResolution() {
        Display dsp = getWindowManager().getDefaultDisplay();
        pt_resolution.x = dsp.getWidth();
        pt_resolution.y = dsp.getHeight();
    }

    protected void loadImageResources() {
        acquireResolution();
        res_srch = getResources();

        LinearLayout ll_mainlayout;

        Bitmap bmp_bg = ImgLoader.decodedSampledBitmapResource(res_srch, R.drawable.o_bg,
                pt_resolution.x / 8, pt_resolution.y / 8); //reduces size of file by factor of 8
        ll_mainlayout = (LinearLayout) findViewById(R.id.ll_view_layout);
        drw_bg = new BitmapDrawable(bmp_bg);
        ll_mainlayout.setBackgroundDrawable(drw_bg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void toggle_ViewStub() {
        if (al_sched.isEmpty() == true) {
            empty_sched.setVisibility(View.VISIBLE);
            lv_sched.setVisibility(View.GONE);
        } else {
            empty_sched.setVisibility(View.GONE);
            lv_sched.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewButtonPress(long id) {
        //needs to be modified to draw a specific schedule
        debugVisualize();
    }

    private void mandatoryDataChange() {
        sch_adp.notifyDataSetChanged();
        lv_sched.invalidateViews();
        lv_sched.refreshDrawableState();
        lv_sched.setAdapter(sch_adp);
        toggle_ViewStub();
    }


}
