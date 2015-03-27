package uhmanoa.droid_sch;

import java.util.ArrayList;

/**
 * Stores a detailed course listing for courses offered at UH at Manoa.
 *
 * @author James Hartman
 */
public class Course {
    private String course;
    /**
     * Class title
     */
    private String title;
    /**
     * Focus Requirements the course meets
     */
    private ArrayList<String> focusReqs;
    /**
     * Course reference number
     */
    private int crn;
    /**
     * Credit hours
     */
    private int credits;
    /**
     * Course professor
     */
    private String professor;
    /**
     * Days for the course's first time segment
     */
    private ArrayList<Character> days1;
    /**
     * Days for the course's second time segment (if applicable)
     */
    private ArrayList<Character> days2;
    /**
     * Start time for course's first time segment
     */
    private int start1;
    /**
     * Start time for course's second time segment (if applicable)
     */
    private int start2;
    /**
     * End time for course's first time segment
     */
    private int end1;
    /**
     * End time for course's second time segment (if applicable)
     */
    private int end2;
    /**
     * Room for course's first time segment
     */
    private String room1;
    /**
     * Room for course's second time segment (if applicable)
     */
    private String room2;
    /**
     * These members are exclusively for Search Activity purposes
     */
    private boolean checked = false;
    /**
     * Used to identify when the view's checkbox ix checked
     */
    private long ID = -1;
    /**
     * Unique identifier for modifying lists contaning courses
     */
    private int section;
    /**
     * Course section number e.g. 001
     */
    private int waitlisted;
    /**
     * Number of students waitlisted in a course
     */
    private int seats_avail;
    /**
     * Number of seats available in course
     */
    private int wait_avail;
    /**
     * Number of waitlist spots available
     */
    private String prereq;
    /**
     * Course prerequsities
     */
    private String dates;
    /**
     * Special use for summer bases courses (start and end dates to indicate session)
     */

    private boolean checkboxhide;
    /*
    Used to hide Checkbox for Schedule Item Purposes
    * */


    private int semester;

    /**
     * Empty constructor
     */
    public Course() {
        initValues();
    }

    public Course(Course c) {
        this.course = c.getCourse();
        this.title = c.getTitle();
        this.crn = c.getCrn();
        this.credits = c.getCredits();
        this.professor = c.getProfessor();
        this.days1 = c.getDays1();
        this.days2 = c.getDays2();
        this.start1 = c.getStart1();
        this.end1 = c.getEnd1();
        this.start2 = c.getStart2();
        this.end2 = c.getEnd2();
        this.room1 = c.getRoom1();
        this.room2 = c.getRoom2();
        this.focusReqs = c.getFocusReqs();
        this.prereq = c.getPrereq();
        this.section = c.getSection();
        this.waitlisted = c.getWaitlisted();
        this.seats_avail = c.getSeats_avail();
        this.wait_avail = c.getWait_avail();
        this.dates = c.getDates();
        this.checkboxhide = c.getCheckboxHide();
        this.ID = c.getID();
        this.checked = c.isChecked();
    }

    /**
     * Constructor providing all information about a course except
     * the days and times offered and their room numbers.  Used when
     * finding the days offered will be done elsewhere in code.
     *
     * @param title     Title of the course (i.e. EE324)
     * @param crn       Course reference number
     * @param credits   Course credit hourse
     * @param professor Instructor for the course
     * @param fr        Focus requirements for the course, can be 'null.'
     */
    public Course(String crs, String title, int crn, int credits, String professor,
                  ArrayList<Character> d1, ArrayList<String> fr) {
        initValues();
        this.course = crs;
        this.title = title;
        this.crn = crn;
        this.credits = credits;
        this.professor = professor;
        this.days1 = d1;
        this.focusReqs = fr;
    }

    /**
     * Constructor providing all information for a course that only has one
     * time segment.  For example, if a course is offered MWF from 10:30-11:20,
     * this is considered one time segment.  Use full constructor if two time segments
     * are used.
     *
     * @param crs    Course name (i.e. EE324)
     * @param title     Title of the course (Digital Sys and Computer Design)
     * @param crn       Course reference number
     * @param credits   Course credit hourse
     * @param professor Instructor for the course
     * @param days1     Character array of days the first time segment is offered.
     * @param start1    Start time of the course
     * @param end1      End time of the course
     * @param room1     Room the class is held in
     */
    public Course(String crs, String title, int crn, int credits, String professor,
                  ArrayList<Character> days1, int start1, int end1, String room1, int sec,
                  int seats, int wlist, int wlist_a, String date, String req) {
        initValues();
        this.course = crs;
        this.title = title;
        this.crn = crn;
        this.credits = credits;
        this.professor = professor;
        setDays1(days1);
        this.start1 = start1;
        this.end1 = end1;
        this.room1 = room1;
        this.section = sec;
        this.seats_avail = seats;
        this.waitlisted = wlist;
        this.wait_avail = wlist_a;
        this.prereq = req;
        this.dates = date;
    }

    /**
     * Constructor providing all information for a course that has two
     * time segments.  For example, if a course is offered MWF from 10:30-11:20 and
     * F from 4:30-5:20, this is considered two time segments.
     *
     * @param title     Title of the course (i.e. EE324)
     * @param crn       Course reference number
     * @param credits   Course credit hourse
     * @param professor Instructor for the course
     * @param days1     Character array of days the first time segment is offered.
     * @param days2     Character array of days the second time segment is offered.
     * @param start1    Start time of the first segment of the course
     * @param start2    Start time of the second segment of the course
     * @param end1      End time of the first segment of the course
     * @param end2      End time of the second segment of the course
     * @param room1     Room the first segment of class is held in
     * @param room2     Room the second segment of class is held in
     */
    public Course(String crs, String title, int crn, int credits, String professor,
                  ArrayList<Character> days1, ArrayList<Character> days2, int start1, int start2,
                  int end1, int end2, String room1, String room2, int sec, int seats, int wlist,
                  int wlist_a, String date, String req) {
        initValues();
        this.course = crs;
        this.title = title;
        this.crn = crn;
        this.credits = credits;
        this.professor = professor;
        setDays1(days1);
        setDays2(days2);
        this.start1 = start1;
        this.start2 = start2;
        this.end1 = end1;
        this.end2 = end2;
        this.room1 = room1;
        this.room2 = room2;
        this.section = sec;
        this.seats_avail = seats;
        this.waitlisted = wlist;
        this.wait_avail = wlist_a;
        this.prereq = req;
        this.dates = date;
    }

    /**
     * Initializes all values in a new course to those expected when
     * the actual values have not been set
     */
    private void initValues() {
        this.course = "";
        this.title = "";
        this.crn = 9999;
        this.credits = 9999;
        this.professor = "";
        this.days1 = null;
        this.days2 = null;
        this.start1 = 9999;	/* 9999 used to check for non-existent times */
        this.start2 = 9999;
        this.end1 = 9999;
        this.end2 = 9999;
        this.room1 = "";
        this.room2 = "";
        this.focusReqs = null;
        this.checkboxhide = false;
        this.dates = "";
        this.section = 9999;
        this.wait_avail = 9999;
        this.seats_avail = 9999;
        this.waitlisted = 9999;
        this.prereq = "";
    }

    /**
     * Returns true if the {@link Course} passed as a parameter overlaps
     * the calling course.  The days a course is offered are checked
     * first and if days overlap, then the times are compared.
     *
     * @param    other    The course the calling course is compared with
     * @return boolean    True if they overlap. False if not.
     */
    public boolean overlaps(Course other) {
        ArrayList<Character> odays1 = other.getDays1();
        ArrayList<Character> odays2 = other.getDays2();

		/*  compares class times of one course against
		 * another to see if they overlap
		 */
        for (char d11 : getDays1()) {
            for (char d21 : odays1) {

                if (d11 == d21) {
                    if (timeOverlaps(getStart1(), getEnd1(), other.getStart1(), other.getEnd1()))
                        return true;
                }

            }
            if (odays2 != null) {
                for (char d22 : odays2) {
                    if (d11 == d22) {
                        if (timeOverlaps(getStart1(), getEnd1(), other.getStart2(), other.getEnd2()))
                            return true;
                    }
                }
            }
        }
        if (getDays2() != null) {
            for (char d12 : getDays2()) {
                for (char d21 : odays1) {

                    if (d12 == d21) {
                        if (timeOverlaps(getStart1(), getEnd1(), other.getStart1(), other.getEnd1()))
                            return true;
                    }

                }
                if (odays2 != null) {
                    for (char d22 : odays2) {
                        if (d12 == d22) {
                            if (timeOverlaps(getStart1(), getEnd1(), other.getStart2(), other.getEnd2()))
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if two time segment overlap.
     *
     * @param s1  Start time of first segment
     * @param e1  End time of first segment
     * @param os1 Start time of second segment
     * @param oe1 End time of second segment
     * @return boolean    True if they overlap. False if not.
     */
    public boolean timeOverlaps(int s1, int e1, int os1, int oe1) {
		/* one class starts after the other ends */
        if (e1 < os1 || s1 > oe1)
            return false;

        return true;
    }

    /**
     * Display course info to user.
     */
    public void display() {

        System.out.print(getTitle());
        System.out.print("\t" + getCrn());
        System.out.print("\t" + getCredits());
        System.out.print("\t" + getProfessor() + "\n\t");

        ArrayList<Character> d1 = getDays1();
        for (int i = 0; i < d1.size(); i++) {
            System.out.print(d1.get(i));
        }
        System.out.print("\t");
        System.out.print(getStdTime(getStart1()) + "-" + getStdTime(getEnd1()));
        System.out.println("\t" + getRoom1());

        if (getDays2() != null) {
            System.out.print("\t");
            ArrayList<Character> d2 = getDays2();
            for (int i = 0; i < d2.size(); i++) {
                System.out.print(d2.get(i));
            }
            System.out.print("\t");
            System.out.print(getStdTime(getStart2()) + "-" + getStdTime(getEnd2()));
            System.out.println("\t" + getRoom2());
        }

    }

    /**
     * @param sec is a boolean used to flag when we want to retrieve standard
     *            string form of the start and end time or secondary start and end time
     */

    public String getStdStartTime(boolean sec) {
        String temp;
        if (!sec) {
            temp = getStdTime(getStart1()) + "-" + getStdTime(getEnd1());
        } else {
            temp = getStdTime(getStart2()) + "-" + getStdTime(getEnd2());
        }
        return temp;
    }

    public String getTimeString(boolean sec) {
        if(sec) {
            return (getStdTime(getStart2()) + "-" + getStdTime(getEnd2()));
        } else {
            return (getStdTime(getStart1()) + "-" + getStdTime(getEnd1()));
        }
    }


    /**
     * @param sec is a boolean used to flag when we want to retrieve day string
     *            for the main set of days or the secondary set of days
     */
    public String getDayString(boolean sec) {
        ArrayList<Character> d1;
        if (sec) {
            d1 = getDays2();
        } else {
            d1 = getDays1();
        }
        String temp = "";
        for (int i = 0; i < d1.size(); i++) {
            temp = temp + (d1.get(i));
        }
        return temp;
    }

    /**
     * Added functions get retrieve time strings for results easily
     */
    public String getStartString(boolean sec) {
        if (sec) {
            return getStdTime(getStart2());
        }
        return getStdTime(getStart1());
    }

    public String getEndString(boolean sec) {
        if (sec) {
            return getStdTime(getEnd2());
        }
        return getStdTime(getEnd1());
    }

    private String getStdTime(int t) {
        String theTime = "99:99";
        String temp;
        boolean am = false;

        if (t < 1300) {
            if (t < 1200)
                am = true;
            temp = String.valueOf(t);
        } else
            temp = String.valueOf(t - 1200);

        theTime = temp.substring(0, temp.length() - 2) +
                ":" + temp.substring(temp.length() - 2);
        if (am)
            theTime += "a";
        else
            theTime += "p";
        return theTime;
    }

    /**
     * Verifies that a Course has all the required information filled out.
     * For each course segment that has ANY information entered, ALL of the
     * information for that segment must be filled out.  Any course must have at
     * lease the first segment filled out.
     *
     * @return True if course is valid. False if information is missing
     */
    public boolean isInvalid() {
        if (getTitle() != "" && getCrn() != 9999 && getCredits() != 9999 &&
                getProfessor() != "" && getDays1() != null &&
                getStart1() != 9999 && getEnd1() != 9999 && getRoom1() != "") {
            if (getDays2() == null && getStart2() == 9999 && getEnd2() == 9999 &&
                    getRoom2() == "")
                return false;
            if (getDays2() != null && getStart2() != 9999 && getEnd2() != 9999 &&
                    getRoom2() != "")
                return false;
        }

        return true;

    }

    /*	Setters	*/
    public void setCourse(String crs) {
        this.course = crs;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Set the focus requirements using an existing array.
     * This method WILL overwrite existing focus requirements
     * that have been set either with this method previousely
     * or using the {@link addFocusReq} method.
     *
     * @param f Array of focus requirements.
     */
    public void setFocusReqs(ArrayList<String> f) {
        if (this.focusReqs == null)
            this.focusReqs = new ArrayList<String>();
        else
            this.focusReqs.clear();

        for (String req : f)
            focusReqs.add(req.toUpperCase());
    }

    public String getFocusReqString () {
        if(focusReqs == null) {
            return "N/A";
        } else {
            StringBuilder sb = new StringBuilder();
            for(int x = 0; x < focusReqs.size(); x++) {
                sb.append(focusReqs.get(x));
                if(!(x == focusReqs.size() - 1)) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    /**
     * Sets the start time for the first time segment.
     *
     * @param start Start time
     */
    public void setStart1(int start) {
        this.start1 = start;
    }

    /**
     * Sets the start time for the second time segment.
     *
     * @param start Start time
     */
    public void setStart2(int start) {
        this.start2 = start;
    }

    /**
     * Sets the end time for the first time segment.
     *
     * @param end End time
     */
    public void setEnd1(int end) {
        this.end1 = end;
    }

    /**
     * Sets the end time for the second time segment.
     *
     * @param end End time
     */
    public void setEnd2(int end) {
        this.end2 = end;
    }

    /**
     * Sets start and end times for the first time segment.
     *
     * @param start Start time
     * @param end   End time
     */
    public void setStartEnd1(int start, int end) {
        this.start1 = start;
        this.end1 = end;
    }

    /**
     * Sets start and end times for the second time segment.
     *
     * @param start Start time
     * @param end   End time
     */
    public void setStartEnd2(int start, int end) {
        this.start2 = start;
        this.end2 = end;
    }

    /**
     * Sets the days for the first time segment. Copies the values
     * in the passed array so changes to the passed array don't affect
     * the values in the Course once set.
     *
     * @param days Array of days to be added.
     */
    public void setDays1(ArrayList<Character> days) {
        if (this.days1 == null)
            this.days1 = new ArrayList<Character>();
        else
            this.days1.clear();

        for (int i = 0; i < days.size(); i++)
            this.days1.add(Character.toUpperCase(days.get(i)));
    }

    /**
     * Sets the days for the second time segment. Copies the values
     * in the passed array so changes to the passed array don't affect
     * the values in the Course once set.
     *
     * @param days Array of days to be added.
     */
    public void setDays2(ArrayList<Character> days) {
        if (days == null) {
            this.days2 = null;
            return;
        }

        if (this.days2 == null)
            this.days2 = new ArrayList<Character>();
        else
            this.days2.clear();

        for (int i = 0; i < days.size(); i++)
            this.days2.add(Character.toUpperCase(days.get(i)));
    }

    public void setRoom1(String rm) {
        this.room1 = rm;
    }

    public void setRoom2(String rm) {
        this.room2 = rm;
    }
	
	/*	
	 * modifiers	
	 */

    /**
     * Adds a single focus requirement to the course.
     *
     * @param r Focus Requirement to add.
     */
    public void addFocusReq(String r) {
        if (focusReqs == null)
            focusReqs = new ArrayList<String>();

        r = r.toUpperCase();
        if (!focusReqs.contains(r))
            focusReqs.add(r);
    }

    /**
     * Add a single day (M,T,W,R,F,S) to the list
     * of days the first time segment of a course is offered.
     *
     * @param d Day the course is offered.
     */
    public void addDay1(char d) {
		/* initialize array if null */
        if (days1 == null)
            days1 = new ArrayList<Character>();

        d = Character.toUpperCase(d);
        if (!days1.contains(d))
            days1.add(d);
    }

    /**
     * Remove a single day from the list of days
     * the first time segment of a course is offered.
     *
     * @param d Day to remove.
     */
    public void removeDay1(char d) {
        days1.remove(Character.toUpperCase(d));
		/*  if no more days then reset the times */
        if (days1.isEmpty()) {
            start1 = 9999;
            end1 = 9999;
        }
    }

    /**
     * Add a single day (M,T,W,R,F,S) to the list
     * of days the second time segment of a course is offered.
     *
     * @param d Day the course is offered.
     */
    public void addDay2(char d) {
		/* initialize array if null */
        if (days2 == null)
            days2 = new ArrayList<Character>();

        d = Character.toUpperCase(d);
        if (!days2.contains(d))
            days2.add(d);
    }

    /**
     * Remove a single day from the list of days
     * the second time segment of a course is offered.
     *
     * @param d Day to remove.
     */
    public void removeDay2(char d) {
        days2.remove(Character.toUpperCase(d));
		/*  if no more days then reset the times */
        if (days2.isEmpty()) {
            start2 = 9999;
            end2 = 9999;
        }
    }

    /**
     * Add a single focus requirement to the list.
     *
     * @param r The focus requirement to be added.
     */
    public void addReq(String r) {
        if (this.focusReqs == null)
            this.focusReqs = new ArrayList<String>();

        this.focusReqs.add(r);
    }

    /*	getters */
    public String getProfessor() {
        return professor;
    }

    public String getTitle() {
        return title;
    }

    public int getCrn() {
        return crn;
    }

    public int getCredits() {
        return credits;
    }

    public ArrayList<Character> getDays1() {
        return days1;
    }

    public ArrayList<Character> getDays2() {
        return days2;
    }

    public int getStart1() {
        return start1;
    }

    public int getStart2() {
        return start2;
    }

    public int getEnd1() {
        return end1;
    }

    public int getEnd2() {
        return end2;
    }

    public String getRoom1() {
        return room1;
    }

    public String getRoom2() {
        return room2;
    }

    public ArrayList<String> getFocusReqs() {
        return focusReqs;
    }

    public String getCourse() {
        return course;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean val) {
        checked = val;
    }

    public void setID(long pid) {
        ID = pid;
    }

    public long getID() {
        return ID;
    }

    public void setDates(String date) {
        dates = date;
    }

    public void setPrereq(String req) {
        prereq = req;
    }

    public void setWait_avail(int w) {
        wait_avail = w;
    }

    public void setSeats_avail(int a) {
        seats_avail = a;
    }

    public void setWaitlisted(int w) {
        waitlisted = w;
    }

    public String getPrereq() {
        return prereq;
    }

    public String getDates() {
        return dates;
    }

    public int getWaitlisted() {
        return waitlisted;
    }

    public int getSeats_avail() {
        return seats_avail;
    }

    public int getWait_avail() {
        return wait_avail;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int sec) {
        section = sec;
    }

    public void setCheckboxHide(boolean val) {
        checkboxhide = val;
    }

    public boolean getCheckboxHide() {
        return checkboxhide;
    }

    public boolean hasLab() {
        return !(this.getStart2() == 9999);
    }

    public void setSemester(int sem) {
        semester = sem;
    }

    public int getSemester() {return semester;}

}
