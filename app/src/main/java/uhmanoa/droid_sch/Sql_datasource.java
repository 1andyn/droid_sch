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

public class Sql_datasource {
    /* Class Member Variables */
    private Sql_helper dbhelper;
    private SQLiteDatabase database;

    /* Enumerations for Columns */
    static enum col {
        course, crn, start, start2, end, end2, room, room2, prof, credit, sch_id, semester
    }

    final int col_id = 2; // Third column of the table is #2 (starting at 0,1...

    static enum col_gefc {
        fga, fgb, fgc, fs, fw, da, db, dh, dl, dp, ds, dy, hsl, ni, eth,
        hap, oc, wi
    }

    private String[] fav_column = {Sql_helper.COLUMN_CRSNAME, Sql_helper.COLUMN_CRN,
    Sql_helper.COLUMN_SEM};
    private String[] sch_column = {Sql_helper.COLUMN_CRSNAME, Sql_helper.COLUMN_CRN,
            Sql_helper.COLUMN_START, Sql_helper.COLUMN_START2, Sql_helper.COLUMN_END,
            Sql_helper.COLUMN_END2, Sql_helper.COLUMN_ROOM, Sql_helper.COLUMN_ROOM2,
            Sql_helper.COLUMN_PROF, Sql_helper.COLUMN_CREDIT, Sql_helper.COLUMN_SCH_ID,
            Sql_helper.COLUMN_SEM};

    private String[] gefc_column = {Sql_helper.COLUMN_fga, Sql_helper.COLUMN_fgb,
            Sql_helper.COLUMN_fgc, Sql_helper.COLUMN_fs, Sql_helper.COLUMN_fw, Sql_helper.COLUMN_da,
            Sql_helper.COLUMN_db, Sql_helper.COLUMN_dh, Sql_helper.COLUMN_dl, Sql_helper.COLUMN_dp,
            Sql_helper.COLUMN_dy, Sql_helper.COLUMN_hsl, Sql_helper.COLUMN_ni,
            Sql_helper.COLUMN_eth, Sql_helper.COLUMN_oc, Sql_helper.COLUMN_wi};

    public void open() throws SQLException {
        database = dbhelper.getWritableDatabase();
    }

    public Sql_datasource(Context context) {
        dbhelper = new Sql_helper(context);
    }

    /* Closes Database Helper */
    public void close() {
        dbhelper.close();
    }

//    /* Returns corresponding Starred Row Data */
//    private Star_obj cursorToStarObj(Cursor curs) {
//        Star_obj so = new Star_obj(curs.getString(col.course.ordinal()),
//                curs.getInt(col.crn.ordinal()),
//                curs.getInt(col_id),
//                curs.getInt(col.semester.ordinal()));
//        return so;
//    }

    /* Stores Favorite Object into corresponding Table
    * It then runs a function for converting the row into a
     * fav_obj (object/class representation of the Favorite Object */

    public Star_obj save_fav(Star_obj pStar) {
        ContentValues values = new ContentValues();
        values.put(Sql_helper.COLUMN_CRSNAME, pStar.getCourse());
        values.put(Sql_helper.COLUMN_CRN, pStar.getCRN());

        long insertId = database.insert(Sql_helper.TABLE_NAME, null, values);
        Cursor curse = database.query(Sql_helper.TABLE_NAME, fav_column, null, null, null, null,
                null);
        curse.moveToFirst();
//        Star_obj so = cursorToStarObj(curse);
        curse.close();
//        return so;
        return null;
    }

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
        System.out.println("Deleted event with id: " + id);
        database.delete(Sql_helper.TABLE_NAME, Sql_helper.COLUMN_SCH_ID + " = " + id, null);
    }

    public Star_obj getStrObj(long id) {
        Star_obj so = null;
        Cursor curse = database.query(Sql_helper.TABLE_NAME, fav_column, null, null, null, null,
                Sql_helper.COLUMN_ID + " ASC,");
        curse.moveToFirst();
        while (!curse.isAfterLast()) {
//            so = cursorToStarObj(curse);
            if (so.getID() == id) {
                return so;
            }
            curse.moveToNext();
        }
        curse.close();
        return so;
    }

    public ArrayList<Star_obj> getAllSO() {
        ArrayList<Star_obj> all_starobj = new ArrayList<Star_obj>();
        Cursor curse = database.query(Sql_helper.TABLE_NAME, fav_column, null, null, null, null,
                Sql_helper.COLUMN_CRN + " ASC");
        curse.moveToFirst();
        while (!curse.isAfterLast()) {
//            Star_obj so = cursorToStarObj(curse);
//            all_starobj.add(so);
            curse.moveToNext();
        }
        curse.close();
        return all_starobj;
    }

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

    public void clear_star() {
        database.delete(Sql_helper.TABLE_NAME, null, null);
        System.out.println("Removed all Starred/Favorite Courses");
    }

    public void clear_saved() {
        database.delete(Sql_helper.TABLE_NAME2, null, null);
        database.delete(Sql_helper.TABLE_NAME3, null, null);
        System.out.println("Cleared Saved Schedules");
    }
}