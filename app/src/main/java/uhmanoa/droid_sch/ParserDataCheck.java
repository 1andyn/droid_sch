package uhmanoa.droid_sch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ParserDataCheck extends AsyncTask<Integer, Void, Integer> {

    private OnCheckTaskComplete listener;
    private ProgressDialog pdialog;
    private ArrayList<Boolean> status; //false = no data, true = data
    final String no_data = "Class availability information for this term is not available " +
            "at this time.";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog.show();
    }

    //constructor, doesnt need to do anything for now
    public ParserDataCheck(ProgressDialog pg, OnCheckTaskComplete listener) {
        this.listener = listener;
        status = new ArrayList<>();
        pdialog = pg;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        status.add(yearDataReadable(params[0], params[1]));
        status.add(yearDataReadable(params[2], params[3]));
        status.add(yearDataReadable(params[4], params[5]));
        return 1;
    }

    public ArrayList<Boolean> getDataStatus() {
        return status;
    }

    @Override
    protected void onPostExecute(Integer result) {
        pdialog.dismiss();
        listener.onCheckTaskComplete();
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

    private Boolean yearDataReadable(int year, int sem) {
        boolean found = false;
        try {
            System.setProperty("https.protocols", "TLSv1,SSLv3,SSLv2Hello");
            Document doc = Jsoup.connect("http://www.sis.hawaii.edu/uhdad/avail.classes?i=MAN&t=" +
            calculateURLField(year, sem)).get();
            Element span_data = doc.select("TD.indefault").first();
            if(span_data == null) {
                found = false;
            } else {
                if(span_data.toString().contains(no_data)) {
                    found = true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(found) {
            return false;
        } else {
          return true;
        }
    }

}
