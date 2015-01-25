package uhmanoa.droid_sch;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class main_menu extends Activity {

    private Drawable drw_bg;
    private Resources res_main;
    private Point pt_resolution;
    private ArrayList<Button> arraylst_btn;

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
        arraylst_btn.add((Button) findViewById(R.id.bt_create));
        arraylst_btn.add((Button) findViewById(R.id.bt_view));
        arraylst_btn.add((Button) findViewById(R.id.bt_search));
        arraylst_btn.add((Button) findViewById(R.id.bt_about));
        arraylst_btn.add((Button) findViewById(R.id.bt_exit));
        configureBtnListeners();
    }

    protected void configureBtnListeners() {

        arraylst_btn.get(app_const.buttons.create.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(main_menu.this, "Create Button Pressed",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        arraylst_btn.get(app_const.buttons.view.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(main_menu.this, "View Button Pressed",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        arraylst_btn.get(app_const.buttons.search.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(main_menu.this, "Search Button Pressed",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        arraylst_btn.get(app_const.buttons.about.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(main_menu.this, "About Button Pressed",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        arraylst_btn.get(app_const.buttons.exit.ordinal()).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        main_menu.this.finish();
                    }
                });


    }

    protected void loadImageResources() {
        acquireResolution();
        res_main = getResources();

        ImageView iv_mmlogo, iv_mmtitle;
        RelativeLayout ll_mainlayout;

        Bitmap bmp_mmlogo = imgLoader.decodedSampledBitmapResource(res_main, R.drawable.mm_logo,
                pt_resolution.x, pt_resolution.y);
        Bitmap bmp_mmtitle = imgLoader.decodedSampledBitmapResource(res_main, R.drawable.mm_title,
                pt_resolution.x, pt_resolution.y);
        Bitmap bmp_mmbg = imgLoader.decodedSampledBitmapResource(res_main, R.drawable.mm_bg,
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
//        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
}
