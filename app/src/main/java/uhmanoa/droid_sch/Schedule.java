package uhmanoa.droid_sch;

import java.util.ArrayList;

/** An array of non-overlapping courses to be taken in a semester.
 * To guarantee courses do not overlap, this class is usually populated
 * using overlap checking methods from the {@link Course} class prior to
 * adding to the Schedule.
 * 
 * @author Jimmy
 *
 */
public class Schedule {
    private long sid;
	private ArrayList<Course> schedule;
    private int semester;
    private int year;
    private boolean checked = false; //always initally unchecked
	
	/** 
	 * Empty constructor.
	 */
	public Schedule(long id, int yr, int sem){
        sid = id;
		year = yr;
        semester = sem;
        schedule = new ArrayList<>();
	}
	
	/**
	 * Copy constructor adds all courses from the given course
	 * to the new course.
	 * @param s Existing schedule of courses to be copied.
	 */
	public Schedule(Schedule s){
		this.sid = s.getID();
        this.year = s.getYear();
        this.semester = s.getSemester();
        schedule = new ArrayList<>();
        checked = false; //alaways initially unchecked
		for (Course c : s.getCourses()){
			schedule.add(c);
		}
	}
	
	/** 
	 * Returns the schedule's list of courses
	 * @return	List of courses.
	 */
	public ArrayList<Course> getCourses(){
		return schedule;
	}
	
	/**
	 * Adds a course to the schedule.
	 * @param c	Course to be added.
	 */
	public void addCourse(Course c){
		if (!schedule.contains(c))
			if (!c.isInvalid())
				schedule.add(new Course(c));
			else{
				System.out.println("Invalid course, please fill in appropriate fields.");
				System.out.println("Current course data is: ");
				c.display();
			}
	}

    public void setID(long pId) {
        sid = pId;
    }

    public long getID() {
        return sid;
    }

    public void setSemester(int pSem) { semester = pSem; }

    public int getSemester() { return semester; }

    public void setYear(int yr) {
        year = yr;
    }

    public int getYear() {
        return year;
    }

	/**
	 * Display the list of courses in the schedule.
	 */
	public void display(){
		for (Course c : getCourses())
			c.display();
	}

    public int earliestStart() {
        int time = getCourses().get(0).getStart1(); //initialize to very first start possible
        if(getCourses().size() == 0) return 0;
        for(Course c: getCourses()) {
            int temp = c.getStart1();
            if(c.getStart1() == -1 || c.getStart2() == -1) {
                continue;
            }
            if(c.getStart2() != 9999) {
                int temp2 = c.getStart2();
                temp = Math.min(temp, temp2);
            }
            time = Math.min(time, temp);
        }
        return time;
    }

    public int latestEnd() {
        int time = getCourses().get(0).getEnd1(); //initialize end to very first end possible
        if(getCourses().size() == 0) return 0;
        for(Course c: getCourses()) {
            int temp = c.getEnd1();
            if(c.getEnd2() != 9999) {
                int temp2 = c.getEnd2();
                temp = Math.max(temp, temp2);
            }
            time = Math.max(time, temp);
        }
        return time;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean chk) {
        checked = chk;
    }

}
