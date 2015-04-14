package uhmanoa.droid_sch;

import java.util.ArrayList;

/**
 * Created by supah_000 on 4/13/2015.
 */
public class ScheduleParsePackage {

    private int semester;
    private int year;
    private ArrayList<String> majors;

    public ScheduleParsePackage (int sem, int yr, ArrayList<String> mjr)
    {
        majors = mjr;
        semester=  sem;
        year = yr;
    }

    public int getSemester() {
        return semester;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<String> getMajors() {
        return majors;
    }

}
