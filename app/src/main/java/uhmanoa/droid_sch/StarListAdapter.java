package uhmanoa.droid_sch;

import java.util.ArrayList;

import android.content.Context;
import android.text.Layout;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class StarListAdapter extends ArrayAdapter<Star_obj> {

    //Context in which eventListAdapter is being used
    private Context app_Context;
    private ArrayList<Star_obj> object_list;
    private ArrayList<Long> checked_list;
    private LayoutInflater inflater;
    private int layout_resrc;

    public StarListAdapter(Context c, int rsrc, ArrayList<Star_obj> star_list) {
        super(c, rsrc, star_list);
        app_Context = c;
        object_list = star_list;
        layout_resrc = rsrc;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public long getItemId(int pos) {
        return object_list.get(pos).getID();
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(layout_resrc, parent, false);
            StarView sview = new StarView(app_Context);
            sview.setObj((Star_obj) getItem(pos));
            sview.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
            convertView = sview;

        } else {
            convertView.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
        }

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.chk_star);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (buttonView.isChecked()) {
                    Toast.makeText(app_Context, "Checked " + pos,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(app_Context, "UnChecked " + pos,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }


}

