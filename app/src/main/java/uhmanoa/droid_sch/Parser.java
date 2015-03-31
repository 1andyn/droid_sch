package uhmanoa.droid_sch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Parser extends AsyncTask<Integer, Void, Integer> {

    private Context app_context;
    private OnParseTaskComplete listener;
    private ProgressDialog pdialog;
    private ArrayList<String> mjr_list;
    private ArrayList<String> full_mjr_list;
    private SQL_DataSource datasource;
    private IOException ex;
    private final String WEB_URL = "https://www.sis.hawaii.edu/uhdad/avail.classes?i=MAN&t=";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog.setMessage("Initializing data parser...");
        pdialog.show();
    }

    //constructor, doesnt need to do anything for now
    public Parser(ProgressDialog pg, SQL_DataSource ds, Context c, OnParseTaskComplete listener) {
        this.listener = listener;
        mjr_list = new ArrayList<>();
        full_mjr_list = new ArrayList<>();
        pdialog = pg;
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
        switch(params[0]) {
            case 0: //fall
                use_yr = year; break;
            case 1:
            case 2:
                use_yr = year2; break;
            default:
                use_yr = year; break;
        }

        String webURL = WEB_URL + calculateURLField(use_yr, params[0]);
        pdialog.setMessage("Retrieving Major List");
        parseData(webURL, params[0], params[1]);

        //Parse Course Data
        return 1;
    }

    public ArrayList<String> getMajors() {
        return mjr_list;
    }

    public ArrayList<String> getFull_mjr_list() {
        return full_mjr_list;
    }

    private void parseData(String webURL, int sem, int year) {
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
                full_mjr_list.add(fullmjr);
                datasource.saveMajor(fullmjr, mjr, sem, year);
            }
        } catch (IOException e) {
            Toast.makeText(app_context, "Unable to retrieve course data, try again later.",
                    Toast.LENGTH_SHORT).show();
            ex = e;
        }

    }

    @Override
    protected void onPostExecute(Integer result) {
        pdialog.dismiss();
        listener.onParseTaskComplete(ex);
    }

    public int calculateURLyear(int year, int sem) {
        int yr = year;
        if(sem == 0) {
            yr = yr + 1; //fall URL offset
        }
        return yr;
    }

    public int getURLDigit(int sem) {
        int x = 10; //default assume fall
        switch(sem) {
            case 0: return x; //fall
            case 1: return 30; //spring
            case 2: return 40; //summer
            default:
                return -1; //something went wrong
        }
    }

    private String calculateURLField(int year, int sem) {
        String yr = String.valueOf(calculateURLyear(year, sem));
        String dig = String.valueOf(getURLDigit(sem));
        return yr+dig;
    }

}
