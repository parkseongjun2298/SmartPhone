package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.BoxCollidable;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.GameObject;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.R;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Recyclable;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.RecycleBin;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Sprite;

public class Bullet extends Sprite implements GameObject, BoxCollidable, Recyclable {
    private static final String TAG = Bullet.class.getSimpleName();
    protected float x, y;
    protected final float length;
    protected final float dy;
    protected RectF boundingRect = new RectF();


    private float power;

    //    private static ArrayList<Bullet> recycleBin = new ArrayList<>();
    public static Bullet get(float x, float y, float power) {
        Bullet bullet = (Bullet) RecycleBin.get(Bullet.class);
        if (bullet != null) {
//            Bullet bullet = recycleBin.remove(0);
            bullet.set(x, y, power);
            return bullet;
        }
        return new Bullet(x, y, power);
    }

    private void set(float x, float y, float power) {
        this.x = x;
        this.y = y;
        this.power = power;
    }

    private Bullet(float x, float y, float power) {
        super(x,y,R.dimen.fighter_radius,R.mipmap.skill_firedragon00);
        this.length = Metrics.size(R.dimen.laser_length);
        this.dy = Metrics.size(R.dimen.laser_speed);
        this.power = power;




    }
    @Override
    public void update() {
        float frameTime = MainGame.getInstance().frameTime;
        x += dy * frameTime;

        float hw = 20 / 2;
        boundingRect.set(x - hw, y, x + hw, y - length);

        if (x > Metrics.width || x<0) {
            MainGame.getInstance().remove(this);

        }
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public RectF getBoundingRect() {
        return boundingRect;
    }

    @Override
    public void finish() {

    }

    public float getPower() {
        return power;
    }
}
