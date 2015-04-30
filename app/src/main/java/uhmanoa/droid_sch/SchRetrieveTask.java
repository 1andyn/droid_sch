package uhmanoa.droid_sch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class SchRetrieveTask extends AsyncTask<String, Void, Integer> {

    private OnRetrieveTaskComplete listener;
    private ProgressDialog pdialog;
    private Context app_context;
    private ArrayList<Schedule> sch = null;
    private SQL_DataSource data;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog = new ProgressDialog(app_context);
        pdialog.setCancelable(false);
        pdialog.setMessage("Loading schedules, please wait...");
        pdialog.show();
    }

    //constructor, doesnt need to do anything for now
    public SchRetrieveTask(Context c, SQL_DataSource ds, OnRetrieveTaskComplete listener) {
        this.listener = listener;
        data = ds;
        app_context = c;
    }

    @Override
    protected Integer doInBackground(String... params) {
        sch = data.getAllSchedules();
        return 1;
    }

    public ArrayList<Schedule> getSchedules() {
        return sch;
    }

    @Override
    protected void onPostExecute(Integer result) {
        pdialog.dismiss();
        listener.onRetrieveTaskComplete();
    }
}

