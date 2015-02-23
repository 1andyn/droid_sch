package uhmanoa.droid_sch.uhmanoa.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uhmanoa.droid_sch.R;

/**
 * Created by LENOVO on 2/11/2015.
 */
public class Sched_Adapter extends BaseAdapter {

    public ArrayList<String> titles;
    public ArrayList<String> subtitles;
    public Context context;

    public Sched_Adapter(Context c, ArrayList<String> t, ArrayList<String> s){
        Log.e("ADAPTER", "Created Adapter");
        this.context = c;
        this.titles = t;
        this.subtitles = s;

    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_available_schedules, parent, false);

            holder.title = (TextView) convertView.findViewById(R.id.tv_sched_title);
            holder.subtitle = (TextView) convertView.findViewById(R.id.tv_sched_subtitle);

            convertView.setTag(holder);

        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(titles.get(position).toString());
        //Log.e("ADAPTER", "item: " + titles.get(position).toString());
        holder.subtitle.setText(subtitles.get(position).toString());

        return convertView;
    }

    public class ViewHolder{
        TextView title;
        TextView subtitle;
    }
}
