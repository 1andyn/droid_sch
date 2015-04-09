package uhmanoa.droid_sch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Viewer extends ActionBarActivity implements OnViewButtonPress {

    private Drawable drw_bg;
    private Resources res_srch;
    private Point pt_resolution;
    private ArrayList<Schedule> al_sched;
    private ViewStub empty_sched;
    private ListView lv_sched;
    private SchListAdapter sch_adp;
    private SingletonSchedule ss;

    private SQL_DataSource ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        al_sched = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        ds = new SQL_DataSource(this);
        ds.open();
        ss = SingletonSchedule.getInstance();

        sch_adp = new SchListAdapter(this, R.layout.sch_view, al_sched, this);
        pt_resolution = new Point();
        loadImageResources();
        configureListView();
        configureViewStubs();
        configureButtons();
        toggle_ViewStub();
        load_schedules();
    }

    private void configureButtons() {
        final Button DeleteItemStar = (Button) findViewById(R.id.delete_button);
        DeleteItemStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(Viewer.this)
                        .setTitle(Html.fromHtml("<font color='#66FFCC'>Deletion Confirmation</font>"))
                        .setMessage("Are you sure you want to delete the selected schedules?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(Viewer.this, "Deleting selected items",
                                        Toast.LENGTH_SHORT).show();
                                ArrayList<Long> checked = sch_adp.getChecked_list();
                                System.out.println("Outputting Selection");
                                for (Long l : checked) {
                                    deleteSchedByID(l);
                                }
                                sch_adp.clearCheckedList();
                                mandatoryDataChange();
                                return;
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                Dialog d = confirm.show();
                int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider",
                        null, null);
                View dv = d.findViewById(dividerId);
                dv.setBackgroundColor(getResources().getColor(R.color.aqua));
            }
        });
    }

    private void deleteSchedByID(long id) {
        for (int x = 0; x < al_sched.size(); x++) {
            Long temp = al_sched.get(x).getID();
            if(temp == id) {
                sch_adp.remove(al_sched.get(x));
                ds.deleteSchedule(id);
            }
        }
    }

    private void load_schedules() {
        ArrayList<Schedule> sch = ds.getAllSchedules();
        for(Schedule s: sch) {
            sch_adp.add(s);
        }
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


    private void mandatoryDataChange() {
        sch_adp.notifyDataSetChanged();
        lv_sched.invalidateViews();
        lv_sched.refreshDrawableState();
        lv_sched.setAdapter(sch_adp);
        toggle_ViewStub();
    }


    @Override
    public void onViewButtonPress(Schedule s) {
        ss.setSchedule(s);
        Intent i = new Intent(this, Visualize.class);
        Bundle b = new Bundle();
        b.putInt("SEMESTER", s.getSemester());
        b.putInt("YEAR", s.getYear());
        i.putExtras(b);
        startActivity(i);
    }
}
