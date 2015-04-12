package uhmanoa.droid_sch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Parser extends AsyncTask<Integer, Integer, Integer> {

    private CountDownLatch cdl;
    private Context app_context;
    private OnParseTaskComplete listener;
    private ArrayList<String> mjr_list;
    private ArrayList<String> full_mjr_list;
    private ArrayList<String> course_urls;
    private SQL_DataSource datasource;
    private ProgressDialog pdialog;
    private int prog = 0;
    private IOException ex;
    private Boolean task_cancelled = false;

    //this is the main URL for retrieving course data
    private final String WEB_URL = "https://www.sis.hawaii.edu/uhdad/avail.classes?i=MAN&t=";


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog = new ProgressDialog(app_context);
        pdialog.setCancelable(false);
        pdialog.setMessage("Initializing data parser...");
        pdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task_cancelled = true;
                dialog.dismiss();
            }
        });
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

    public boolean getTaskCancelled() {
        return task_cancelled;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        //DEBUG DELETE LATER
        long startTime = System.currentTimeMillis();
        //DEBUG DELETE LATER


        //0 = SEMESTER, 1 = YEAR, 2 = MONTH

        if(task_cancelled) return -1;

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

        if(task_cancelled) return -1;

        datasource.clearCourseData(params[0], use_yr);

        String webURL = WEB_URL + calculateURLField(use_yr, params[0]);

        publishProgress(0);
        parseMajorData(webURL, params[0], use_yr);

        publishProgress(1);

        if(task_cancelled) return -1;

        //Parse Course Data

        int wait_req = course_urls.size();
        ExecutorService es = parseCourseData(params[0], use_yr);
        while (!es.isTerminated()) {
            try {
                if(task_cancelled) {
                    es.shutdownNow();
                    return -1;
                }
                publishProgress(2);
                Thread.sleep(1000);                 //sleep for one second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //DEBUG DELETE LATER
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("RUNTIME: " + elapsedTime);
        //DEBUG DELETE LATER

        return 1;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        switch (progress[0]) {
            case 0:
                pdialog.setMessage("Retrieving Major List...");
                break;
            case 1:
                pdialog.setMessage("Retrieving Course data...");
                break;
            case 2:
                prog++;
                pdialog.setMessage("Downloading Course data (" + String.valueOf(course_urls.size() -
                cdl.getCount()) + "/" + String.valueOf(course_urls.size()) + ")");
                break;
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

    private ExecutorService parseCourseData(int sem, int year) {
        // int thread_lim = Runtime.getRuntime().availableProcessors();
        //System.out.println("POOL SIZE:" + thread_lim);
        cdl = new CountDownLatch(course_urls.size());
        ExecutorService es = Executors.newFixedThreadPool(5); //five threads limit
        for (int x = 0; x < course_urls.size(); x++) {
            es.submit(new ParserThread(datasource, course_urls.get(x), sem, year, cdl));
        }
        es.shutdown();
        return es;
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
