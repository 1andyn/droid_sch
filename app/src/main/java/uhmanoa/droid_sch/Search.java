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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import android.widget.AbsListView.MultiChoiceModeListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class Search extends ActionBarActivity implements App_const{

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
    private ArrayList<Course> al_course;
    private ArrayAdapter<CharSequence> spinner_data;
    private boolean en_start_tp, en_end_tp = false;
    private int start_hr, end_hr, start_min, end_min = 0;
    private ListView lv_results, lv_sobj;

    // Dialog for Timer Picker
    private CheckBox en_start;
    private CheckBox en_end;
    private TimePicker dtp_start;
    private TimePicker dtp_end;

    // Container Adapter
    private StarListAdapter sobj_adp;

    private final int sliderHeight = 175;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pt_resolution = new Point();
        SelectedItems = new ArrayList<Integer>();
        al_strobj = new ArrayList<Star_obj>();
        al_course = new ArrayList<Course>();
        sobj_adp = new StarListAdapter(this, R.layout.star_view, al_strobj);
        loadImageResources();
        configureSpinner();
        configureSlidingPanel();
        configureViewStubs();
        configureListeners();
        configureListViews();
        handleIntent(getIntent());
        toggle_ViewStub();
    }

    private void configureListViews() {
        lv_results = (ListView)findViewById(R.id.lv_result);
        lv_sobj = (ListView)findViewById(R.id.lv_star);
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
        slideupl.setPanelSlideListener(new PanelSlideListener() {
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
        // Configures Listener for Clear Favorites List
        final Button ClearStarButton = (Button)findViewById(R.id.star_panel_clear);
        ClearStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Search.this, "Cleared starred list",
                        Toast.LENGTH_SHORT).show();
                al_strobj.clear();
                mandatoryDataChange();
            }
        });

        final Button DeleteStarButton = (Button)findViewById(R.id.star_panel_delete);
        DeleteStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Search.this, "Delete selected entries",
                        Toast.LENGTH_SHORT).show();

//                for(int x = 0; x < al_strobj.size(); x++) {
//                    if(al_strobj.get(x).isChecked()) {
//                        System.out.println("SELECTED: " + x);
//                    }
//                }
            }
        });

    }

    private long uniqueStarID() {
        long id = 0;
        boolean unique = false; // Initialize Unique to False
        while(!unique) {
            boolean match = false; // Reset Match Flag to False
            for (int x = 0; x < al_strobj.size(); x++) {
                // Iterate al_strobj and check if there's an existing match to the ID
                Long cmp = al_strobj.get(x).getID();
                // If Match Exist, set match to true
                if(cmp.equals(id)) {
                    //Match found
                    match = true;
                    break; // Break out of For Loop
                }
            }
            if(match) {
                id++; //Increment ID
            } else {
                unique = true;
            }
        }
        return id;
    }

    private void mandatoryDataChange() {
        sobj_adp.notifyDataSetChanged();
        lv_sobj.invalidateViews();
        toggle_ViewStub();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(Search.this, "Search for: " + query,
                    Toast.LENGTH_SHORT).show();
            //search();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

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

    protected void configureSpinner(){
        spinner = (Spinner) findViewById(R.id.major_spinner);
        spinner_data = ArrayAdapter.createFromResource(this,
                R.array.major_list, android.R.layout.simple_spinner_item);
        spinner_data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_data);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                Toast.makeText(Search.this, "Item selected: " + pos + " with Id: " + id,
                        Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    protected void loadImageResources() {
        acquireResolution();
        res_srch = getResources();

        ImageView iv_mmlogo, iv_mmtitle;
        RelativeLayout ll_mainlayout;
        LinearLayout ll_sliderlayout;

        Bitmap bmp_bg = ImgLoader.decodedSampledBitmapResource(res_srch, R.drawable.o_bg,
                pt_resolution.x / 8, pt_resolution.y / 8); //reduces size of file by factor of 8

        ll_mainlayout = (RelativeLayout) findViewById(R.id.srch_rllayout);
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
        //int id = item.getItemId();
        switch(item.getItemId()) {
            case R.id.action_gefc_fil:
                Dialog diag_fil = createFilterDialog();
                return true;
            case R.id.action_time_fil:
                Dialog diag_time = createTimeDialog();
                return true;
            case R.id.action_debug_add:

                Random r = new Random( System.currentTimeMillis() );
                int x = 10000 + r.nextInt(20000);
                Star_obj debug = new Star_obj("ACM 250", "Movies, Films and Stuff",
                        x, uniqueStarID(), semester.fall.ordinal());
                al_strobj.add(debug);
//                Toast.makeText(Search.this, "Added debug star object",
//                        Toast.LENGTH_SHORT).show();
                mandatoryDataChange();
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
                (slideupl.getPanelState() == PanelState.EXPANDED ||
                        slideupl.getPanelState() == PanelState.ANCHORED)) {
            slideupl.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    protected void toggle_ViewStub()
    {
        if(al_strobj.isEmpty() == true){
            empty_star.setVisibility(View.VISIBLE);
            lv_sobj.setVisibility(View.GONE);
        } else {
            empty_star.setVisibility(View.GONE);
            lv_sobj.setVisibility(View.VISIBLE);
        }
        if(al_course.isEmpty() == true){
            empty_search.setVisibility(View.VISIBLE);
            lv_results.setVisibility(View.GONE);
        } else {
            empty_search.setVisibility(View.GONE);
            lv_results.setVisibility(View.VISIBLE);
        }
    }

    private boolean[] gefcFilterState() {
        Resources res = getResources();
        String[] gefc_list = res.getStringArray(R.array.gefc_list);
        boolean[] checked = new boolean[gefc_list.length];

        for(int x = 0; x < gefc_list.length; x++) {
            if(SelectedItems.contains(x)) {
                checked[x] = true;
            } else {
                checked[x] = false;
            }
        }
        return checked;
    }

    public Dialog createFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
        final ArrayList<Integer> SelectedOriginal = new ArrayList<Integer>();
        //Deep Copy
        Iterator<Integer> it = SelectedItems.iterator();
        for(Integer i : SelectedItems) {
            SelectedOriginal.add(i);
        }

        builder.setTitle(Html.fromHtml("<font color='#66FFCC'>General Ed/ Focus Filter</font>"))
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.gefc_list, gefcFilterState(),
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    SelectedItems.add(which);
                                } else if (SelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    SelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        for (Integer o : SelectedItems) {
                            Toast.makeText(Search.this, String.valueOf(o),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Restore Original
                        SelectedItems.clear();
                        Iterator<Integer> it = SelectedOriginal.iterator();
                        for(Integer i : SelectedOriginal) {
                            SelectedItems.add(i);
                        }
                        dialog.cancel();
                    }
                });
        //This is a bit hackish, maybe Google will create an easier way to change divider color?
        Dialog dlg = builder.show();
        int dividerId = dlg.getContext().getResources().getIdentifier("android:id/titleDivider",
                null, null);
        View dv = dlg.findViewById(dividerId);
        dv.setBackgroundColor(getResources().getColor(R.color.aqua));
        return builder.create();
    }

    // For future Changes, Dialogs Should probably be split into Classes
    public Dialog createTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
        LayoutInflater infl = Search.this.getLayoutInflater();
        final View diag_view = infl.inflate(R.layout.time_dialog, null);
        en_start = (CheckBox)diag_view.findViewById(R.id.start_chkbox);
        en_end = (CheckBox)diag_view.findViewById(R.id.end_chkbox);
        dtp_start = (TimePicker)diag_view.findViewById(R.id.start_picker);
        dtp_end = (TimePicker)diag_view.findViewById(R.id.end_picker);

        //Load previous settings
        en_start.setChecked(en_start_tp);
        en_end.setChecked(en_end_tp);
        dtp_start.setCurrentHour(start_hr);
        dtp_start.setCurrentMinute(start_min);
        dtp_end.setCurrentHour(end_hr);
        dtp_end.setCurrentMinute(end_min);

        if(!en_start.isChecked()) {
            dtp_start.setVisibility(View.GONE);
        }

        if(!en_end.isChecked()) {
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
