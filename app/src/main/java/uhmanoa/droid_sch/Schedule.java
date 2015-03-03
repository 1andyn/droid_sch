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
    private int sid;
	private ArrayList<Course> schedule;
    private int semester;
	
	/** 
	 * Empty constructor.
	 */
	public Schedule(){
		schedule = new ArrayList<Course>();
	}
	
	/**
	 * Copy constructor adds all courses from the given course
	 * to the new course.
	 * @param s Existing schedule of courses to be copied.
	 */
	public Schedule(Schedule s){
		this();
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

    public void setID(int pId) {
        sid = pId;
    }

    public int getID() {
        return sid;
    }

    public void setSemester(int pSem) { semester = pSem; }

    public int getSemester() { return semester; }

	/**
	 * Display the list of courses in the schedule.
	 */
	public void display(){
		for (Course c : getCourses())
			c.display();
	}

    public int earliestStart() {
        int time = 0;
        for(Course c: getCourses()) {
            int temp = c.getStart1();
            int temp2 = c.getStart2();
            temp = Math.min(temp, temp2);
            time = Math.min(time, temp);
        }
        return time;
    }

    public int latestEnd() {
        int time = 0;
        for(Course c: getCourses()) {
            int temp = c.getEnd1();
            int temp2 = c.getEnd2();
            temp = Math.max(temp, temp2);
            time = Math.max(time, temp);
        }
        return time;
    }

}
