package uhmanoa.droid_sch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Parser extends AsyncTask<Integer, Integer, Integer> {

    private Context app_context;
    private OnParseTaskComplete listener;
    private ArrayList<String> mjr_list;
    private ArrayList<String> full_mjr_list;
    private ArrayList<String> course_urls;
    private SQL_DataSource datasource;
    private ProgressDialog pdialog;
    private IOException ex;
    private final String WEB_URL = "https://www.sis.hawaii.edu/uhdad/avail.classes?i=MAN&t=";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog = new ProgressDialog(app_context);
        pdialog.setCancelable(false);
        pdialog.setMessage("Initializing data parser...");
        pdialog.show();
    }

    //constructor, doesnt need to do anything for now
    public Parser(SQL_DataSource ds, Context c, OnParseTaskComplete listener) {
        this.listener = listener;
        mjr_list = new ArrayList<>();
        full_mjr_list = new ArrayList<>();
        course_urls = new ArrayList<>();
        datasource = ds;
        app_context = c;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        //0 = SEMESTER, 1 = YEAR, 2 = MONTH

        int year = params[1];
        int year2 = year;
        if (params[2] >= Calendar.SEPTEMBER) {
            //if month is september or later
            year2 = year2 + 1;
        }

        int use_yr;
        switch (params[0]) {
            case 0: //fall
                use_yr = year;
                break;
            case 1:
            case 2:
                use_yr = year2;
                break;
            default:
                use_yr = year;
                break;
        }

        datasource.clearCourseData(params[0], use_yr);

        String webURL = WEB_URL + calculateURLField(use_yr, params[0]);

        publishProgress(0);
        parseMajorData(webURL, params[0], use_yr);

        publishProgress(1);
        parseCourseData(params[0], use_yr);
        //Parse Course Data
        return 1;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        switch(progress[0]) {
            case 0: pdialog.setMessage("Retrieving Major List..."); break;
            case 1: pdialog.setMessage("Retrieving Course data..."); break;
            case 2: pdialog.setMessage("Retrieving Course data (" + String.valueOf(progress[1]+1)
                    + "/" + course_urls.size() + ")");
        }
    }

    public ArrayList<String> getMajors() {
        return mjr_list;
    }

    public ArrayList<String> getFull_mjr_list() {
        return full_mjr_list;
    }

    private void parseMajorData(String webURL, int sem, int year) {
        try {
            System.setProperty("https.protocols", "TLSv1,SSLv3,SSLv2Hello");
            Document startDoc = Jsoup.connect(
                    webURL).timeout(0).userAgent("Mozilla").get();
            Elements subjectLinks = startDoc.select("ul.subjects").select("a[href]");
            for (Element subjectLink : subjectLinks) {
                String subjectURL = subjectLink.attr("abs:href");
                String fullmjr = subjectLink.text();
                String mjr = subjectURL.substring(subjectURL.indexOf("&s=") + 3,
                        subjectURL.length());
                mjr_list.add(mjr);
                course_urls.add(subjectURL);
                full_mjr_list.add(fullmjr);
                datasource.saveMajor(fullmjr, mjr, sem, year);
            }
        } catch (IOException e) {
            ex = e;
        }

    }

    private void parseCourseData(int sem, int year) {
        for (int x = 0; x < course_urls.size(); x++) {
            publishProgress(2, x);
            try {
                String web_url = course_urls.get(x);
                Document doc = Jsoup.connect(web_url).timeout(10000).get();
                String mjr = web_url.substring(web_url.indexOf("&s=") + 3, web_url.length());
                String crn = "9999";
                String credits = "9999";
                String section = "9999";
                String seats = "9999";
                String wlisted = "9999";
                String wlava = "9999";
                String className = "NULL";
                String crsTitle = "NULL";
                String teacher = "NULL";
                String days = "NULL";
                String days2 = "NULL";
                String time = "NULL";
                String time2 = "NULL";
                String room = "NULL";
                String room2 = "NULL";
                String dates = "NULL";

                int count = 0;
                int max = doc.select("tr").size();
                boolean exists = false;

                for (Element row : doc.select("tr")) { //iterates
                    count++;

                    if (count < max) {
                        Element nextRow = doc.select("tr").get(count);
                        if (nextRow.select("td").size() > 1) {
                            if (isInteger(nextRow.select("td").get(1).text()) == true
                                    || nextRow.select("td").size() < 10
                                    || nextRow.select("td").get(0).text().charAt(0)
                                    != 0xA0) {
                                exists = true;
                            } else {
                                exists = false;
                            }
                        }
                    }

                    Elements cells = row.select("td");
                    List<String> focusArray = new ArrayList<>();

                    if (!cells.isEmpty()) {
                        if (cells.size() > 10) { //Detects if the Cell is Empty
                            // There are two cases; Case #1 size 12, Case #2 size 14
                            if (isInteger(cells.get(1).text()) == true) {
                                //Checks if First Cell is Integer
                                if (!(cells.get(0).text().charAt(0) == 0xA0)) {
                                    // Checks if Character is new line
                                    String temp = cells.get(0).text();
                                    focusArray = Arrays.asList(temp.split("\\s*,\\s*"));
                                }

                                //first 8 columns are always the same; 0-8
                                crn = cells.get(1).text();
                                className = cells.get(2).text();
                                section = cells.get(3).text();
                                crsTitle = cells.get(4).text();
                                //Needs to be formatted for special cells
                                int endIndex = crsTitle.indexOf("<br>");
                                if (endIndex != -1) {
                                    //<br> tag found
                                    crsTitle = crsTitle.substring(0, endIndex);
                                }

                                endIndex = crsTitle.indexOf("Restriction:");
                                if (endIndex != -1) {
                                    //<br> tag found
                                    crsTitle = crsTitle.substring(0, endIndex);
                                }

                                crsTitle.trim(); //trim extra spacing if any

                                credits = cells.get(5).text();

                                // gets full name
                                Elements abbrs = cells.get(6).select("abbr[title]");
                                if (!abbrs.isEmpty()) teacher = abbrs.get(0).attr("title");

                                // Upcoming Semester
                                if (cells.size() == 14) {
                                    //seat data is available
                                    seats = cells.get(7).text();
                                    wlisted = cells.get(8).text();
                                    wlava = cells.get(9).text();
                                    days = cells.get(10).text();
                                    time = cells.get(11).text();
                                    room = cells.get(12).text();
                                    dates = cells.get(13).text();
                                } else if (cells.size() == 12) {
                                    //only seat available
                                    seats = cells.get(7).text();
                                    days = cells.get(8).text();
                                    time = cells.get(9).text();
                                    room = cells.get(10).text();
                                    dates = cells.get(11).text();
                                } else {
                                    System.out.println("ERROR, UNIQUE CASE DETECTED!");
                                    System.out.println("CHECK COURSES FOR: " + className);
                                }

                            } else {
                                if (!(cells.get(1).text().charAt(0) == 0xA0)) break;
                            }

                            boolean multi = false;
                            if (cells.get(1).text().charAt(0) == 0xA0) {
                                multi = true;
                                days2 = cells.get(7).text();
                                time2 = cells.get(8).text();
                                room2 = cells.get(9).text();
                            }

                            int start1 = 9999, start2 = 9999, end1 = 9999,
                                    end2 = 9999;

                            if (!time.equalsIgnoreCase("TBA")
                                    && !time.equals("NULL")) {

                                int times[] = getStartEndTime(time);
                                start1 = times[0];
                                end1 = times[1];
                            } else if (time.equals("TBA")){
                                start1 = -1;
                                end1 = -1;
                            }

                            if (!time2.equalsIgnoreCase("TBA")
                                    && !time2.equals("NULL")) {
                                int times[] = getStartEndTime(time2);
                                start2 = times[0];
                                end2 = times[1];
                            } else if (time2.equals("TBA")){
                                start2 = -1;
                                end2 = -1;
                            }

                            int sts, wt, wla;
                            if (seats.equals("9999")) {
                                sts = 0;
                            } else {
                                sts = Integer.valueOf(seats);
                            }

                            if (wlisted.equals("9999")) {
                                wt = 0;
                            } else {
                                wt = Integer.valueOf(wlisted);
                            }

                            if (wlava.equals("9999")) {
                                wla = 0;
                            } else {
                                wla = Integer.valueOf(wlava);
                            }

                            if (exists) {
                                if (!multi) {
                                    ArrayList<Character> d = new ArrayList<>();
                                    for (int i = 0; i < days.length(); i++) {
                                        d.add(days.charAt(i));
                                    }

                                    Course c = new Course(
                                            className,
                                            crsTitle,
                                            Integer.valueOf(crn),
                                            credits,
                                            teacher,
                                            d,
                                            start1,
                                            end1,
                                            room,
                                            Integer.valueOf(section),
                                            sts,
                                            wt,
                                            wla,
                                            dates,
                                            ""
                                    );
                                    c.setMajor(mjr);
                                    c.setYear(year);
                                    c.setSemester(sem);
                                    c.setFocusReqs(new ArrayList(focusArray));
                                    datasource.saveCourse(c);
                                } else {
                                    ArrayList<Character> d = new ArrayList<>();
                                    for (int i = 0; i < days.length(); i++) {
                                        d.add(days.charAt(i));
                                    }

                                    ArrayList<Character> d2 = new ArrayList<>();
                                    for (int i = 0; i < days2.length(); i++) {
                                        d2.add(days2.charAt(i));
                                    }

                                    Course c = new Course(
                                            className,
                                            crsTitle,
                                            Integer.valueOf(crn),
                                            credits,
                                            teacher,
                                            d,
                                            d2,
                                            start1,
                                            start2,
                                            end1,
                                            end2,
                                            room,
                                            room2,
                                            Integer.valueOf(section),
                                            sts,
                                            wt,
                                            wla,
                                            dates,
                                            ""
                                    );
                                    c.setMajor(mjr);
                                    c.setYear(year);
                                    c.setSemester(sem);
                                    c.setFocusReqs(new ArrayList(focusArray));
                                    datasource.saveCourse(c);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                ex = e;
            }
        }
    }

    public static int[] getStartEndTime(String t) {
        int[] time = new int[2];
        boolean am = true;
        String start = t.substring(0, t.indexOf("-"));
        String end = t.substring(t.indexOf("-") + 1, t.length());

        // determining if times are AM or PM
        if (end.indexOf('a') == -1)
            am = false;

        int s = Integer.parseInt(start.substring(0, start.length()));
        int e = Integer.parseInt(end.substring(0, end.length() - 1));

        // if PM => add 1200 to time to put it into military time
        if (!am) {
            if (s > e) {
                e += 1200;
            } else if (e < 1200) {
                e += 1200;
                s += 1200;
            }

        }

        if (s > e) {
            time[0] = -1;
            time[1] = -2;
            return time;
        }

        time[0] = s;
        time[1] = e;

        return time;
    }


    /**
     * Method used to see if row starts with a CRN Number
     *
     * @param str String to be checked if is also an integer.
     * @return True if string is also an integer. False if not.
     */

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onPostExecute(Integer result) {
        pdialog.dismiss();
        listener.onParseTaskComplete(ex);
    }

    public int calculateURLyear(int year, int sem) {
        int yr = year;
        if (sem == 0) {
            yr = yr + 1; //fall URL offset
        }
        return yr;
    }

    public int getURLDigit(int sem) {
        int x = 10; //default assume fall
        switch (sem) {
            case 0:
                return x; //fall
            case 1:
                return 30; //spring
            case 2:
                return 40; //summer
            default:
                return -1; //something went wrong
        }
    }

    private String calculateURLField(int year, int sem) {
        String yr = String.valueOf(calculateURLyear(year, sem));
        String dig = String.valueOf(getURLDigit(sem));
        return yr + dig;
    }

}
