import android.app.Activity;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;

import java.io.File;
import java.util.Properties;

/**
 * Created by supah_000 on 3/3/2015.
 */
public class CustomRobolectricRunner extends RobolectricTestRunner {
    private static final int MAX_SDK_SUPPORTED_BY_ROBOLECTRIC = 18;

    public CustomRobolectricRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        String path = "/src/main/AndroidManifest.xml";
        String res = "src/main/res";
        if (!new File(path).exists()) {
            path = "app/" + path;
            res = "app/" + res;
        }

        return new AndroidManifest(Fs.fileFromPath(path), Fs.fileFromPath(res)) {
            @Override
            public int getTargetSdkVersion() {
                return MAX_SDK_SUPPORTED_BY_ROBOLECTRIC;
            }

            @Override
            public String getThemeRef(Class<? extends Activity> activityClass) {
                return "@style/RoboAppTheme";
            }
        };
    }

//    @Override
//    protected AndroidManifest getAppManifest(Config config) {
//        String path = "src/main/AndroidManifest.xml";
//
//        // android studio has a different execution root for tests than pure gradle
//        // so we avoid here manual effort to get them running inside android studio
//        if (!new File(path).exists()) {
//            path = "app/" + path;
//        }
//
//        config = overwriteConfig(config, "manifest", path);
//        return super.getAppManifest(config);
//    }
//
//    protected Config.Implementation overwriteConfig(
//            Config config, String key, String value) {
//        Properties properties = new Properties();
//        properties.setProperty(key, value);
//        return new Config.Implementation(config,
//                Config.Implementation.fromProperties(properties));
//    }
}