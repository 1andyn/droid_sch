package uhmanoa.droid_sch;

import android.app.Activity;
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
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.jsoup.HttpStatusException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.MissingFormatArgumentException;

public class Main_menu extends Activity {

    private Drawable drw_bg;
    private Resources res_main;
    private Point pt_resolution;
    private ArrayList<Button> arraylst_btn;
    private ArrayList<Boolean> available;
    private int curr_year;
    final int FALL = 0;
    final int SPRING = 1;
    final int SUMMER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        pt_resolution = new Point();
        loadImageResources();
        populate_btncontainer();
    }

    protected void populate_btncontainer() {
        arraylst_btn = new ArrayList<Button>();
        available = new ArrayList<>();
        arraylst_btn.add((Button) findViewById(R.id.bt_create));
        arraylst_btn.add((Button) findViewById(R.id.bt_view));
        arraylst_btn.add((Button) findViewById(R.id.bt_search));
        arraylst_btn.add((Button) findViewById(R.id.bt_pref));
        arraylst_btn.add((Button) findViewById(R.id.bt_exit));
        Calendar curr_time = Calendar.getInstance();
        curr_year = curr_time.get(Calendar.YEAR);
        configureBtnListeners();
    }

    protected void configureBtnListeners() {

        arraylst_btn.get(App_const.buttons.create.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            checkAvailability();
                            Dialog d = createSemesterDialog(0);
                            d.show();
                        } catch (Exception e) {
                            Toast.makeText(Main_menu.this, "Unable to access course data online, try again later.",
                                    Toast.LENGTH_SHORT).show();
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
                            checkAvailability();
                            Dialog d = createSemesterDialog(2);
                            d.show();
                        } catch (Exception e) {
                            Toast.makeText(Main_menu.this, "Unable to access course data online, try again later.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        arraylst_btn.get(App_const.buttons.about.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Main_menu.this, Preferences.class);
                        startActivity(i); //Preferences hasn't been implemented yet
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
        int yr, yr2 = 0;
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

        List<String> lst = new ArrayList<String>();
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
        Parser prs = new Parser();
        available.add(prs.yearDataReadable(curr_year, FALL));
        available.add(prs.yearDataReadable(curr_year, SPRING));
        available.add(prs.yearDataReadable(curr_year, SUMMER));
    }

    private void switchActivity(int activity, int sem) {
        Intent i;
        switch (activity) {
            case 0:
                i = new Intent(Main_menu.this, Builder.class);
                break;
            case 1:
                i = new Intent(Main_menu.this, Viewer.class);
                break;
            case 2:
                i = new Intent(Main_menu.this, Search.class);
                break;
            default:
                i = new Intent(Main_menu.this, Builder.class);
                break; //
        }
        Bundle b = new Bundle();
        b.putInt("SEMESTER", sem);
        i.putExtras(b);
        startActivity(i);
    }

    private Dialog createSemesterDialog(int button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main_menu.this);

        String data[] = calculateSemString();

        final int activity = button;
        builder.setTitle("Choose Semester")
                .setItems(data, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item

                        //vieeer activity doesn't need internet
                        if (available.get(which) == false) {
                            Toast.makeText(Main_menu.this, "Course data is currently " +
                                            "unavailable for that semester.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            switchActivity(activity, which);
                        }
                    }
                });
        return builder.create();
    }

}
