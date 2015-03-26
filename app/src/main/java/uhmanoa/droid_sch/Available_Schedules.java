package uhmanoa.droid_sch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
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

import uhmanoa.droid_sch.uhmanoa.adapters.Sched_Adapter;


public class Available_Schedules extends ActionBarActivity implements View.OnClickListener{

    public static final String LOGTAG = "SCHED";
    public static final String CONFIRM_SAVE = "Warning: By clicking ok you will save the selected " +
            "schedules and discard all other schedules.  Are you sure you want to continue?";
    public static final int ITEMS_PER_PAGE = 5;

    ArrayList<String> titles, t1;
    ArrayList<String> subtitles, s1;
    ListView lv_item;
    Button btnPrev, btnNext, btnGoto, btnSave;
    TextView tvTitle;
    Sched_Adapter adapter;

    ArrayList<Schedule> schedules, schedPage;

    int totalPages, currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_schedules);

        setBackground();
        populateList();
        initLayout();
        populateNextPage();

    }

    private void setBackground(){
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

    private void initLayout(){
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

        tvTitle.setText("Your search generated " + titles.size() + " schedules.");
        updateGotoButton();
    }

    private void populateList(){
        titles = new ArrayList<String>();
        subtitles = new ArrayList<String>();

        schedules = new ArrayList<Schedule>();

        for (int i = 0; i < 17; i++){
            titles.add("Sched" + i);
            subtitles.add("Num classes in sched " + i);
            Schedule s = new Schedule();
            for (int j = 0; j < 4; j++) {
                ArrayList<String> days = new ArrayList<String>();
                days.add("M");
                days.add("W");
                days.add("F");
                Course c = new Course("EE" + j, "Stuff", 39390 + j, 3, "Professor " + j,
                        days);// 1230 + j, 1320+ j, "POST" + j);
                s.addCourse(c);
            }
            schedules.add(s);
        }

        totalPages = 0;
        currentPage = 0;

        // find out how many pages we have
        totalPages = schedules.size() / ITEMS_PER_PAGE;
        if ((schedules.size() % ITEMS_PER_PAGE) != 0)
            totalPages ++;
    }

    private void populateNextPage(){
        t1 = new ArrayList<String>(ITEMS_PER_PAGE);
        s1 = new ArrayList<String>(ITEMS_PER_PAGE);

        schedPage = new ArrayList<Schedule>(ITEMS_PER_PAGE);

        int itemsOnPage = ITEMS_PER_PAGE;
        if (currentPage == totalPages - 1)
            itemsOnPage = titles.size() % ITEMS_PER_PAGE;

        for (int i = (currentPage * ITEMS_PER_PAGE); i < ((currentPage * ITEMS_PER_PAGE) + itemsOnPage); i++){
            t1.add(titles.get(i).toString());
            s1.add(subtitles.get(i).toString());
            schedPage.add(schedules.get(i));
        }

        Log.e(LOGTAG, "Size! " + schedules.size());
        adapter = new Sched_Adapter((Activity) this, schedPage);
        lv_item.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateGotoButton();
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
            case R.id.btn_goto:
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                AlertDialog.Builder pagenum = new AlertDialog.Builder(this)
                        .setTitle("Go To...")
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
                                    Toast.makeText(getApplicationContext(), "You must enter a page number in the valid range from 1 to " + totalPages , Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                pagenum.show();

                break;
            case R.id.btn_save_selected:
                AlertDialog.Builder confirm = new AlertDialog.Builder(this)
                        .setTitle("Confirm?")
                        .setMessage(CONFIRM_SAVE)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                confirm.show();

                break;
        }
    }

    private void updateGotoButton(){
        btnGoto.setText("Page \t" + (currentPage + 1) + " / " + totalPages);
    }
}
