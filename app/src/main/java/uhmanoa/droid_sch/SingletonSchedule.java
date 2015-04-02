package uhmanoa.droid_sch;

import java.util.ArrayList;


public class SingletonSchedule {
    private static SingletonSchedule instance = null;
	private ArrayList<Course> courses;

	private SingletonSchedule(){
        courses = new ArrayList<Course>();
	}

    public static SingletonSchedule getInstance() {
        if(instance == null) {
            instance = new SingletonSchedule();
        }
        return instance;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> c) {

        if(c != null && !c.isEmpty()) {
            courses.clear();
            for (Course crs : c) {
                courses.add(crs);
            }
        }

    }

	public void addCourse(Course c){
		if (!courses.contains(c))
			if (!c.isInvalid())
				courses.add(new Course(c));
			else{
				System.out.println("Invalid course, please fill in appropriate fields.");
				System.out.println("Current course data is: ");
				c.display();
			}
	}


}
