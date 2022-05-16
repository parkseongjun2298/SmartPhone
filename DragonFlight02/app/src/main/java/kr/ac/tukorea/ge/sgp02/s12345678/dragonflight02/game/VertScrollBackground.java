package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Sprite;

// homework: make HorzScrollBackground class
public class VertScrollBackground extends Sprite {
    private final float speed;

    private final int width;
    public VertScrollBackground(int bitmapResId, float speed) {
        super(Metrics.width , Metrics.height ,
                Metrics.width, Metrics.height, bitmapResId);
        // height = bitmap.getHeight() * Metrics.width / bitmap.getWidth();
        width =bitmap.getHeight()*Metrics.height/bitmap.getHeight();
        setDstRect(width, Metrics.height);

        this.speed = speed;
    }

    @Override
    public void update() {
       // this.x += speed * MainGame.getInstance().frameTime;
//        if (y > Metrics.height) y = 0;
//        setDstRect(Metrics.width, height);
    }

    @Override
    public void draw(Canvas canvas) {
      dstRect.set(0, 0, Metrics.width, Metrics.height);
      canvas.drawBitmap(bitmap, null, dstRect, null);

    }
}
