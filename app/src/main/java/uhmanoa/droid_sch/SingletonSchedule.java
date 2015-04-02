package uhmanoa.droid_sch;

import java.util.ArrayList;


public class SingletonSchedule {
    private static SingletonSchedule instance = null;
	private Schedule sched;

	private SingletonSchedule(){
        sched = new Schedule(-1, -1, -1);
	}

    public static SingletonSchedule getInstance() {
        if(instance == null) {
            instance = new SingletonSchedule();
        }
        return instance;
    }

    public void setSchedule(Schedule s) {
        sched = new Schedule(s);
    }

	public Schedule getSchedule(){
		return sched;
	}


}
