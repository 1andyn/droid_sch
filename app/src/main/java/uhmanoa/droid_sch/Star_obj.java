package uhmanoa.droid_sch;

/**
 * Created by supah_000 on 1/27/2015.
 */
public class Star_obj {
    private String course_name;
    private String course_title;
    private int CRN;
    private int ID; // Incase user for some reason stars both a course and class
    private int Semester;
    private long index_id; //Used for List Adapter to Identify specific item in container

    //Constructor
    public Star_obj(String pCrsname, String pCrsTitle, int pCRN, int pID, int pSem, long pIid) {
        pID = ID;
        CRN = pCRN;
        Semester = pSem;
        index_id = pIid;
        course_name = pCrsname;
        course_title = pCrsTitle;

    }

    public int getCRN() {
        return CRN;
    }

    public int getID() {
        return ID;
    }

    public int getSemester() {
        return Semester;
    }

    public long getIndex_id() {
        return index_id;
    }

    public String getCourseTitle() { return course_title; }

    public String getCourse() {
        return course_name;
    }

    /* Class refers to a course with a specific CRN,
    *  Course refers to a course such as CHEM 161, where there maybe multiple lectures
    *  with different CRN's and Times */
    public boolean isClass() {
        return (CRN != -1);
    }

}
