package uhmanoa.droid_sch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by supah_000 on 4/11/2015.
 */
public class ToastWrapper {
    public ToastWrapper(Context c, String toast_text, int duration) {
        LayoutInflater inf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.custom_toast, null);

        TextView t = (TextView) v.findViewById(R.id.toast_text);
        t.setText(toast_text);
        Toast tst = new Toast(c);
        tst.setDuration(duration);
        tst.setView(v);
        tst.show();
    }
}
