package uhmanoa.droid_sch;

/**
 * Created by supah_000 on 2/24/2015.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SchHashMap {

    private ArrayList<Schedule> sch_data;
    private final String dark_aqua = "#52CCA3";


    public SchHashMap (ArrayList<Schedule> sch) {
        sch_data = sch;
    }

    public HashMap getData() {
        HashMap SchedHashMap = new HashMap();
        for(int x = 0; x < sch_data.size(); x++) {
            Schedule temp = sch_data.get(x);
            List crs_data = new ArrayList();

            //Add Expandable List Data
            ArrayList<Course> courses = temp.getCourses();
            for(int y = 0; y < courses.size(); y++) {
                Course crt = courses.get(y);
                StringBuilder sb = new StringBuilder();
                sb.append(getAquaText(crt.getCourse()) + getSpace() + crt.getTitle() + getNewLine()

                        + "Instr: " + crt.getProfessor() + getSpace() + "CRN: " + crt.getCrn() +
                        getSpace() + "Sec: " + crt.getSection() + getSpace() + "Crd: " +
                        crt.getCredits() + getNewLine() +

                        "Seats: " + crt.getSeats_avail() + getSpace() + "Waitlisted: " +
                        crt.getWaitlisted() + getSpace() + "Waitlist Avail: " +
                        crt.getWait_avail() + getNewLine()

                        + getAquaText(crt.getDayString(false) + getSpace())
                         + crt.getTimeString(false) + getSpace() +
                        getAquaText(crt.getRoom1()) + getNewLine());
                if(crt.hasLab()) {
                    sb.append(getAquaText(crt.getDayString(true) + getSpace())
                            + crt.getTimeString(true) + getSpace() +
                            getAquaText(crt.getRoom2()) + getNewLine());
                }
                sb.append("Dates: " + crt.getDates() + getNewLine() + "Prereqs: " +
                        crt.getPrereq() + getNewLine() + "Focus/GE: " + crt.getFocusReqString());
                crs_data.add(getBoldText(sb.toString()));
            }
            SchedHashMap.put(getSchedGroup(temp), crs_data);
        }

        return SchedHashMap;
    }

    private String getAquaText(String input) {
        StringBuilder color = new StringBuilder();
        //Appends Code for Color
        color.append("<font color='"); //start font tag
        color.append(dark_aqua);
        color.append("'>");
        color.append(input);
        color.append("</font>"); //end font tag
        return color.toString();
    }

    private String getBoldText (String input) {
        StringBuilder text = new StringBuilder();
        text.append("<b>"); //Start bold tag
        text.append(input);
        text.append("</b>"); //End bold tag
        return text.toString();
    }

    private String preserveSpace(String input) {
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>");
        sb.append(input);
        sb.append("</pre>");
        return sb.toString();
    }

    private String getSpace() {
        return " ";
    }

    private String getNewLine() {
        return "<br>";
    }

    private String getSchedGroup (Schedule sch) {
        ArrayList<Course> crses = sch.getCourses();
        String temp = "";
        for (int x = 0; x < crses.size(); x++) {
            Course crt = crses.get(x);
            temp = temp + crt.getCourse() + getSpace() + getAquaText(crt.getDayString(false)) +
                    getSpace() + crt.getStdStartTime(false) + getSpace() +
                    getAquaText(crt.getRoom1());
            if(crt.hasLab()) {
                temp = temp + getNewLine() + crt.getCourse() + getSpace() +
                        getAquaText(crt.getDayString(true)) +
                        getSpace() + crt.getStdStartTime(true) + getSpace() +
                        getAquaText(crt.getRoom2());
            }

            if(!(x == (crses.size() - 1))) {
                // This is not the last course
                temp = temp + getNewLine();
            }
        }
        return temp;
    }


}