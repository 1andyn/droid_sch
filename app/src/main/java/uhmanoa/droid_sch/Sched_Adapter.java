package uhmanoa.droid_sch;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uhmanoa.droid_sch.Course;
import uhmanoa.droid_sch.R;
import uhmanoa.droid_sch.Schedule;

/**
 * Created by LENOVO on 2/11/2015.
 */
public class Sched_Adapter extends BaseAdapter {

    public ArrayList<String> titles;
    public ArrayList<String> subtitles;
    public ArrayList<Schedule> schedules;
    public Context context;

    public Sched_Adapter(Context c, ArrayList<String> t, ArrayList<String> s){
        this.context = c;
        this.titles = t;
        this.subtitles = s;

    }

    public Sched_Adapter(Context c, ArrayList<Schedule> s){
        this.context = c;
        this.schedules = s;
    }

    @Override
    public int getCount() {
        return schedules.size();
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

        //holder.title.setText(titles.get(position).toString());
        //holder.subtitle.setText(subtitles.get(position).toString());
        holder.title.setText("Schedule " + (position + 1));
        String classes = "";
        Schedule current = schedules.get(position);
        for (Course c : current.getCourses()){
            String time1 = c.getTimeString(false);
            if(c.getStart1() == -1) {
                time1 = "TBA";
            }
            classes += c.getCourse() + "     " + c.getCrn()  + "     " + time1 + "     "
                    + c.getDayString(false) + " \n";

            if(c.getStart2() != 9999) {
                String time2 = c.getTimeString(true);
                if (c.getStart2() == -1) {
                    time2 = "TBA";
                }
                classes += "---" + "     "  + time2 + "     " + c.getDayString(
                        true) + " \n";
            }

        }
        holder.subtitle.setText(classes);

        return convertView;
    }

    public class ViewHolder{
        TextView title;
        TextView subtitle;
    }
}
