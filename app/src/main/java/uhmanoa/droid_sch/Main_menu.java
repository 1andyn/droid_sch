package uhmanoa.droid_sch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main_menu extends ActionBarActivity implements OnCheckTaskComplete{

    private Drawable drw_bg;
    private Resources res_main;
    private Point pt_resolution;
    private ArrayList<Button> arraylst_btn;
    private ArrayList<Boolean> available;
    private int curr_year, curr_year2;
    final int FALL = 0;
    final int SPRING = 1;
    final int SUMMER = 2;
    private ProgressDialog pg;
    ParserDataCheck prs;
    Dialog d;
    protected SQL_DataSource datasource;
    PreferencesHelper ph = new PreferencesHelper(this);

    BuilderOptions bos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        pt_resolution = new Point();
        loadImageResources();
        populate_btncontainer();
        pg = new ProgressDialog(Main_menu.this);
        pg.setMessage("Checking course data availability...");
        pg.setCancelable(false);
        Calendar curr_time = Calendar.getInstance();
        curr_year = curr_time.get(Calendar.YEAR);
        datasource = new SQL_DataSource(this);
        datasource.open();

        bos = new BuilderOptions(getApplicationContext());
        if (bos.isFirstUse()) {
            ph.createPreferencesFile(getString(R.string.spin_default_profile), bos);
            bos.setSelectedOption(0);
            bos.setNotFirst();
        }

    }

    private void showHelp(){

        AlertDialog.Builder help = new AlertDialog.Builder(Main_menu.this);
        LayoutInflater inflater = this.getLayoutInflater();
        help.setTitle(Html.fromHtml("<font color='#66FFCC'>" +
                    getApplicationContext().getString(R.string.help_main_title) + "</font>"))
                .setView(inflater.inflate(R.layout.dialog_help_main, null))
                .setPositiveButton("OK", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        CheckBox cb = (CheckBox) ((AlertDialog)dialog).findViewById(R.id.chkDontShow);
                        bos.setShowHelpMain(!cb.isChecked());
                        return;
                    }
                })
                .show();

    }

    private void checkPrefFiles(){
        ArrayList<String> names = ph.getPreferenceFiles();
        String prefs = "";
        for (String s : names)
            prefs += s + " ";
        Toast.makeText(this, "Pref files: " + prefs, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume()
    {
        datasource.open();
        super.onResume();

        if (bos.getShowHelpMain())
            showHelp();
    }

    @Override
    protected void onPause()
    {
        datasource.close();
        super.onPause();
    }

    protected void populate_btncontainer() {

        arraylst_btn = new ArrayList<>();
        available = new ArrayList<>();
        arraylst_btn.add((Button) findViewById(R.id.bt_create));
        arraylst_btn.add((Button) findViewById(R.id.bt_view));
        arraylst_btn.add((Button) findViewById(R.id.bt_search));
        arraylst_btn.add((Button) findViewById(R.id.bt_pref));
        arraylst_btn.add((Button) findViewById(R.id.bt_exit));

        configureBtnListeners();
    }

    protected void configureBtnListeners() {

        arraylst_btn.get(App_const.buttons.create.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            d = createSemesterDialog(0);
                        } catch (Exception e) {
                            new ToastWrapper(Main_menu.this, "Unable to access course data online, try again later.",
                                    Toast.LENGTH_SHORT);
                        }
                    }
                });

        arraylst_btn.get(App_const.buttons.view.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchActivity(1, -1); //semester doesn't matter for viewer,
                        //it will show all results regardless
                    }
                });

        arraylst_btn.get(App_const.buttons.search.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            d = createSemesterDialog(2);
                        } catch (Exception e) {
                            new ToastWrapper(Main_menu.this, "Unable to access course data online, try again later.",
                                    Toast.LENGTH_SHORT);
                        }
                    }
                });

        arraylst_btn.get(App_const.buttons.prefs.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent prefIntent = new Intent(Main_menu.this, Preferences.class);
                        startActivity(prefIntent);

                    }
                });

        arraylst_btn.get(App_const.buttons.exit.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Main_menu.this.finish();
                    }
                });


    }

    protected void loadImageResources() {
        acquireResolution();
        res_main = getResources();

        ImageView iv_mmlogo, iv_mmtitle;
        RelativeLayout ll_mainlayout;

        Bitmap bmp_mmlogo = ImgLoader.decodedSampledBitmapResource(res_main, R.drawable.mm_logo,
                pt_resolution.x, pt_resolution.y);
        Bitmap bmp_mmtitle = ImgLoader.decodedSampledBitmapResource(res_main, R.drawable.mm_title,
                pt_resolution.x, pt_resolution.y);
        Bitmap bmp_mmbg = ImgLoader.decodedSampledBitmapResource(res_main, R.drawable.mm_bg,
                pt_resolution.x / 8, pt_resolution.y / 8); //reduces size of file by factor of 8

        iv_mmlogo = (ImageView) findViewById(R.id.img_logo);
        iv_mmlogo.setImageBitmap(bmp_mmlogo);

        iv_mmtitle = (ImageView) findViewById(R.id.img_title);
        iv_mmtitle.setImageBitmap(bmp_mmtitle);

        ll_mainlayout = (RelativeLayout) findViewById(R.id.mm_layout);
        drw_bg = new BitmapDrawable(bmp_mmbg);
        ll_mainlayout.setBackgroundDrawable(drw_bg);
    }

    protected void acquireResolution() {
        Display dsp = getWindowManager().getDefaultDisplay();
        pt_resolution.x = dsp.getWidth();
        pt_resolution.y = dsp.getHeight();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }


    private String[] calculateSemString() {

        Calendar rightNow = Calendar.getInstance();
        int month = rightNow.get(Calendar.MONTH);
        int yr, yr2;
        //yr = fall, yr2 = spring, summer

        //ALWAYS USE CURRENT YEAR FOR FALL (might be unavail)
        //If month is BEFORE SEPTEMBER,
        // USE CURRENT YEAR SPRING, CURRENT YEAR SUMMER
        // ELSE USE NEXT YEARS'S SPRING/SUMMER for YEAR

        int current_year = rightNow.get(Calendar.YEAR);
        yr = current_year;

        if (month <= Calendar.SEPTEMBER) {
            yr2 = current_year;
        } else {
            yr2 = current_year + 1;
        }

        curr_year2 = yr2;

        List<String> lst = new ArrayList<>();
        String fall = "FALL " + String.valueOf(yr);
        lst.add(fall);
        String spring = "SPRING " + String.valueOf(yr2);
        lst.add(spring);
        String summer = "SUMMER " + String.valueOf(yr2);
        lst.add(summer);

        String[] semesters = lst.toArray(new String[lst.size()]);
        return semesters;
    }

    private void checkAvailability() {
        prs = new ParserDataCheck(pg, this);
        prs.execute(curr_year, FALL, curr_year2, SPRING, curr_year2, SUMMER);

    }

    private void switchActivity(int activity, int sem) {
        Intent i;
        int activity_code = 0;
        switch (activity) {
            case 0:
                i = new Intent(Main_menu.this, Builder.class);
                activity_code = 0;
                break;
            case 1:
                i = new Intent(Main_menu.this, Viewer.class);
                activity_code = 1;
                break;
            case 2:
                activity_code = 2;
                i = new Intent(Main_menu.this, Search.class);
                break;
            default:
                i = new Intent(Main_menu.this, Builder.class);
                break; //
        }
        Bundle b = new Bundle();
        Calendar rightNow = Calendar.getInstance();
        int month = rightNow.get(Calendar.MONTH);
        int current_year = rightNow.get(Calendar.YEAR);
        b.putInt("SEMESTER", sem);
        b.putInt("YEAR", current_year);
        b.putInt("MONTH", month);
        i.putExtras(b);
        startActivityForResult(i, activity_code);
    }

    protected void onActivityResult(int req, int res, Intent data) {
        switch(req) {
            case 2:
                if(res == RESULT_OK) {
                    Bundle results = data.getExtras();
                    int sem = Integer.valueOf(results.getString("SEM_KEY"));
                    switchActivity(0, sem);
                }
        }
    }



    private Dialog createSemesterDialog(int button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main_menu.this);

        String data[] = calculateSemString();
        checkAvailability();

        final int activity = button;
        builder.setTitle(Html.fromHtml("<font color='#66FFCC'>Choose a Semester</font>"))
                .setItems(data, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item

                        //vieeer activity doesn't need internet
                        if (available.get(which) == false) {
                            new ToastWrapper(Main_menu.this, "Course data is currently " +
                                            "unavailable for that semester.",
                                    Toast.LENGTH_SHORT);
                        } else {
                            switchActivity(activity, which);
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onCheckTaskComplete() {
        available = prs.getDataStatus();
        d.show();
//        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider",
//                null, null);
//        View dv = d.findViewById(dividerId);
//        dv.setBackgroundColor(getResources().getColor(R.color.aqua));

        if(prs.getException() != null) {
            new ToastWrapper(Main_menu.this, "Warning: the class availability website did not respond " +
                            " in a timely manner.",
                    Toast.LENGTH_LONG);
        }
        prs = null; //deference
    }
}
