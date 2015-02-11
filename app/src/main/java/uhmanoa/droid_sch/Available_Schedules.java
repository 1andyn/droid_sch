package uhmanoa.droid_sch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


public class Available_Schedules extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available__schedules);

        setBackground();
    }

    private void setBackground(){
        Resources res_main = getResources();
        Point pt_resolution = new Point();
        Display dsp = getWindowManager().getDefaultDisplay();
        pt_resolution.x = dsp.getWidth();
        pt_resolution.y = dsp.getHeight();

        LinearLayout avail_sched = (LinearLayout) findViewById(R.id.ll_avail_sched);

        Bitmap bmp_mmbg = ImgLoader.decodedSampledBitmapResource(res_main, R.drawable.mm_bg,
                pt_resolution.x / 8, pt_resolution.y / 8);
        BitmapDrawable drw_bg = new BitmapDrawable(bmp_mmbg);
        avail_sched.setBackgroundDrawable(drw_bg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_available__schedules, menu);
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
