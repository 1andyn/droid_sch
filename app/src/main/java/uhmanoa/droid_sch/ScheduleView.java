package uhmanoa.droid_sch;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by supah_000 on 2/15/2015.
 */
public class ScheduleView extends LinearLayout {

    private Schedule sched;
    private Context context;
    private LinearLayout ll_crs_list, ll_crn_list;

    public ScheduleView(Context con) {
        super(con);
        context = con;
        LayoutInflater inflater = (LayoutInflater)con.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sch_view, this, true);
    }

    private void configureTextViews() {
        // Linear Layouts
        ll_crs_list = (LinearLayout) findViewById(R.id.crs_list);
        ll_crn_list = (LinearLayout) findViewById(R.id.crn_list);
        ll_crn_list.removeAllViews();
        ll_crs_list.removeAllViews();

        ArrayList<Course> crs = sched.getCourses();
        for(int x = 0; x < sched.getCourses().size(); x++){
            String course = crs.get(x).getCourse();
            TextView tvcrs = new TextView(context);
            tvcrs.setText(course);
            tvcrs.setTextSize(14);
            tvcrs.setTypeface(null, Typeface.BOLD);
            tvcrs.setTextColor(getResources().getColor(R.color.dark_aqua));

            TextView tvcrn = new TextView(context);
            tvcrn.setText(String.valueOf(crs.get(x).getCrn()));
            tvcrn.setTextSize(14);
            tvcrn.setTypeface(null, Typeface.BOLD);
            tvcrn.setTextColor(getResources().getColor(R.color.white));

            ll_crs_list.addView(tvcrs);
            ll_crn_list.addView(tvcrn);
        }

        TextView tv = (TextView) findViewById(R.id.text_year_sem);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(14);

        String sem = "SEM";
        switch (sched.getSemester()) {
            case 0: sem = "FALL"; break;
            case 1: sem = "SPRG"; break;
            case 2: sem = "SUMR"; break;
        }

        tv.setText(sem + " " + String.valueOf(sched.getYear()));

    }

    public void setObj(Schedule sch) {
        sched = sch;
        configureTextViews();
    }

}
