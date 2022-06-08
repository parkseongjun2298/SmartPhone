package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.R;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Sprite;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.BitmapPool;

public class Fighter extends Sprite {

    private static final String TAG = Fighter.class.getSimpleName();
    private Bitmap targetBitmap;
    private RectF targetRect = new RectF();

    private float dx;
    private float dy;
    private float tx;
    private float ty;

    private float elapsedTimeForFire;
    private float fireInterval;

    public Fighter(float x, float y) {
        super(x, y, R.dimen.fighter_radius, R.mipmap.r_idle);
       // setTargetPosition(x, y);

     //   targetBitmap = BitmapPool.get(R.mipmap.target);
     //   fireInterval = Metrics.floatValue(R.dimen.fighter_fire_interval);
    }

    public void update() {
        float frameTime = MainGame.getInstance().frameTime;
        elapsedTimeForFire += frameTime;
//        if (elapsedTimeForFire > fireInterval) {
//           // fire();
////            elapsedTimeForFire = 0;
//            elapsedTimeForFire -= fireInterval;
//        }

//        if (dx == 0)
//            return;
//
//        float dx = this.dx * frameTime;
//        if ((dx > 0 && x + dx > tx) || (dx < 0 && x + dx < tx)) {
//            dx = tx - x;
//            x = tx;
//            this.dx = 0;
//        } else {
//            x += dx;
//        }
//
//        float dy = this.dy * frameTime;
//        if ((dy > 0 && y+ dy > ty) || (dy < 0 && y + dy < ty)) {
//            dy = ty - y;
//            y = ty;
//            this.dy = 0;
//        } else {
//            y += dy;
//        }
//
//
//        dstRect.offset(dx, dy);
    }

    public void draw(Canvas canvas) {
//        canvas.save();
//        canvas.rotate((float) (angle * 180 / Math.PI) + 90, x, y);
        canvas.drawBitmap(bitmap, null, dstRect, null);
//        canvas.restore();
//        if (dx != 0) {
//            canvas.drawBitmap(targetBitmap, null, targetRect, null);
//        }
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

    public void fire() {
        int score = MainGame.getInstance().score.get();
        if (score > 100000) score = 100000;
        float power = 10 + score / 1000;
        Fire bullet = new Fire(x,y);
        MainGame.getInstance().add(MainGame.Layer.fireball, bullet);
    }


    public void left(float frameTime) {

        dx=-this.x*frameTime*10;

        dstRect.offset(dx, 0);

    }
    public void right(float frameTime) {

        dx=this.x*frameTime*10;

        dstRect.offset(dx, 0);

    }
    public void up(float frameTime) {

        dy=-this.y*frameTime*5;

        dstRect.offset(0, dy);

    }
    public void down(float frameTime) {

        dy=this.y*frameTime*5;

        dstRect.offset(0, dy);

    }
    public float get_x()
    {
        return this.x;
    }
    public float get_y()
    {
        return this.y;
    }

}
