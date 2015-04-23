package uhmanoa.droid_sch;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by supah_000 on 2/15/2015.
 */
public class ScheduleView extends LinearLayout {

    private Schedule sched;
    private Context context;
    private LinearLayout ll_crs_list, ll_time_list, ll_day_list;
    private int font_size = 14;

    public ScheduleView(Context con) {
        super(con);
        context = con;
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sch_view, this, true);

        //get Font size to ensure the text fits
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x < 1440) {
            font_size = 12;
        }
    }


    private void configureTextViews() {
        // Linear Layouts
        ll_crs_list = (LinearLayout) findViewById(R.id.crs_list);
        ll_time_list = (LinearLayout) findViewById(R.id.time_list);
        ll_day_list = (LinearLayout) findViewById(R.id.day_list);

        ll_crs_list.removeAllViews();
        ll_time_list.removeAllViews();
        ll_day_list.removeAllViews();

        ArrayList<Course> crs = sched.getCourses();
        for(int x = 0; x < sched.getCourses().size(); x++){
            Course c = crs.get(x);

            TextView tvcrs = produceFormattedTV(c.getCourse());
            tvcrs.setTextColor(getResources().getColor(R.color.dark_aqua));

            TextView tvtime = produceFormattedTV(c.getTimeString(false));
            tvtime.setTextColor(getResources().getColor(R.color.white));

            TextView tvday = produceFormattedTV(c.getDayString(false));
            tvday.setTextColor(getResources().getColor(R.color.dark_aqua));

            ll_crs_list.addView(tvcrs);
            ll_time_list.addView(tvtime);
            ll_day_list.addView(tvday);

            if(c.getStart2() != 9999) {

                TextView tvc = produceFormattedTV(c.getCourse()+"*");
                tvc.setTextColor(getResources().getColor(R.color.dark_aqua));

                TextView tvt = produceFormattedTV(c.getTimeString(true));
                tvt.setTextColor(getResources().getColor(R.color.white));

                TextView tvd = produceFormattedTV(c.getDayString(true));
                tvd.setTextColor(getResources().getColor(R.color.dark_aqua));

                ll_crs_list.addView(tvc);
                ll_time_list.addView(tvt);
                ll_day_list.addView(tvd);

            }
        }
    }

    private TextView produceFormattedTV(String input) {
        TextView text = new TextView(context);
        text.setText(input);
        text.setTextSize(font_size);
        text.setTypeface(null, Typeface.BOLD);
        return text;
    }

    public void setObj(Schedule sch) {
        sched = sch;
        configureTextViews();
    }

}
