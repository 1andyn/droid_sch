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

	private String []fav_column = {sql_helper.COLUMN_CRSNAME, sql_helper.COLUMN_CRN};
    private String []sch_column = {sql_helper.COLUMN_CRSNAME, sql_helper.COLUMN_CRN,
            sql_helper.COLUMN_START, sql_helper.COLUMN_START2, sql_helper.COLUMN_END,
        sql_helper.COLUMN_END2, sql_helper.COLUMN_ROOM, sql_helper.COLUMN_ROOM2,
        sql_helper.COLUMN_PROF, sql_helper.COLUMN_CREDIT, sql_helper.COLUMN_SCH_ID };

    private String []gefc_column = {sql_helper.COLUMN_fga, sql_helper.COLUMN_fgb,
            sql_helper.COLUMN_fgc, sql_helper.COLUMN_fs, sql_helper.COLUMN_fw, sql_helper.COLUMN_da,
            sql_helper.COLUMN_db, sql_helper.COLUMN_dh, sql_helper.COLUMN_dl, sql_helper.COLUMN_dp,
            sql_helper.COLUMN_dy, sql_helper.COLUMN_hsl, sql_helper.COLUMN_ni,
            sql_helper.COLUMN_eth, sql_helper.COLUMN_oc, sql_helper.COLUMN_wi };


	public sql_datasource(Context context)
	{
		dbhelper = new sql_helper(context);
	}
	
	public void open() throws SQLException{
		database = dbhelper.getWritableDatabase();
	}

    /* Closes Database Helper */
	public void close()
	{
		dbhelper.close();
	}

    /* Stores Favorite Object into corresponding Table
    * It then runs a function for converting the row into a
     * fav_obj (object/class representation of the Favorite Object */
	public star_obj save_fav(star_obj pStar)
	{
		ContentValues values = new ContentValues();
		values.put(sql_helper.COLUMN_CRSNAME, pStar.getName());
		values.put(sql_helper.COLUMN_CRN, pStar.getCRN());

		long insertId = database.insert(sql_helper.TABLE_NAME, null, values);
		Cursor curse = database.query(sql_helper.TABLE_NAME, fav_column, null, null, null, null,
                null);
		curse.moveToFirst();
		Event newEvent = cursorToEvent(curse);
		curse.close();
		
		return newEvent;
	}
	
	public String saveObjectID(long id, String ObjectID)
	{
		ContentValues values = new ContentValues();
		values.put(sql_helper.COLUMN_EVENT_ID, id);
		values.put(sql_helper.COLUMN_OBJECT_ID, ObjectID);
		
		/* Supposedly adds all values in ContenValues values to second table*/
		long insertId = database.insert(sql_helper.OBJECT_TABLE_NAME, null, values);
		Cursor curse = database.query(sql_helper.OBJECT_TABLE_NAME, allColumnsObj, 
				sql_helper.COLUMN_ID + " = " + insertId, null, null, null, null);
		curse.moveToFirst();

		curse.close();
		
		return ObjectID;
	}
		
	public void deleteEvent(Event e)
	{
		long id = e.GetID();
		System.out.println("Deleted event with id: " + id);
		database.delete(sql_helper.TABLE_NAME, sql_helper.COLUMN_ID + " = " + id, null);
	}
	
	public void deleteEvent(long id)
	{
		System.out.println("Deleted event with id: " + id);
		database.delete(sql_helper.TABLE_NAME, sql_helper.COLUMN_ID + " = " + id, null);
	}
	
	public void deleteEventObj(long id)
	{
		System.out.print("Deleted objectid with event id " + id);
		database.delete(sql_helper.OBJECT_TABLE_NAME, sql_helper.COLUMN_EVENT_ID + " = " + id, null);
	}
	
	public Event getEvent(long id)
	{
		Event my_event = new Event();
		Cursor curse = database.query(sql_helper.TABLE_NAME, allColumns, null, null, null, null, sql_helper.COLUMN_YEAR + " ASC, "
				+ sql_helper.COLUMN_MONTH + " ASC, " + sql_helper.COLUMN_DAY + " ASC, " + sql_helper.COLUMN_END + " ASC");
		curse.moveToFirst();
		while(!curse.isAfterLast()){
			Event event = cursorToEvent(curse);
			if(event.GetID() == id){
				return event;
			}
			curse.moveToNext();
		}
		curse.close();
		return my_event;
	}
	
	public ArrayList<String> getAllObjects()
	{
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
	
	public String acquireObjectId (long id)
	{
		String s = null;
		boolean stop = false;
		Cursor curse = database.query(sql_helper.OBJECT_TABLE_NAME, allColumnsObj, null, null, null, null, null);
		curse.moveToFirst();
		while (!curse.isAfterLast() && !stop) {
	    	  if(curse.getInt(COL_E_ID) == id){
	    		  s = curse.getString(COL_OBJ_ID);
	    		  stop = true;
	    	  }
		      curse.moveToNext();
	    }
	    return s;
	}
	
	public ArrayList<Event> getAllEvents()
	{		
		ArrayList<Event> allEvents = new ArrayList<Event>();
		Cursor curse = database.query(sql_helper.TABLE_NAME, allColumns, null, null, null, null, sql_helper.COLUMN_YEAR + " ASC, "
				+ sql_helper.COLUMN_MONTH + " ASC, " + sql_helper.COLUMN_DAY + " ASC, " + sql_helper.COLUMN_END + " ASC");
		curse.moveToFirst();
		while(!curse.isAfterLast()){
			Event event = cursorToEvent(curse);
			allEvents.add(event);
			curse.moveToNext();
		}
		curse.close();
		return allEvents;
	}
	
	public ArrayList<Event> getEventsForDate(Cal_Date d)
	{
		ArrayList<Event> partialEvents = new ArrayList<Event>();
		cal_mod = new Cal_Module();
		int dayofWeek = cal_mod.getWeekday(d);
		cal_mod = null; // Deallocate (save memory)
		rep_mod = new Repetition_Module();
			
		Cursor curse = database.query(sql_helper.TABLE_NAME, allColumns, null, null, null, null, sql_helper.COLUMN_YEAR + " ASC, "
				+ sql_helper.COLUMN_MONTH + " ASC, " + sql_helper.COLUMN_DAY + " ASC, " + sql_helper.COLUMN_END + " ASC");
		curse.moveToFirst();
		while(!curse.isAfterLast()){
			Event event = cursorToEvent(curse);
			if(event.GetDate().get_CDate().isEqual(d)){
				partialEvents.add(event);
			} else {
				rep_mod.set_RepString(event.getRep());
				if(rep_mod.toggle_Check(dayofWeek)){	
					partialEvents.add(event);
				}		
			}
			curse.moveToNext();
		}
		curse.close();
		rep_mod = null;
		
		if(!partialEvents.isEmpty())
			quickSort(partialEvents, START, partialEvents.size() - 1);
		
		return partialEvents;
	}
	
	public String overlapExists(Event d, long id)
	{
		ArrayList<Event> coreEvents = getEventsForDate(d.GetDate().get_CDate());
		/* Tests for Regular Event Overlap */
		for(int x = 0; x < coreEvents.size(); x++){
			if(d.GetDate().overlapDate(coreEvents.get(x).GetDate())){	
				if(coreEvents.get(x).GetID() != id){
					return coreEvents.get(x).getName();
				}
			}
		}
		coreEvents = null; // Deallocate Regular Events
		
		ArrayList<Event> repeatedEvents = getEventsOfDay(d);
		/* Tests for Repeating Event Overlap */
		for(int x = 0; x < repeatedEvents.size(); x++){
			if(d.GetDate().overlapDate(repeatedEvents.get(x).GetDate())){
				if(repeatedEvents.get(x).GetID() != id){
					return repeatedEvents.get(x).getName();
				}
			}
		}
		return nofaultEvent;
	}
	
	public String endTimeExists(Date d, long id)
	{
		String nofaultTodo = NO_OVERLAP;
		Cursor curse = database.query(sql_helper.TABLE_NAME, allColumns, 
				null, null, null ,null, null);
		curse.moveToFirst();
		while(!curse.isAfterLast())
		{
			Event event = cursorToEvent(curse);
			if(d.endTimeEqual(event.GetDate()))
			{
				if(event.GetID() != id) return event.getName();
			}
			curse.moveToNext();
		}
		curse.close();
		return nofaultTodo;
	}
	
	private Event cursorToEvent(Cursor curs)
	{
		Event newEvent = new Event();
		newEvent.setID(curs.getLong(COL_ID));
		newEvent.setName(curs.getString(COL_NAME));
		newEvent.setDescription(curs.getString(COL_DESC));
		newEvent.setAlarm(curs.getString(COL_ALARM));
		newEvent.setColor(curs.getInt(COL_COL));
		newEvent.set_Rep(curs.getString(COL_REP));
		newEvent.set_Asec(curs.getLong(COL_ALA));
		Date newDate = new Date();
		newDate.setMth(curs.getInt(COL_MONTH));
		newDate.setDay(curs.getInt(COL_DAY));
		newDate.setYr(curs.getInt(COL_YEAR));
		newDate.setStartTime(curs.getInt(COL_START));
		newDate.setEndTime(curs.getInt(COL_END));
		
		newEvent.setDate(newDate);
		return newEvent;
	}
	
	private String cursorToObjID(Cursor curs)
	{
		String s = curs.getString(COL_OBJ_ID);
		return s;
	}
	
	public void clear_table()
	{
		database.delete(sql_helper.TABLE_NAME, null, null);
		database.delete(sql_helper.OBJECT_TABLE_NAME, null, null);
		System.out.println("Removed all Table Elements");
	}
	
	public void clear_object_table()
	{
		database.delete(sql_helper.OBJECT_TABLE_NAME, null, null);
	}
	
}