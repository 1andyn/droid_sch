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
        checked_list = new ArrayList<Long>();
    }

    @Override
    public long getItemId(int pos) {
        return object_list.get(pos).getID();
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        final Star_obj so = object_list.get(pos);
        final CheckBox cb;

        if (convertView == null) {
            convertView = inflater.inflate(layout_resrc, parent, false);
            StarView sview = new StarView(app_Context);
            sview.setObj((Star_obj) getItem(pos));
            sview.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));

            convertView = sview;

            cb = (CheckBox) convertView.findViewById(R.id.chk_star);
            convertView.setTag(cb);
        } else {
            convertView.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));

            cb = (CheckBox) convertView.getTag();
        }


        cb.setChecked(so.isChecked());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (buttonView.isChecked()) {
                    Toast.makeText(app_Context, "Checked " + so.getID() + " " + so.getCRN(),
                            Toast.LENGTH_SHORT).show();
                    checked_list.add(object_list.get(pos).getID());
                    so.setChecked(true);
                } else {
                    Toast.makeText(app_Context, "UnChecked " + so.getID() + " " + so.getCRN(),
                            Toast.LENGTH_SHORT).show();
                    so.setChecked(false);
                    checkedRemove(object_list.get(pos).getID());
                }
            }
        });
        return convertView;
    }

    public ArrayList<Long> getChecked_list() {
        return checked_list;
    }

    public void checkedRemove(long id) {
        for(int x = 0; x < checked_list.size(); x++) {
            Long temp = checked_list.get(x);
            if(temp.equals(id)) {
                checked_list.remove(x);
            }
        }
    }

    public void clearCheckedList() {
        checked_list.clear();
    }

}

