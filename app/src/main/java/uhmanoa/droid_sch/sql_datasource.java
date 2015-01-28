package uhmanoa.droid_sch;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class sql_datasource {
	
	/* Enumeration for COLUMNS */
	private int COL_ID = 0;
	private int COL_NAME = 1;
	private int COL_DESC = 2;
	private int COL_ALARM = 3;
	private int COL_MONTH = 4;
	private int COL_DAY = 5;
	private int COL_YEAR = 6;
	private int COL_START = 7;
	private int COL_END = 8;
	private int COL_COL = 9;
	private int COL_REP = 10;
	private int COL_ALA = 11;
	
	private int START = 0;
	private int COL_E_ID = 1;
	private int COL_OBJ_ID = 2;
	
	private String []allColumnsObj = {sql_helper.COLUMN_ID, sql_helper.COLUMN_EVENT_ID, sql_helper.COLUMN_OBJECT_ID};
	
	private final static String NO_OVERLAP = "N";
	private final static String nofaultEvent = NO_OVERLAP;
	
	private Cal_Module cal_mod;
	private Repetition_Module rep_mod;
	private SQLHelper dbhelper;
	private SQLiteDatabase database;
	private String[] allColumns = { sql_helper.COLUMN_ID, sql_helper.COLUMN_NAME, sql_helper.COLUMN_DESC,
			sql_helper.COLUMN_ALARM, sql_helper.COLUMN_MONTH, sql_helper.COLUMN_DAY, sql_helper.COLUMN_YEAR,
			sql_helper.COLUMN_START, sql_helper.COLUMN_END, sql_helper.COLUMN_COLOR, sql_helper.COLUMN_REP,
			sql_helper.COLUMN_ASEC };
	
	public SQL_DataSource(Context context)
	{
		dbhelper = new SQLHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbhelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbhelper.close();
	}
	
	public table_insert createEvent(Event e)
	{
		ContentValues values = new ContentValues();
		values.put(SQLHelper.COLUMN_NAME, e.getName());
		values.put(SQLHelper.COLUMN_DESC, e.getDescription());
		values.put(SQLHelper.COLUMN_ALARM, e.getAlarm());
		values.put(SQLHelper.COLUMN_MONTH, e.GetMonth());
		values.put(SQLHelper.COLUMN_DAY, e.GetDay());
		values.put(SQLHelper.COLUMN_YEAR, e.GetYear());
		values.put(SQLHelper.COLUMN_START, e.GetStart());
		values.put(SQLHelper.COLUMN_END, e.GetEnd());
		values.put(SQLHelper.COLUMN_COLOR, e.getColor());
		values.put(SQLHelper.COLUMN_REP, e.getRep());
		values.put(SQLHelper.COLUMN_ASEC, e.get_Asec());
		
		/* Supposedly adds all values in ContentValues values to database*/
		long insertId = database.insert(SQLHelper.TABLE_NAME, null, values);
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, 
				SQLHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		curse.moveToFirst();
		Event newEvent = cursorToEvent(curse);
		curse.close();
		
		return newEvent;
	}
	
	public String saveObjectID(long id, String ObjectID)
	{
		ContentValues values = new ContentValues();
		values.put(SQLHelper.COLUMN_EVENT_ID, id);
		values.put(SQLHelper.COLUMN_OBJECT_ID, ObjectID);
		
		/* Supposedly adds all values in ContenValues values to second table*/
		long insertId = database.insert(SQLHelper.OBJECT_TABLE_NAME, null, values);
		Cursor curse = database.query(SQLHelper.OBJECT_TABLE_NAME, allColumnsObj, 
				SQLHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		curse.moveToFirst();

		curse.close();
		
		return ObjectID;
	}
		
	public void deleteEvent(Event e)
	{
		long id = e.GetID();
		System.out.println("Deleted event with id: " + id);
		database.delete(SQLHelper.TABLE_NAME, SQLHelper.COLUMN_ID + " = " + id, null);
	}
	
	public void deleteEvent(long id)
	{
		System.out.println("Deleted event with id: " + id);
		database.delete(SQLHelper.TABLE_NAME, SQLHelper.COLUMN_ID + " = " + id, null);
	}
	
	public void deleteEventObj(long id)
	{
		System.out.print("Deleted objectid with event id " + id);
		database.delete(SQLHelper.OBJECT_TABLE_NAME, SQLHelper.COLUMN_EVENT_ID + " = " + id, null);
	}
	
	public Event getEvent(long id)
	{
		Event my_event = new Event();
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, null, null, null, null, SQLHelper.COLUMN_YEAR + " ASC, "
				+ SQLHelper.COLUMN_MONTH + " ASC, " + SQLHelper.COLUMN_DAY + " ASC, " + SQLHelper.COLUMN_END + " ASC");
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

	    Cursor curse = database.query(SQLHelper.OBJECT_TABLE_NAME, allColumnsObj, null, null, null, null, null);

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
		Cursor curse = database.query(SQLHelper.OBJECT_TABLE_NAME, allColumnsObj, null, null, null, null, null);
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
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, null, null, null, null, SQLHelper.COLUMN_YEAR + " ASC, "
				+ SQLHelper.COLUMN_MONTH + " ASC, " + SQLHelper.COLUMN_DAY + " ASC, " + SQLHelper.COLUMN_END + " ASC");
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
			
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, null, null, null, null, SQLHelper.COLUMN_YEAR + " ASC, "
				+ SQLHelper.COLUMN_MONTH + " ASC, " + SQLHelper.COLUMN_DAY + " ASC, " + SQLHelper.COLUMN_END + " ASC");
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
	
	private int partition(ArrayList<Event> e, int left, int right)
	{
		int i = left, j = right;
		Event tmp;
		int pivot = e.get((left+right)/2).GetEnd();
		
		while(i <= j){
			while(e.get(i).GetEnd() < pivot)
				++i;
			while(e.get(j).GetEnd() > pivot)
				j--;
			if(i <= j){
				tmp = e.get(i);
				e.set(i, e.get(j));
				e.set(j, tmp);
				i++;
				j--;
			}
		}
		return i;
	}
	
	private void quickSort(ArrayList<Event> e, int left, int right)
	{
		int index = partition(e, left, right);
		if(left < index - 1) 
			quickSort(e, left, index - 1);
		if(left < right) 
			quickSort(e, index, right);
	}
	
	
	public String endTimeExists(Date d, long id)
	{
		String nofaultTodo = NO_OVERLAP;
		Cursor curse = database.query(SQLHelper.TABLE_NAME, allColumns, 
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
		database.delete(SQLHelper.TABLE_NAME, null, null);
		database.delete(SQLHelper.OBJECT_TABLE_NAME, null, null);
		System.out.println("Removed all Table Elements");
	}
	
	public void clear_object_table()
	{
		database.delete(SQLHelper.OBJECT_TABLE_NAME, null, null);
	}
	
	private ArrayList<Event> getEventsOfDay(Event e)
	{
		cal_mod = new Cal_Module();
		rep_mod = new Repetition_Module();
		rep_mod.set_RepString(e.getRep());
		int dayw;
		ArrayList<Event> repeatedEvents =  getAllEvents();
		ArrayList<Event> specRepeatedEvents = new ArrayList<Event>();
		for(int x = 0; x < repeatedEvents.size(); x++){	
			dayw = cal_mod.getWeekday(repeatedEvents.get(x).GetDate().get_CDate());
			if(rep_mod.toggle_Check(dayw)) specRepeatedEvents.add(repeatedEvents.get(x));
		}
		rep_mod = null; // Deallocate
		return specRepeatedEvents;
	}	
	

	
	
}
