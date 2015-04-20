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
    private int eStartTime1 = -1;
    private int lEndTime1 = -1;
    private int eStartTime2 = -1;
    private int lEndTime2 = -1;

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
        if (chkDays.isChecked()) {
            String days = "";
            if (chkDaysM.isChecked()) {
                days = days + "M";
            }
            if (chkDaysT.isChecked()) {
                days = days + "T";
            }
            if (chkDaysW.isChecked()) {
                days = days + "W";
            }
            if (chkDaysR.isChecked()) {
                days = days + "R";
            }
            if (chkDaysF.isChecked()) {
                days = days + "F";
            }
            if (chkDaysS.isChecked()) {
                days = days + "S";
            }

            //DEBUG
            System.out.println("DEBUG " + days);

            bos.setDaysOffBoolean(true);
            bos.setDaysOffString(days);

        } else {
            bos.setDaysOffBoolean(false);
            bos.setDaysOffString("");
        }

        //check if earleist start is configured
        if (chkEarliestStart.isChecked()) {
            bos.setEarliestStartBoolean(true);
            bos.setStartTime(earliestStart);
        } else {
            bos.setEarliestStartBoolean(false);
            bos.setStartTime(-1);
        }

        //check if latest end is configured
        if (chkLatestEnd.isChecked()) {
            bos.setLatestEndBoolean(true);
            bos.setEndTime(latestEnd);
        } else {
            bos.setLatestEndBoolean(false);
            bos.setEndTime(-1);
        }

        if(chkLatestEnd.isChecked() && chkEarliestStart.isChecked()) {
            if (earliestStart != -1 && latestEnd != -1) {
                if (earliestStart > latestEnd) {
                    new ToastWrapper(getApplicationContext(), "Warning: Your designated EARLIEST START TIME " +
                            "appears to be later than your LATEST END TIME!", Toast.LENGTH_LONG);
                    new ToastWrapper(getApplicationContext(), "This may result in the builder " +
                            "producing no schedules!", Toast.LENGTH_LONG);
                }
            }
        }

        //check if timeblocks are enabled
        if (chkTimes.isChecked()) {
            bos.setTimeOffBoolean(true);

            //first block check
            String days = "";
            if (chkTimesM.isChecked()) {
                days = days + "M";
            }
            if (chkTimesT.isChecked()) {
                days = days + "T";
            }
            if (chkTimesW.isChecked()) {
                days = days + "W";
            }
            if (chkTimesR.isChecked()) {
                days = days + "R";
            }
            if (chkTimesF.isChecked()) {
                days = days + "F";
            }
            if (chkTimesS.isChecked()) {
                days = days + "S";
            }

            if (!days.equals("")) {
                bos.setDayTimeOffString1(days);
            } else {
                bos.setDayTimeOffString1("");
            }

            bos.setTimeOffStart1(eStartTime1);
            bos.setTimeOffEnd1(lEndTime1);


            if (!days.equals("")) {
                if (eStartTime1 != -1 && lEndTime1 != -1) {
                    //both have been entered
                    if (eStartTime1 > lEndTime1) {
                        new ToastWrapper(getApplicationContext(), "WARNING: Your designated TIME OFF" +
                                "START TIME is be later than your TIME OFF END TIME.",
                                Toast.LENGTH_LONG);
                        new ToastWrapper(getApplicationContext(), "This may result in the builder " +
                                "producing no schedules!",
                                Toast.LENGTH_LONG);
                    }
                } else {
                    new ToastWrapper(getApplicationContext(), "Error: Please set both Start and End " +
                            "times for the time off time block functionality.", Toast.LENGTH_LONG);
                }
            } else {
                new ToastWrapper(getApplicationContext(), "Error: Days were not designated for" +
                        " the first TIME OFF time block", Toast.LENGTH_LONG);
                new ToastWrapper(getApplicationContext(), "Without designated days, the first " +
                        "TIME OFF time block will not be used by the builder", Toast.LENGTH_LONG);
            }

            //second block check
            days = "";
            if (chkTimesM2.isChecked()) {
                days = days + "M";
            }
            if (chkTimesT2.isChecked()) {
                days = days + "T";
            }
            if (chkTimesW2.isChecked()) {
                days = days + "W";
            }
            if (chkTimesR2.isChecked()) {
                days = days + "R";
            }
            if (chkTimesF2.isChecked()) {
                days = days + "F";
            }
            if (chkTimesS2.isChecked()) {
                days = days + "S";
            }

            if (!days.equals("")) {
                bos.setDayTimeOffString2(days);
            } else {
                bos.setDayTimeOffString2("");
            }

            bos.setTimeOffStart2(eStartTime2);
            bos.setTimeOffEnd2(lEndTime2);

            if (!days.equals("")) {
                if (eStartTime1 != -1 && lEndTime1 != -1) {
                    //both have been entered
                    if (eStartTime2 > lEndTime2) {
                        new ToastWrapper(getApplicationContext(), "WARNING: Your SECOND designated " +
                                "TIME OFF START TIME is be later than your TIME OFF END TIME.",
                                Toast.LENGTH_LONG);
                        new ToastWrapper(getApplicationContext(), "This may result in the builder " +
                                "producing no schedules!",
                                Toast.LENGTH_LONG);
                    }
                } else {
                    new ToastWrapper(getApplicationContext(), "Error: Please set both Start and End " +
                            "times for the second TIME OFF time block functionality.", Toast.LENGTH_LONG);
                }
            } else {
                new ToastWrapper(getApplicationContext(), "Warning: Days were not designated for" +
                        " the second TIME OFF time block", Toast.LENGTH_LONG);
                new ToastWrapper(getApplicationContext(), "Without designated days, the second " +
                        "TIME OFF time block will not be used by the builder", Toast.LENGTH_LONG);
            }

        } else {
            bos.setTimeOffBoolean(false);
            bos.setTimeOffStart1(-1);
            bos.setTimeOffEnd1(-1);
            bos.setTimeOffStart2(-1);
            bos.setTimeOffEnd2(-1);
        }
    }

    private void loadSettings() {
        //check if offdays were saved
        if (bos.getDaysOffBoolean()) {
            chkDays.setChecked(true);
            ArrayList<Character> day_char = bos.getDaysOffArray();

            if (day_char.contains('M')) {
                chkDaysM.setChecked(true);
            }
            if (day_char.contains('T')) {
                chkDaysT.setChecked(true);
            }
            if (day_char.contains('W')) {
                chkDaysW.setChecked(true);
            }
            if (day_char.contains('R')) {
                chkDaysR.setChecked(true);
            }
            if (day_char.contains('F')) {
                chkDaysF.setChecked(true);
            }
            if (day_char.contains('S')) {
                chkDaysS.setChecked(true);
            }
        }

        //check if earliest start was saved
        if (bos.getBooleanEarliestStart() && bos.getEarliestStart() != -1) {
            earliestStart = bos.getEarliestStart();
            int hr = earliestStart / 100;
            int min = earliestStart % 100;
            etEarliestStart.setText(getTimeString(hr, min));
            chkEarliestStart.setChecked(true);
        }

        //check if latest end was saved
        if (bos.getBooleanLatestEnd() && bos.getLatestEnd() != -1) {
            latestEnd = bos.getLatestEnd();
            int hr = latestEnd / 100;
            int min = latestEnd % 100;
            etLatestEnd.setText(getTimeString(hr, min));
            chkLatestEnd.setChecked(true);
        }

        //check if the time blocks were saved and configured before
        if (bos.getTimeOffBoolean()) {
            chkTimes.setChecked(true);
            eStartTime1 = bos.getTimeOffStart1();
            lEndTime1 = bos.getTimeOffEnd1();
            eStartTime2 = bos.getTimeOffStart2();
            lEndTime2 = bos.getTimeOffEnd2();

            if (eStartTime1 != -1) {
                int hr = eStartTime1 / 100;
                int min = eStartTime1 % 100;
                etStartTimeOff.setText(getTimeString(hr, min));
            }

            if (eStartTime2 != -1) {
                int hr = eStartTime2 / 100;
                int min = eStartTime2 % 100;
                etStartTimeOff2.setText(getTimeString(hr, min));
            }


            if (lEndTime1 != -1) {
                int hr = lEndTime1 / 100;
                int min = lEndTime1 % 100;
                etEndTimeOff.setText(getTimeString(hr, min));
            }

            if (lEndTime2 != -1) {
                int hr = lEndTime2 / 100;
                int min = lEndTime2 % 100;
                etEndTimeOff2.setText(getTimeString(hr, min));
            }

            ArrayList<Character> day_char = bos.getDaysTOArray1();

            if (day_char.contains('M')) {
                chkTimesM.setChecked(true);
            }
            if (day_char.contains('T')) {
                chkTimesT.setChecked(true);
            }
            if (day_char.contains('W')) {
                chkTimesW.setChecked(true);
            }
            if (day_char.contains('R')) {
                chkTimesR.setChecked(true);
            }
            if (day_char.contains('F')) {
                chkTimesF.setChecked(true);
            }
            if (day_char.contains('S')) {
                chkTimesS.setChecked(true);
            }

            day_char = bos.getDaysTOArray2();
            if (day_char.contains('M')) {
                chkTimesM2.setChecked(true);
            }
            if (day_char.contains('T')) {
                chkTimesT2.setChecked(true);
            }
            if (day_char.contains('W')) {
                chkTimesW2.setChecked(true);
            }
            if (day_char.contains('R')) {
                chkTimesR2.setChecked(true);
            }
            if (day_char.contains('F')) {
                chkTimesF2.setChecked(true);
            }
            if (day_char.contains('S')) {
                chkTimesS2.setChecked(true);
            }

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
                                eStartTime1 = tp_time;
                                etStartTimeOff.setText(showTime);
                                break;
                            case END_TIME_OFF:
                                lEndTime1 = tp_time;
                                etEndTimeOff.setText(showTime);
                                break;
                            case START_TIME_OFF_2:
                                eStartTime2 = tp_time;
                                etStartTimeOff2.setText(showTime);
                                break;
                            case END_TIME_OFF_2:
                                lEndTime2 = tp_time;
                                etEndTimeOff2.setText(showTime);
                                break;
                            case EARLIEST_START:
                                earliestStart = tp_time;
                                etEarliestStart.setText(showTime);
                                break;
                            case LATEST_END:
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
        return ((hr * 100) + min);
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
