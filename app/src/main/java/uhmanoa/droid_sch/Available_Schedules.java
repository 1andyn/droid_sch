package uhmanoa.droid_sch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Available_Schedules extends ActionBarActivity implements View.OnClickListener,
        OnBuildTaskComplete, OnViewButtonPress {

    public static final String LOGTAG = "SCHED";
    public static final String CONFIRM_SAVE = "Warning: By clicking ok you will save the selected " +
            "schedules and discard all other schedules.  Are you sure you want to continue?";
    public static final int ITEMS_PER_PAGE = 4;

    private int sem, year, month;

    private BuilderOptions bos;


    SingletonOptions sgo;
    ArrayList<String> titles, t1;
    ArrayList<String> subtitles, s1;
    ListView lv_item;
    Button btnPrev, btnNext, btnGoto, btnSave;
    TextView tvTitle;
    SchListAdapter adapter;
    SingletonSchedule ss;


    ArrayList<Schedule> schedules, schedPage;
    private ArrayList<Star_obj> star_list;
    private SQL_DataSource datasource;
    private ScheduleBuildTask sbt;

    int totalPages, currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_schedules);
        setBackground();

        sgo = SingletonOptions.getInstance();
        ss = SingletonSchedule.getInstance();

        bos = new BuilderOptions(getApplicationContext());

        star_list = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        sem = extras.getInt("SEMESTER");
        year = extras.getInt("YEAR");
        month = extras.getInt("MONTH");

        datasource = new SQL_DataSource(this);
        datasource.open();

        loadStarObjects();
        runBuildTask();
    }

    private void loadStarObjects() {
        star_list = datasource.getAllTempStar(sem, year);
    }

    private void runBuildTask() {
        sbt = new ScheduleBuildTask(Available_Schedules.this, datasource,
                Available_Schedules.this, sem, year, star_list, sgo.getMinCourse());
        sbt.execute();
    }

    private void setBackground() {
        Resources res_main = getResources();
        Point pt_resolution = new Point();
        Display dsp = getWindowManager().getDefaultDisplay();
        pt_resolution.x = dsp.getWidth();
        pt_resolution.y = dsp.getHeight();

        LinearLayout avail_sched = (LinearLayout) findViewById(R.id.ll_avail_sched);

        Bitmap bmp_mmbg = ImgLoader.decodedSampledBitmapResource(res_main, R.drawable.o_bg,
                pt_resolution.x / 8, pt_resolution.y / 8);
        BitmapDrawable drw_bg = new BitmapDrawable(bmp_mmbg);
        avail_sched.setBackgroundDrawable(drw_bg);
    }

    private void initLayout() {
        lv_item = (ListView) findViewById(R.id.lvScheds);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnGoto = (Button) findViewById(R.id.btn_goto);
        btnSave = (Button) findViewById(R.id.btn_save_selected);
        tvTitle = (TextView) findViewById(R.id.tvNumSchedules);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnGoto.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        btnPrev.setEnabled(false);

        tvTitle.setText("Your courses generated " + titles.size() + " schedules.");
        updateGotoButton();
    }

    private void populateList(ArrayList<Schedule> res) {
        titles = new ArrayList<>();
        subtitles = new ArrayList<>();

        schedules = new ArrayList<>();

        int x = 1;
        for (Schedule s : res) {
            titles.add("Sched " + x);
            subtitles.add("Num classes in sched" + s.getCourses().size());
            schedules.add(s);
            x++;
        }

        totalPages = 0;
        currentPage = 0;

        // find out how many pages we have
        totalPages = schedules.size() / ITEMS_PER_PAGE;
        if ((schedules.size() % ITEMS_PER_PAGE) != 0)
            totalPages++;
    }

    private void populateNextPage() {
        t1 = new ArrayList<>(ITEMS_PER_PAGE);
        s1 = new ArrayList<>(ITEMS_PER_PAGE);

        schedPage = new ArrayList<>(ITEMS_PER_PAGE);

        int itemsOnPage = ITEMS_PER_PAGE;
        if (currentPage == totalPages - 1)
            itemsOnPage = titles.size() % ITEMS_PER_PAGE;

        for (int i = (currentPage * ITEMS_PER_PAGE); i < ((currentPage * ITEMS_PER_PAGE) +
                itemsOnPage); i++) {
            t1.add(titles.get(i).toString());
            s1.add(subtitles.get(i).toString());
            schedPage.add(schedules.get(i));
        }

        Log.e(LOGTAG, "Size! " + schedules.size());
        adapter = new SchListAdapter(this, R.layout.sch_view, schedPage, this);
        lv_item.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateGotoButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_prev:
                if (currentPage > 0)
                    currentPage--;
                populateNextPage();
                if (currentPage == 0)
                    btnPrev.setEnabled(false);
                if (currentPage < totalPages - 1)
                    btnNext.setEnabled(true);

                break;
            case R.id.btn_next:
                if (currentPage < totalPages - 1)
                    currentPage++;
                populateNextPage();
                if (currentPage == totalPages - 1)
                    btnNext.setEnabled(false);
                if (currentPage > 0)
                    btnPrev.setEnabled(true);

                break;
            case R.id.btn_goto:
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                AlertDialog.Builder pagenum = new AlertDialog.Builder(this)
                        .setTitle(Html.fromHtml("<font color='#66FFCC'>Go to page..</font>"))
                        .setMessage("Enter page number between 1-" + totalPages)
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int page = Integer.parseInt(input.getText().toString());
                                if (page > 0 && page <= totalPages) {
                                    currentPage = page - 1;  // subtract one for starting at zero
                                    if (currentPage == 0)
                                        btnPrev.setEnabled(false);
                                    if (currentPage == totalPages - 1)
                                        btnNext.setEnabled(false);
                                    if (currentPage < totalPages - 1)
                                        btnNext.setEnabled(true);
                                    if (currentPage > 0)
                                        btnPrev.setEnabled(true);
                                    populateNextPage();
                                } else {
                                    new ToastWrapper(getApplicationContext(), "You must enter a " +
                                            "page number in the valid range from 1 to " +
                                            totalPages, Toast.LENGTH_SHORT);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                Dialog d = pagenum.show();
                break;
            case R.id.btn_save_selected:
                AlertDialog.Builder confirm = new AlertDialog.Builder(this)
                        .setTitle(Html.fromHtml("<font color='#66FFCC'>Confirm Save</font>"))
                        .setMessage(CONFIRM_SAVE)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (Schedule s : schedules) {
                                    if (s.isChecked()) {
                                        datasource.saveSched(s);
                                    }
                                }
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                Dialog dg = confirm.show();
                break;
        }
    }

    private void updateGotoButton() {
        btnGoto.setText("Page \t" + (currentPage + 1) + " / " + totalPages);
    }

    private ArrayList<Schedule> timeFilter(ArrayList<Schedule> s) {
        ArrayList<Schedule> res = s;
        ArrayList<Schedule> final_results = s;

        //time filtering START TIME
        if (sgo.getEnStart()) {
            final_results = new ArrayList<>();
            int time = sgo.getStartTime();
            for (Schedule sch : s) {
                if (sch.earliestStart() >= time) {
                    final_results.add(sch);
                }
            }
        }

        //time filtering END TIME
        if (sgo.getEnEnd()) {
            ArrayList<Schedule> temp = new ArrayList<>();
            int time = sgo.getEndTime();
            for (Schedule sch : final_results) {
                if (time >= sch.latestEnd()) {
                    temp.add(sch);
                }
            }
            res = temp;
        }
        return res;
    }

    private ArrayList<Schedule> dayFilter(ArrayList<Schedule> s) {
        ArrayList<Character> offDays = bos.getDaysOffArray();
        ArrayList<Schedule> filtered_results = new ArrayList<>();
        for (Schedule sc : s) {
            boolean match = false;
            ArrayList<Character> days = sc.getDays();
            for (int x = 0; x < offDays.size(); x++) {
                if (days.contains(offDays.get(x))) {
                    match = true;
                    break;
                }
            }
            if (match) {
                continue;
            } else {
                filtered_results.add((sc));
            }
        }
        return filtered_results;
    }

    @Override
    public void onBuildTaskComplete() {
        ArrayList<Schedule> final_results = timeFilter(sbt.getResults());
        int size = (bos.getDaysOffArray().size());
        if(bos.getDaysOffBoolean() && size != 0) {
            final_results = dayFilter(final_results);
        }
        if (final_results.size() == 0) {
            new ToastWrapper(getApplicationContext(), "Your course list produced 0 possible " +
                    "schedules. Try using different courses or schedule options.", Toast.LENGTH_LONG);
            finish();
        } else {
            populateList(final_results);
            initLayout();
            populateNextPage();
        }
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
