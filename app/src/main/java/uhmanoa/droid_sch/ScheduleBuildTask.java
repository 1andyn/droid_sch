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
    private int min;
    private Course timeblock;

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
                             int sem, int yr, ArrayList<Star_obj> so, int mini, Course tb) {
        this.listener = listener;
        data = ds;
        year = yr;
        semester = sem;
        app_context = c;
        inputs = so;
        min = mini;
        timeblock = tb;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        ArrayList<Star_obj> filtered_list = filterCourses(inputs);
        ClassScheduler cs = new ClassScheduler(semester, year, data, min,
                getCrnExclusive(filtered_list), timeblock);
        results = cs.getPossibleSchedules(filtered_list);
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

    private ArrayList<Star_obj> getCrnExclusive(ArrayList<Star_obj> ex) {
        ArrayList<Star_obj> so = new ArrayList<>();
        for(Star_obj s : ex) {
            if(s.getCRN() != -1) {
                so.add(s);
            }
        }
        return so;
    }

    private ArrayList<Star_obj> filterCourses(ArrayList<Star_obj> list) {
        // ----------------------- REDUNDANCY CHECK ---------------------------------
        ArrayList<String> courses = new ArrayList<>(); //Contains List of "Named" of Courses
        //Obtain List of Courses listed by Name
        for (Star_obj s : list) {
            String crs = s.getCourse();
            if (!courses.contains(crs) && s.getCRN() == -1) {
                //The list doesn't already contain the course and
                courses.add(crs);
            }
        }

        //Create new Star_Obj list containg non-reduant Star_Objs
        ArrayList<Star_obj> course_list = new ArrayList<>();
        //Iterate through current list
        for (Star_obj s : list) {
            boolean delete_current = false;
            for (String c : courses) {
                if (s.getCourse().equals(c) && s.getCRN() != -1) {
                    delete_current = true; //this star_obj is repeated
                    break;
                }
            }
            if (!delete_current) {
                course_list.add(s);
            }

        }
        // ----------------------- REDUNDANCY CHECK ---------------------------------
        return course_list;
    }

}