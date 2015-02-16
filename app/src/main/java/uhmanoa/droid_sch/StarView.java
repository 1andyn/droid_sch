package uhmanoa.droid_sch;

import android.content.Context;
import android.view.LayoutInflater;
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

        tvCrn.setText(ModifySpacingString(String.valueOf(sobj.getCRN()),ViewStringCat.crn));
        tvCrs.setText(ModifySpacingString(sobj.getCourse(),ViewStringCat.course));
        tvTitle.setText(ModifySpacingString(sobj.getCourseTitle(),ViewStringCat.title));
    }

    public void setObj(Star_obj pstar) {
        sobj = pstar;
        configureTextViews();
    }

    // Basically return a string with extra spaces so the spacing in the view is consistent
    private String ModifySpacingString(String s, ViewStringCat type) {
        int size = s.length();
        int update = 0;
        String temp = s;
        switch (type) {
            case course:
                update = course_max - size;
            case crn:
                update = crn_max - size;
            case title:
                update = title_max - size;
        }

        //Only modify string if we want to increase the space count
        if(update > 0) temp.format(s, "%" + update + "s");
        return temp;
    }

}
