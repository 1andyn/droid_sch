package uhmanoa.droid_sch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Class created to load large resolution resources on Android
 *
 * When using high resolution image sources for android, android does not innately efficiently
 * downscale resources. As a result small images can easily use memory in orders of megabytes for
 * resizing images in the kilobytes range. As a result the heap will run out of memory and
 * the application will be very slow or crash. As a result this class was created to force
 * android to load a smaller version of the image and then resize.
 *
 */
public class imgLoader {
   public static int calcInSampleSize(BitmapFactory.Options p_bmp_opt, int p_i_width,
                                      int p_i_height) {
       final int i_height = p_bmp_opt.outHeight;
       final int i_width = p_bmp_opt.outWidth;
       int inSampleSize = 1;

       // Uses a power of two for reduction
       if(i_height > p_i_height || i_width > p_i_width) {
           final int halfheight = i_height / 2;
           final int halfwidth = i_width / 2;

           while((halfheight/inSampleSize) > p_i_height
                   && (halfwidth/inSampleSize) > p_i_width ) {
               inSampleSize *= 2;
           }
       }
       return inSampleSize;
   }

    public static Bitmap decodedSampledBitmapResource(Resources p_res, int p_i_rid, int p_i_width,
                                                    int p_i_height) {
    final BitmapFactory.Options bmp_options = new BitmapFactory.Options();
        bmp_options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(p_res, p_i_rid, bmp_options);

        bmp_options.inSampleSize = calcInSampleSize(bmp_options, p_i_width, p_i_height);
        bmp_options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(p_res, p_i_rid, bmp_options);
    }
}
