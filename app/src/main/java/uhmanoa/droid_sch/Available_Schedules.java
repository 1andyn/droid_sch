package uhmanoa.droid_sch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import uhmanoa.droid_sch.uhmanoa.adapters.Sched_Adapter;


public class Available_Schedules extends ActionBarActivity implements View.OnClickListener{

    public static final String LOGTAG = "SCHED";
    public static final int ITEMS_PER_PAGE = 5;

    ArrayList<String> titles, t1;
    ArrayList<String> subtitles, s1;
    ListView lv_item;
    Button btnPrev, btnNext;
    Sched_Adapter adapter;

    int totalPages, currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_schedules);

        setBackground();
        initLayout();
    }

    private void initLayout(){
        lv_item = (ListView) findViewById(R.id.lvScheds);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnNext = (Button) findViewById(R.id.btn_next);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        titles = new ArrayList<String>();
        subtitles = new ArrayList<String>();

        for (int i = 0; i < 17; i++){
            titles.add("Sched" + i);
            subtitles.add("Num classes in sched " + i);

        }

        totalPages = 0;
        currentPage = 0;

        // find out how many pages we have
        totalPages = titles.size() / ITEMS_PER_PAGE;
        if ((titles.size() % ITEMS_PER_PAGE) != 0)
            totalPages ++;

        populateNextPage();

        Log.w(LOGTAG, "Set items in adapter");
    }

    private void populateNextPage(){
        t1 = new ArrayList<String>(ITEMS_PER_PAGE);
        s1 = new ArrayList<String>(ITEMS_PER_PAGE);

        int itemsOnPage = ITEMS_PER_PAGE;
        if (currentPage == totalPages - 1)
            itemsOnPage = titles.size() % ITEMS_PER_PAGE;

        for (int i = (currentPage * ITEMS_PER_PAGE); i < ((currentPage * ITEMS_PER_PAGE) + itemsOnPage); i++){
            t1.add(titles.get(i).toString());
            s1.add(subtitles.get(i).toString());
        }

        adapter = new Sched_Adapter(this, t1, s1);
        lv_item.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
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
        }
    }
}
