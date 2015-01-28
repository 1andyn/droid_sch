package uhmanoa.droid_sch;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class sql_datasource {

    /* Class Member Variables */
    private sql_helper dbhelper;
    private SQLiteDatabase database;

    /* Enumerations for Columns */
    static enum col {
        course, crn, start, start2, end, end2, room, room2, prof, credit, sch_id
    }

    static enum col_gefc {
        fga, fgb, fgc, fs, fw, da, db, dh, dl, dp, ds, dy, hsl, ni, eth,
        hap, oc, wi
    }

    private String[] fav_column = {sql_helper.COLUMN_CRSNAME, sql_helper.COLUMN_CRN};
    private String[] sch_column = {sql_helper.COLUMN_CRSNAME, sql_helper.COLUMN_CRN,
            sql_helper.COLUMN_START, sql_helper.COLUMN_START2, sql_helper.COLUMN_END,
            sql_helper.COLUMN_END2, sql_helper.COLUMN_ROOM, sql_helper.COLUMN_ROOM2,
            sql_helper.COLUMN_PROF, sql_helper.COLUMN_CREDIT, sql_helper.COLUMN_SCH_ID};

    private String[] gefc_column = {sql_helper.COLUMN_fga, sql_helper.COLUMN_fgb,
            sql_helper.COLUMN_fgc, sql_helper.COLUMN_fs, sql_helper.COLUMN_fw, sql_helper.COLUMN_da,
            sql_helper.COLUMN_db, sql_helper.COLUMN_dh, sql_helper.COLUMN_dl, sql_helper.COLUMN_dp,
            sql_helper.COLUMN_dy, sql_helper.COLUMN_hsl, sql_helper.COLUMN_ni,
            sql_helper.COLUMN_eth, sql_helper.COLUMN_oc, sql_helper.COLUMN_wi};

    public sql_datasource(Context context) {
        dbhelper = new sql_helper(context);
    }

    public void open() throws SQLException {
        database = dbhelper.getWritableDatabase();
    }

    /* Closes Database Helper */
    public void close() {
        dbhelper.close();
    }

    /* Returns corresponding Starred Row Data */
    private star_obj cursorToStarObj(Cursor curs) {
        star_obj so = new star_obj(curs.getString(col.course.ordinal()),
                curs.getInt(col.crn.ordinal()));
        return so;
    }

    /* Stores Favorite Object into corresponding Table
    * It then runs a function for converting the row into a
     * fav_obj (object/class representation of the Favorite Object */
    public star_obj save_fav(star_obj pStar) {
        ContentValues values = new ContentValues();
        values.put(sql_helper.COLUMN_CRSNAME, pStar.getCourse());
        values.put(sql_helper.COLUMN_CRN, pStar.getCRN());

        long insertId = database.insert(sql_helper.TABLE_NAME, null, values);
        Cursor curse = database.query(sql_helper.TABLE_NAME, fav_column, null, null, null, null,
                null);
        curse.moveToFirst();
        star_obj so = cursorToStarObj(curse);
        curse.close();
        return so;
    }

    public String save_gefc() {
        //Stub
    }

    /* Need function for saving Schedules, have to create algorithm for creating unique sch id's
    * but multiple rows will share the same ID
    */

    public void saveSch() {
        //Stub
    }

    public void deleteSchedule(Schedule s) {
        long id = e.GetID();
        System.out.println("Deleted event with id: " + id);
        database.delete(sql_helper.TABLE_NAME, sql_helper.COLUMN_ID + " = " + id, null);
    }

    public void deleteEvent(long id) {
        System.out.println("Deleted event with id: " + id);
        database.delete(sql_helper.TABLE_NAME, sql_helper.COLUMN_ID + " = " + id, null);
    }

    public void deleteEventObj(long id) {
        System.out.print("Deleted objectid with event id " + id);
        database.delete(sql_helper.OBJECT_TABLE_NAME, sql_helper.COLUMN_EVENT_ID + " = " + id, null);
    }

    public star_obj getStrObj(long id) {
        Event my_event = new Event();
        Cursor curse = database.query(sql_helper.TABLE_NAME, allColumns, null, null, null, null, sql_helper.COLUMN_YEAR + " ASC, "
                + sql_helper.COLUMN_MONTH + " ASC, " + sql_helper.COLUMN_DAY + " ASC, " + sql_helper.COLUMN_END + " ASC");
        curse.moveToFirst();
        while (!curse.isAfterLast()) {
            Event event = cursorToEvent(curse);
            if (event.GetID() == id) {
                return event;
            }
            curse.moveToNext();
        }
        curse.close();
        return my_event;
    }

    public ArrayList<String> getAllObjects() {
        ArrayList<String> table_objid = new ArrayList<String>();

        Cursor curse = database.query(sql_helper.OBJECT_TABLE_NAME, allColumnsObj, null, null, null, null, null);

        curse.moveToFirst();
        while (!curse.isAfterLast()) {
            String ob_id = cursorToObjID(curse);
            table_objid.add(ob_id);
            curse.moveToNext();
        }

        return table_objid;

    }

    public String acquireObjectId(long id) {
        String s = null;
        boolean stop = false;
        Cursor curse = database.query(sql_helper.OBJECT_TABLE_NAME, allColumnsObj, null, null, null, null, null);
        curse.moveToFirst();
        while (!curse.isAfterLast() && !stop) {
            if (curse.getInt(COL_E_ID) == id) {
                s = curse.getString(COL_OBJ_ID);
                stop = true;
            }
            curse.moveToNext();
        }
        return s;
    }

    public ArrayList<star_obj> getAllSO() {
        ArrayList<star_obj> all_starobj = new ArrayList<star_obj>();
        Cursor curse = database.query(sql_helper.TABLE_NAME, fav_column, null, null, null, null,
                sql_helper.COLUMN_CRN + " ASC");
        curse.moveToFirst();
        while (!curse.isAfterLast()) {
            star_obj so = cursorToStarObj(curse);
            all_starobj.add(so);
            curse.moveToNext();
        }
        curse.close();
        return all_starobj;
    }

    /* Will be used for loading schedules */
    public void stub_function() {

        Cursor curse = database.query(sql_helper.TABLE_NAME, sch_column, null, null, null, null,
                sql_helper.COLUMN_SCH_ID + " ASC");
        curse.moveToFirst();
        while (!curse.isAfterLast()) {
            Event event = cursorToEvent(curse);
            if (event.GetDate().get_CDate().isEqual(d)) {
                partialEvents.add(event);
            } else {
                rep_mod.set_RepString(event.getRep());
                if (rep_mod.toggle_Check(dayofWeek)) {
                    partialEvents.add(event);
                }
            }
            curse.moveToNext();
        }
        curse.close();

        if (!partialEvents.isEmpty())
            quickSort(partialEvents, START, partialEvents.size() - 1);

        return partialEvents;
    }

    public void clear_star() {
        database.delete(sql_helper.TABLE_NAME, null, null);
        System.out.println("Removed all Starred/Favorite Courses");
    }

    public void clear_saved() {
        database.delete(sql_helper.TABLE_NAME2, null, null);
        database.delete(sql_helper.TABLE_NAME3, null, null);
        System.out.println("Cleared Saved Schedules");
    }
}
