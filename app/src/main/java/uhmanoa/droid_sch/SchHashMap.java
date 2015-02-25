package uhmanoa.droid_sch;

/**
 * Created by supah_000 on 2/24/2015.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class SchHashMap {

    private ArrayList<Schedule> sch_data;

    public SchHashMap (ArrayList<Schedule> sch) {
        sch_data = sch;
    }

    public static HashMap getData() {
        HashMap SchedHashMap = new HashMap();
        Html.fromHtml("test");
        List crs1 = new ArrayList();
        crs1.add("EE 361 \"Computer Sys & Design \n" +
                "CRN: 50111 Sec: 1 Cred: 3\n" +
                 "\"Instructor: Sasaki \n" +
                "7:30a-8:20a MWF Room: Post 124 \n" +
                        "12:00p-3:00p R Post 120");
        crs1.add("EE 306 \"Computer Sys & Design \n" +
                "CRN: 50111 Sec: 1 Cred: 3\n" +
                "\"Instructor: Sasaki \n" +
                "7:30a-8:20a MWF Room: Post 124 \n" +
                "12:00p-3:00p R Post 120");
        crs1.add("EE 361 \"Computer Sys & Design \n" +
                "CRN: 50111 Sec: 1 Cred: 3\n" +
                "\"Instructor: Sasaki \n" +
                "7:30a-8:20a MWF Room: Post 124 \n" +
                "12:00p-3:00p R Post 120");
        crs1.add("EE 361 \"Computer Sys & Design \n" +
                "CRN: 50111 Sec: 1 Cred: 3\n" +
                "\"Instructor: Sasaki \n" +
                "7:30a-8:20a MWF Room: Post 124 \n" +
                "12:00p-3:00p R Post 120");

        SchedHashMap.put("EE 361 POST 124 7:30a-8:20a \n" +
                "EE 205 POST 124 7:30a-8:20a \n" +
                "EE 371 POST 124 7:30a-8:20a \n" +
                "EE 260 POST 124 7:30a-8:20a"
                , crs1);

        return SchedHashMap;
    }
}