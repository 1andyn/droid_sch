package uhmanoa.droid_sch;


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

        // ----------------------- REDUNDANCY CHECK ---------------------------------

//        System.out.println("DEBUG DATA");
//        for(Star_obj so: course_list) {
//            System.out.println(so.getCourse() + " " +  so.getCRN());
//        }

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

        return results;
    }

    public ArrayList<Schedule> getPossibleSchedules(ArrayList<Star_obj> so) {
        int semester, year;
        semester = so.get(0).getSemester();
        year = so.get(0).getYear();
        ArrayList<Schedule> course_list = findCourses(so); //gets all courses into one container
        ArrayList<Schedule> sorted_list = sortSchedules(course_list, SCHED_SIZE);
        ArrayList<Schedule> result_list = createSchedules(sorted_list, sorted_list.size(),
                semester, year);
        return result_list;
    }

    /**
     * Creates all possible non-conflicting schedules given a list of classes
     * a student desires to take.
     *
     * @return Array of possible non-conflicting schedules.
     */
    public static ArrayList<Schedule> createSchedules(ArrayList<Schedule> input, int schedMinSize,
                                                      int sem, int yr) {
        ArrayList<Schedule> scheds = new ArrayList<>();

        int numClasses = input.size(); // e.g. 4
        int[] numChoices = new int[numClasses]; // e.g. 4 1 2 1
        int maxResults = 0; // e.g. 8

        // Initialize numChoices, maxResults:
        for (int i = 0; i < numClasses; i++) {
            numChoices[i] = input.get(i).getCourses().size();
//            System.out.println("num:" + numChoices[i]);
            if (i == 0) {
                maxResults = numChoices[i];
            } else {
                maxResults *= numChoices[i];
            }
        }
//        System.out.println("max results: " + maxResults);

        // Set up all of the paths:
        int[][] paths = new int[maxResults][numClasses];
        for (int i = 0; i < maxResults; i++) for (int j = 0; j < numClasses; j++) paths[i][j] = -1;

        int[] current = new int[numClasses];
        for (int i = 0; i < numClasses; i++) current[i] = 0;

        for (int i = 0; i < maxResults; i++) {
            // write current to path
            for (int j = 0; j < numClasses; j++) {
                paths[i][j] = current[j];
            }

            // increment current:
            boolean carryOver = true;

            for (int j = numClasses - 1; j >= 0; j--) {
                if (carryOver == true) {
                    current[j]++;
                    if (current[j] >= numChoices[j]) {
                        current[j] = 0;
                        carryOver = true;
                    } else carryOver = false;
                }
            }
        }

        // Check each path, see if there are no overlaps:
        boolean[] allowed = new boolean[maxResults];
        for (int i = 0; i < maxResults; i++) allowed[i] = false;

        for (int i = 0; i < maxResults; i++) {
            // test each path
            boolean pass = true;
            outerloop:
            for (int j = 0; j < numClasses - 1; j++) {
                for (int k = j + 1; k < numClasses; k++) {
                    if (input.get(j).getCourses().get(paths[i][j]).overlaps(input.get(k).getCourses().get(paths[i][k]))) {
                        pass = false;
                        break outerloop;
                    }
                }
            }
            allowed[i] = pass;
        }

        // For each allowed path, add it to schedules:
        for (int i = 0; i < maxResults; i++) {
            if (allowed[i] == true) {
                Schedule sched = new Schedule(i, yr, sem);
                for (int j = 0; j < numClasses; j++) {
                    sched.addCourse(input.get(j).getCourses().get(paths[i][j]));
                }
                scheds.add(sched);
            }
        }

        return scheds;
    }

    public static void quitOnError(String msg) {
        debug(msg);
        System.exit(0);
    }

    public static void debug(String msg) {
        System.out.println(msg);
    }


    public static ArrayList<Schedule> sortSchedules(ArrayList<Schedule> s, int orderBy) {
        ArrayList<Schedule> sorted = new ArrayList<>();

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
}