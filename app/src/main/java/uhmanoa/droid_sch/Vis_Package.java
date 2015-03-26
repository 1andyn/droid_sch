package uhmanoa.droid_sch;

import java.util.ArrayList;

/**
 * Created by supah_000 on 3/5/2015.
 */
public class Vis_Package {
    ArrayList<Course> matches;
    ArrayList<Boolean> sec_courses;
    int vcase;

    public Vis_Package(ArrayList<Course> mtc, ArrayList<Boolean> sec, int vase) {
        vcase = vase;
        matches = mtc;
        sec_courses = sec;
    }

    public ArrayList<Course> getMatches() {
        return matches;
    }

    public ArrayList<Boolean> getSec() {
        return sec_courses;
    }

    public int getCase() {
        return vcase;
    }

}
