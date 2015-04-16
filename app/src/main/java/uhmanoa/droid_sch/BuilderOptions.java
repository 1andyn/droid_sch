package uhmanoa.droid_sch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by supah_000 on 4/15/2015.
 */
public class BuilderOptions{

    private Context c;
    private final String selectOption = "SELECTED_OPTION";

    private final String booleanDaysOff = "PREF_DAYS_OFF";
    private final String stringDaysOff = "PREF_DAYS_OFF_S";

    private final String booleanStart = "PREF_START";
    private final String booleanEnd = "PREF_END";
    private final String intStart = "PREF_START_INT";
    private final String intEnd = "PREF_END_INT";

    private final String booleanTimeOff = "PREF_TIME_OFF";


    public BuilderOptions (Context ctx) {
        c = ctx;
    }

    public int getSelectedOption() {
        int x = 0; //default = 0;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        x = settings.getInt(selectOption, 0);
        return x;
    }

    public void setSelectedOption(int x) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(selectOption, x);
        editor.commit();
    }


    public void setEarliestStartBoolean(boolean b) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(booleanStart, b);
        editor.commit();
    }

    public void setLatestEndBoolean(boolean b) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(booleanEnd, b);
        editor.commit();
    }

    public void setStartTime(int time) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(intStart, time);
        editor.commit();
    }

    public void setEndTime(int time) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(intEnd, time);
        editor.commit();
    }

    public void setDaysOffBoolean(Boolean b) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(booleanDaysOff, b);
        editor.commit();
    }

    public void setDaysOffString(String s) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(stringDaysOff, s);
        editor.commit();
    }

    public boolean getDaysOffBoolean() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getBoolean(booleanDaysOff, false);
    }

    public String getDaysOffString() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getString(stringDaysOff ,"");
    }

    public boolean getBooleanLatestEnd() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getBoolean(booleanEnd, false);
    }

    public boolean getBooleanEarliestStart() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getBoolean(booleanStart, false);
    }

    public int getLatestEnd() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getInt(intEnd, -1);
    }
    public int getEarliestStart() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getInt(intStart, -1);
    }

}
