package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.GameView;
public class BitmapPool {
    private static final String TAG = BitmapPool.class.getSimpleName();
    private static HashMap<Integer, Bitmap> bitmaps = new HashMap<>();
    private static BitmapFactory.Options opts;
    static {
        opts = new BitmapFactory.Options();
        opts.inScaled = false;
    }

    public static Bitmap get(int mipmapResId) {
        Bitmap bitmap = bitmaps.get(mipmapResId);
        if (bitmap == null) {
            Resources res = GameView.view.getResources();
            bitmap = BitmapFactory.decodeResource(res, mipmapResId, opts);
//            Log.d(TAG, "BID=" + mipmapResId + " w:" + bitmap.getWidth() + " h:" + bitmap.getHeight());
            bitmaps.put(mipmapResId, bitmap);
        }
        return bitmap;
    }
}