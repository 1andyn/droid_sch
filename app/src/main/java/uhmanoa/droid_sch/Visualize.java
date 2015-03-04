package uhmanoa.droid_sch;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Visualize extends Activity {
    TableLayout table_layout;

    ArrayList<String> time_values;
    ArrayList<String> day_values;

    final int hours_day = 24;
    final static int first_hour = 0;
    final int days_week = 7;

    int pxWidth;

    private void initializeTimeValues() {
        time_values = new ArrayList<String>();
        int counter = 1;
        boolean start_PM = false;
        for (int x = 0; x < hours_day; x++) {
            if (x == first_hour) {
                time_values.add("12:00a");
            } else {
                if (counter == 12 && start_PM == false) {
                    //Change to pm
                    start_PM = true;
                }
                if (start_PM) {
                    time_values.add(String.valueOf(counter) + ":00p");
                } else {
                    time_values.add(String.valueOf(counter) + ":00a");
                }
                counter++;
                if(counter > 12) {
                    counter = 1;
                }
            }
        }
    }

    //debug
    public ArrayList<String> DEBUG_getTimeValues() {
        return time_values;
    }

    private void initializeDayValues() {
        day_values = new ArrayList<String>();
        day_values.add("U");
        day_values.add("M");
        day_values.add("T");
        day_values.add("W");
        day_values.add("R");
        day_values.add("F");
        day_values.add("S");
    }

    private void configureDisplay() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        pxWidth = metrics.widthPixels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
        configureDisplay();
        initializeTimeValues();
        initializeDayValues();
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        Schedule test = new Schedule();
        BuildTable(test);
    }

    private void BuildTable(Schedule sch) {
        //Time is in military format e.g. 2400 for 12am
//        int start_time = getStartHour(sch.earliestStart());
//        int end_time = getEndHour(sch.latestEnd());

        int start_time = getStartHour(630);
        int end_time = getEndHour(1430);


        // Row Loop
        for (int row = 0; row < hours_day + 1; row++) {

            TableRow table_row = new TableRow(this);
            table_row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            if (!(row >= start_time && row < end_time || row == hours_day)) {
                table_row.setVisibility(TableRow.GONE);
            }

            // Column Loop
            for (int col = 0; col <= days_week; col++) {

                //This is the first column so this is time Data
                if (col == 0) {
                    //If this is the last row then it should not have a time
                    if (row >= (hours_day)) {
                        View vw = timeTextView("   ");
                        table_row.addView(vw);
                    } else {
                        // else set a time
                        View vw = timeTextView(time_values.get(row));
                        table_row.addView(vw);
                    }
                } else {
                    // If this is the last row
                    if (row == (hours_day)) {
                        //Set a letter to represent the day
                        View vw = timeTextView(day_values.get(col - 1));
                        table_row.addView(vw);
                    } else {
                        //Configure what it should look like
                        View vis_box = getLayoutInflater().inflate(R.layout.vis_layout, null);

                        View start = vis_box.findViewById(R.id.vis_top);
                        View middle = vis_box.findViewById(R.id.vis_mid);
                        View end = vis_box.findViewById(R.id.vis_bot);

                        start.setLayoutParams(new TableRow.LayoutParams());
                        start.getLayoutParams().height = 100;
                        start.getLayoutParams().width = getColumnWidth();
                        start.setBackgroundColor(getResources().getColor(R.color.dark_aqua));


                        middle.setLayoutParams(new TableRow.LayoutParams());
                        middle.getLayoutParams().height = 100;
                        middle.getLayoutParams().width = getColumnWidth();
                        middle.setBackgroundColor(getResources().getColor(R.color.dark_gray));


                        end.setLayoutParams(new TableRow.LayoutParams());
                        end.getLayoutParams().height = 100;
                        end.getLayoutParams().width = getColumnWidth();
                        end.setBackgroundColor(getResources().getColor(R.color.aqua));

                        table_row.addView(vis_box);
                    }
                }

            }

            table_layout.addView(table_row);

        }
    }

    private int getStartHour(int input) {
        return input / 100;
    }

    private int getEndHour(int input) {
        return (int) Math.ceil((double) input / 100);
    }

    private int getColumnWidth() {
        //return Math.round(dpWidth/(8));
        return pxWidth / (days_week + 1);
    }

    private View timeTextView(String input) {
        View table_view = getLayoutInflater().inflate(R.layout.vis_time, null);
        LinearLayout lv = (LinearLayout) table_view.findViewById(R.id.vis_ll);

        TextView tv = (TextView) table_view.findViewById(R.id.tv_time);
        tv.setText(input);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setLayoutParams(new TableRow.LayoutParams(getColumnWidth() - 10,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER);
        return table_view;
    }

}