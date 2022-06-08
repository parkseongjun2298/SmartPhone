package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.R;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.BitmapPool;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Sprite;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.BoxCollidable;

public class Fire extends Sprite implements BoxCollidable{

    private static final String TAG = WarrierMonster.class.getSimpleName();

    private float dx;
    private float dy;
    private float tx;
    private float ty;
    private float power;
    protected RectF boundingRect = new RectF();
    public Fire(float x, float y) {
        super(x, y, R.dimen.fighter_radius, R.mipmap.skill_firedragon00);
        this.power=10.f;

    }

    private static float size, inset;
    public static void setSize(float size) {
        Fire.size = size;
        Fire.inset = size / 16;
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



      //  setDstRectWithRadius();
        boundingRect.set(dstRect);
        boundingRect.inset(inset, inset);
        dstRect.offset(dx, dy);
    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(bitmap, null, dstRect, null);


    }



    public float getPower() {

        return this.power;
    }

    @Override
    public RectF getBoundingRect() {
        return boundingRect;
    }
}