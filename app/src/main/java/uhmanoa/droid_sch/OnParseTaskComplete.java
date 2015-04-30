package uhmanoa.droid_sch;

import java.io.IOException;

/**
 * Created by supah_000 on 3/26/2015.
 */
public interface OnParseTaskComplete {
    void onParseTaskComplete(IOException e);
    void onPartialParseTaskComplete();
}
