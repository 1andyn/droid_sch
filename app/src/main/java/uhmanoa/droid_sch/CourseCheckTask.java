package uhmanoa.droid_sch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class CourseCheckTask extends AsyncTask<String, Void, Integer> {

    private OnCheckTaskComplete listener;
    private ProgressDialog pdialog;
    private Context app_context;
    private Star_obj so = null; //false = no data, true = data
    private SQL_DataSource data;
    private int yr;
    private int semester;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog = new ProgressDialog(app_context);
        pdialog.setCancelable(false);
        pdialog.setMessage("Finding matching course...");
        pdialog.show();
    }

    //constructor, doesnt need to do anything for now
    public CourseCheckTask(Context c, SQL_DataSource ds, OnCheckTaskComplete listener,
                           int sem, int year) {
        this.listener = listener;
        data = ds;
        yr = year;
        semester = sem;
        app_context = c;
    }

    @Override
    protected Integer doInBackground(String... params) {
        so = data.findMatch(semester, yr, params[0]);
        return 1;
    }

    public Star_obj getMatch() {
        return so;
    }

    @Override
    protected void onPostExecute(Integer result) {
        pdialog.dismiss();
        listener.onCheckTaskComplete();
    }

}

