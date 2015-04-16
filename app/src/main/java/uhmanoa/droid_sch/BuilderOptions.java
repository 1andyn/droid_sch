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

}
