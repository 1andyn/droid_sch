package uhmanoa.droid_sch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
* This class is for defining the SCHEMA for the local database file (creating/destroying the db)
* SQLite database will be used for storing local data such as generated schedules
* and starred courses/classes and other data that will require immense organization
* Saved preferences/options and such will be saved using SharedPreferences API instead,
* use SQLite DB exclusively for large amounts of data
*
* Current Design: 3 Tables
* Table #1 for Storing Starred/Favorited Course/Classes
*   Schema -> course_name, crn. If crn is -1, then a course is saved, if not, a class is saved
* Table #2 for Storing Generated Schedules
*   Schema -> course_name, crn, start, start1, end, end1, room, room2, credits, prof, schid
*       schid is used to denote group of classes per course
* Table #3 for Storing Focus/GE Flags
*   Schema -> course_name,
*/

public class Sql_helper extends SQLiteOpenHelper {

    /* Columns for Starred/Favorites & Schedules */
    public static final String COLUMN_CRSNAME = "strCourse"; /* This field will be used as a key
    to match a specific row on the Focus/GE Flag table */
    public static final String COLUMN_CRN = "intCRN";
    public static final String COLUMN_ID = "intID";
    public static final String COLUMN_SEM = "intSEM";

    /* Columns for saved Schedules exclusively */
    public static final String COLUMN_START = "intStart";
    public static final String COLUMN_START2 = "intStart2";
    public static final String COLUMN_END = "intEnd";
    public static final String COLUMN_END2 = "intEnd2";
    public static final String COLUMN_ROOM = "strRoom";
    public static final String COLUMN_ROOM2 = "strRoom2";
    public static final String COLUMN_CREDIT = "intCredits";
    public static final String COLUMN_PROF = "strProf";
    public static final String COLUMN_SCH_ID = "intIDsch";/* Used for matching schedules together */

    /* Columns for Focus/GE Flags */
    public static final String COLUMN_fga = "blnFga";
    public static final String COLUMN_fgb = "blnFgb";
    public static final String COLUMN_fgc = "blnFgc";
    public static final String COLUMN_fs = "blnFs";
    public static final String COLUMN_fw = "blnFw";
    public static final String COLUMN_da = "blnDa";
    public static final String COLUMN_db = "blnDb";
    public static final String COLUMN_dh = "blnDh";
    public static final String COLUMN_dl = "blnDl";
    public static final String COLUMN_dp = "blnDp";
    public static final String COLUMN_dy = "blnDy";
    public static final String COLUMN_hsl = "blnHsl";
    public static final String COLUMN_ni = "blnNi";
    public static final String COLUMN_eth = "blnEth";
    public static final String COLUMN_hap = "blnHap";
    public static final String COLUMN_oc = "blnOc";
    public static final String COLUMN_wi = "blnWi";

    /* Table Names */
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "starred_data";
    public static final String TABLE_NAME2 = "saved_sch";
    public static final String TABLE_NAME3 = "focus_ge_flags";

    /* Database Name */
    private final static String DATABASE_NAME = "manoa_sch_db.db";

    private static final String FAV_TB_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + "(" + COLUMN_CRSNAME + " TEXT NOT NULL, "
            + COLUMN_CRN + " INT NOT NULL, "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SEM + " INT NOT NULL " + " );";

    private static final String SCH_TB_CREATE = "CREATE TABLE "
            + TABLE_NAME2
            + "(" + COLUMN_CRSNAME + " TEXT NOT NULL, "
            + COLUMN_CRN + " INT NOT NULL, "
            + COLUMN_START + " INT NOT NULL, "
            + COLUMN_START2 + " INT NOT NULL, "
            + COLUMN_END + " INT NOT NULL, "
            + COLUMN_END2 + " INT NOT NULL, "
            + COLUMN_ROOM + " TEXT NOT NULL, "
            + COLUMN_ROOM2 + " TEXT NOT NULL, "
            + COLUMN_PROF + " TEXT NOT NULL, "
            + COLUMN_CREDIT + " INT NOT NULL, "
            + COLUMN_SCH_ID + " INT NOT NULL, "
            + COLUMN_SEM + " INT NOT NULL " + " );";

    // For Reference Purposes
    //+ "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

    private static final String GEFC_TB_CREATE = "CREATE TABLE "
            + TABLE_NAME3
            + COLUMN_fga + " INT NOT NULL, "
            + COLUMN_fgb + " INT NOT NULL, "
            + COLUMN_fgc + " INT NOT NULL, "
            + COLUMN_fs + " INT NOT NULL, "
            + COLUMN_fw + " INT NOT NULL, "
            + COLUMN_da + " INT NOT NULL, "
            + COLUMN_db + " INT NOT NULL, "
            + COLUMN_dh + " INT NOT NULL, "
            + COLUMN_dl + " INT NOT NULL, "
            + COLUMN_dp + " INT NOT NULL, "
            + COLUMN_dy + " INT NOT NULL, "
            + COLUMN_hsl + " INT NOT NULL, "
            + COLUMN_ni + " INT NOT NULL, "
            + COLUMN_eth + " INT NOT NULL, "
            + COLUMN_oc + " INT NOT NULL, "
            + COLUMN_wi + " INT NOT NULL, " + " );";

    public Sql_helper(Context context) {
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
        super(context, "/mnt/sdcard/" + DATABASE_NAME, null, DATABASE_VERSION);
		    /*DEBUG ONLY, use COMMENTED VERSION FOR RELEASE */
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(FAV_TB_CREATE);
        database.execSQL(SCH_TB_CREATE);
        database.execSQL(GEFC_TB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Sql_helper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        onCreate(db);
    }
}
