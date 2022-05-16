package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.R;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.BitmapPool;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Sprite;


public class Fire extends Sprite {

    private static final String TAG = WarrierMonster.class.getSimpleName();

    private float dx;
    private float dy;
    private float tx;
    private float ty;


    public Fire(float x, float y) {
        super(x, y, R.dimen.fighter_radius, R.mipmap.skill_firedragon00);


    }

    public void update() {
        float frameTime = MainGame.getInstance().frameTime;
        float dx = this.dx * frameTime;
        float dy = this.dy * frameTime;
        dx+=500*frameTime;
        if (x > Metrics.width || x<0) {
            MainGame.getInstance().remove(this);
            //recycleBin.add(this);
        }
        dstRect.offset(dx, dy);
    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(bitmap, null, dstRect, null);


    }
}