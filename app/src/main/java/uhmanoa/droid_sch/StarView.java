package uhmanoa.droid_sch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by supah_000 on 2/15/2015.
 */
public class StarView extends LinearLayout implements App_const{

    private Star_obj sobj;
    private Context context;
    private TextView tvCrn, tvTitle, tvCrs;

    public StarView (Context con) {
        super(con);
        context = con;
        LayoutInflater inflater = (LayoutInflater)con.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.star_view, this, true);
    }

    private void configureTextViews() {
        tvCrn = (TextView) findViewById(R.id.tv_crn);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCrs = (TextView) findViewById(R.id.tv_crs);

        CheckBox cb = (CheckBox) findViewById(R.id.check_box_star);
        if(sobj.isChecked()) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }

        if(sobj.isClass()) {
            tvCrn.setText(ModifySpacingString(String.valueOf(sobj.getCRN()), ViewStringCat.crn));
        } else {
            tvCrn.setText(ModifySpacingString("N/A", ViewStringCat.crn));
        }

        tvCrs.setText(ModifySpacingString(sobj.getCourse(),ViewStringCat.course));
        tvTitle.setText(ModifySpacingString(sobj.getCourseTitle(),ViewStringCat.title));
    }

    public void setObj(Star_obj pstar) {
        sobj = pstar;
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
            default:
                temp = s;
                break;
        }
        return temp;
    }

}
