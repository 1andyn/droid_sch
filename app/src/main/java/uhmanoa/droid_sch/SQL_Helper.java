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
* Current Design: 7 Tables
* Table #1 Starred Items: ID, CRN, COURSE, TITLE, SEMESTER, YEAR
* Table #2 Saved Schedules: ID, CRN, SEMESTER, YEAR
* Table #3 Schedule Temp: (Generated Schedules to be Displayed): ID, CRN, SEMESTER, YEAR
* Table #4 Courses: SEMESTER, YEAR, CRN, COURSE, TITLE, SECTION, CREDITS, INSTRUCTOR, SEATS,
* WAITLISTED, WAIT AVAIL, START1, END1, START2, END2, ROOM, ROOM2, DATES
* Table #5 Focuses: COURSE, FGA, FGB, FGC, FS, FW, DA, DB, DH, DL, DP, DY, HSL, NI, ETH, HAP, OC, WI,
* SEMESTER, YEAR
* Table #6 Days: CRN, SUN, MON, TUE, WED, THUR, FRI, SAT, SEMESTER, YEAR
* Table #7 Profiles: NAME, SUN, MON, TUE, WED, THUR, FRI, SAT, MINIMUM
* Table #8 Blocks: NAME, START, END, DAY
*/

public class SQL_Helper extends SQLiteOpenHelper {

    // Table #1 Columns, count = 6:
    public static final String COLUMN_ID = "intID";
    public static final String COLUMN_CRN = "intCRN";
    public static final String COLUMN_CRS = "strCourse"; /* This field will be used as a key */
    public static final String COLUMN_TITL = "strTitle";
    public static final String COLUMN_SEM = "intSemester";
    public static final String COLUMN_YEAR = "intYear";

    // Table #2 Columns, count = 4, reusing 4: ID, CRN, SEM, YEAR columns -> 0
    // Table #3 Columns, count = 4,  reusing 4: ID, CRN, SEM, YEAR columns -> 0

    // Table #4 Columns, count = 19, resusing 5: CRN, SEM, YEAR, CRS, TITLE-> 13
    public static final String COLUMN_SECT = "intSection";
    public static final String COLUMN_CREDIT = "strCredits";
    public static final String COLUMN_PROF = "strProf";
    public static final String COLUMN_START = "intStart";
    public static final String COLUMN_START2 = "intStart2";
    public static final String COLUMN_END = "intEnd";
    public static final String COLUMN_END2 = "intEnd2";
    public static final String COLUMN_ROOM = "strRoom";
    public static final String COLUMN_ROOM2 = "strRoom2";
    public static final String COLUMN_SEAT = "strSeats";
    public static final String COLUMN_WAITL = "strWaitlisted";
    public static final String COLUMN_WAITLA = "strWaitAvail";
    public static final String COLUMN_DATES = "strDates";
    public static final String COLUMN_MJR = "strMajor";

    /* Columns for Focus/GE Flags */
    // Table #5 Columns count = 20, rusing 3 Course, Semester, Year -> 17
    public static final String COLUMN_FGA = "blnFga";
    public static final String COLUMN_FGB = "blnFgb";
    public static final String COLUMN_FGC = "blnFgc";
    public static final String COLUMN_FS = "blnFs";
    public static final String COLUMN_FW = "blnFw";
    public static final String COLUMN_DA = "blnDa";
    public static final String COLUMN_DB = "blnDb";
    public static final String COLUMN_DH = "blnDh";
    public static final String COLUMN_DL = "blnDl";
    public static final String COLUMN_DP = "blnDp";
    public static final String COLUMN_DY = "blnDy";
    public static final String COLUMN_HSL = "blnHsl";
    public static final String COLUMN_NI = "blnNi";
    public static final String COLUMN_ETH = "blnEth";
    public static final String COLUMN_HAP = "blnHap";
    public static final String COLUMN_OC = "blnOc";
    public static final String COLUMN_WI = "blnWi";

    //Table #6; count = 11; reusing 3 CRN, Semester, Year -> 8
    public static final String COLUMN_SECDAY = "blnSec";
    public static final String COLUMN_SUN = "blnSun";
    public static final String COLUMN_MON = "blnMon";
    public static final String COLUMN_TUE = "blnTue";
    public static final String COLUMN_WED = "blnWed";
    public static final String COLUMN_THR = "blnThr";
    public static final String COLUMN_FRI = "blnFri";
    public static final String COLUMN_SAT = "blnSat";

    //Table #7; count = 9; resuse 7: sun, mon, tue, wed, thr, fri, sat -> 2
    public static final String COLUMN_PNAME = "strName"; //profile Name
    public static final String COLUMN_CRSMIN = "intMin"; //minimum course count

    //Table #8; count = 10; reuse 10: pname, start, end sun, mon, tue, wed, thr, fri, sat -> 0
    //Table #9 count = 4; reuse: semester, year, mjr -> 1
    public static final String COLUMN_FMAJOR = "strFMajor"; //full major name

    /* Table Names */
    private static final int DATABASE_VERSION = 6;
    public static final String TABLE_STAR = "tbStarred";
    public static final String TABLE_SCH = "tbSched";
    public static final String TABLE_TSCH = "tbTempSched";
    public static final String TABLE_COURSE = "tbCourse";
    public static final String TABLE_CFOCUS = "tbCFocus";
    public static final String TABLE_CDAY = "tbCDays";
    public static final String TABLE_PREF = "tbPref";
    public static final String TABLE_PBLOCK = "tbPBlock";
    public static final String TABLE_MAJOR = "tbMajor";

    /* Database Name */
    private final static String DATABASE_NAME = "dbManoaSch.db";

    /* Create Starred Data Table */
    private static final String TABLE_STAR_CREATE = "CREATE TABLE "
            + TABLE_STAR
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CRN + " INT NOT NULL, "
            + COLUMN_CRS + " TEXT NOT NULL, "
            + COLUMN_TITL + " TEXT NOT NULL, "
            + COLUMN_SEM + " INT NOT NULL, "
            + COLUMN_YEAR + " INT NOT NULL "
            + " );";

    /*
    * The ID for this table needs to be specically calculated,
    * since multiple rows will have same ID*/

    /* Create Saved Schedules Table */
    private static final String TABLE_SCH_CREATE = "CREATE TABLE "
            + TABLE_SCH
            + "("
            + COLUMN_ID + " INT NOT NULL, "
            + COLUMN_CRN + " INT NOT NULL, "
            + COLUMN_YEAR + " INT NOT NULL, "
            + COLUMN_SEM + " INT NOT NULL "
            + " );";

    /* Create Temp Schedules Table */
    private static final String TABLE_TSCH_CREATE = "CREATE TABLE "
            + TABLE_TSCH
            + "("
            + COLUMN_ID + " INT NOT NULL, "
            + COLUMN_CRN + " INT NOT NULL, "
            + COLUMN_YEAR + " INT NOT NULL, "
            + COLUMN_SEM + " INT NOT NULL "
            + " );";


    /* SPEED VERSION */
    private static final String TABLE_FTS_COURSE_CREATE = "CREATE VIRTUAL TABLE "
            + TABLE_COURSE + " USING fts4"
            + "("
            + COLUMN_CRN + " INT NOT NULL, "
            + COLUMN_CRS + " TEXT NOT NULL, "
            + COLUMN_SECT + " INT NOT NULL, "
            + COLUMN_TITL + " TEXT NOT NULL, "
            + COLUMN_SEM + " INT NOT NULL, "
            + COLUMN_YEAR + " INT NOT NULL, "
            + COLUMN_START + " INT NOT NULL, "
            + COLUMN_START2 + " INT NOT NULL, "
            + COLUMN_END + " INT NOT NULL, "
            + COLUMN_END2 + " INT NOT NULL, "
            + COLUMN_ROOM + " TEXT NOT NULL, "
            + COLUMN_ROOM2 + " TEXT NOT NULL, "
            + COLUMN_PROF + " TEXT NOT NULL, "
            + COLUMN_CREDIT + " INT NOT NULL, "
            + COLUMN_SEAT + " INT NOT NULL, "
            + COLUMN_WAITL + " INT NOT NULL, "
            + COLUMN_WAITLA + " INT NOT NULL, "
            + COLUMN_DATES + " TEXT NOT NULL, "
            + COLUMN_MJR + " TEXT NOT NULL "
            + " );";


    /* ORIGINAL: Create Course Storage Table */
    private static final String TABLE_COURSE_CREATE = "CREATE TABLE "
            + TABLE_COURSE
            + "("
            + COLUMN_CRN + " INT NOT NULL, "
            + COLUMN_CRS + " TEXT NOT NULL, "
            + COLUMN_SECT + " INT NOT NULL, "
            + COLUMN_TITL + " TEXT NOT NULL, "
            + COLUMN_SEM + " INT NOT NULL, "
            + COLUMN_YEAR + " INT NOT NULL, "
            + COLUMN_START + " INT NOT NULL, "
            + COLUMN_START2 + " INT NOT NULL, "
            + COLUMN_END + " INT NOT NULL, "
            + COLUMN_END2 + " INT NOT NULL, "
            + COLUMN_ROOM + " TEXT NOT NULL, "
            + COLUMN_ROOM2 + " TEXT NOT NULL, "
            + COLUMN_PROF + " TEXT NOT NULL, "
            + COLUMN_CREDIT + " TEXT NOT NULL, "
            + COLUMN_SEAT + " INT NOT NULL, "
            + COLUMN_WAITL + " INT NOT NULL, "
            + COLUMN_WAITLA + " INT NOT NULL, "
            + COLUMN_DATES + " TEXT NOT NULL, "
            + COLUMN_MJR + " TEXT NOT NULL "
            + " );";

    // For Reference Purposes
    //+ "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "

    private static final String TABLE_CFOCUS_CREATE = "CREATE TABLE "
            + TABLE_CFOCUS
            + "("
            + COLUMN_CRS + " TEXT NOT NULL, "
            + COLUMN_SEM + " INT NOT NULL, "
            + COLUMN_YEAR + " INT NOT NULL, "
            + COLUMN_FGA + " INT NOT NULL, "
            + COLUMN_FGB + " INT NOT NULL, "
            + COLUMN_FGC + " INT NOT NULL, "
            + COLUMN_FS + " INT NOT NULL, "
            + COLUMN_FW + " INT NOT NULL, "
            + COLUMN_DA + " INT NOT NULL, "
            + COLUMN_DB + " INT NOT NULL, "
            + COLUMN_DH + " INT NOT NULL, "
            + COLUMN_DL + " INT NOT NULL, "
            + COLUMN_DP + " INT NOT NULL, "
            + COLUMN_DY + " INT NOT NULL, "
            + COLUMN_HSL + " INT NOT NULL, "
            + COLUMN_NI + " INT NOT NULL, "
            + COLUMN_ETH + " INT NOT NULL, "
            + COLUMN_HAP + " INT NOT NULL, "
            + COLUMN_OC + " INT NOT NULL, "
            + COLUMN_WI + " INT NOT NULL "
            + " );";

    private static final String TABLE_CDAYS_CREATE = "CREATE TABLE "
            + TABLE_CDAY
            + "("
            + COLUMN_CRN + " INT NOT NULL, "
            + COLUMN_YEAR + " INT NOT NULL, "
            + COLUMN_SEM + " INT NOT NULL, "
            + COLUMN_SECDAY + " INT NOT NULL, "
            + COLUMN_SUN + " INT NOT NULL, "
            + COLUMN_MON + " INT NOT NULL, "
            + COLUMN_TUE + " INT NOT NULL, "
            + COLUMN_WED + " INT NOT NULL, "
            + COLUMN_THR + " INT NOT NULL, "
            + COLUMN_FRI + " INT NOT NULL, "
            + COLUMN_SAT + " INT NOT NULL "
            + " );";

    private static final String TABLE_PREF_CREATE = "CREATE TABLE "
            + TABLE_PREF
            + "("
            + COLUMN_PNAME + " TEXT NOT NULL, "
            + COLUMN_CRSMIN + " INT NOT NULL, "
            + COLUMN_SUN + " INT NOT NULL, "
            + COLUMN_MON + " INT NOT NULL, "
            + COLUMN_TUE + " INT NOT NULL, "
            + COLUMN_WED + " INT NOT NULL, "
            + COLUMN_THR + " INT NOT NULL, "
            + COLUMN_FRI + " INT NOT NULL, "
            + COLUMN_SAT + " INT NOT NULL "
            + " );";

    private static final String TABLE_PBLOCK_CREATE = "CREATE TABLE "
            + TABLE_PBLOCK
            + "("
            + COLUMN_PNAME + " TEXT NOT NULL, "
            + COLUMN_START + " INT NOT NULL, "
            + COLUMN_END + " INT NOT NULL, "
            + COLUMN_SUN + " INT NOT NULL, "
            + COLUMN_MON + " INT NOT NULL, "
            + COLUMN_TUE + " INT NOT NULL, "
            + COLUMN_WED + " INT NOT NULL, "
            + COLUMN_THR + " INT NOT NULL, "
            + COLUMN_FRI + " INT NOT NULL, "
            + COLUMN_SAT + " INT NOT NULL "
            + " );";

    private static final String TABLE_MAJOR_CREATE = "CREATE TABLE "
            + TABLE_MAJOR
            + "("
            + COLUMN_SEM + " INT NOT NULL, "
            + COLUMN_YEAR + " INT NOT NULL, "
            + COLUMN_MJR + " TEXT NOT NULL, "
            + COLUMN_FMAJOR + " TEXT NOT NULL "
            + " );";

    public SQL_Helper(Context context) {
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
        super(context, "/mnt/sdcard/" + DATABASE_NAME, null, DATABASE_VERSION);
            /*DEBUG ONLY, use COMMENTED VERSION FOR RELEASE */
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_STAR_CREATE);
        database.execSQL(TABLE_SCH_CREATE);
        database.execSQL(TABLE_TSCH_CREATE);
        database.execSQL(TABLE_FTS_COURSE_CREATE);
        database.execSQL(TABLE_CFOCUS_CREATE);
        database.execSQL(TABLE_CDAYS_CREATE);
        database.execSQL(TABLE_PREF_CREATE);
        database.execSQL(TABLE_PBLOCK_CREATE);
        database.execSQL(TABLE_MAJOR_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQL_Helper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TSCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CFOCUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CDAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PBLOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAJOR);
        onCreate(db);
    }
}
