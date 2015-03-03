package uhmanoa.droid_sch;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Visualize extends ActionBarActivity {
    TableLayout table_layout;

    ArrayList<String> time_values;
    ArrayList<String> day_values;

    final int hours_day = 24;
    final static int first_hour = 0;
    final int days_week = 7;

    private void initializeTimeValues() {
        time_values = new ArrayList<String>();
        int counter = 1;
        boolean start_PM = false;
        for(int x = 0; x < hours_day; x++) {
            if (x == first_hour) {
                time_values.add("12:00a");
            } else {
                if(counter > 11 ) {
                    //Change to pm
                    if(start_PM) {
                        start_PM = false;
                        counter = 12;
                    } else {
                        start_PM = true;
                        counter = 1; //reset counter to start at 1
                    }
                }
                if(start_PM) {
                    time_values.add(String.valueOf(counter) + ":00p");
                } else {
                    time_values.add(String.valueOf(counter) + ":00a");
                }
                counter++;
            }
        }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
        initializeTimeValues();
        initializeDayValues();
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        Schedule test = new Schedule();
        BuildTable(test);
    }

    private void BuildTable(Schedule sch) {
        // Row Loop
        for (int row = 0; row < hours_day + 1; row++) {

            TableRow table_row = new TableRow(this);
            table_row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // Column Loop
            for (int col = 0; col <= days_week; col++) {

                //This is the first column so this is time Data
                if(col == 0) {
                    //If this is the last row then it should not have a time
                    if(row >= (hours_day)) {
                        TextView tv = timeTextView();
                       tv.setText("---");
                        table_row.addView(tv);
                    } else {
                        // else set a time
                        TextView tv = timeTextView();
                        tv.setText(time_values.get(row));
                        table_row.addView(tv);
                    }
                } else {
                    // If this is the last row
                    if(row == (hours_day)) {
                        //Set a letter to represent the day
                        TextView tv = timeTextView();
                        tv.setText(day_values.get(col - 1));
                        table_row.addView(tv);
                    } else {
                        //Configure what it should look like
                        TextView tv = timeTextView();
                        tv.setText("XXXX");
                        table_row.addView(tv);
                    }
                }

            }

            table_layout.addView(table_row);

        }
    }

    private TextView timeTextView() {
        TextView tv = new TextView(this);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.drawable.vis_cell);
        tv.setPadding(5, 5, 5, 5);
        return tv;
    }

}