package uhmanoa.droid_sch;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by LENOVO on 4/17/2015.
 */
public class PreferencesHelper {
    private final String LOGTAG = "SCHEDULER";

    private Context context;

    public PreferencesHelper(Context c){
        this.context = c;
    }

    public ArrayList<String> getPreferenceFiles(){
        ArrayList<String> preferences = new ArrayList<String>();

        // get the preferences folder location
        File prefsDir = new File(context.getApplicationInfo().dataDir, "shared_prefs");

        if (prefsDir.exists() && prefsDir.isDirectory()){
            String[] list = prefsDir.list();
            for (String pref : list) {
                if (pref.contains("droid_sch"))// || pref.contains(context.getString(R.string.spin_default_profile)))
                    continue;
                String shortpref = pref.substring(0, pref.indexOf(".xml"));
                preferences.add(shortpref);
            }

        }
        return preferences;
    }

    public void removePreferenceFile(String fileName){

        File prefsDir = new File(context.getApplicationInfo().dataDir, "shared_prefs");

        // First clear all values from memory before removing the file
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().clear().commit();

        // remove the file if it exists
        File del = new File(prefsDir, fileName + ".xml" );
        if (del.exists())
            del.delete();
        else
            Log.e(LOGTAG, "Error removing preference file " + fileName + ".  File not found.");

        // remove .bak file if it exists
        String deln = fileName + ".bak";
        del = new File(prefsDir, deln);
        if (del.exists())
            del.delete();
    }

    // Simply opens a shared preferences file with the new name to create it in the system
    public void createPreferencesFile(String fileName, BuilderOptions bos) {
        bos.createProfile(fileName);
    }
}
