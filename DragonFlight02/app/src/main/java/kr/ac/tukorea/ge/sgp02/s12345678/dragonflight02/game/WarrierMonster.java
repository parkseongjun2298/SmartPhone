package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.R;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.BitmapPool;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.GameObject;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Sprite;



public class WarrierMonster extends Sprite implements GameObject {

    private static final String TAG = WarrierMonster.class.getSimpleName();

    private float dx;
    private float dy;
    private float tx;
    private float ty;
    private RectF targetRect = new RectF();


    public WarrierMonster(float x, float y) {
        super(x, y, R.dimen.fighter_radius, R.mipmap.swordmon_idle);
        setTargetPosition(x, y);


    }

    public void update() {
        float frameTime = MainGame.getInstance().frameTime;



        if (dx == 0)
            return;

        float dx = this.dx * frameTime;
        if ((dx > 0 && x + dx > tx) || (dx < 0 && x + dx < tx)) {
            dx = tx - x;
            x = tx;
            this.dx = 0;
        } else {
            x += dx;
        }
        float dy = this.dy * frameTime;
        if ((dy > 0 && y+ dy > ty) || (dy < 0 && y + dy < ty)) {
            dy = ty - y;
            y = ty;
            this.dy = 0;
        } else {
            y += dy;
        }


        dstRect.offset(dx, dy);
    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(bitmap, null, dstRect, null);


    }

    public void setTargetPosition(float tx, float ty) {
        this.tx = tx;
        this.ty = ty;
        targetRect.set(tx - radius/2, ty - radius/2,
                tx + radius/2, ty + radius/2);

        dx = Metrics.size(R.dimen.fighter_speed);
        dy = Metrics.size(R.dimen.fighter_speed);
        if (tx < x) {
            dx = -dx;
        }
        if (ty < y)
        {
            dy = -dy;
        }


    }


}