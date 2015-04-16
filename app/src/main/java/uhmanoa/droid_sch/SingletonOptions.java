package uhmanoa.droid_sch;


import java.util.ArrayList;

public class SingletonOptions {
    private static SingletonOptions instance = null;
	private boolean en_StartTime = false;
    private boolean en_EndTime = false;
    private int startTime = 0;
    private int endTime = 0;
    private int mincrs = -1;
    private ArrayList<Character> daysOff;

	private SingletonOptions(){
        //do nothing
	}

    public static SingletonOptions getInstance() {
        if(instance == null) {
            instance = new SingletonOptions();
        }
        return instance;
    }

    public void setEn_StartTime(int time, boolean en) {
        en_StartTime = en;
        startTime = time;
    }

    public void setEn_EndTime(int time, boolean en) {
        en_EndTime = en;
        endTime = time;
    }

    public boolean getEnStart()
    {
        return en_StartTime;
    }

    public boolean getEnEnd() {
        return en_EndTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getMinCourse() {
        return mincrs;
    }

    public void setMinCrs(int crs) {
        mincrs = crs;
    }

    public boolean defaultCrs() {
        return mincrs == -1;
    }

    private void setDaysOff(ArrayList<Character> input) {
        daysOff = input;
    }

    private ArrayList<Character> getDaysOff() {
        return daysOff;
    }


}
