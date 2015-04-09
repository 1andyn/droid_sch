package uhmanoa.droid_sch;

/**
 * Created by supah_000 on 1/27/2015.
 */
public class Star_obj {
    private String course_name;
    private String course_title;
    private int CRN;
    private long ID; // Incase user for some reason stars both a course and class
    private int Semester;
    private int Year = 2015;
    private boolean checked = false;

    //Constructor
    public Star_obj(String pCrsname, String pCrsTitle, int pCRN, long pID, int pSem, int yr) {
        ID = pID;
        CRN = pCRN;
        Semester = pSem;
        course_name = pCrsname;
        course_title = pCrsTitle;
        Year = yr;
    }

    public void setID(long pid) {
        ID = pid;
    }

    public int getYear() {
        return Year;
    }

    public int getCRN() {
        return CRN;
    }

    public long getID() {
        return ID;
    }

    public int getSemester() {
        return Semester;
    }

    public String getCourseTitle() {
        return course_title;
    }

    public String getCourse() {
        return course_name;
    }

    /* Class refers to a course with a specific CRN,
    *  Course refers to a course such as CHEM 161, where there maybe multiple lectures
    *  with different CRN's and Times */
    public boolean isClass() {
        return (CRN != -1);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean val) {
        checked = val;
    }

}
