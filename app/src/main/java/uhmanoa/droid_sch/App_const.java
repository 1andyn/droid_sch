package uhmanoa.droid_sch;

/**
 * Created by supah_000 on 1/12/2015.
 */
public interface App_const {
    static enum buttons {
        create, view, search, exit
    }

    // Used for spacing on Views
    final int course_max = 8;
    final int title_max = 30;
    final int crn_max = 5;
    final int prof_max = 15;
    final int day_max = 8;
    final int loc_max = 10;
    final int time_max = 6;
    final int sec_max = 3;

    static final String gefc_fga = "Foundation GMP-A";
    static final String gefc_fgb = "Foundation GMP-B";
    static final String gefc_fgc = "Foundation GMP-C";
    static final String gefc_fs = "Foundation Symbolic Reasoning";
    static final String gefc_fw = "Foundation Written Communication";
    static final String gefc_da = "Diversification-Arts";
    static final String gefc_db = "Diversification-Biological Science";
    static final String gefc_dh = "Diversification-Humanities";
    static final String gefc_dl = "Diversification-Literatures";
    static final String gefc_dp = "Diversification-Physical Science";
    static final String gefc_dy = "Diversification-Laboratory (science)";
    static final String gefc_hsl = "Hawaiian or Second Language";
    static final String gefc_ni = "Non-introductory Course";
    static final String gefc_eth = "Contemporary Ethical Issues";
    static final String gefc_hap = "Hawaiian, Asian, Pacific Issues";
    static final String gefc_oc = "Oral Communication";
    static final String gefc_wi = "Writing Intensive";

    //View String Categories for determining what conversion to run
    static enum ViewStringCat {
        course, title, crn, prof, day, loc, time, sect
    }

    static enum semester {
        fall, spring, summer
    }

    //Major enumerations


}
