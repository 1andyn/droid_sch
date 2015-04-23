package uhmanoa.droid_sch;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class Preferences extends ActionBarActivity implements View.OnClickListener {

    //----------  DEBUG  ----------
    private final boolean DEBUG = false;
    private final String LOGTAG = "SCHEDULER";
    //----------  DEBUG  ----------

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
    CheckBox chkHelpPreferences, chkHelpMain, chkHelpCreate, chkHelpView, chkHelpSearch;
    LinearLayout llTimesOffTimes, llTimesOffDays, llDaysOff;
    LinearLayout llTimesOffTimes2, llTimesOffDays2;
    LinearLayout llTimesOffTimesWrapper1, llTimesOffTimesWrapper2;
    ImageButton btnAddTimeOff;
    Button btnDeleteProfile, btnCancel, btnSaveProfile, btnResetProfile, btnDone;
    Spinner spProfiles;

    PreferencesHelper ph = new PreferencesHelper(this);
    
    ArrayAdapter<String> spinner_data;
    ArrayList<String> profilesList;

    private int earliestStart = -1;
    private int latestEnd = -1;
    private int timesStart1 = -1;
    private int timesStart2 = -1;
    private int timesEnd1 = -1;
    private int timesEnd2 = -1;

    //private int builderSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        bos = new BuilderOptions(getApplicationContext());

        setBackground();
        initLayout();
        loadSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        spProfiles.setSelection(bos.getSelectedOption());

        if (bos.getShowHelpPreferences())
            showHelp();
    }

    private void saveSettings() {

        /* ********************************
        ********* Days Off Section ********
        ***********************************/

        bos.setDaysOffBoolean(chkDays.isChecked());

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

        bos.setDaysOffString(days);

        /* *********************************
        ********* Times Off Section ********
        ************************************/

        bos.setDayTimesOffBoolean(chkTimes.isChecked());

                /* ********* Day Times Off part 1  ************/
        bos.setStartTime1(timesStart1);
        bos.setEndTime1(timesEnd1);

        String days1 = "";
        if (chkTimesM.isChecked())
            days1 += "M";
        if (chkTimesT.isChecked())
            days1 += "T";
        if (chkTimesW.isChecked())
            days1 += "W";
        if (chkTimesR.isChecked())
            days1 += "R";
        if (chkTimesF.isChecked())
            days1 += "F";
        if (chkTimesS.isChecked())
            days1 += "S";

        bos.setDayTimesOff1String(days1);

                /* ********* Day Times Off part 2  ************/
        bos.setStartTime2(timesStart2);
        bos.setEndTime2(timesEnd2);

        String days2 = "";
        if (chkTimesM2.isChecked())
            days2 += "M";
        if (chkTimesT2.isChecked())
            days2 += "T";
        if (chkTimesW2.isChecked())
            days2 += "W";
        if (chkTimesR2.isChecked())
            days2 += "R";
        if (chkTimesF2.isChecked())
            days2 += "F";
        if (chkTimesS2.isChecked())
            days2 += "S";

        bos.setDayTimesOff2String(days2);

        /* *******************************************
        ********* Earliest Start Time Section ********
        **********************************************/

        bos.setEarliestStartBoolean(chkEarliestStart.isChecked());
        bos.setStartTime(earliestStart);

        /* ***************************************
        ********* Latest End Time Section ********
        ******************************************/

        bos.setLatestEndBoolean(chkLatestEnd.isChecked());
        bos.setEndTime(latestEnd);

        /* ***************************************
        *************** Help Section *************
        ******************************************/

        bos.setShowHelpMain(chkHelpMain.isChecked());
        bos.setShowHelpCreateSchedules(chkHelpCreate.isChecked());
        bos.setShowHelpViewSchedules(chkHelpView.isChecked());
        bos.setShowHelpSearchCourses(chkHelpSearch.isChecked());
        bos.setShowHelpPreferences(chkHelpPreferences.isChecked());
    }

    private void loadSettings() {
        /* ************************************
        /****    settings for Days Off    *****
        /*************************************/

        chkDays.setChecked(bos.getDaysOffBoolean());

        ArrayList<Character> day_char = bos.getDaysOffArray();

        chkDaysM.setChecked(day_char.contains('M'));
        chkDaysT.setChecked(day_char.contains('T'));
        chkDaysW.setChecked(day_char.contains('W'));
        chkDaysR.setChecked(day_char.contains('R'));
        chkDaysF.setChecked(day_char.contains('F'));
        chkDaysS.setChecked(day_char.contains('S'));

        /* ***************************************
         /****    settings for Times Off     *****
         /****************************************/

        chkTimes.setChecked(bos.getBooleanDayTimesOff());

                /* ***********  Start Time 1 for Times off section  **********/
        int dayTimesStart1 = bos.getDayTimesStart1();
        if (dayTimesStart1 != -1){
            int hr = dayTimesStart1 / 100;
            int min = dayTimesStart1 % 100;
            etStartTimeOff.setText(getTimeString(hr, min));
        }
        else
            etStartTimeOff.setText("");

                /* ***********  End Time 1 for Times off section  **********/
        int dayTimesEnd1 = bos.getDayTimesEnd1();
        if (dayTimesEnd1 != -1){
            int hr = dayTimesEnd1 / 100;
            int min = dayTimesEnd1 % 100;
            etEndTimeOff.setText(getTimeString(hr, min));
        }
        else
            etEndTimeOff.setText("");

                /* ************  Start Time 2 for Times off section  ***********/
        int dayTimesStart2 = bos.getDayTimesStart2();
        if (dayTimesStart2 != -1){
            int hr = dayTimesStart2 / 100;
            int min = dayTimesStart2 % 100;
            etStartTimeOff2.setText(getTimeString(hr, min));
        }
        else
            etStartTimeOff2.setText("");

                /* *************  End Time 2 for Times off section  *************/
        int dayTimesEnd2 = bos.getDayTimesEnd2();
        if (dayTimesEnd2 != -1){
            int hr = dayTimesEnd2 / 100;
            int min = dayTimesEnd2 % 100;
            etEndTimeOff2.setText(getTimeString(hr, min));
        }
        else
            etEndTimeOff2.setText("");

                /* *************  List of Days 1 for Times Off section  *************/
        ArrayList<Character> days1 = bos.getDayTimesOff1Array();

        chkTimesM.setChecked(days1.contains('M'));
        chkTimesT.setChecked(days1.contains('T'));
        chkTimesW.setChecked(days1.contains('W'));
        chkTimesR.setChecked(days1.contains('R'));
        chkTimesF.setChecked(days1.contains('F'));
        chkTimesS.setChecked(days1.contains('S'));

        /* *************  List of Days 2 for Times Off section  *************/
        ArrayList<Character> days2 = bos.getDayTimesOff2Array();

        chkTimesM2.setChecked(days2.contains('M'));
        chkTimesT2.setChecked(days2.contains('T'));
        chkTimesW2.setChecked(days2.contains('W'));
        chkTimesR2.setChecked(days2.contains('R'));
        chkTimesF2.setChecked(days2.contains('F'));
        chkTimesS2.setChecked(days2.contains('S'));

        /* ********************************************
         /****    settings for Earliest Start    *****
         /********************************************/

        chkEarliestStart.setChecked(bos.getBooleanEarliestStart());

        earliestStart = bos.getEarliestStart();
        if (earliestStart != -1) {
            int hr = earliestStart / 100;
            int min = earliestStart % 100;
            etEarliestStart.setText(getTimeString(hr, min));
        }
        else
            etEarliestStart.setText("");

        /* ****************************************
         /****    settings for Latest End    *****
         /****************************************/

        chkLatestEnd.setChecked(bos.getBooleanLatestEnd());

        latestEnd = bos.getLatestEnd();
        if (latestEnd != -1) {
            int hr = latestEnd / 100;
            int min = latestEnd % 100;
            etLatestEnd.setText(getTimeString(hr, min));
        }
        else
            etLatestEnd.setText("");

        /* ***************************************
        *************** Help Section *************
        ******************************************/

        chkHelpMain.setChecked(bos.getShowHelpMain());
        chkHelpCreate.setChecked(bos.getShowHelpCreateSchedules());
        chkHelpView.setChecked(bos.getShowHelpViewSchedules());
        chkHelpSearch.setChecked(bos.getShowHelpSearchCourses());
        chkHelpPreferences.setChecked(bos.getShowHelpPreferences());

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
            case R.id.btnCancel:            //  Cancel Button
                Bundle b = new Bundle();
                Intent i = new Intent();
                b.putBoolean("SAVE", false);
                i.putExtras(b);
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.btnSave:              // Save Profile Button
                saveSettings();
                new ToastWrapper(Preferences.this, "Settings for " + bos.getCurrentProfile() +
                        " saved.", Toast.LENGTH_SHORT);
                break;
            case R.id.btnReset:
                resetPreferenceValues();
                break;
            case R.id.btnDone:
                saveSettings();
                finish();
                break;
            case R.id.btnDeleteProfile:
                final String toDelete = spProfiles.getSelectedItem().toString();
                AlertDialog.Builder deleteConfirm = new AlertDialog.Builder(Preferences.this)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete the profile \'" +
                            toDelete + "?\'  This operation cannot be undone")
                        .setPositiveButton("OK", new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //toDelete = spProfiles.getSelectedItem().toString();
                                if (toDelete.equals(getString(R.string.spin_default_profile))) {
                                    new ToastWrapper(getApplicationContext(), "Cannot remove the " +
                                            "default profile.", Toast.LENGTH_SHORT);
                                    return;
                                }
                                profilesList.remove(toDelete);
                                spinner_data.notifyDataSetChanged();
                                spProfiles.setSelection(0);
                                bos.setSelectedOption(0);
                                ph.removePreferenceFile(toDelete);

//----------------- DEBUG --------------------------
                                if (DEBUG){
                                    Log.w(LOGTAG, "(Delete)Set selected option: " + bos.getSelectedOption());
                                }

//----------------- END DEBUG ----------------------
                            }
                        })
                        .setNegativeButton("No", null);
                deleteConfirm.show();

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
                                timesStart1 = tp_time;
                                etStartTimeOff.setText(showTime);
                                break;
                            case END_TIME_OFF:
                                timesEnd1 = tp_time;
                                etEndTimeOff.setText(showTime);
                                break;
                            case START_TIME_OFF_2:
                                timesStart2 = tp_time;
                                etStartTimeOff2.setText(showTime);
                                break;
                            case END_TIME_OFF_2:
                                timesEnd2 = tp_time;
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

    public void initLayout() {
        llDaysOff = (LinearLayout) findViewById(R.id.llDaysOff);
        llTimesOffDays = (LinearLayout) findViewById(R.id.llTimesOffDays);
        llTimesOffTimes = (LinearLayout) findViewById(R.id.llTimesOffTimes);
        llTimesOffDays2 = (LinearLayout) findViewById(R.id.llTimesOffDays2);
        llTimesOffTimes2 = (LinearLayout) findViewById(R.id.llTimesOffTimes2);
        llTimesOffTimesWrapper1 = (LinearLayout) findViewById(R.id.llTimesOffTimesWrapper1);
        llTimesOffTimesWrapper2 = (LinearLayout) findViewById(R.id.llTimesOffTimesWrapper2);

        btnAddTimeOff = (ImageButton) findViewById(R.id.btnAddTimeOff);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSaveProfile = (Button) findViewById(R.id.btnSave);
        btnResetProfile = (Button) findViewById(R.id.btnReset);
        btnDone = (Button) findViewById(R.id.btnDone);

        btnAddTimeOff.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSaveProfile.setOnClickListener(this);
        btnResetProfile.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        chkDays = (CheckBox) findViewById(R.id.chkDays);
        chkDaysM = (CheckBox) findViewById(R.id.chkDaysM);
        chkDaysT = (CheckBox) findViewById(R.id.chkDaysT);
        chkDaysW = (CheckBox) findViewById(R.id.chkDaysW);
        chkDaysR = (CheckBox) findViewById(R.id.chkDaysR);
        chkDaysF = (CheckBox) findViewById(R.id.chkDaysF);
        chkDaysS = (CheckBox) findViewById(R.id.chkDaysS);
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

        //-------------------------------------------------
        // Check boxes for Earliest Start and Latest End
        //-------------------------------------------------
        chkEarliestStart = (CheckBox) findViewById(R.id.chkEarliestStart);
        chkLatestEnd = (CheckBox) findViewById(R.id.chkLatestEnd);

        etEarliestStart = (EditText) findViewById(R.id.etEarliestStart);
        etLatestEnd = (EditText) findViewById(R.id.etLatestEnd);

        etEarliestStart.setOnClickListener(this);
        etLatestEnd.setOnClickListener(this);

        //-----------------------------------------
        // Check boxes for saving help settings
        //----------------------------------------
        chkHelpMain = (CheckBox) findViewById(R.id.chkHelpMain);
        chkHelpCreate = (CheckBox) findViewById(R.id.chkHelpCreate);
        chkHelpView = (CheckBox) findViewById(R.id.chkHelpView);
        chkHelpSearch = (CheckBox) findViewById(R.id.chkHelpSearch);
        chkHelpPreferences = (CheckBox) findViewById(R.id.chkHelpPreferences);


        //----------------------------------------
        // Profile manipulation
        //--------------------------------------
        btnDeleteProfile = (Button) findViewById(R.id.btnDeleteProfile);
        spProfiles = (Spinner) findViewById(R.id.spProfiles);
        
        btnDeleteProfile.setOnClickListener(this);
        populateProfiles();
        configureSpinner();

        // used for setting the image button size
        setButtonDimensions();

    }

    private void populateProfiles() {
        profilesList = new ArrayList<>();
        profilesList.addAll(ph.getPreferenceFiles());
        profilesList.add(getString(R.string.spin_new_profile));
    }

    private void updateProfiles(){
        populateProfiles();
        spinner_data.notifyDataSetChanged();
    }
    
    private void configureSpinner(){
        spinner_data = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                profilesList);
        spinner_data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProfiles.setAdapter(spinner_data);
        spProfiles.setSelection(bos.getSelectedOption());
        spProfiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String profName = spProfiles.getSelectedItem().toString();

                if (profName.equals(getString(R.string.spin_new_profile))){
                    addProfileName();
                }
                else {
                    bos = new BuilderOptions(getApplicationContext(), profName);
                    bos.setSelectedOption(pos);
                    loadSettings();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void addProfileName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_new_profile, null);
        final EditText newName = (EditText) v.findViewById(R.id.etProfileName);
        builder.setView(v)
                .setTitle(Html.fromHtml("<font color='#66FFCC'>" + getApplicationContext().getString(R.string.tv_add_profile_desc) + "</font>"))
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String tempName = newName.getText().toString();
                        if (tempName.equals("") || tempName.equals(" ")) {
                            new ToastWrapper(Preferences.this, "Please enter a valid profile name.",
                                    Toast.LENGTH_SHORT);
                            spProfiles.setSelection(bos.getSelectedOption());
                            return;
                        }

                        while (tempName.charAt(tempName.length() - 1) == ' ') {
                            tempName = tempName.substring(0, tempName.indexOf(' '));
                        }
                        profilesList.remove(profilesList.size() - 1);
                        profilesList.add(tempName);
                        profilesList.add(getString(R.string.spin_new_profile));
                        spinner_data.notifyDataSetChanged();
                        spProfiles.setSelection(profilesList.size() - 2);
                        bos.setSelectedOption(profilesList.size() - 2); // sets remembered profile to the new profile
                        ph.createPreferencesFile(tempName, bos);
                        resetPreferenceValues();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        spProfiles.setSelection(bos.getSelectedOption());
                    }
                })
                .show();

    }

    private void showHelp(){

        AlertDialog.Builder warning = new AlertDialog.Builder(Preferences.this);
        LayoutInflater li = this.getLayoutInflater();
        warning.setTitle(Html.fromHtml("<font color='#66FFCC'>" +
                getApplicationContext().getString(R.string.help_preferences_title) + "</font>"))
                .setView(li.inflate(R.layout.dialog_help_preferences, null))
                .setPositiveButton("OK", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        CheckBox cb = (CheckBox) ((AlertDialog)dialog).findViewById(R.id.chkDontShow);
                        chkHelpPreferences.setChecked(!cb.isChecked());
                        bos.setShowHelpPreferences(!cb.isChecked());
                        return;
                    }
                })
                .show();

    }

    private void resetPreferenceValues(){
        chkDays.setChecked(false);
        chkDaysM.setChecked(false);
        chkDaysT.setChecked(false);
        chkDaysW.setChecked(false);
        chkDaysR.setChecked(false);
        chkDaysF.setChecked(false);
        chkDaysS.setChecked(false);

        chkTimes.setChecked(false);
        chkTimesM.setChecked(false);
        chkTimesT.setChecked(false);
        chkTimesW.setChecked(false);
        chkTimesR.setChecked(false);
        chkTimesF.setChecked(false);
        chkTimesS.setChecked(false);

        chkTimesM2.setChecked(false);
        chkTimesT2.setChecked(false);
        chkTimesW2.setChecked(false);
        chkTimesR2.setChecked(false);
        chkTimesF2.setChecked(false);
        chkTimesS2.setChecked(false);

        etStartTimeOff.setText("");
        etEndTimeOff.setText("");
        etStartTimeOff2.setText("");
        etEndTimeOff2.setText("");

        chkEarliestStart.setChecked(false);
        chkLatestEnd.setChecked(false);
        etEarliestStart.setText("");
        etLatestEnd.setText("");

    }

    private void setButtonDimensions() {
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

    @Override
    public void onBackPressed(){
        spProfiles.setSelection(bos.getSelectedOption());
        super.onBackPressed();

    }
}
