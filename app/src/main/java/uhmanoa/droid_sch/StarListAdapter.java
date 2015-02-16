package uhmanoa.droid_sch;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class StarListAdapter<T> extends BaseAdapter {

    //Context in which eventListAdapter is being used
    private Context app_Context;
    private ArrayList<Star_obj> object_list;

    public StarListAdapter(Context c, ArrayList<Star_obj> star_list)
    {
        app_Context = c;
        object_list = star_list;
    }

    @Override
    public int getCount() {
        return object_list.size();
    }

    @Override
    public Object getItem(int pos) {
        return object_list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return object_list.get(pos).getID();
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            StarView sview = new StarView(app_Context);
            sview.setObj((Star_obj)getItem(pos));
            sview.setLongClickable(true);
            sview.setFocusable(true);
            sview.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
            return sview;
        }
        else
        {
            convertView.setLongClickable(true);
            convertView.setFocusable(true);
            convertView.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
            StarView sview = (StarView)convertView;
            sview.setObj((Star_obj)getItem(pos));
            return convertView;
        }
    }


}
