package uhmanoa.droid_sch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/* This is a helper class for interacting with the database such as retrieving, inserting
* or deleting data. Functionality for this class will have to be implemented as more functionality
* is added to the core application */

public class SQL_DataSource {
    /* Class Member Variables */
    private SQL_Helper dbhelper;
    private SQLiteDatabase database;


    private String[] STAR_COLUMN = {
            SQL_Helper.COLUMN_ID,
            SQL_Helper.COLUMN_CRN,
            SQL_Helper.COLUMN_CRS,
            SQL_Helper.COLUMN_TITL,
            SQL_Helper.COLUMN_SEM,
            SQL_Helper.COLUMN_YEAR
    };

    enum STAR_ENUM {
        COLUMN_ID,
        COLUMN_CRN,
        COLUMN_CRS,
        COLUMN_TITL,
        COLUMN_SEM,
        COLUMN_YEAR
    }

    private String[] SCH_COLUMN = {
            SQL_Helper.COLUMN_ID,
            SQL_Helper.COLUMN_CRN,
            SQL_Helper.COLUMN_YEAR,
            SQL_Helper.COLUMN_SEM
    };

    enum SCH_ENUM {
        COLUMN_ID,
        COLUMN_CRN,
        COLUMN_YEAR,
        COLUMN_SEM
    }

    private String[] TSCH_COLUMN = {
            SQL_Helper.COLUMN_ID,
            SQL_Helper.COLUMN_CRN,
            SQL_Helper.COLUMN_YEAR,
            SQL_Helper.COLUMN_SEM
    };

    private String[] COURSE_COLUMN = {
            SQL_Helper.COLUMN_CRN,
            SQL_Helper.COLUMN_CRS,
            SQL_Helper.COLUMN_SECT,
            SQL_Helper.COLUMN_TITL,
            SQL_Helper.COLUMN_SEM,
            SQL_Helper.COLUMN_YEAR,
            SQL_Helper.COLUMN_START,
            SQL_Helper.COLUMN_START2,
            SQL_Helper.COLUMN_END,
            SQL_Helper.COLUMN_END2,
            SQL_Helper.COLUMN_ROOM,
            SQL_Helper.COLUMN_ROOM2,
            SQL_Helper.COLUMN_PROF,
            SQL_Helper.COLUMN_CREDIT,
            SQL_Helper.COLUMN_SEAT,
            SQL_Helper.COLUMN_WAITL,
            SQL_Helper.COLUMN_WAITLA,
            SQL_Helper.COLUMN_DATES
    };

    enum COURSE_ENUM {
        COLUMN_CRN,
        COLUMN_CRS,
        COLUMN_SECT,
        COLUMN_TITL,
        COLUMN_SEM,
        COLUMN_YEAR,
        COLUMN_START,
        COLUMN_START2,
        COLUMN_END,
        COLUMN_END2,
        COLUMN_ROOM,
        COLUMN_ROOM2,
        COLUMN_PROF,
        COLUMN_CREDIT,
        COLUMN_SEAT,
        COLUMN_WAITL,
        COLUMN_WAITLA,
        COLUMN_DATES
    }

    private String[] CFOCUS_COLUMN = {
            SQL_Helper.COLUMN_CRS,
            SQL_Helper.COLUMN_SEM,
            SQL_Helper.COLUMN_YEAR,
            SQL_Helper.COLUMN_FGA,
            SQL_Helper.COLUMN_FGB,
            SQL_Helper.COLUMN_FGC,
            SQL_Helper.COLUMN_FS,
            SQL_Helper.COLUMN_FW,
            SQL_Helper.COLUMN_DA,
            SQL_Helper.COLUMN_DB,
            SQL_Helper.COLUMN_DH,
            SQL_Helper.COLUMN_DL,
            SQL_Helper.COLUMN_DP,
            SQL_Helper.COLUMN_DY,
            SQL_Helper.COLUMN_HSL,
            SQL_Helper.COLUMN_NI,
            SQL_Helper.COLUMN_ETH,
            SQL_Helper.COLUMN_HAP,
            SQL_Helper.COLUMN_OC,
            SQL_Helper.COLUMN_WI
    };

    enum CFOC_ENUM {
        COLUMN_CRS,
        COLUMN_SEM,
        COLUMN_YEAR,
        COLUMN_FGA,
        COLUMN_FGB,
        COLUMN_FGC,
        COLUMN_FS,
        COLUMN_FW,
        COLUMN_DA,
        COLUMN_DB,
        COLUMN_DH,
        COLUMN_DL,
        COLUMN_DP,
        COLUMN_DY,
        COLUMN_HSL,
        COLUMN_NI,
        COLUMN_ETH,
        COLUMN_HAP,
        COLUMN_OC,
        COLUMN_WI
    }

    private String[] CDAYS_COLUMN = {
            SQL_Helper.COLUMN_CRN,
            SQL_Helper.COLUMN_YEAR,
            SQL_Helper.COLUMN_SEM,
            SQL_Helper.COLUMN_SECDAY,
            SQL_Helper.COLUMN_SUN,
            SQL_Helper.COLUMN_MON,
            SQL_Helper.COLUMN_TUE,
            SQL_Helper.COLUMN_WED,
            SQL_Helper.COLUMN_THR,
            SQL_Helper.COLUMN_FRI,
            SQL_Helper.COLUMN_SAT
    };

    enum CDAY_ENUM {
        COLUMN_CRN,
        COLUMN_YEAR,
        COLUMN_SEM,
        COLUMN_SECDAY,
        COLUMN_SUN,
        COLUMN_MON,
        COLUMN_TUE,
        COLUMN_WED,
        COLUMN_THR,
        COLUMN_FRI,
        COLUMN_SAT
    }

    private String[] PREF_COLUMN = {
            SQL_Helper.COLUMN_PNAME,
            SQL_Helper.COLUMN_CRSMIN,
            SQL_Helper.COLUMN_SUN,
            SQL_Helper.COLUMN_MON,
            SQL_Helper.COLUMN_TUE,
            SQL_Helper.COLUMN_WED,
            SQL_Helper.COLUMN_THR,
            SQL_Helper.COLUMN_FRI,
            SQL_Helper.COLUMN_SAT
    };

    enum PREF_ENUM {
        COLUMN_PNAME,
        COLUMN_CRSMIN,
        COLUMN_SUN,
        COLUMN_MON,
        COLUMN_TUE,
        COLUMN_WED,
        COLUMN_THR,
        COLUMN_FRI,
        COLUMN_SAT
    }

    private String[] PBLOCK_COLUMN = {
            SQL_Helper.COLUMN_PNAME,
            SQL_Helper.COLUMN_START,
            SQL_Helper.COLUMN_END,
            SQL_Helper.COLUMN_SUN,
            SQL_Helper.COLUMN_MON,
            SQL_Helper.COLUMN_TUE,
            SQL_Helper.COLUMN_WED,
            SQL_Helper.COLUMN_THR,
            SQL_Helper.COLUMN_FRI,
            SQL_Helper.COLUMN_SAT
    };

    enum PBLOCK_ENUM {
        COLUMN_PNAME,
        COLUMN_START,
        COLUMN_END,
        COLUMN_SUN,
        COLUMN_MON,
        COLUMN_TUE,
        COLUMN_WED,
        COLUMN_THR,
        COLUMN_FRI,
        COLUMN_SAT
    }


    public void open() throws SQLException {
        database = dbhelper.getWritableDatabase();
    }

    public SQL_DataSource(Context context) {
        dbhelper = new SQL_Helper(context);
    }

    /* Closes Database Helper */
    public void close() {
        dbhelper.close();
    }

    //--------------------------- STARRED OBJECT DB HELPER FUNCTIONS----------------------------//
    //save starred
    /* Stores Favorite Object into corresponding Table
    * It then runs a function for converting the row into a
     * fav_obj (object/class representation of the Favorite Object */

    public Star_obj saveStar(Star_obj pStar) {
        ContentValues values = new ContentValues();
        values.put(SQL_Helper.COLUMN_CRS, pStar.getCourse());
        values.put(SQL_Helper.COLUMN_CRN, pStar.getCRN());
        values.put(SQL_Helper.COLUMN_TITL, pStar.getCourseTitle());
        values.put(SQL_Helper.COLUMN_SEM, pStar.getSemester());
        values.put(SQL_Helper.COLUMN_YEAR, pStar.getYear());

        long id = database.insert(SQL_Helper.TABLE_STAR, null, values);
        pStar.setID(id);
        Cursor curse = database.query(SQL_Helper.TABLE_STAR, STAR_COLUMN, null, null, null, null,
                null);
        curse.moveToFirst();
        Star_obj so = cursorToStarObj(curse);
        curse.close();
        return so;
    }

    //delete starred
    public void deleteStar(long id) {
        System.out.println("Deleting Starred Object with id: " + id);
        database.delete(SQL_Helper.TABLE_STAR, SQL_Helper.COLUMN_ID + " = " + id, null);
    }

    //get all starred
    /* Returns corresponding Starred Row Data */
    private Star_obj cursorToStarObj(Cursor curs) {
        Star_obj so = new Star_obj(
                curs.getString(STAR_ENUM.COLUMN_CRS.ordinal()),
                curs.getString(STAR_ENUM.COLUMN_TITL.ordinal()),
                curs.getInt(STAR_ENUM.COLUMN_CRN.ordinal()),
                curs.getInt(STAR_ENUM.COLUMN_ID.ordinal()),
                curs.getInt(STAR_ENUM.COLUMN_SEM.ordinal()),
                curs.getInt(STAR_ENUM.COLUMN_YEAR.ordinal()));
        return so;
    }


    public ArrayList<Star_obj> getAllStar() {
        ArrayList<Star_obj> all_starobj = new ArrayList<Star_obj>();
        Cursor curse = database.query(SQL_Helper.TABLE_STAR, STAR_COLUMN, null, null, null, null,
                SQL_Helper.COLUMN_CRN + " ASC");
        curse.moveToFirst();
        while (!curse.isAfterLast()) {
            Star_obj so = cursorToStarObj(curse);
            all_starobj.add(so);
            curse.moveToNext();
        }
        curse.close();
        return all_starobj;
    }

    //delete all starred
    public void deleteAllStar() {
        database.delete(SQL_Helper.TABLE_STAR, null, null);
        System.out.println("Removed all Starred/Favorite Courses");
    }


    //--------------------------- STARRED OBJECT DB HELPER FUNCTIONS----------------------------//

    public void save_gefc() {
        //Stub
    }

    /* Need function for saving Schedules, have to create algorithm for creating unique sch id's
    * but multiple rows will share the same ID
    */

    public void saveSch() {
        //Stub
    }

    public void deleteSchedule(Schedule s) {
        long id = s.getID();
        System.out.println("Deleted Schedule with id: " + id);
        database.delete(SQL_Helper.TABLE_SCH, SQL_Helper.COLUMN_ID + " = " + id, null);
    }


//    public Star_obj getStrObj(long id) {
//        Star_obj so = null;
//        Cursor curse = database.query(SQL_Helper.TABLE_NAME, fav_column, null, null, null, null,
//                SQL_Helper.COLUMN_ID + " ASC,");
//        curse.moveToFirst();
//        while (!curse.isAfterLast()) {
////            so = cursorToStarObj(curse);
//            if (so.getID() == id) {
//                return so;
//            }
//            curse.moveToNext();
//        }
//        curse.close();
//        return so;
//    }


    /* Will be used for loading schedules */
    public void stub_function() {
//
//        Cursor curse = database.query(Sql_helper.TABLE_NAME, sch_column, null, null, null, null,
//                Sql_helper.COLUMN_SCH_ID + " ASC");
//        curse.moveToFirst();
//        while (!curse.isAfterLast()) {
//            Event event = cursorToEvent(curse);
//            if (event.GetDate().get_CDate().isEqual(d)) {
//                partialEvents.add(event);
//            } else {
//                rep_mod.set_RepString(event.getRep());
//                if (rep_mod.toggle_Check(dayofWeek)) {
//                    partialEvents.add(event);
//                }
//            }
//            curse.moveToNext();
//        }
//        curse.close();
//        if (!partialEvents.isEmpty())
//            quickSort(partialEvents, START, partialEvents.size() - 1);
//        return partialEvents;
    }

    public void clearCourseData() {
        database.delete(SQL_Helper.TABLE_COURSE, null, null);
        database.delete(SQL_Helper.TABLE_CFOCUS, null, null);
        database.delete(SQL_Helper.TABLE_CDAY, null, null);
    }

    public void clearTempSch() {
        database.delete(SQL_Helper.TABLE_TSCH, null, null);
    }

}