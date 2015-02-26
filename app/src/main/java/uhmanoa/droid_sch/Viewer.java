package uhmanoa.droid_sch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Viewer extends ActionBarActivity {

    private Drawable drw_bg;
    private Resources res_srch;
    private Point pt_resolution;
    private ArrayList<Schedule> al_sched;
    private ViewStub empty_sched;
    private ExpandableListView exlv_sched;
    private SchListAdapter sch_adp;
    private SchHashMap sch_map;
    private List sch_group;
    private HashMap sch_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        al_sched = new ArrayList<Schedule>();
        setupDebugSchedules();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        pt_resolution = new Point();
        sch_map = new SchHashMap(al_sched);
        loadImageResources();
        configureViewStubs();
        configureListView();
//        toggle_ViewStub();
    }

    private void setupDebugSchedules() {
        Schedule sch = new Schedule();

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
        sch.addCourse(crt);

        ArrayList<Character> days3 = new ArrayList<Character>();
        days3.add('M');
        days3.add('W');
        Course crt2 = new Course ("EE 205", "Object Oriented Programming", 85518, 3,
                "R Zhang", days3, days4, 1130, 300, 1220, 545, "POST 214", "POST 214", 1, 20, 0,
                5, "01/12-05/15", "MAJOR");
        crt2.setFocusReqs(fr);


        sch.addCourse(crt2);
        al_sched.add(sch);
    }

    private void configureListView() {
        exlv_sched = (ExpandableListView) findViewById(R.id.elv_sched);
        sch_detail = sch_map.getData();
        sch_group = new ArrayList(sch_detail.keySet());
        sch_adp = new SchListAdapter(this, sch_group, sch_detail);
        exlv_sched.setAdapter(sch_adp);
        exlv_sched.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        sch_group.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        exlv_sched.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        sch_group.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        exlv_sched.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        "woooo", Toast.LENGTH_SHORT
                )
                        .show();
                return false;
            }
        });
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

        ImageView iv_mmlogo, iv_mmtitle;
        LinearLayout ll_mainlayout;
        LinearLayout ll_sliderlayout;

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
            exlv_sched.setVisibility(View.GONE);
        } else {
            empty_sched.setVisibility(View.GONE);
            exlv_sched.setVisibility(View.VISIBLE);
        }
    }

}
