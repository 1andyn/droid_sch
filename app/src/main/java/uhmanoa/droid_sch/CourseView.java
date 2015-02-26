package uhmanoa.droid_sch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by supah_000 on 2/15/2015.
 */
public class CourseView extends LinearLayout implements App_const{

    private Course crs;
    private Context context;
    private TextView tvCrn, tvTitle, tvCrs, tvProf, tvLoc, tvDay, tvStart, tvEnd, tvLoc2, tvDay2,
            tvStart2, tvEnd2, tvSec, tvCred, tvSeats, tvWait, tvWaita, tvDates, tvPreq, tvFoc;

    public CourseView(Context con) {
        super(con);
        context = con;
        LayoutInflater inflater = (LayoutInflater)con.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.course_view, this, true);
    }

    private void configureTextViews() {
        // Str_Obj Data
        tvCrn = (TextView) findViewById(R.id.tv_crn);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCrs = (TextView) findViewById(R.id.tv_crs);

        tvCrn.setText(ModifySpacingString(String.valueOf(crs.getCrn()), ViewStringCat.crn));
        tvCrs.setText(ModifySpacingString(crs.getCourse(),ViewStringCat.course));
        tvTitle.setText(ModifySpacingString(crs.getTitle(),ViewStringCat.title));

        // Course Specific Data
        tvProf = (TextView) findViewById(R.id.tv_prof);
        tvDay = (TextView) findViewById(R.id.tv_day);
        tvLoc = (TextView) findViewById(R.id.tv_loc);
        tvStart = (TextView) findViewById(R.id.tv_start);
        tvEnd = (TextView) findViewById(R.id.tv_end);

        tvLoc2 = (TextView) findViewById(R.id.tv_loc2);
        tvStart2 = (TextView) findViewById(R.id.tv_start2);
        tvEnd2 = (TextView) findViewById(R.id.tv_end2);
        tvDay2 = (TextView) findViewById(R.id.tv_day2);

        tvSec = (TextView) findViewById(R.id.tv_sect);
        tvCred = (TextView) findViewById(R.id.tv_credits);

        tvSeats = (TextView) findViewById(R.id.tv_seats);
        tvWait = (TextView) findViewById(R.id.tv_wl);
        tvWaita = (TextView) findViewById(R.id.tv_wla);
        tvDates = (TextView) findViewById(R.id.tv_date);
        tvPreq = (TextView) findViewById(R.id.tv_preq);

        tvFoc = (TextView) findViewById(R.id.tv_foc);

        tvProf.setText(ModifySpacingString(String.valueOf(crs.getProfessor()), ViewStringCat.prof));
        tvDay.setText(ModifySpacingString(String.valueOf(crs.getDayString(false)),
                ViewStringCat.day));
        tvLoc.setText(ModifySpacingString(String.valueOf(crs.getRoom1()), ViewStringCat.loc));
        tvStart.setText(ModifySpacingString(String.valueOf(crs.getStartString(false)),
                ViewStringCat.time));
        tvEnd.setText(ModifySpacingString(String.valueOf(crs.getEndString(false)),
                ViewStringCat.time));

        tvCred.setText(String.valueOf(crs.getCredits()));
        tvSec.setText(ModifySpacingString(String.valueOf(crs.getSection()),
                ViewStringCat.sect));

        tvSeats.setText(String.valueOf(crs.getSeats_avail()));
        tvWait.setText(String.valueOf(crs.getWaitlisted()));
        tvWaita.setText(String.valueOf(crs.getWait_avail()));
        tvDates.setText(crs.getDates());
        tvPreq.setText(crs.getPrereq());
        tvFoc.setText(crs.getFocusReqString());

        /* This check sees if there is a second day, if not it hides it */
        if(crs.getStart2() == 9999) {
            TextView tvStartStatic = (TextView)findViewById(R.id.tv_start2_static);
            TextView tvEndStatic = (TextView)findViewById(R.id.tv_end2_static);
            TextView tvLocStatic = (TextView)findViewById(R.id.tv_loc2_static);
            TextView tvDayStatic = (TextView)findViewById(R.id.tv_day2_static);
            final View viewDivider = (View) findViewById(R.id.day2_divider);

            viewDivider.setVisibility(View.GONE);
            tvStartStatic.setVisibility(View.GONE);
            tvEndStatic.setVisibility(View.GONE);
            tvLocStatic.setVisibility(View.GONE);
            tvDayStatic.setVisibility(View.GONE);
            tvLoc2.setVisibility(View.GONE);
            tvStart2.setVisibility(View.GONE);
            tvEnd2.setVisibility(View.GONE);
            tvDay2.setVisibility(View.GONE);
        } else {
            tvLoc2.setText(ModifySpacingString(String.valueOf(crs.getRoom2()), ViewStringCat.loc));
            tvStart2.setText(ModifySpacingString(String.valueOf(crs.getStartString(true)),
                    ViewStringCat.time));
            tvEnd2.setText(ModifySpacingString(String.valueOf(crs.getEndString(true)),
                    ViewStringCat.time));
            tvDay2.setText(ModifySpacingString(String.valueOf(crs.getDayString(true)),
                    ViewStringCat.day));
        }

    }

    public void setObj(Course pcrs) {
        crs = pcrs;
        configureTextViews();
    }

    // Basically return a string with extra spaces so the spacing in the view is consistent
    private String ModifySpacingString(String s, ViewStringCat type) {
        String temp;
        switch (type) {
            case course:
                temp = String.format("%-"+ course_max + "s", s);
                break;
            case crn:
                temp = String.format("%-"+ crn_max + "s", s);
                break;
            case title:
                temp = String.format("%-"+ title_max + "s", s);
                break;
            case loc:
                temp = String.format("%-"+ loc_max + "s", s);
                break;
            case prof:
                temp = String.format("%-"+ prof_max + "s", s);
                break;
            case day:
                temp = String.format("%-"+ day_max + "s", s);
                break;
            case time:
                temp = String.format("%-"+ time_max + "s", s);
                break;
            case sect:
                temp = String.format("%-"+ sec_max + "s", s);
                break;
            default:
                temp = s;
                break;
        }

        return temp;
    }

}
