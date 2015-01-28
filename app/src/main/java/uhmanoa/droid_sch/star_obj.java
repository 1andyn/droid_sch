package uhmanoa.droid_sch;

/**
 * Created by supah_000 on 1/27/2015.
 */
public class star_obj {
    private String course_name;
    private int CRN;

    //Constructor
    private star_obj(String pCrsname, int pCRN) {
        if(pCRN == NULL) {
            CRN = -1;
        } else {
            CRN = pCRN;
        }
        course_name = pCrsname;
    }

    public int getCRN() {
        return CRN;
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


}
