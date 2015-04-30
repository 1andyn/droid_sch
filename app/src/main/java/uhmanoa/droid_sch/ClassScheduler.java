package uhmanoa.droid_sch;


import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Iterator;

public class ClassScheduler {

    public static final int SCHED_SIZE = 0;
    private int year;
    private int semester;
    private SQL_DataSource ds;
    private int min = -1;
    private ArrayList<Star_obj> crns;
    private Course timeblock = null;


    /* each schedule item in selectedClasses contains a list of courses
     * selected by the user that all belong to the same subject (ex. EE, ICS, etc).
     * These will be used for creating the final schedule options.
     */
    ArrayList<Schedule> selectedClasses;
    /* Each schedule item in possibleSchedules contains a list of courses
     * representing a non-overlaping schedule of courses selected by the user.
     */
    ArrayList<Schedule> possibleSchedules;

    public ClassScheduler(int sem, int yr, SQL_DataSource data, int mini, ArrayList<Star_obj> crn,
                          Course tb) {
        ds = data;
        year = yr;
        semester = sem;
        min = mini;
        crns = crn;
        timeblock = tb;
    }

    private ArrayList<Schedule> findCourses(ArrayList<String> course_list) {

        ArrayList<Schedule> results = new ArrayList<>();
        //course_list = star_obj w/o redudancies
        //names only contains courses searched by name

        //create schedules of the same courses in same schedule container
        for (String str : course_list) {
            Schedule s = new Schedule(-1, year, semester);
            ArrayList<Course> crs = ds.getCoursesByName(semester, year, str);
            for (Course c : crs) {
                s.addCourse(c);
            }
            results.add(s);
        }

        if(timeblock != null) {
            Schedule sch = new Schedule(0, year, semester);
            sch.addTimeBlock(timeblock);
            results.add(sch);
        }

        //if there aren't any specific CRN's we are looking at, just return this list
        if(crns.isEmpty()) {
            return results;
        }

        for (Star_obj so : crns) {
            String crs = so.getCourse();

            ArrayList<Integer> crn_match = new ArrayList<>();
            for (int x = 0; x < crns.size(); x++) {
                if (crs.equals(crns.get(x).getCourse())) {
                    crn_match.add(crns.get(x).getCRN());
                }
            }

            for (Schedule s : results) {
                ArrayList<Course> crz = s.getCourses();
                for(Iterator<Course> c = crz.iterator(); c.hasNext();) {
                    Course crse = c.next();
                    String crs_name = crse.getCourse();
                    int crs_crn = crse.getCrn();

                    if(crs_name.equals(crs)) {
                        //If the name matches the current course we are looking at
                        if(!crn_match.contains(crs_crn)) {
                            c.remove();
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        return results;
    }


    private ArrayList<String> getCourseList (ArrayList<Star_obj> so) {
        ArrayList<String> results = new ArrayList<>();
        for(Star_obj s : so) {
            if(!results.contains(s.getCourse())) {
                results.add(s.getCourse());
            }
        }
        return  results;
    }

    public ArrayList<Schedule> getPossibleSchedules(ArrayList<Star_obj> so) {

        ArrayList<Schedule> results = new ArrayList<>();

        int semester, year;
        semester = so.get(0).getSemester();
        year = so.get(0).getYear();
        ArrayList<String> crs_list = getCourseList(so);

        ArrayList<ArrayList<String>> search_list = new ArrayList<>();

        int minimum = min;

        if(minimum == -1) {
            minimum = crs_list.size();
        }
        int maximum = crs_list.size();

        String input[] = crs_list.toArray(new String[crs_list.size()]);
        ICombinatoricsVector<String> initVec = Factory.createVector(input);

        for(int x = minimum; x < maximum + 1; x++) {
            Generator<String> cgen = Factory.createSimpleCombinationGenerator(initVec, x);
            for(ICombinatoricsVector<String> combo: cgen) {
                ArrayList<String> temp = new ArrayList<>();
                temp.addAll(combo.getVector());
                search_list.add(temp);
            }
        }

        for(ArrayList<String> collection : search_list) {
            ArrayList<Schedule> course_list = findCourses(collection); //gets all courses into one container
            ArrayList<Schedule> sorted_list = sortSchedules(course_list, SCHED_SIZE);
            ArrayList<Schedule> result_list = createSchedules(sorted_list, sorted_list.size(),
                    semester, year);
            results.addAll(result_list);
        }
        return results;
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