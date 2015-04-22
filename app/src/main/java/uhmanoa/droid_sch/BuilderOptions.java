package uhmanoa.droid_sch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by supah_000 on 4/15/2015.
 */
public class BuilderOptions{

    private Context c;
    private final String selectOption = "SELECTED_OPTION";  // Remember last used profile

    /*  For General Preferences  */
    private final String helpProfileSwitchSave = "HELP_PREFERENCES_SAVE";

    /*  For Days Off section */
    private final String booleanDaysOff = "PREF_DAYS_OFF";
    private final String stringDaysOff = "PREF_DAYS_OFF_S";

    /*  For Times Off section  */
    private final String booleanDayTimesOff = "PREF_DAYTIMES_OFF";
    private final String intStart1 = "PREF_START1_INT";
    private final String intEnd1 = "PREF_END1_INT";
    private final String stringDayTimes1 = "PREF_DAYTIMES1_S";
    private final String intStart2 = "PREF_START2_INT";
    private final String intEnd2 = "PREF_END2_INT";
    private final String stringDayTimes2 = "PREF_DAYTIMES2_S";

    /*  For Earliest Start and Latest End times  */
    private final String booleanStart = "PREF_START";
    private final String booleanEnd = "PREF_END";
    private final String intStart = "PREF_START_INT";
    private final String intEnd = "PREF_END_INT";

    private final String booleanTimeOff = "PREF_TIME_OFF";

    private String currentProfile;

    public BuilderOptions (Context ctx) {
        this.c = ctx;
        this.currentProfile = c.getString(R.string.spin_default_profile);
    }

    public BuilderOptions (Context c, String profile){
        this.c = c;
        this.currentProfile = profile;
        SharedPreferences settings = c.getSharedPreferences(currentProfile, c.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.commit();
    }

    public void createProfile(String profileName){
        setCurrentProfile(profileName);
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(helpProfileSwitchSave, true);
        editor.commit();
    }

    public boolean contains(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.contains(key);
    }

    public boolean isFirstUse(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return !prefs.contains(selectOption);
    }

    public int getSelectedOption() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getInt(selectOption, 0);
    }

    public void setSelectedOption(int x) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(selectOption, x);
        editor.commit();
    }

    /* ******************************************/
    /* ************    Setters   ****************/
    /* ******************************************/

    public void setShowHelpPreferences(boolean b){
        PreferenceManager.getDefaultSharedPreferences(c).edit().putBoolean(helpProfileSwitchSave, b).commit();
    }

    public void setCurrentProfile(String profName){
        this.currentProfile = profName;
    }

    public void setEarliestStartBoolean(boolean b) {
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putBoolean(booleanStart, b);
        editor.commit();
    }

    public void setLatestEndBoolean(boolean b) {
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putBoolean(booleanEnd, b);
        editor.commit();
    }

    public void setStartTime(int time) {
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putInt(intStart, time);
        editor.commit();
    }

    public void setEndTime(int time) {
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putInt(intEnd, time);
        editor.commit();
    }

    public void setDaysOffBoolean(Boolean b) {
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putBoolean(booleanDaysOff, b);
        editor.commit();
    }

    public void setDaysOffString(String s) {
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putString(stringDaysOff, s);
        editor.commit();
    }

    public void setDayTimesOffBoolean(Boolean b){
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putBoolean(booleanDayTimesOff, b).commit();
    }

            /* ******* First Set of Times off  ********/

    public void setDayTimesOff1String(String s){
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putString(stringDayTimes1, s).commit();
    }

    public void setStartTime1(int time){
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putInt(intStart1, time).commit();
    }

    public void setEndTime1(int time){
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putInt(intEnd1, time).commit();
    }

             /* ******* Second Set of Times off  ********/

    public void setDayTimesOff2String(String s){
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putString(stringDayTimes2, s).commit();
    }

    public void setStartTime2(int time){
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putInt(intStart2, time).commit();
    }

    public void setEndTime2(int time){
        SharedPreferences.Editor editor = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
        editor.putInt(intEnd2, time).commit();
    }

    /* ******************************************/
    /* ************    Getters   ****************/
    /* ******************************************/

    public String getCurrentProfile(){
        return currentProfile;
    }

    public boolean getShowHelpPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(helpProfileSwitchSave, true);
    }

    public boolean getDaysOffBoolean() {
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getBoolean(booleanDaysOff, false);
    }

    public ArrayList<Character> getDaysOffArray() {
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return getCharsFromString(stringDaysOff);
    }

    public boolean getBooleanLatestEnd() {
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getBoolean(booleanEnd, false);
    }

    public boolean getBooleanEarliestStart() {
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getBoolean(booleanStart, false);
    }

    public int getLatestEnd() {
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getInt(intEnd, -1);
    }
    public int getEarliestStart() {
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getInt(intStart, -1);
    }

            /* *********  First set of Times Off  *************/

    public boolean getBooleanDayTimesOff(){
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getBoolean(booleanDayTimesOff, false);
    }

    public ArrayList<Character> getDayTimesOff1Array(){
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return getCharsFromString(stringDayTimes1);
    }

    public int getDayTimesStart1(){
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getInt(intStart1, -1);
    }

    public int getDayTimesEnd1(){
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getInt(intEnd1, -1);
    }

            /* ********* Second set of Times Off  ************/

    public ArrayList<Character> getDayTimesOff2Array(){
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return getCharsFromString(stringDayTimes2);
    }

    public int getDayTimesStart2(){
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getInt(intStart2, -1);
    }

    public int getDayTimesEnd2(){
        SharedPreferences settings = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        return settings.getInt(intEnd2, -1);
    }


            /* *********  Helper Functions  ***********/

    private ArrayList<Character> getCharsFromString(String key){
        SharedPreferences prefs = c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE);
        String charString = prefs.getString(key ,"");
        ArrayList<Character> charArray = new ArrayList<>();
        for(int x = 0; x < charString.length(); x++) {
            charArray.add(charString.charAt(x));
        }
        return charArray;
    }

    private SharedPreferences.Editor getEditor(){
        return c.getSharedPreferences(currentProfile, Context.MODE_PRIVATE).edit();
    }
}
