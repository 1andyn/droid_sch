package uhmanoa.droid_sch;

/**
 * Created by supah_000 on 2/24/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

public class SchListAdapter extends ArrayAdapter<Schedule> {

        static class SchViewHolder {
            ScheduleView sv;
            CheckBox cbx;
            Button btn;
        }


        //Context in which eventListAdapter is being used
        private Context app_Context;
        private ArrayList<Schedule> object_list;
        private ArrayList<Long> checked_list;
        private LayoutInflater inflater;
        private int layout_resrc;
        private OnViewButtonPress listen;

        public SchListAdapter(Context c, int rsrc, ArrayList<Schedule> sch, OnViewButtonPress bp) {
            super(c, rsrc, sch);
            app_Context = c;
            object_list = sch;
            layout_resrc = rsrc;
            this.listen = bp;
            inflater = LayoutInflater.from(c);
            checked_list = new ArrayList<>();
        }

        @Override
        public long getItemId(int pos) {
            return object_list.get(pos).getID();
        }

        @Override
        public View getView(final int pos, View convertView, ViewGroup parent) {
            final Schedule sch = object_list.get(pos);
            final CheckBox cb;
            final Button bt;

            SchViewHolder scv;

            if (convertView == null) {
                scv = new SchViewHolder();

                convertView = inflater.inflate(layout_resrc, parent, false);
                ScheduleView schview = new ScheduleView(app_Context);
                schview.setObj((Schedule) getItem(pos));
                schview.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
                convertView = schview;
                cb = (CheckBox) convertView.findViewById(R.id.check_box);
                bt = (Button) convertView.findViewById(R.id.view_button);

                scv.sv = schview;
                scv.cbx = cb;
                scv.btn = bt;

                convertView.setTag(scv);
            } else {
                scv = (SchViewHolder) convertView.getTag();
                scv.sv.setObj(getItem(pos));
                scv.sv.setBackgroundColor(app_Context.getResources().getColor(R.color.dark_gray));
                convertView = scv.sv;
                cb = scv.cbx;
                bt = scv.btn;
            }

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //This needs to be rewritten to display the specific schedule
                    listen.onViewButtonPress(getItem(pos));
                }
            });

            //checkbox listener
            cb.setChecked(sch.isChecked());
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (buttonView.isChecked()) {
//                        new ToastWrapper(app_Context, "Checked " + sch.getID(),
//                                Toast.LENGTH_SHORT);
                        checked_list.add(object_list.get(pos).getID());
                        sch.setChecked(true);
                    } else {
//                        new ToastWrapper(app_Context, "UnChecked " + sch.getID(),
//                                Toast.LENGTH_SHORT);
                        sch.setChecked(false);
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