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
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;


public class Search extends ActionBarActivity implements App_const{

    private Drawable drw_bg;
    private Resources res_srch;
    private Point pt_resolution;
    private Spinner spinner;
    private ArrayList SelectedItems;
    private SlidingUpPanelLayout slideupl;
    private ViewStub empty_search;
    private ViewStub empty_star;
    private LinearLayout starpanel;
    private ArrayList<Star_obj> al_strobj;
    private ArrayList<Course> al_course;
    private ArrayAdapter<CharSequence> spinner_data;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        pt_resolution = new Point();
        SelectedItems = new ArrayList();
        al_strobj = new ArrayList<Star_obj>();
        al_course = new ArrayList<Course>();
        loadImageResources();
        configureSpinner();
        configureSlidingPanel();
        configureViewStubs();
        handleIntent(getIntent());
        toggle_ViewStub();
    }

    protected void configureViewStubs() {
        empty_search = (ViewStub) findViewById(R.id.empty_search);
        empty_star = (ViewStub) findViewById(R.id.empty_star);
    }

    protected void configureSlidingPanel() {
        starpanel = (LinearLayout) findViewById(R.id.panel_view);
        slideupl = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slideupl.setDragView(starpanel);
        slideupl.setOverlayed(true);
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
        searchView.setIconifiedByDefault(false);

//        Action Bar configs
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);

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
                System.out.println("Item selected: " + pos + " with Id: " + id);
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
                Dialog dg = createFilterDialog();
                return true;
            case R.id.action_time_fil:
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
        if(slideupl == null) System.out.println("wtf");
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
            //e_listview.setVisibility(View.GONE);
        } else {
            empty_star.setVisibility(View.GONE);
            //e_listview.setVisibility(View.VISIBLE);
        }
        if(al_course.isEmpty() == true){
            empty_search.setVisibility(View.VISIBLE);
            //t_listview.setVisibility(View.GONE);
        } else {
            empty_search.setVisibility(View.GONE);
            //t_listview.setVisibility(View.VISIBLE);
        }
    }

    public Dialog createFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
        builder.setTitle( Html.fromHtml("<font color='#66FFCC'>General Ed/ Focus Filter</font>"))
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.gefc_list, null,
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
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
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

}
