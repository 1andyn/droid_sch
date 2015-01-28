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
* Current Design
*
*
*
*
*/

public class sql_helper extends SQLiteOpenHelper {

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESC = "desc";
	public static final String COLUMN_ALARM = "alarm";
	public static final String COLUMN_MONTH = "month";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_START = "start_time";
	public static final String COLUMN_END = "end_time";
	public static final String COLUMN_COLOR = "color";
	public static final String COLUMN_REP = "repetition";
	public static final String COLUMN_ASEC = "alarm_second";
	
	public static final String COLUMN_EVENT_ID = "e_id";
	public static final String COLUMN_OBJECT_ID = "obj_id";
	
	private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "USREVENTS";
    public static final String OBJECT_TABLE_NAME = "OBJTABLE";
	private final static String DATABASE_NAME = "plannerplus.db";
		
	private static final String DATABASE_CREATE = "CREATE TABLE "
		      + TABLE_NAME 
		      + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		      + COLUMN_NAME + " TEXT NOT NULL, "
		      + COLUMN_DESC + " TEXT, "
		      + COLUMN_ALARM + " TEXT NOT NULL, "
		      + COLUMN_MONTH + " INT NOT NULL, "
		      + COLUMN_DAY + " INT NOT NULL, "
		      + COLUMN_YEAR + " INT NOT NULL, "
		      + COLUMN_START + " INT NOT NULL, "
		      + COLUMN_END + " INT NOT NULL, "
		      + COLUMN_COLOR + " INT NOT NULL," 
		      +	COLUMN_REP + " TEXT NOT NULL,"
		      + COLUMN_ASEC + " BIGINT " + " );";
	
	private static final String OBJECT_TABLE_CREATE = "CREATE TABLE "
		      + OBJECT_TABLE_NAME
		      + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		      + COLUMN_EVENT_ID + " BIGINT NOT NULL, "
		      +	COLUMN_OBJECT_ID + " TEXT NOT NULL " + " );";
	
	  public SQLHelper(Context context) 
	  {
		    //super(context, DATABASE_NAME, null, DATABASE_VERSION);
		    super(context, "/mnt/sdcard/" + DATABASE_NAME, null, DATABASE_VERSION);
	  }
	
	  @Override
	  public void onCreate(SQLiteDatabase database) 
	  {
	    database.execSQL(DATABASE_CREATE);
	    database.execSQL(OBJECT_TABLE_CREATE);
	  }
	
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	  {
	    Log.w(SQLHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    db.execSQL("DROP TABLE IF EXISTS " + OBJECT_TABLE_NAME);
	    onCreate(db);
	  }
}
