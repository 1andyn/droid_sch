package uhmanoa.droid_sch;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by supah_000 on 2/15/2015.
 */
public class StarView extends LinearLayout{

    private Star_obj sobj;
    private Context context;

    public StarView (Context con) {
        super(con);
        context = con;
    }

    public void setObj(Star_obj pstar) {
        sobj = pstar;
    }


}
