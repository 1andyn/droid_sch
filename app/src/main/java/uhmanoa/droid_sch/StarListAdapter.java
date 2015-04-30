package uhmanoa.droid_sch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

public class StarListAdapter extends ArrayAdapter<Star_obj> {

    static class StarHolder {
        StarView sv;
        CheckBox cbx;
    }

    //Context in which eventListAdapter is being used
    private Context app_Context;
    private ArrayList<Star_obj> object_list;
    private ArrayList<Long> checked_list;
    private LayoutInflater inflater;
    private int layout_resrc;

    public StarListAdapter(Context c, int rsrc, ArrayList<Star_obj> star_list,
                           ArrayList<Long> checked) {
        super(c, rsrc, star_list);
        app_Context = c;
        object_list = star_list;
        layout_resrc = rsrc;
        inflater = LayoutInflater.from(c);
        checked_list = checked;
    }

    @Override
    public long getItemId(int pos) {
        return object_list.get(pos).getID();
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        final Star_obj so = object_list.get(pos);
        final CheckBox cb;

        StarHolder sh;

        if (convertView == null) {
            sh = new StarHolder();
            convertView = inflater.inflate(layout_resrc, parent, false);
            StarView sview = new StarView(app_Context);
            sview.setObj((Star_obj) getItem(pos));
            sview.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
            convertView = sview;
            sh.sv = sview;

            cb = (CheckBox) convertView.findViewById(R.id.check_box_star);
            sh.cbx = cb;

            convertView.setTag(sh);
        } else {
            sh = (StarHolder) convertView.getTag();
            sh.sv.setObj(getItem(pos));
            sh.sv.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
            convertView = sh.sv;
            cb = sh.cbx;
        }

        if(checked_list.contains(so.getID())) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (buttonView.isChecked()) {
                    checked_list.add(object_list.get(pos).getID());
                    so.setChecked(true);
                } else {
                    so.setChecked(false);
                    checked_list.remove(object_list.get(pos).getID());
                }
            }
        });
        return convertView;
    }
}

