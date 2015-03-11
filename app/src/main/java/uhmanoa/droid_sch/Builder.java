package uhmanoa.droid_sch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class Builder extends ActionBarActivity implements App_const {

    // --------DEBUG
    private boolean DEBUG = true;
    // --------DEBUG

    private Drawable drw_bg;
    private Resources res_srch;
    private Point pt_resolution;
    private Spinner spinner;
    private ArrayList<Integer> SelectedItems;
    private SlidingUpPanelLayout slideupl;
    private ViewStub empty_search;
    private ViewStub empty_star;
    private ArrayList<Star_obj> al_strobj;
    private ArrayList<Star_obj> al_desired;
    private ArrayList<String> al_profiles;
    private ArrayAdapter<String> spinner_data;
    private boolean en_start_tp, en_end_tp = false;
    private int start_hr, end_hr, start_min, end_min = 0;
    private ListView lv_desd, lv_sobj;

    // Dialog for Timer Picker
    private CheckBox en_start;
    private CheckBox en_end;
    private TimePicker dtp_start;
    private TimePicker dtp_end;

    // Container Adapter
    private StarListAdapter sobj_adp, desd_adp;

    private final int sliderHeight = 175;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builder);
        pt_resolution = new Point();
        SelectedItems = new ArrayList<Integer>();
        al_strobj = new ArrayList<Star_obj>();
        al_desired = new ArrayList<Star_obj>();
        sobj_adp = new StarListAdapter(this, R.layout.star_view, al_strobj);
        desd_adp = new StarListAdapter(this, R.layout.course_view, al_desired);
        loadImageResources();
        loadProfiles();
        configureSpinner();
        configureSlidingPanel();
        configureViewStubs();
        configureListeners();
        configureListViews();
        handleIntent(getIntent());
        toggle_ViewStub();
    }

    private void loadProfiles() {
        al_profiles = new ArrayList<String>();
        al_profiles.add("Default Profile");
    }

    private void configureSpinner() {
            spinner = (Spinner) findViewById(R.id.major_spinner);
            spinner_data = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                    al_profiles);
            spinner_data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinner_data);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    // An item was selected. You can retrieve the selected item using
                    Toast.makeText(Builder.this, "Profile selected: " + pos + " with Id: " + id,
                            Toast.LENGTH_SHORT).show();
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
        empty_search = (ViewStub) findViewById(R.id.empty_search);
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
            }

            @Override
            public void onPanelCollapsed(View panel) {
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
                Toast.makeText(Builder.this, "Deleting selected items on star list.",
                        Toast.LENGTH_SHORT).show();
                ArrayList<Long> checked = desd_adp.getChecked_list();
                System.out.println("Outputting Selection");
                for (Long l : checked) {
                    //Do something
                }
                mandatoryDataChange();
            }
        });

        // Listener for Add Button on Star List
        final Button AddStarButton = (Button) findViewById(R.id.star_panel_add);
        AddStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Builder.this, "Added to courses desired list",
                        Toast.LENGTH_SHORT).show();
                sobj_adp.clear();
                sobj_adp.clearCheckedList();
                mandatoryDataChange();
            }
        });

        final Button DeleteStarButton = (Button) findViewById(R.id.des_delete);
        DeleteStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Builder.this, "Delete selected entries",
                        Toast.LENGTH_SHORT).show();
                ArrayList<Long> checked = sobj_adp.getChecked_list();
                System.out.println("Outputting Selection");
                for (Long l : checked) {
                    if (DEBUG) System.out.println(l);
                    deleteStarByID(l);
                }
                sobj_adp.clearCheckedList(); //Finished deleting so clear this list
                mandatoryDataChange();
            }
        });

        final Button BuildScheduleButton = (Button) findViewById(R.id.des_build);
        BuildScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Builder.this, "Building Schedules",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    private Star_obj getResultById(long id) {
        for (int x = 0; x < al_strobj.size(); x++) {
            Long temp = al_strobj.get(x).getID();
            if(temp.equals(id)) {
                return al_strobj.get(x);
            }
        }
        return null;
    }

    private void addDesiredFromStar(Star_obj star, boolean isclass){
        if(isclass) {
            Star_obj so = new Star_obj(star.getCourse(), star.getCourseTitle(), star.getCRN(),
                    uniqueStarID(true), semester.fall.ordinal());
            desd_adp.add(so);
        } else {
            Star_obj so = new Star_obj(star.getCourse(), star.getCourseTitle(), -1,
                    uniqueStarID(true), semester.fall.ordinal());
            desd_adp.add(so);
        }
        mandatoryDataChange();
    }

    private void deleteStarByID(long id) {
        for (int x = 0; x < al_strobj.size(); x++) {
            Long temp = al_strobj.get(x).getID();
            if (temp.equals(id)) {
                if (DEBUG) System.out.println("Deleting " + id + " " + al_strobj.get(x).getCRN());
                sobj_adp.remove(al_strobj.get(x));
            }
        }
    }

    private long uniqueStarID(boolean main_list) {
        long id = 0;
        boolean unique = false; // Initialize Unique to False
        while (!unique) {
            boolean match = false; // Reset Match Flag to False
            int size = 0;
            if(main_list) {
                size = al_desired.size();
            } else {
                size = al_strobj.size();
            }
            for (int x = 0; x < size; x++) {
                // Iterate al_strobj and check if there's an existing match to the ID
                Long cmp;
                if(main_list) {
                    cmp = al_desired.get(x).getID();
                } else {
                    cmp = al_strobj.get(x).getID();
                }
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
            Toast.makeText(Builder.this, "Search for: " + query,
                    Toast.LENGTH_SHORT).show();
            //search();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_builder, menu);

        //Config ActionBar's Search Box
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        return true;
    }

    protected void loadImageResources() {
        acquireResolution();
        res_srch = getResources();

        ImageView iv_mmlogo, iv_mmtitle;
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

    // DEBUG
    private int randValue() {
        // for 0 to 1         return (1 == (r.nextInt(2) + 0));
        int count = 3;
        int offset = 0;
        Random r = new Random(System.currentTimeMillis());
        return (r.nextInt(count) + offset);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_time_frame:
                Dialog diag_time = createTimeDialog();
                return true;
            case R.id.action_timeblock:
                return true;
            case R.id.action_min:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            empty_search.setVisibility(View.VISIBLE);
            lv_desd.setVisibility(View.GONE);
        } else {
            empty_search.setVisibility(View.GONE);
            lv_desd.setVisibility(View.VISIBLE);
        }
    }

    private boolean[] gefcFilterState() {
        Resources res = getResources();
        String[] gefc_list = res.getStringArray(R.array.gefc_list);
        boolean[] checked = new boolean[gefc_list.length];

        for (int x = 0; x < gefc_list.length; x++) {
            if (SelectedItems.contains(x)) {
                checked[x] = true;
            } else {
                checked[x] = false;
            }
        }
        return checked;
    }

    // For future Changes, Dialogs Should probably be split into Classes
    public Dialog createTimeDialog() {
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

}
