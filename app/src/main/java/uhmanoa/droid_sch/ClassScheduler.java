package uhmanoa.droid_sch;


import java.sql.*;
import java.util.ArrayList;

public class ClassScheduler {

    private static final int START = 0;
    private static final int END = 1;
    public static final int SCHED_SIZE = 0;
    public static final int SCHED_START = 1;
    public static final int SCHED_DAYS = 2;
    private int year;
    private int semester;
    private SQL_DataSource ds;


    /* each schedule item in selectedClasses contains a list of courses
     * selected by the user that all belong to the same subject (ex. EE, ICS, etc).
     * These will be used for creating the final schedule options.
     */
    ArrayList<Schedule> selectedClasses;
    /* Each schedule item in possibleSchedules contains a list of courses
     * representing a non-overlaping schedule of courses selected by the user.
     */
    ArrayList<Schedule> possibleSchedules;

    public ClassScheduler(int sem, int yr, SQL_DataSource data) {
        ds = data;
        year = yr;
        semester = sem;
    }

    /**
     * Takes in an array of course titles (Strings) and returns
     * the cross product of those courses
     */
    private ArrayList<Schedule> findCourses(ArrayList<Star_obj> list) {

        ArrayList<Schedule> results = new ArrayList<>();

        // ----------------------- REDUNDANCY CHECK ---------------------------------
        ArrayList<String> courses = new ArrayList<>(); //Contains List of "Named" of Courses
        //Obtain List of Courses listed by Name
        for (Star_obj s : list) {
            String crs = s.getCourse();
            if (!courses.contains(crs) && s.getCRN() == -1) {
                //The list doesn't already contain the course and
                courses.add(crs);
            }
        }

        //Create new Star_Obj list containg non-reduant Star_Objs
        ArrayList<Star_obj> course_list = new ArrayList<>();
        //Iterate through current list
        for (Star_obj s : list) {
            boolean delete_current = false;
            for (String c : courses) {
                if (s.getCourse().equals(c) && s.getCRN() != -1) {
                    delete_current = true; //this star_obj is repeated
                    break;
                }
            }
            if (!delete_current) {
                course_list.add(s);
            }

        }
        courses = null; //dereference courses list, no longer used
        // ----------------------- REDUNDANCY CHECK ---------------------------------

        //course_list = star_obj w/o redudancies
        //names only contains courses searched by name
        ArrayList<Star_obj> names = new ArrayList<>();
        for (Star_obj so : course_list) {
            if (so.getCRN() == -1) {
                names.add(so);
            }
        }

        //create schedules of the same courses in same schedule container
        for (Star_obj so : names) {
            Schedule s = new Schedule(-1, year, semester);
            ArrayList<Course> crs = ds.getCoursesByName(semester, year, so.getCourse());
            for (Course c : crs) {
                s.addCourse(c);
            }
            results.add(s);
        }

        //crns only contains courses searched by
        ArrayList<Star_obj> crns = new ArrayList<>();
        for (Star_obj so : course_list) {
            if (so.getCRN() != -1) {
                crns.add(so);
            }
        }
        course_list = null; //dereference

        ArrayList<String> unique_crs = new ArrayList<>();
        for (Star_obj so : crns) {
            if (!unique_crs.contains(so.getCourse())) {
                unique_crs.add(so.getCourse());
            }
        }

        for (String s : unique_crs) {
            Schedule sch = new Schedule(-1, year, semester);
            for (Star_obj so : crns) {
                if (so.getCourse().equals(s)) {
                    Course c = ds.getCourseByCRN(semester, year, so.getCRN());
                    sch.addCourse(c);
                }
            }
            results.add(sch);
        }

        for (Schedule s : results) {
            s.display();
        }

        return results;
    }

    public ArrayList<Schedule> getPossibleSchedules(ArrayList<Star_obj> so) {
        ArrayList<Schedule> course_list = findCourses(so); //gets all courses into one container
        ArrayList<Schedule> sorted_list = sortSchedules(course_list, SCHED_SIZE);
        ArrayList<Schedule> result_list = createSchedules(sorted_list, sorted_list.size());
        return result_list;
    }

    /**
     * Given schedules marks how many conflicts there are
     * per schedule, then returns desired schedules
     *
     * @param list Array of schedules.  Each schedule contains a list of CRN's offered
     *             for a given class.
     * @return Array of possible minimum courses with/without overlap
     */
    private ArrayList<Schedule> createSchedules(ArrayList<Schedule> list, int schedMinSize) {
        ArrayList<Schedule> scheds = new ArrayList<Schedule>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (checkOverlap(list.get(i), schedMinSize))
                scheds.add(list.get(i));
        }

        return scheds;
    }

    /**
     * Given schedule returns if schedule falls within minimum overlap
     *
     * @param list Schedule and desired amount of non-conflicting classes
     * @return True or False if the schedule falls within minimum desired courses
     */
    private boolean checkOverlap(Schedule list, int schedMinSize) {
        if ((list.getCourses().size() - schedMinSize) < 0)
            return false;
        if (Conflict(list) <= (list.getCourses().size() - schedMinSize))
            return true;
        else
            return false;
    }

    /**
     * Given schedule returns how many courses do not conflict
     *
     * @param Schedule of Courses
     * @return Number of courses that don't overlap
     */
    private int Conflict(Schedule list) {
        int conflict = 0;
        ArrayList<Course> sched = list.getCourses();
        int size = sched.size();

        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++)
                if (sched.get(i).overlaps(sched.get(j)))
                    conflict++;
        }

        return conflict;

    }


    /**
     * Finds all possible combinations of non-overlapping courses and creates
     * an array of possible schedules. Functions finds the next non-overlapping
     * class and, if there are no more CRN's for that course, it adds it and
     * checks the next course.  If there are more CRN's for the current course, it
     * copies the current schedule and branches to find all combinations of classes
     * for the next CRN.
     * <p> For the parameters - Courses are defined as a specific course title (ex. EE 324),
     * while Classes refer to specific CRN's within the course.</p>
     *
     * @param sched          The current schedule being created.
     * @param desiredCourses Array of desired courses entered by the user.  Includes
     *                       all CRN's available for the desired courses.
     * @param courseNum      The index for the array of courses currently being checked.
     * @param classNum       The index for the array of classes for the current course.
     * @param allScheds      The array of all schedules being created.
     * @return An array of all schedules created.
     */
    private ArrayList<Schedule> addNextCourse(Schedule sched, ArrayList<Schedule> desiredCourses,
                                              int courseNum, int classNum, ArrayList<Schedule> allScheds, int schedMinSize) {

		/*	If we've gone through each desired class, add the current
         * schedule to the list of schedules and return */
        if (courseNum >= desiredCourses.size()) {
            if (sched.getCourses().size() >= schedMinSize)
                if (!allScheds.contains(sched))
                    allScheds.add(sched);

            return allScheds;
        }

        ArrayList<Course> classes = desiredCourses.get(courseNum).getCourses();
		/* If classNum is out of bounds, return  */
        if (classNum >= classes.size())
            return allScheds;

		/* find the next class that doesn't overlap classes in the
		 * current schedule
		 */
        for (Course c : sched.getCourses()) {
            while ((classNum < classes.size()) && (classes.get(classNum).overlaps(c))) {
                classNum++;
            }
        }

		/*  if there are more classes in the current course listing, create another
		 * schedule and find more alternatives 	 */
        if (classNum < classes.size() - 1)
            addNextCourse(new Schedule(sched), desiredCourses, courseNum, classNum + 1, allScheds, schedMinSize);

		/*  if we've found a class that doesn't overlap the current schedule, then
		 * add the class to the current schedule */
        if (classNum < classes.size())
            sched.addCourse(classes.get(classNum));

		/*  find the next class to add to the schedule */
        addNextCourse(sched, desiredCourses, courseNum + 1, 0, allScheds, schedMinSize);

        return allScheds;
    }

    /**
     * Sorts an array of schedules based on the passed criteria.
     *
     * @param s       Unsorted array of schedules.
     * @param orderBy Int constant specifying how to sort the array.
     * @return Sorted list of schedules.
     */
    private ArrayList<Schedule> sortSchedules(ArrayList<Schedule> s, int orderBy) {
        ArrayList<Schedule> sorted = new ArrayList<Schedule>();

        switch (orderBy) {
            case SCHED_SIZE:
                int numCourses = s.size();
                int[] order = new int[numCourses];

                // order will contain indexes (of the array to be sorted) in
                // the order the array should be sorted
                for (int i = 0; i < numCourses; i++)
                    order[i] = i;

                for (int i = 0; i < numCourses - 1; i++) {
                    Schedule s1 = s.get(order[i]);
                    Schedule s2 = s.get(order[i + 1]);
                    if ((s.get(order[i]).getCourses().size()) >
                            s.get(order[i + 1]).getCourses().size()) {
                        int temp = order[i];
                        order[i] = order[i + 1];
                        order[i + 1] = temp;
                        for (int j = i; j > 0; j--) {
                            if ((s.get(order[j]).getCourses().size()) <
                                    s.get(order[j - 1]).getCourses().size()) {
                                temp = order[j - 1];
                                order[j - 1] = order[j];
                                order[j] = temp;
                            }

                        }
                    }
                }
                // build the sorted list
                for (int i = 0; i < numCourses; i++)
                    sorted.add(s.get(order[i]));

                break;


        }
        return sorted;
    }

    /**
     * Parses a string of days (ex. 'MWF') into an array of characters.
     * One entry for each day (ex. M,W,F).
     *
     * @param days String of days to be parsed.
     * @return Array of the parsed days.
     */
    private ArrayList<Character> getDaysList(String days) {
        if (days.equals("NULL"))
            return null;

        ArrayList<Character> d = new ArrayList<Character>();
        for (int i = 0; i < days.length(); i++)
            d.add(days.charAt(i));

        return d;
    }

    /**
     * Breaks a string of focus requirements into separate, individual
     * requirements.
     *
     * @param f String of focus requirements (delimited by '.')
     * @return Array of strings - one for
     */
    private ArrayList<String> getFocusList(String f) {
        if (f == "NULL") return null;

        ArrayList<String> focus = new ArrayList<String>();
        String temp = f;

        int num = 0;
        // count number of delimiters ('.')
        for (int i = 0; i < f.length(); i++)
            if (f.charAt(i) == '.') num++;

        // for each delimiter, break up into separate words
        for (int i = 0; i < num; i++) {
            int place = temp.indexOf('.');
            focus.add(temp.substring(0, place));
            temp = temp.substring(place + 1, temp.length());
        }
        focus.add(temp);

        return focus;
    }

    public static void quitOnError(String msg) {
        debug(msg);
        System.exit(0);
    }

    public static void debug(String msg) {
        System.out.println(msg);
    }

}