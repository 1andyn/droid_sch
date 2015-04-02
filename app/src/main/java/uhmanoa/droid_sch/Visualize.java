package uhmanoa.droid_sch;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Visualize extends Activity {

    final int white = -1;
    final int none = -1;
    final int max_height = 300;

    private TableLayout table_layout;

    private Schedule sch;
    private ArrayList<String> time_values;
    private ArrayList<String> day_values;
    private ArrayList<Vis_CellRow> height_values;
    private ArrayList<Integer> color_values;

    final int hours_day = 24;
    final static int first_hour = 0;
    final int days_week = 7;

    int pxWidth;

    private void initializeTimeValues() {
        time_values = new ArrayList<>();
        int counter = 1;
        boolean start_PM = false;
        for (int x = 0; x < hours_day; x++) {
            if (x == first_hour) {
                time_values.add("12:00a");
            } else {
                if (counter == 12 && start_PM == false) {
                    //Change to pm
                    start_PM = true;
                }
                if (start_PM) {
                    time_values.add(String.valueOf(counter) + ":00p");
                } else {
                    time_values.add(String.valueOf(counter) + ":00a");
                }
                counter++;
                if (counter > 12) {
                    counter = 1;
                }
            }
        }
    }

    private int getStartHour(int input) {
        return input / 100;
    }

    //debug
    public ArrayList<String> DEBUG_getTimeValues() {
        return time_values;
    }

    public ArrayList<Vis_CellRow> DEBUG_getHeights(Schedule sce) {
        return height_values;
    }

    public ArrayList<Course> DEBUG_getDayMatches(int day, Schedule s) {
        return getDayMatches(day, s);
    }

    private void initializeColorValues() {
        color_values = new ArrayList<>();
        color_values.add(R.color.light_red);
        color_values.add(R.color.light_blue);
        color_values.add(R.color.light_green);
        color_values.add(R.color.light_orange);
        color_values.add(R.color.light_magenta);
        color_values.add(R.color.navy);
        color_values.add(R.color.mauve);
        color_values.add(R.color.dark_aqua);
    }

    private void initializeDayValues() {
        day_values = new ArrayList<>();
        day_values.add("U");
        day_values.add("M");
        day_values.add("T");
        day_values.add("W");
        day_values.add("R");
        day_values.add("F");
        day_values.add("S");
    }

    private void configureDisplay() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        pxWidth = metrics.widthPixels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
        configureDisplay();
        initializeTimeValues();
        initializeDayValues();
        initializeColorValues();
        loadSchedule();
        height_values = new ArrayList<Vis_CellRow>();
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        populateHeights();
        BuildTable(sch);
        config_ListDisplay();
    }

    private void config_ListDisplay() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.vis_LinearLayout1);

        for(Course c: sch.getCourses()) {
            View vw = getLayoutInflater().inflate(R.layout.vis_list_item, null);
            vw.setBackgroundColor(getResources().getColor(color_values.get((int)c.getID())));
            //LinearLayout top = (LinearLayout) vw.findViewById(R.id.vis_frs_crs);
            LinearLayout bot = (LinearLayout) vw.findViewById(R.id.vis_sec_crs);
            LinearLayout seatdta = (LinearLayout) vw.findViewById(R.id.ll_seat_data);
            if(c.getStart2() == 9999) {
                bot.setVisibility(View.GONE);
            } else {
                TextView crs2 = (TextView) vw.findViewById(R.id.vis_crs2);
                TextView time2 = (TextView) vw.findViewById(R.id.vis_time2);
                TextView room2 = (TextView) vw.findViewById(R.id.vis_room2);
                TextView day2 = (TextView) vw.findViewById(R.id.vis_days2);

                crs2.setText(""); //doesn't need to be populated since its the same course
                time2.setText(c.getTimeString(true));
                room2.setText(c.getRoom2());
                day2.setText(c.getDayString(true));
            }

            TextView crs1 = (TextView) vw.findViewById(R.id.vis_crs);
            TextView time1 = (TextView) vw.findViewById(R.id.vis_time);
            TextView room1 = (TextView) vw.findViewById(R.id.vis_room);
            TextView day = (TextView) vw.findViewById(R.id.vis_days);

            TextView prof = (TextView) vw.findViewById(R.id.vis_prof);
            TextView sect = (TextView) vw.findViewById(R.id.vis_sect);
            TextView creds = (TextView) vw.findViewById(R.id.vis_cred);

            TextView crn = (TextView) vw.findViewById(R.id.vis_CRN);

            TextView seats = (TextView) vw.findViewById(R.id.vis_seats);
            TextView wait = (TextView) vw.findViewById(R.id.vis_wait);
            TextView waitav = (TextView) vw.findViewById(R.id.vis_wait_av);

            TextView dates = (TextView) vw.findViewById(R.id.vis_dates);
            TextView focus = (TextView) vw.findViewById(R.id.vis_focus);

            crn.setText("CRN: " + String.valueOf(c.getCrn()));

            prof.setText("Prof: " + c.getProfessor());
            sect.setText("Section: " + String.valueOf(c.getSection()));
            creds.setText("Credits: " + String.valueOf(c.getCredits()));

            seats.setText("Seats Avail: " + String.valueOf(c.getSeats_avail()));
            wait.setText("Waitlsited: " + String.valueOf(c.getWaitlisted()));
            waitav.setText("Wait Avail: " + String.valueOf(c.getWait_avail()));

            dates.setText("Dates: " + c.getDates());
            focus.setText("Focus: " + c.getFocusReqString());

            crs1.setText(c.getCourse());
            time1.setText(c.getTimeString(false));
            room1.setText(c.getRoom1());
            day.setText(c.getDayString(false));

            ll.addView(vw);
            ll.addView(producerDivider());
        }

    }

    private void DEBUG_schedules() {
        ArrayList<Character> days1 = new ArrayList<Character>();
        days1.add('M');
        days1.add('W');
        days1.add('F');
        sch = new Schedule(0, 2015, 1);

        ArrayList<String> foc = new ArrayList<>();
        foc.add("WI");

        Course crs = new Course("ICS 314", "Software Engineering I", 51804, "3",
                "B Auernheimer", days1, 830, 920, "SAKAM D101", 1, 10, 0, 10, "3/3 to 4/27",
                "MATH CLASS ");
        crs.setFocusReqs(foc);
        Course crs2 = new Course("ICS 314", "Software Engineering I", 51804, "3",
                "B Auernheimer", days1, 930, 1020, "SAKAM D101", 1, 10, 0, 10, "3/3 to 4/27",
                "MATH CLASS ");
        Course crs3 = new Course("ICS 314", "Software Engineering I", 51804, "3",
                "B Auernheimer", days1, 1030, 1120, "SAKAM D101", 1, 10, 0, 10, "3/3 to 4/27",
                "MATH CLASS ");
        sch.addCourse(crs);
        sch.addCourse(crs2);
        sch.addCourse(crs3);
        ArrayList<Character> days2 = new ArrayList<>();
        days2.add('T');
        days2.add('R');

        ArrayList<Character> days3 = new ArrayList<>();
        days3.add('S');
        Course crs4 = new Course("ICS 314", "Software Engineering I", 51804, "3",
                "B Auernheimer", days2, 855, 1145, "SAKAM D101", 1, 10, 0, 10, "3/3 to 4/27",
                "MATH CLASS ");
        Course crs5 = new Course("ICS 314", "Software Engineering I", 51804, "3",
                "B Auernheimer", days3, 1130, 1245, "SAKAM D101", 1, 10, 0, 10, "3/3 to 4/27",
                "MATH CLASS ");
        sch.addCourse(crs4);
        sch.addCourse(crs5);

        ArrayList<Character> days4 = new ArrayList<Character>();
        days4.add('M');
        days4.add('W');
        days4.add('F');
        ArrayList<Character> days5 = new ArrayList<Character>();
        days5.add('S');

        Course crs6 = new Course("ICS 314", "Software Engineering I", 51804, "3",
                "B Auernheimer", days4, days5, 1230, 900, 1330, 1000, "SAKAM D101", "SAKAM D202",
                1, 10, 0, 10, "3/3 to 4/27",
                "MATH CLASS ");
        sch.addCourse(crs6);
    }

    private void loadSchedule() {
        //stub for now, needs to load schedule from SQLite database
        DEBUG_schedules();
    }

    private void BuildTable(Schedule sch) {
        //Time is in military format e.g. 2400 for 12am
        int start_time = getStartHour(sch.earliestStart());
        int end_time = getEndHour(sch.latestEnd());

        // Row Loop
        for (int row = 0; row < hours_day + 1; row++) {

            TableRow table_row = new TableRow(this);
            table_row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            if (!(row >= start_time && row < end_time || row == hours_day)) {
                table_row.setVisibility(TableRow.GONE);
            }

            Vis_CellRow vis_row = null;
            if(row != hours_day) {
                vis_row = height_values.get(row);
            }

            // Column Loop
            for (int col = 0; col <= days_week; col++) {

                //This is the first column so this is time Data
                if (col == 0) {
                    //If this is the last row then it should not have a time
                    if (row >= (hours_day)) {
                        View vw = timeTextView("   ");
                        table_row.addView(vw);
                    } else {
                        // else set a time
                        View vw = timeTextView(time_values.get(row));
                        table_row.addView(vw);
                    }
                } else {
                    // If this is the last row
                    if (row == (hours_day)) {
                        //Set a letter to represent the day
                        View vw = timeTextView(day_values.get(col - 1));
                        table_row.addView(vw);
                    } else {
                        //Configure what it should look like

                        View vis_box = getLayoutInflater().inflate(R.layout.vis_layout, null);

                        View start = vis_box.findViewById(R.id.vis_top);
                        View middle = vis_box.findViewById(R.id.vis_mid);
                        View end = vis_box.findViewById(R.id.vis_bot);

                        Vis_Cell vc = vis_row.getVisCell(col - 1);

                        start.setLayoutParams(new TableRow.LayoutParams());
                        start.getLayoutParams().height = vc.getTop();
                        start.getLayoutParams().width = getColumnWidth();
                        if((int)vc.getTopColor() == white) {
                            start.setBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            start.setBackgroundColor(getResources().getColor(color_values.get(
                                    (int) vc.getTopColor())));
                        }
                        middle.setLayoutParams(new TableRow.LayoutParams());
                        middle.getLayoutParams().height = vc.getMid();
                        middle.getLayoutParams().width = getColumnWidth();
                        if((int)vc.getMidColor() == white) {
                            middle.setBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            middle.setBackgroundColor(getResources().getColor(color_values.get(
                                    (int) vc.getMidColor())));
                        }

                        end.setLayoutParams(new TableRow.LayoutParams());
                        end.getLayoutParams().height = vc.getBot();
                        end.getLayoutParams().width = getColumnWidth();
                        if((int)vc.getBotColor() == white) {
                            end.setBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            end.setBackgroundColor(getResources().getColor(color_values.get(
                                    (int) vc.getBotColor())));
                        }

                        table_row.addView(vis_box);
                    }
                }
            }
            table_layout.addView(table_row);
        }
    }

    private void populateHeights() {
        //assign unique ids to courses
        assignUniqueID(sch.getCourses());

        int start = 0;
        int end = 59;
        for (int x = 0; x < hours_day; x++) {
            //row logic

            Vis_CellRow vcr = new Vis_CellRow();
            for (int y = 0; y < days_week; y++) {
                //column logic
                ArrayList<Course> matches = getDayMatches(y, sch);
                if (matches.size() == 0) {
                    //is empty so add empty vis_cell
                    vcr.addVisCell(getVisCell(null, null, false, false, 5));
                } else {
                    int vis_case = 5;

                    Vis_Package vp = getCoursesWithin(start, end, matches, y);
                    ArrayList<Course> actual_matches = vp.getMatches();
                    ArrayList<Boolean> course_selector = vp.getSec();
                    vis_case = vp.getCase();
                    matches = null; //dereference

                    if (actual_matches.size() == 0) {
                        //is empty so add empty vis_cell
                        vcr.addVisCell(getVisCell(null, null, false, false, 5));
                        continue;
                    }

                    switch (vis_case) {
                        case 1:
                            vcr.addVisCell(getVisCell(actual_matches.get(0), null,
                                    course_selector.get(0), false, 1));
                            continue;
                        case 2:
                            vcr.addVisCell(getVisCell(null, actual_matches.get(0),
                                    false, course_selector.get(0), 2));
                            continue;
                        case 3:
                            vcr.addVisCell(getVisCell(actual_matches.get(0), actual_matches.get(1),
                                    course_selector.get(0), course_selector.get(1), 3));
                            continue;
                        case 4:
                            vcr.addVisCell(getVisCell(actual_matches.get(0), null,
                                    course_selector.get(0), false, 4));
                            continue;
                    }
                    //CASE 5 FOUND NOTHING
                    vcr.addVisCell(getVisCell(null, null, false, false, 5));
                    continue;
                }
            }
            height_values.add(vcr);
            start = end + 1;
            end = end + 60;
        }
    }

    private boolean containsDay (Course c, boolean sec, int day) {
        ArrayList<Character> days;
        if(sec) {
            days = c.getDays2();
        } else {
            days = c.getDays1();
        }

        char firstLetter = day_values.get(day).charAt(0);
        if(days.contains(firstLetter)) {
            return true;
        }

        return false;
    }

    private Vis_Package getCoursesWithin(int start, int end, ArrayList<Course> matches, int day) {

        ArrayList<Course> actual_matches = new ArrayList<Course>();
        ArrayList<Boolean> secondary_crs = new ArrayList<Boolean>();
        //boolean values of sel, FALSE = first course e.g. getStart1
        // TRUE = second course e.g. getStart2
        // Sel is for keeping track of which start/end time we are looking at


        //CASE 4 CHECKER
        for (Course c : matches) {

            boolean firstDayCheck = containsDay(c, false, day);
            boolean secDayCheck = false;

            if(c.getStart2() != 9999) {
                secDayCheck = containsDay(c, true, day);
            }


            if ((courseTimeToMinutes(c.getStart1()) <= start) &&
                    (courseTimeToMinutes(c.getEnd1()) >= end) &&
                    firstDayCheck
                    ) {
                //this course goes through this block of time
                actual_matches.add(c);
                secondary_crs.add(false);
                return new Vis_Package(actual_matches, secondary_crs, 4);
            }

            if ((courseTimeToMinutes(c.getStart2()) <= start) &&
                    (courseTimeToMinutes(c.getEnd2()) >= end) &&
                    secDayCheck) {
                actual_matches.add(c);
                secondary_crs.add(true);
                return new Vis_Package(actual_matches, secondary_crs, 4);
            }
        }

        //CASE 1,2,3
        ArrayList<Course> special_matches = new ArrayList<Course>();
        ArrayList<Boolean> start_end = new ArrayList<Boolean>();
        ArrayList<Boolean> sec_sel = new ArrayList<Boolean>();
        // start_end, TRUE if time in question START, FALSE if the time in question is END
        // sec_sel is same as sel ArrayList, except since we may rearrange it, use temp for now

        for (Course c : matches) {

            boolean firstDayCheck = containsDay(c, false, day);
            boolean secDayCheck = false;

            if(c.getStart2() != 9999) {
                secDayCheck = containsDay(c, true, day);
            }

            //  CASE 1 CHECKS
            int top = courseTimeToMinutes(c.getEnd1());
            if (top >= start && top <= end && firstDayCheck) {
                special_matches.add(c);
                start_end.add(false); //Looking at END TIME
                sec_sel.add(false); //Looking at START1/END1
            }

            if(c.getEnd2() != 9999) {
                top = courseTimeToMinutes(c.getEnd2());
                if (top >= start && top <= end && secDayCheck) {
                    special_matches.add(c);
                    start_end.add(false);//Looking at END TIME
                    sec_sel.add(true);//Looking at START2/END2
                }
            }
            // ----------------

            //  CASE 2 CHECKS
            int bot = courseTimeToMinutes(c.getStart1());
            if (bot >= start && bot <= end && firstDayCheck) {
                special_matches.add(c);
                start_end.add(true); //Looking at START TIME
                sec_sel.add(false); //Looking at START1/END1
            }
            if(c.getStart2() != 9999) {
                bot = courseTimeToMinutes(c.getStart2());
                if (bot >= start && bot <= end && secDayCheck) {
                    special_matches.add(c);
                    start_end.add(true); //Looking at START TIME
                    sec_sel.add(true);//Looking at START2/END2
                }
            }
            // ----------------
            // CASE 3 -> Multiple Courses  inside ACTUAL_MATCHES

        }

        int vcase = 5;
        switch (special_matches.size()) {
            case 1:
                //if theres size 1, theres only one course; either case 1 or 2
                if (start_end.get(0)) {
                    //looking at starting time
                    vcase = 2; //CASE 2
                } else {
                    //looking at ending time
                    vcase = 1; //CASE 1
                }
                return new Vis_Package(special_matches, sec_sel, vcase);

            case 2:
                //first element need of actual matches and sel arraylist need to be TOP
                //second element needs to be BOT
                //LARGER TIME we are looking at is BOT

                vcase = 3;

                int first_course_time;
                int second_course_time;

                if (start_end.get(0)) {
                    if (sec_sel.get(0)) {
                        first_course_time = courseTimeToMinutes(special_matches.get(0).getStart2());
                    } else {
                        first_course_time = courseTimeToMinutes(special_matches.get(0).getStart1());
                    }
                } else {
                    if (sec_sel.get(0)) {
                        first_course_time = courseTimeToMinutes(special_matches.get(0).getEnd2());
                    } else {
                        first_course_time = courseTimeToMinutes(special_matches.get(0).getEnd1());
                    }
                }

                if (start_end.get(1)) {
                    if (sec_sel.get(1)) {
                        second_course_time = courseTimeToMinutes(
                                special_matches.get(1).getStart2());
                    } else {
                        second_course_time = courseTimeToMinutes(
                                special_matches.get(1).getStart1());
                    }
                } else {
                    if (sec_sel.get(1)) {
                        second_course_time = courseTimeToMinutes(
                                special_matches.get(1).getEnd2());
                    } else {
                        second_course_time = courseTimeToMinutes(
                                special_matches.get(1).getEnd1());
                    }
                }

                if (first_course_time > second_course_time) {
                    //this means first course is BOT so it needs to be SECOND element
                    ArrayList<Course> corrected_list = new ArrayList<>();
                    corrected_list.add(special_matches.get(1));
                    corrected_list.add(special_matches.get(0));
                    secondary_crs.add(sec_sel.get(1));
                    secondary_crs.add(sec_sel.get(0));
                    return new Vis_Package(corrected_list, secondary_crs, vcase);
                } else {
                    //original order is correct
                    return new Vis_Package(special_matches, sec_sel, vcase);
                }
        }

        //CASE 5
        return new Vis_Package(actual_matches, secondary_crs, 5);
    }

    public void assignUniqueID(ArrayList<Course> crs) {
        if(crs == null || crs.size() == 0) return;
        for (Course c : crs) {
            c.setID(uniqueCourseID(crs));
        }
    }


    private long uniqueCourseID(ArrayList<Course> crs) {
        long id = 0;
        boolean unique = false; // Initialize Unique to False
        while (!unique) {
            boolean match = false; // Reset Match Flag to False
            for (int x = 0; x < crs.size(); x++) {
                // Iterate al_strobj and check if there's an existing match to the ID
                Long cmp;
                cmp = crs.get(x).getID();
                // If Match Exist, set match to true
                if (cmp.equals(id)) {
                    //Match found
                    match = true;
                    break; // Break out of For Loop
                }
            }
            if (match) {
                id++; //Increment ID
            } else {
                unique = true;
            }
        }
        return id;
    }

    private ArrayList<Course> getDayMatches(int day, Schedule s) {
        ArrayList<Course> matches = new ArrayList<>();
        if(s.getCourses().size() == 0 || s.getCourses() == null) {
            return matches;
        }

        char firstLetter = day_values.get(day).charAt(0);
        for (Course c : s.getCourses()) {
            ArrayList<Character> days = c.getDays1();
            if (days.contains(firstLetter)) {
                matches.add(c);
                continue; //already added to list so remove
            }
            if (c.getStart2() != 9999) {
                if (c.getDays2().contains(firstLetter)) {
                    matches.add(c);
                    continue;
                }
            }
        }
        return matches;
    }


    private Vis_Cell getVisCell(Course top, Course bot, boolean top_sec, boolean bot_sec,
                                int vcase) {
    /*
        Course Top = Course that starts in the left side of the cell
        Course bot = Course that starts in the right side of the cell
        top_sec; if true the time data in question refers to the secondary end time
        bot_sec; if true the time data in question refers to the secondary start time
    */

	/*
        CASE 1, Top = Scaled, Middle = Remaining. Bot = 0; TOP = Course color, Middle = White
    	CASE 2, Top = 0, Middle = Remaining, Bot = Scaled; BOT = Course color, Middle = White
        CASE 3, Top = Scaled, Middle = Remaining, Bot = Scaled; Top/Bot = Course colors,
        Middle = White
        CASE 4, Top = 0, Middle = FULL Width, Bot = 0; Middle = Course color
        CASE 5, Top = 0, Middle = FULL Width, Bot = 0; Middle = White
    */

        int top_end;
        int bot_start;

        if (top != null) {
            if (top_sec) {
                top_end = top.getEnd2();
            } else
                top_end = top.getEnd1();
        } else {
            top_end = none;
        }

        if (bot != null) {
            if (bot_sec) {
                bot_start = bot.getStart2();
            } else {
                bot_start = bot.getStart1();
            }
        } else {
            bot_start = none;
        }

        int top_height = getTopHeight(top_end);
        int bot_height = getBotHeight(bot_start);

        //Vis_Cell (top, mid, bot, color, color, color)
        final int max_height = 300;
        switch (vcase) {
            case 1:
                return new Vis_Cell(top_height, (max_height - top_height), 0, top.getID(), white,
                        white, vcase);
            case 2:
                return new Vis_Cell(0, (max_height - bot_height), bot_height, white, white,
                        bot.getID(), vcase);
            case 3:
                return new Vis_Cell(top_height, (max_height - top_height - bot_height), bot_height,
                        top.getID(), white, bot.getID(),vcase);
            case 4:
                // Assumes that if a time entire hour is taken by a course then it the course is
                // passed through the TOP course parameter
                return new Vis_Cell(0, max_height, 0, white, top.getID(), white, vcase);
            case 5:
                return new Vis_Cell(0, max_height, 0, white, white, white, vcase);
            default:
                //just leave it empty
                return new Vis_Cell(0, max_height, 0, white, white, white, vcase);
        }
    }

    private int courseTimeToMinutes(int crsTimeFormat) {
        final int min_hr = 60;
        int hours = getHoursFromTime(crsTimeFormat);
        int minutes = getTimeMinutes(crsTimeFormat);
        return (hours * min_hr) + minutes;
    }

    private int getHoursFromTime(int crstime) {
        return crstime / 100;
    }

    private int getTimeMinutes(int crstime) {
        return crstime % 100;
    }

    public int DEBUG_getTopHeight(int EndTime) {
        return getTopHeight(EndTime);
    }

    public int DEBUG_getBotHeight(int StartTime) {
        return getBotHeight(StartTime);
    }

    private int getTopHeight(int EndTime) {
        if (EndTime == -1) {
            return 0;
        }
        final int max_height = 300;
        final int min_hour = 60;

        double et = (double) getTimeMinutes(EndTime);
        double height = (et/60) * max_height;

        return (int) height;
    }

    private int getBotHeight(int StartTime) {
        if (StartTime == -1) {
            return 0;
        }
        final int max_height = 300;
        final int min_hour = 60;

        double st= (double) getTimeMinutes(StartTime);
        double height = ((min_hour - st)/60) * max_height;

        return (int) height;
    }

    private int getEndHour(int input) {
        return (int) Math.ceil((double) input / 100);
    }

    private int getColumnWidth() {
        return pxWidth / (days_week + 1);
    }

    private View timeTextView(String input) {
        View table_view = getLayoutInflater().inflate(R.layout.vis_time, null);
        LinearLayout lv = (LinearLayout) table_view.findViewById(R.id.vis_ll);

        TextView tv = (TextView) table_view.findViewById(R.id.tv_time);
        tv.setText(input);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setLayoutParams(new TableRow.LayoutParams(getColumnWidth() - 10,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER);
        return table_view;
    }

    private ImageView producerDivider() {
        ImageView divider = new ImageView(this);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        divider.setLayoutParams(lp);
        divider.setBackgroundColor(this.getResources().getColor(R.color.darker_gray));
        return divider;
    }

}