package uhmanoa.droid_sch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

public class ResultListAdapter extends ArrayAdapter<Course> {

    static class ViewHolder {
        CourseView cv;
        CheckBox cbx;
    }

    //Context in which eventListAdapter is being used
    private Context app_Context;
    private ArrayList<Course> object_list;
    private ArrayList<Long> checked_list;
    private LayoutInflater inflater;
    private int layout_resrc;

    public ResultListAdapter(Context c, int rsrc, ArrayList<Course> star_list) {
        super(c, rsrc, star_list);
        app_Context = c;
        object_list = star_list;
        layout_resrc = rsrc;
        inflater = LayoutInflater.from(c);
        checked_list = new ArrayList<>();
    }

    @Override
    public long getItemId(int pos) {
        return object_list.get(pos).getID();
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        final Course crs = object_list.get(pos);
        final CheckBox cb;

        ViewHolder v;

        if (convertView == null) {

            v = new ViewHolder();
            convertView = inflater.inflate(layout_resrc, parent, false);
            CourseView crsview = new CourseView(app_Context);
            crsview.setObj((Course) getItem(pos));
            crsview.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
            convertView = crsview;
            v.cv = crsview;

            cb = (CheckBox) convertView.findViewById(R.id.chk_star);
            v.cbx = cb;
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
            v.cv.setObj(getItem(pos));
            v.cv.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
            convertView = v.cv;
            cb = v.cbx;
        }

        cb.setChecked(crs.isChecked());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (buttonView.isChecked()) {
//                    Toast.makeText(app_Context, "Checked " + crs.getID() + " " + crs.getCrn(),
//                            Toast.LENGTH_SHORT).show();
                    checked_list.add(object_list.get(pos).getID());
                    crs.setChecked(true);
                } else {
//                    Toast.makeText(app_Context, "UnChecked " + crs.getID() + " " + crs.getCrn(),
//                            Toast.LENGTH_SHORT).show();
                    crs.setChecked(false);
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

