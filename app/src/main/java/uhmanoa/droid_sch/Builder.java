package uhmanoa.droid_sch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class Builder extends ActionBarActivity implements App_const, OnCheckTaskComplete,
        OnParseTaskComplete {

    // --------DEBUG
    private boolean DEBUG = false;
    // --------DEBUG

    private SingletonOptions sgo;


    private BuilderOptions bos;
    private boolean lastLoadSuccess = false;

    private SearchView sv;
    private int sem; //semester value
    private int year; //year value
    private int month; //month value
    private Drawable drw_bg;
    private Resources res_srch;
    private Point pt_resolution;
    private Spinner spinner;
    private SlidingUpPanelLayout slideupl;
    private ViewStub empty_desire;
    private ViewStub empty_star;
    private ArrayList<Star_obj> al_strobj;
    private ArrayList<Star_obj> al_desired;
    private ArrayList<String> al_profiles;
    private ArrayAdapter<String> spinner_data;
    private boolean en_start_tp, en_end_tp, en_min_np = false;
    private int start_hr, end_hr, start_min, end_min = 0;
    private ListView lv_desd, lv_sobj;

    // Dialog for Timer Picker
    private CheckBox en_start, en_end, en_min;
    private NumberPicker min_pick;
    private TimePicker dtp_start;
    private TimePicker dtp_end;

    //Course Data Check Vars
    PowerManager.WakeLock wl;
    private Parser p;
    private CourseCheckTask cct;

    // Container Adapter
    private StarListAdapter sobj_adp, desd_adp;
    protected SQL_DataSource datasource;

    private final int sliderHeight = 100;
    private int min_course = -1; //if -1, then use size equal to desired list

    private int builderSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder);

        Bundle extras = getIntent().getExtras();
        sem = extras.getInt("SEMESTER");
        year = extras.getInt("YEAR");
        month = extras.getInt("MONTH");

        pt_resolution = new Point();
        al_strobj = new ArrayList<>();
        al_desired = new ArrayList<>();
        sobj_adp = new StarListAdapter(this, R.layout.star_view, al_strobj);
        desd_adp = new StarListAdapter(this, R.layout.course_view, al_desired);

        sgo = SingletonOptions.getInstance();

        datasource = new SQL_DataSource(this);
        datasource.open();

        bos = new BuilderOptions(getApplicationContext());

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        lastLoadSuccess = settings.getBoolean("lastLoadSuccess" + String.valueOf(sem) +
                String.valueOf(year), false);

        loadImageResources();
        loadProfiles();
        configureSpinner();
        configureSlidingPanel();
        configureViewStubs();
        configureListeners();
        configureListViews();
        handleIntent(getIntent());
        toggle_ViewStub();

        reloadDBData();
        checkCourseData();

    }

    private void checkCourseData() {

        if (!datasource.courseDataExists(sem, year) || !lastLoadSuccess) {
            //Retrieve Course Data
            datasource.clearCourseData(sem, year);
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(
                    getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("lastLoadSuccess" + String.valueOf(sem) + String.valueOf(year), false);
            editor.commit();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "PARSEDATA_BUILDER");
            wl.acquire();
            p = new Parser(datasource, this, this);
            p.execute(sem, year, month);
        }
        //don't need to do anything if data already exists
    }

    private void reloadDBData() {
        ArrayList<Star_obj> so = datasource.getAllStar(sem, year);
        sobj_adp.clear();
        for (int x = 0; x < so.size(); x++) {
            sobj_adp.add(so.get(x));
        }

        ArrayList<Star_obj> dso = datasource.getAllTempStar(sem, year);
        desd_adp.clear();
        for (int x = 0; x < dso.size(); x++) {
            desd_adp.add(dso.get(x));
        }

        mandatoryDataChange();
    }

    @Override
    protected void onResume() {
        reloadDBData();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void loadProfiles() {
        al_profiles = new ArrayList<>();
        al_profiles.add("Default Settings");
        al_profiles.add("Custom Settings");
        cfg_settings_from_profile();
    }

    private void cfg_settings_from_profile() {
        //Set up Dialog settings from Profile settings
        en_start_tp = false; //stub
        en_end_tp = false; //stub
        en_min_np = false; //stub
    }

    private void configureSpinner() {
        spinner = (Spinner) findViewById(R.id.major_spinner);
        spinner_data = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                al_profiles);
        spinner_data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_data);
        spinner.setSelection(bos.getSelectedOption());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                bos.setSelectedOption(pos);
                builderSelection = pos;
                new ToastWrapper(Builder.this, "Using " + al_profiles.get(pos),
                        Toast.LENGTH_SHORT);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void configureListViews() {
        lv_desd = (ListView) findViewById(R.id.lv_desired);
        lv_desd.setAdapter(desd_adp);
        lv_sobj = (ListView) findViewById(R.id.lv_star);
        lv_sobj.setAdapter(sobj_adp);
    }

    protected void configureViewStubs() {
        empty_desire = (ViewStub) findViewById(R.id.empty_desired);
        empty_star = (ViewStub) findViewById(R.id.empty_star);
    }

    protected void configureSlidingPanel() {
        LinearLayout starpanel = (LinearLayout) findViewById(R.id.panel_view);
        slideupl = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slideupl.setDragView(starpanel);
        slideupl.setOverlayed(true);
        slideupl.setPanelHeight(sliderHeight);
        slideupl.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelExpanded(View panel) {
                TextView tv = (TextView) findViewById(R.id.star_slider);
                tv.setText("Slide down or Tap Here to hide Starred Items");
            }

            @Override
            public void onPanelCollapsed(View panel) {
                TextView tv = (TextView) findViewById(R.id.star_slider);
                tv.setText("Slide up or Tap Here to show Starred Items");
            }

            @Override
            public void onPanelAnchored(View panel) {
            }

            @Override
            public void onPanelHidden(View panel) {
            }
        });
    }

    private void configureListeners() {

        //Listener for Star Panel Deletion
        final Button DeleteItemStar = (Button) findViewById(R.id.star_panel_delete);
        DeleteItemStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToastWrapper(Builder.this, "Deleting selected items on star list.",
                        Toast.LENGTH_SHORT);
                ArrayList<Long> checked = sobj_adp.getChecked_list();
                System.out.println("Outputting Selection");
                for (Long l : checked) {
                    if (DEBUG) System.out.println(l);
                    deleteStarByID(l);
                }
                sobj_adp.clearCheckedList();
                mandatoryDataChange();
            }
        });

        // Listener for Add Button on Star List
        final Button AddStarButton = (Button) findViewById(R.id.star_panel_add);
        AddStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToastWrapper(Builder.this, "Added to courses desired list",
                        Toast.LENGTH_SHORT);
                ArrayList<Long> checked = sobj_adp.getChecked_list();
                System.out.println("Outputting Selection");
                for (Long l : checked) {
                    if (DEBUG) System.out.println(l);
                    addDesiredFromStar(l);
                }
                mandatoryDataChange();
            }
        });

        final Button DeleteStarButton = (Button) findViewById(R.id.main_delete);
        DeleteStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToastWrapper(Builder.this, "Delete selected entries",
                        Toast.LENGTH_SHORT);
                ArrayList<Long> checked = desd_adp.getChecked_list();
                System.out.println("Outputting Selection");
                for (Long l : checked) {
                    if (DEBUG) System.out.println(l);
                    deleteItemByID(l);
                }
                desd_adp.clearCheckedList(); //Finished deleting so clear this list
                mandatoryDataChange();
            }
        });

        final Button BuildScheduleButton = (Button) findViewById(R.id.main_build);
        BuildScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (desd_adp.getCount() < 2) {
                    new ToastWrapper(Builder.this, "Please add atleast two courses.",
                            Toast.LENGTH_SHORT);
                } else {
                    Intent i = new Intent(Builder.this, Available_Schedules.class);
                    Bundle b = new Bundle();
                    b.putInt("SEMESTER", sem);
                    b.putInt("YEAR", year);
                    b.putInt("MONTH", month);
                    i.putExtras(b);

                    //configure SingletonOptions
                    configBuilderOptions();

                    startActivity(i);
                }
            }
        });

    }

    private void configBuilderOptions() {
        if (builderSelection == 0) {
            //default don't use any options
            sgo.setEn_StartTime(0, false);
            sgo.setEn_EndTime(0, false);
            sgo.setTimeOff(null);
            sgo.setDaysOff(null);
        } else {
            boolean start = bos.getBooleanEarliestStart();
            boolean end = bos.getBooleanLatestEnd();
            sgo.setEn_StartTime(bos.getEarliestStart(), start);
            sgo.setEn_EndTime(bos.getLatestEnd(), end);
            if (bos.getTimeOffBoolean()) {
                sgo.setTimeOff(timeBlockBuilder());
            } else {
                sgo.setTimeOff(null);
            }
            sgo.setDaysOff(bos.getDaysOffArray());
        }

        if (en_min_np) {
            sgo.setMinCrs(min_course);
        } else {
            sgo.setMinCrs(-1);
        }
    }

    private Course timeBlockBuilder() {
        Course c;
        int start1 = bos.getTimeOffStart1();
        int end1 = bos.getTimeOffEnd1();
        int start2 = bos.getTimeOffStart2();
        int end2 = bos.getTimeOffEnd2();

        //Four Unique Cases
        // #1 Both are complete, add both
        // #2 One is complete, create one
        // #3 One is complete (other) create one
        // Else don't add either
        if (start1 != -1 && end1 != -1 && start2 != -1 && end2 != -1 &&
                bos.getDaysTOArray1().size() > 0 &&
                bos.getDaysTOArray2().size() > 0) {
            c = new Course("T",
                    "",
                    0,
                    "",
                    "",
                    bos.getDaysTOArray1(),
                    bos.getDaysTOArray2(),
                    start1,
                    start2,
                    end1,
                    end2,
                    "",
                    "",
                    0,
                    0,
                    0,
                    0,
                    "",
                    "");
        } else if (start1 != -1 && end1 != -1 && bos.getDaysTOArray1().size() > 0) {
            c = new Course("T",
                    "",
                    0,
                    "",
                    "",
                    bos.getDaysTOArray1(),
                    start1, end1,
                    "",
                    0,
                    0,
                    0,
                    0,
                    "",
                    "");
        } else if (start2 != -1 && end2 != -1 && bos.getDaysTOArray2().size() > 0) {
            c = new Course("T",
                    "",
                    0,
                    "",
                    "",
                    bos.getDaysTOArray2(),
                    start2, end2,
                    "",
                    0,
                    0,
                    0,
                    0,
                    "",
                    "");
        } else {
            return null;
        }
        return c;
    }

    private Star_obj getResultById(long id) {
        for (int x = 0; x < al_strobj.size(); x++) {
            Long temp = al_strobj.get(x).getID();
            if (temp.equals(id)) {
                return al_strobj.get(x);
            }
        }
        return null;
    }

    private boolean crnExists(int crn) {
        for (int x = 0; x < al_desired.size(); x++) {
            if (al_desired.get(x).getCRN() == crn) {
                return true;
            }
        }
        return false;
    }

    private boolean crsExists(String crs) {
        for (int x = 0; x < al_desired.size(); x++) {
            if (al_desired.get(x).getCourse().equals(crs) &&
                    al_desired.get(x).getCRN() == -1) {
                return true;
            }
        }
        return false;
    }

    private void addDesiredFromStar(long id) {
        Star_obj resd = getResultById(id);
        Star_obj so = new Star_obj(resd.getCourse(), resd.getCourseTitle(), resd.getCRN(), id,
                resd.getSemester(), resd.getYear());

        if (so.isClass()) {
            if (!crnExists(so.getCRN())) {
                so.setID(datasource.saveTStar(so));
                desd_adp.add(so);
            } else {
                new ToastWrapper(Builder.this,
                        "A course with the CRN already exists in the Course List",
                        Toast.LENGTH_SHORT);
            }
        } else {
            if (!crsExists(so.getCourse())) {
                so.setID(datasource.saveTStar(so));
                desd_adp.add(so);
            } else {
                new ToastWrapper(Builder.this, "Course already exists in Course List",
                        Toast.LENGTH_SHORT);
            }
        }
        mandatoryDataChange();
    }

    private void deleteStarByID(long id) {
        for (int x = 0; x < al_strobj.size(); x++) {
            Long temp = al_strobj.get(x).getID();
            if (temp.equals(id)) {
                if (DEBUG) System.out.println("Deleting " + id + " " + al_strobj.get(x).getCRN());
                datasource.deleteStar(id);
                sobj_adp.remove(al_strobj.get(x));
            }
        }
    }

    private void deleteItemByID(long id) {
        for (int x = 0; x < al_desired.size(); x++) {
            Long temp = al_desired.get(x).getID();
            if (temp.equals(id)) {
                if (DEBUG) System.out.println("Deleting " + id + " " + al_desired.get(x).getCRN());
                datasource.deleteTStar(id);
                desd_adp.remove(al_desired.get(x));
            }
        }
    }

    private void mandatoryDataChange() {
        sobj_adp.notifyDataSetChanged();
        desd_adp.notifyDataSetChanged();
        lv_sobj.invalidateViews();
        lv_desd.invalidateViews();
        lv_sobj.refreshDrawableState();
        lv_sobj.setAdapter(sobj_adp);
        lv_desd.refreshDrawableState();
        lv_desd.setAdapter(desd_adp);
        toggle_ViewStub();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query.length() < 3) {
                new ToastWrapper(Builder.this, "Please enter atleast 3 characters.",
                        Toast.LENGTH_LONG);
            } else {
                cct = new CourseCheckTask(this, datasource, this, sem, year);
                cct.execute(query);
                sv.setQuery("", false);
            }
            sv.clearFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_builder, menu);
        //Config ActionBar's Search Box
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv = (SearchView) menu.findItem(R.id.search).getActionView();
        sv.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        sv.setIconifiedByDefault(true);

        return true;
    }

    protected void loadImageResources() {
        acquireResolution();
        res_srch = getResources();

        LinearLayout ll_mainlayout;
        LinearLayout ll_sliderlayout;

        Bitmap bmp_bg = ImgLoader.decodedSampledBitmapResource(res_srch, R.drawable.o_bg,
                pt_resolution.x / 8, pt_resolution.y / 8); //reduces size of file by factor of 8
        ll_mainlayout = (LinearLayout) findViewById(R.id.srch_rllayout);
        drw_bg = new BitmapDrawable(bmp_bg);
        ll_mainlayout.setBackgroundDrawable(drw_bg);

        bmp_bg = ImgLoader.decodedSampledBitmapResource(res_srch, R.drawable.mm_bg,
                pt_resolution.x / 8, pt_resolution.y / 8);
        ll_sliderlayout = (LinearLayout) findViewById(R.id.slide_ll);
        drw_bg = new BitmapDrawable(bmp_bg);
        ll_sliderlayout.setBackgroundDrawable(drw_bg);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_min:
                if (uniqueDesiredCount() >= 2) {
                    Dialog diag_min = createMinDialog();
                } else {
                    new ToastWrapper(Builder.this, "Please add atleast two courses before" +
                            " attempting to configure this option.",
                            Toast.LENGTH_SHORT);
                }
                return true;
            case R.id.action_pref:
                Intent i = new Intent(this, Preferences.class);
                startActivityForResult(i, 0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            case 0:
                if (res == RESULT_OK) {
                    Bundle results = data.getExtras();
                    boolean saved = results.getBoolean("SAVE");
                    if (saved) {
                        bos.setSelectedOption(1);
                        spinner.setSelection(1);
                    }
                }
        }
    }

    private long uniqueID() {
        long id = 0;
        boolean unique = false; // Initialize Unique to False
        while (!unique) {
            boolean match = false; // Reset Match Flag to False
            int size = al_desired.size();
            for (int x = 0; x < size; x++) {
                // Iterate al_strobj and check if there's an existing match to the ID
                Long cmp;
                cmp = al_desired.get(x).getID();
                // If Match Exist, set match to true
                if (cmp.equals(id)) {
                    //Match found
                    match = true;
                    break; // Break out of For Loop
                }
            }
            if (match) {
                id++; //Increment ID
            } else {
                unique = true;
            }
        }
        return id;
    }

    protected void acquireResolution() {
        Display dsp = getWindowManager().getDefaultDisplay();
        pt_resolution.x = dsp.getWidth();
        pt_resolution.y = dsp.getHeight();
    }

    // Override original "back" function
    @Override
    public void onBackPressed() {
        if (slideupl != null &&
                (slideupl.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                        slideupl.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slideupl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    protected void toggle_ViewStub() {
        if (al_strobj.isEmpty() == true) {
            empty_star.setVisibility(View.VISIBLE);
            lv_sobj.setVisibility(View.GONE);
        } else {
            empty_star.setVisibility(View.GONE);
            lv_sobj.setVisibility(View.VISIBLE);
        }
        if (al_desired.isEmpty() == true) {
            empty_desire.setVisibility(View.VISIBLE);
            lv_desd.setVisibility(View.GONE);
        } else {
            empty_desire.setVisibility(View.GONE);
            lv_desd.setVisibility(View.VISIBLE);
        }
    }

    // For future Changes, Dialogs Should probably be split into Classes
    private Dialog createTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Builder.this);
        LayoutInflater infl = Builder.this.getLayoutInflater();
        final View diag_view = infl.inflate(R.layout.time_dialog, null);
        en_start = (CheckBox) diag_view.findViewById(R.id.start_chkbox);
        en_end = (CheckBox) diag_view.findViewById(R.id.end_chkbox);
        dtp_start = (TimePicker) diag_view.findViewById(R.id.start_picker);
        dtp_end = (TimePicker) diag_view.findViewById(R.id.end_picker);

        //Load previous settings
        en_start.setChecked(en_start_tp);
        en_end.setChecked(en_end_tp);
        dtp_start.setCurrentHour(start_hr);
        dtp_start.setCurrentMinute(start_min);
        dtp_end.setCurrentHour(end_hr);
        dtp_end.setCurrentMinute(end_min);

        if (!en_start.isChecked()) {
            dtp_start.setVisibility(View.GONE);
        }

        if (!en_end.isChecked()) {
            dtp_end.setVisibility(View.GONE);
        }

        en_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!(en_start.isChecked())) {
                    dtp_start.setVisibility(View.GONE);
                } else {
                    dtp_start.setVisibility(View.VISIBLE);
                }
            }
        });

        en_end.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!(en_end.isChecked())) {
                    dtp_end.setVisibility(View.GONE);
                } else {
                    dtp_end.setVisibility(View.VISIBLE);
                }
            }
        });

        builder.setView(diag_view)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        en_start_tp = en_start.isChecked();
                        en_end_tp = en_end.isChecked();
                        start_hr = dtp_start.getCurrentHour();
                        start_min = dtp_start.getCurrentMinute();
                        end_hr = dtp_end.getCurrentHour();
                        end_min = dtp_end.getCurrentMinute();

                        if (DEBUG) {
                            System.out.println(en_start_tp);
                            System.out.println(en_end_tp);
                            System.out.println("START" + start_hr + ":" + start_min);
                            System.out.println("END" + end_hr + ":" + end_min);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); // Effectively don't do anything
                    }
                });

        Dialog dlg = builder.show();
        return builder.create();
    }

    private int uniqueDesiredCount() {
        int count = desd_adp.getCount();

        ArrayList<String> crs_list = new ArrayList<>();
        for (Star_obj so : al_desired) {
            if (!crs_list.contains(so.getCourse())) {
                crs_list.add(so.getCourse());
            }
        }

        if (!crs_list.isEmpty()) {
            count = crs_list.size();
        }

        return count;
    }

    private Dialog createMinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Builder.this);
        LayoutInflater infl = Builder.this.getLayoutInflater();
        final View diag_view = infl.inflate(R.layout.min_dialog, null);
        en_min = (CheckBox) diag_view.findViewById(R.id.enable_chkbox);
        min_pick = (NumberPicker) diag_view.findViewById(R.id.num_picker);
        min_pick.setMinValue(2);
        min_pick.setMaxValue(uniqueDesiredCount());

        //Load previous settings
        en_min.setChecked(en_min_np);
        if (min_course == -1) {
            min_pick.setValue(uniqueDesiredCount());
        } else {
            min_pick.setValue(min_course);
        }

        if (!en_min.isChecked()) {
            min_pick.setVisibility(View.GONE);
        }

        en_min.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!(en_min.isChecked())) {
                    min_pick.setVisibility(View.GONE);
                } else {
                    min_pick.setVisibility(View.VISIBLE);
                }
            }
        });

        builder.setView(diag_view)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        en_min_np = en_min.isChecked();
                        if (en_min_np) {
                            min_course = min_pick.getValue();
                        } else {
                            min_course = -1; //should still use default max
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); // Effectively don't do anything
                    }
                });

        Dialog dlg = builder.show();
        return builder.create();
    }

    @Override
    public void onCheckTaskComplete() {
        Star_obj match = cct.getMatch();
        if (match == null) {
            new ToastWrapper(this, "No courses matched the input, try again.",
                    Toast.LENGTH_LONG);
        } else {
            match.setID(uniqueID());
            match.setID(datasource.saveTStar(match));
            desd_adp.add(match);
            mandatoryDataChange();
        }
        sv.setQuery("", false);
        sv.clearFocus();
    }

    @Override
    public void onParseTaskComplete(IOException e) {
        if (p.getTaskCancelled()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            wl.release();
            finish();
            return;
        }

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        wl.release();

        if (e != null) {
            new ToastWrapper(this, "Unable to retrieve course data, try again later.",
                    Toast.LENGTH_SHORT);
            e.printStackTrace();
        } else {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(
                    getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("lastLoadSuccess" + String.valueOf(sem) + String.valueOf(year), true);
            editor.commit();
        }
    }
}
