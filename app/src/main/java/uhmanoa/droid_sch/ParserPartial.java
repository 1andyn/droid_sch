package uhmanoa.droid_sch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParserPartial extends AsyncTask<Void, Integer, Integer> {

    private CountDownLatch cdl;
    private Context app_context;
    private OnParseTaskComplete listener;
    private ArrayList<String> course_urls;
    private SQL_DataSource datasource;
    private ProgressDialog pdialog;
    private ArrayList<ScheduleParsePackage> spp;
    private IOException ex;
    private Boolean task_cancelled = false;

    //this is the main URL for retrieving course data
    private final String WEB_URL = "https://www.sis.hawaii.edu/uhdad/avail.classes?i=MAN&t=";
    private final String MJR_FIELD = "&s=";


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog = new ProgressDialog(app_context);
        pdialog.setCancelable(false);
        pdialog.setMessage("Attempting to update Seating Data...");
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
    public ParserPartial(SQL_DataSource ds, Context c, OnParseTaskComplete listener,
                         ArrayList<ScheduleParsePackage> spp_data) {
        this.listener = listener;
        course_urls = new ArrayList<>();
        datasource = ds;
        app_context = c;
        spp = spp_data;
    }

    public boolean getTaskCancelled() {
        return task_cancelled;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        ArrayList<Integer> sem_data = new ArrayList<>();
        ArrayList<Integer> yr_data = new ArrayList<>();

        publishProgress(0);
        if(task_cancelled) return -1;
        for(ScheduleParsePackage s : spp) {
            ArrayList<String> mjrs = s.getMajors();
            for(String m : mjrs) {
                String url = produceURL(s.getSemester(), s.getYear(), m);
                if(!course_urls.contains(url)){
                    course_urls.add(url);
                    sem_data.add(s.getSemester());
                    yr_data.add(s.getYear());
                }
            }
        }

        publishProgress(1);
        if(task_cancelled) return -1;

        ExecutorService es = parseCourseData(sem_data, yr_data);
        while (!es.isTerminated()) {
            try {
                if(task_cancelled) {
                    es.shutdownNow();
                    return -1;
                }
                publishProgress(2);
                Thread.sleep(100);                 //sleep for one second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        switch (progress[0]) {
            case 0:
                pdialog.setMessage("Preparing to retrieve data...");
                break;
            case 1:
                pdialog.setMessage("Retrieving Course data...");
                break;
            case 2:
                pdialog.setMessage("Downloading Course data (" + String.valueOf(course_urls.size() -
                cdl.getCount()) + "/" + String.valueOf(course_urls.size()) + ")");
                break;
        }
    }


    private ExecutorService parseCourseData(ArrayList<Integer> sem, ArrayList<Integer> yr) {
        cdl = new CountDownLatch(course_urls.size());
        ExecutorService es = Executors.newFixedThreadPool(5); //five threads limit
        for (int x = 0; x < course_urls.size(); x++) {
            es.submit(new ParserThread(datasource, course_urls.get(x), sem.get(x), yr.get(x), cdl));
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

    private String produceURL(int sem, int year, String mjr) {
        String temp = WEB_URL + calculateURLField(year, sem) + MJR_FIELD + mjr;
        return temp;
    }

}
