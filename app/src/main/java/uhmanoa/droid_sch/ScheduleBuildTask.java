package uhmanoa.droid_sch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class ScheduleBuildTask extends AsyncTask<Void, Void, Integer> {

    private OnBuildTaskComplete listener;
    private ProgressDialog pdialog;
    private Context app_context;
    private ArrayList<Schedule> results;
    private ArrayList<Star_obj> inputs;
    private SQL_DataSource data;
    private int year;
    private int semester;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog = new ProgressDialog(app_context);
        pdialog.setCancelable(false);
        pdialog.setMessage("Building schedules...");
        pdialog.show();
    }

    //constructor, doesnt need to do anything for now
    public ScheduleBuildTask(Context c, SQL_DataSource ds, OnBuildTaskComplete listener,
                             int sem, int yr, ArrayList<Star_obj> so) {
        this.listener = listener;
        data = ds;
        year = yr;
        semester = sem;
        app_context = c;
        inputs = so;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        ClassScheduler cs = new ClassScheduler(semester, year, data);

        return 1;
    }

    public ArrayList<Schedule> getResults() {
        return results;
    }

    @Override
    protected void onPostExecute(Integer result) {
        pdialog.dismiss();
        listener.onBuildTaskComplete();
    }

}

