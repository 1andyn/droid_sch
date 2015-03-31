package uhmanoa.droid_sch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class SearchTask extends AsyncTask<String, Void, Integer> {

    private OnSearchTaskComplete listener;
    private ProgressDialog pdialog;
    private Context app_context;
    private ArrayList<Course> courses; //false = no data, true = data
    private SQL_DataSource data;
    private int yr;
    private int semester;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog = new ProgressDialog(app_context);
        pdialog.setCancelable(false);
        pdialog.setMessage("Searching...");
        pdialog.show();
    }

    //constructor, doesnt need to do anything for now
    public SearchTask(Context c, SQL_DataSource ds, OnSearchTaskComplete listener,
                      int sem, int year) {
        this.listener = listener;
        courses = new ArrayList<>();
        data = ds;
        yr = year;
        semester = sem;
        app_context = c;
    }

    @Override
    protected Integer doInBackground(String... params) {
        courses = data.getSearchResults(semester, yr, params[0], params[1]);
        return 1;
    }

    public ArrayList<Course> getResults() {
        return courses;
    }

    @Override
    protected void onPostExecute(Integer result) {
        pdialog.dismiss();
        listener.onSearchTaskComplete();
    }

}

