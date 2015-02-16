package uhmanoa.droid_sch;

/**
 * Created by supah_000 on 2/15/2015.
 */
public class Result_obj extends Course {
    /* This is the class used to store Courses into a list for the search activity */

    private long index_id;
    /* This parameter will be used to stored the ID user to identify the specific object in a list
    */

    public Result_obj(Course c, long pIid) {
        index_id = pIid;
        this.setCourse(c.getCourse());
        this.setTitle(c.getTitle());
        this.setCrn(c.getCrn());
        this.setCredits(c.getCredits());
        this.setProfessor(c.getProfessor());
        this.setDays1(c.getDays1());
        this.setDays2(c.getDays2());
        this.setStart1(c.getStart1());
        this.setEnd1(c.getEnd1());
        this.setStart2(c.getStart2());
        this.setEnd2(c.getEnd2());
        this.setRoom1(c.getRoom1());
        this.setRoom2(c.getRoom2());
        this.setFocusReqs(c.getFocusReqs());
    }

    public long getID() {
        return index_id;
    }

}
