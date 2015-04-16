package uhmanoa.droid_sch;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class Preferences extends ActionBarActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private int DEST_FIELD;
    private final int START_TIME_OFF = 0;
    private final int END_TIME_OFF = 1;
    private final int START_TIME_OFF_2 = 2;
    private final int END_TIME_OFF_2 = 3;
    private final int EARLIEST_START = 4;
    private final int LATEST_END = 5;

    private BuilderOptions bos;

    EditText etStartTimeOff, etEndTimeOff;
    EditText etStartTimeOff2, etEndTimeOff2;
    EditText etEarliestStart, etLatestEnd;
    CheckBox chkDaysM, chkDaysT, chkDaysW, chkDaysR, chkDaysF, chkDaysS, chkDays;
    CheckBox chkTimesM, chkTimesT, chkTimesW, chkTimesR, chkTimesF, chkTimesS, chkTimes;
    CheckBox chkTimesM2, chkTimesT2, chkTimesW2, chkTimesR2, chkTimesF2, chkTimesS2;
    CheckBox chkEarliestStart, chkLatestEnd;
    LinearLayout llTimesOffTimes, llTimesOffDays, llDaysOff;
    LinearLayout llTimesOffTimes2, llTimesOffDays2;
    LinearLayout llTimesOffTimesWrapper1, llTimesOffTimesWrapper2;
    ImageButton btnAddTimeOff;

    private int earliestStart = -1;
    private int latestEnd = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        bos = new BuilderOptions(getApplicationContext());

        setBackground();
        initLayout();
        loadSettings();
        configureButtons();
    }

    private void saveSettings() {
        //Checked days is checked
        if(chkDays.isChecked()) {
            String days = "";
            if(chkDaysM.isChecked()) {
                days = days + "M";
            }
            if(chkDaysT.isChecked()) {
                days = days + "T";
            }
            if(chkDaysW.isChecked()) {
                days = days + "W";
            }
            if(chkDaysR.isChecked()) {
                days = days + "R";
            }
            if(chkDaysF.isChecked()) {
                days = days + "F";
            }
            if(chkDaysS.isChecked()) {
                days = days + "S";
            }

            //DEBUG
            System.out.println("DEBUG " + days);

            bos.setDaysOffBoolean(true);
            bos.setDaysOffString(days);

        } else {
            bos.setDaysOffBoolean(false);
        }

        //check if earleist start is configured
        if(chkEarliestStart.isChecked()) {
            bos.setEarliestStartBoolean(true);
            bos.setStartTime(earliestStart);
        } else {
            bos.setEarliestStartBoolean(false);
            bos.setStartTime(-1);
        }

        //check if latest end is configured
        if(chkLatestEnd.isChecked()){
            bos.setLatestEndBoolean(true);
            bos.setEndTime(latestEnd);
        } else {
            bos.setLatestEndBoolean(false);
            bos.setEndTime(-1);
        }
    }

    private void loadSettings() {
        //check if offdays were saved
        if(bos.getDaysOffBoolean()) {
            chkDays.setChecked(true);
            ArrayList<Character> day_char = bos.getDaysOffArray();

            if(day_char.contains('M')) {
                chkDaysM.setChecked(true);
            }
            if(day_char.contains('T')) {
                chkDaysT.setChecked(true);
            }
            if(day_char.contains('W')) {
                chkDaysW.setChecked(true);
            }
            if(day_char.contains('R')) {
                chkDaysR.setChecked(true);
            }
            if(day_char.contains('F')) {
                chkDaysF.setChecked(true);
            }
            if(day_char.contains('S')) {
                chkDaysS.setChecked(true);
            }
        }

        //check if earliest start was saved
        if(bos.getBooleanEarliestStart()) {
            earliestStart = bos.getEarliestStart();
            int hr = earliestStart/100;
            int min = earliestStart%100;
            etEarliestStart.setText(getTimeString(hr, min));
            chkEarliestStart.setChecked(true);
        }

        //check if latest end was saved
        if(bos.getBooleanLatestEnd()) {
            latestEnd = bos.getLatestEnd();
            int hr = latestEnd/100;
            int min = latestEnd%100;
            etLatestEnd.setText(getTimeString(hr, min));
            chkLatestEnd.setChecked(true);
        }
    }

    private void configureButtons() {
        Button saveButton = (Button) findViewById(R.id.btnSave);
        Button cancelButton = (Button) findViewById(R.id.btnCancel);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveSettings();
                        Bundle b = new Bundle();
                        Intent i = new Intent();
                        b.putBoolean("SAVE", true);
                        i.putExtras(b);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });

        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b = new Bundle();
                        Intent i = new Intent();
                        b.putBoolean("SAVE", false);
                        i.putExtras(b);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddTimeOff:

                break;
            case R.id.etStartTimeOff:
                DEST_FIELD = START_TIME_OFF;
                setEditTextTime();

                break;
            case R.id.etEndTimeOff:
                DEST_FIELD = END_TIME_OFF;
                setEditTextTime();

                break;
            case R.id.etStartTimeOff2:
                DEST_FIELD = START_TIME_OFF_2;
                setEditTextTime();

                break;
            case R.id.etEndTimeOff2:
                DEST_FIELD = END_TIME_OFF_2;
                setEditTextTime();

                break;
            case R.id.etEarliestStart:
                DEST_FIELD = EARLIEST_START;
                setEditTextTime();

                break;
            case R.id.etLatestEnd:
                DEST_FIELD = LATEST_END;
                setEditTextTime();

                break;
        }
    }

    private String getTimeString(int hr, int min) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hr);
        time.set(Calendar.MINUTE, min);
        String ampm = (time.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM";
        String showHour = (time.get(Calendar.HOUR) == 0) ?
                "12" : time.get(Calendar.HOUR) + "";
        String showMin = (time.get(Calendar.MINUTE) < 10) ?
                "0" + time.get(Calendar.MINUTE) : time.get(Calendar.MINUTE) + "";
        return ("" + showHour + ":" + showMin + " " + ampm);
    }

    public void setEditTextTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hr, int min) {
                        Log.w("TIME:", "Setting time...");

                        int tp_time = timeConversion(hr, min);
                        String showTime = getTimeString(hr, min);

                        switch (DEST_FIELD) {
                            case START_TIME_OFF:
                                etStartTimeOff.setText(showTime);
                                break;
                            case END_TIME_OFF:
                                etEndTimeOff.setText(showTime);
                                break;
                            case START_TIME_OFF_2:
                                etStartTimeOff2.setText(showTime);
                                break;
                            case END_TIME_OFF_2:
                                etEndTimeOff2.setText(showTime);
                                break;
                            case EARLIEST_START:

                                System.out.println("DEBUG ES: " + tp_time);

                                earliestStart = tp_time;
                                etEarliestStart.setText(showTime);
                                break;
                            case LATEST_END:

                                System.out.println("DEBUG LE: " + tp_time);

                                latestEnd = tp_time;
                                etLatestEnd.setText(showTime);
                                break;
                        }
                    }
                }, hour, minute, false);
        timePicker.setTitle("Select Time");
        timePicker.show();
    }

    private int timeConversion(int hr, int min) {
        return ((hr*100) + min);
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        switch (v.getId()) {
            case R.id.chkDays:
                if (chkDays.isChecked())
                    llDaysOff.setVisibility(View.VISIBLE);
                else
                    llDaysOff.setVisibility(View.GONE);
                break;
            case R.id.chkTimes:
                if (chkTimes.isChecked()) {
                    llTimesOffTimesWrapper1.setVisibility(View.VISIBLE);
                    llTimesOffTimesWrapper2.setVisibility(View.VISIBLE);
                } else {
                    llTimesOffTimesWrapper1.setVisibility(View.GONE);
                    llTimesOffTimesWrapper2.setVisibility(View.GONE);
                }
                break;
            case R.id.chkEarliestStart:

                break;
            case R.id.chkLatestEnd:

                break;
        }
    }


    public void initLayout() {
        llDaysOff = (LinearLayout) findViewById(R.id.llDaysOff);
        llTimesOffDays = (LinearLayout) findViewById(R.id.llTimesOffDays);
        llTimesOffTimes = (LinearLayout) findViewById(R.id.llTimesOffTimes);
        llTimesOffDays2 = (LinearLayout) findViewById(R.id.llTimesOffDays2);
        llTimesOffTimes2 = (LinearLayout) findViewById(R.id.llTimesOffTimes2);
        llTimesOffTimesWrapper1 = (LinearLayout) findViewById(R.id.llTimesOffTimesWrapper1);
        llTimesOffTimesWrapper2 = (LinearLayout) findViewById(R.id.llTimesOffTimesWrapper2);

        llDaysOff.setVisibility(View.GONE);
        llTimesOffTimesWrapper1.setVisibility(View.GONE);
        llTimesOffTimesWrapper2.setVisibility(View.GONE);

        btnAddTimeOff = (ImageButton) findViewById(R.id.btnAddTimeOff);
        btnAddTimeOff.setOnClickListener(this);

        chkDays = (CheckBox) findViewById(R.id.chkDays);
        chkDaysM = (CheckBox) findViewById(R.id.chkDaysM);
        chkDaysT = (CheckBox) findViewById(R.id.chkDaysT);
        chkDaysW = (CheckBox) findViewById(R.id.chkDaysW);
        chkDaysR = (CheckBox) findViewById(R.id.chkDaysR);
        chkDaysF = (CheckBox) findViewById(R.id.chkDaysF);
        chkDaysS = (CheckBox) findViewById(R.id.chkDaysS);

        chkDays.setOnCheckedChangeListener(this);
        chkDaysM.setOnCheckedChangeListener(this);
        chkDaysT.setOnCheckedChangeListener(this);
        chkDaysW.setOnCheckedChangeListener(this);
        chkDaysR.setOnCheckedChangeListener(this);
        chkDaysF.setOnCheckedChangeListener(this);
        chkDaysS.setOnCheckedChangeListener(this);
        chkDays.requestFocus();

        chkTimes = (CheckBox) findViewById(R.id.chkTimes);
        etStartTimeOff = (EditText) findViewById(R.id.etStartTimeOff);
        etEndTimeOff = (EditText) findViewById(R.id.etEndTimeOff);
        chkTimesM = (CheckBox) findViewById(R.id.chkTimesM);
        chkTimesT = (CheckBox) findViewById(R.id.chkTimesT);
        chkTimesW = (CheckBox) findViewById(R.id.chkTimesW);
        chkTimesR = (CheckBox) findViewById(R.id.chkTimesR);
        chkTimesF = (CheckBox) findViewById(R.id.chkTimesF);
        chkTimesS = (CheckBox) findViewById(R.id.chkTimesS);

        etStartTimeOff.setOnClickListener(this);
        etEndTimeOff.setOnClickListener(this);

        etStartTimeOff2 = (EditText) findViewById(R.id.etStartTimeOff2);
        etEndTimeOff2 = (EditText) findViewById(R.id.etEndTimeOff2);
        chkTimesM2 = (CheckBox) findViewById(R.id.chkTimesM2);
        chkTimesT2 = (CheckBox) findViewById(R.id.chkTimesT2);
        chkTimesW2 = (CheckBox) findViewById(R.id.chkTimesW2);
        chkTimesR2 = (CheckBox) findViewById(R.id.chkTimesR2);
        chkTimesF2 = (CheckBox) findViewById(R.id.chkTimesF2);
        chkTimesS2 = (CheckBox) findViewById(R.id.chkTimesS2);

        etStartTimeOff2.setOnClickListener(this);
        etEndTimeOff2.setOnClickListener(this);

        chkTimes.setOnCheckedChangeListener(this);
        /*
        chkTimesM.setOnCheckedChangeListener(this);
        chkTimesT.setOnCheckedChangeListener(this);
        chkTimesW.setOnCheckedChangeListener(this);
        chkTimesR.setOnCheckedChangeListener(this);
        chkTimesF.setOnCheckedChangeListener(this);
        chkTimesS.setOnCheckedChangeListener(this);

        chkTimesM2.setOnCheckedChangeListener(this);
        chkTimesT2.setOnCheckedChangeListener(this);
        chkTimesW2.setOnCheckedChangeListener(this);
        chkTimesR2.setOnCheckedChangeListener(this);
        chkTimesF2.setOnCheckedChangeListener(this);
        chkTimesS2.setOnCheckedChangeListener(this);
        */

        chkEarliestStart = (CheckBox) findViewById(R.id.chkEarliestStart);
        chkLatestEnd = (CheckBox) findViewById(R.id.chkLatestEnd);

        chkEarliestStart.setOnCheckedChangeListener(this);
        chkLatestEnd.setOnCheckedChangeListener(this);

        etEarliestStart = (EditText) findViewById(R.id.etEarliestStart);
        etLatestEnd = (EditText) findViewById(R.id.etLatestEnd);

        etEarliestStart.setOnClickListener(this);
        etLatestEnd.setOnClickListener(this);

        setButtonDimensions();

    }

    public void setButtonDimensions() {
        final ViewTreeObserver vto = chkTimes.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams params = btnAddTimeOff.getLayoutParams();
                int chkHeight = (chkTimes.getHeight() * 3) / 4;
                params.height = chkHeight;
                params.width = chkHeight;
                btnAddTimeOff.setLayoutParams(params);
            }
        });

    }

    private void setBackground() {
        Resources res_main = getResources();
        Point pt_resolution = new Point();
        Display dsp = getWindowManager().getDefaultDisplay();
        pt_resolution.x = dsp.getWidth();
        pt_resolution.y = dsp.getHeight();

        LinearLayout prefs = (LinearLayout) findViewById(R.id.ll_preferences);

        Bitmap bmp_mmbg = ImgLoader.decodedSampledBitmapResource(res_main, R.drawable.o_bg,
                pt_resolution.x / 8, pt_resolution.y / 8);
        BitmapDrawable drw_bg = new BitmapDrawable(bmp_mmbg);
        prefs.setBackgroundDrawable(drw_bg);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
