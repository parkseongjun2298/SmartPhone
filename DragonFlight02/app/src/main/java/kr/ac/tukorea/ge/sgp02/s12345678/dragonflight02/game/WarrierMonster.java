package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.R;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.BitmapPool;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.BoxCollidable;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.GameObject;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Sprite;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game.Fighter;
//import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.SheetSprite;

public class WarrierMonster extends Sprite implements GameObject, BoxCollidable {

    private static final String TAG = WarrierMonster.class.getSimpleName();
    private float life, maxLife;

    protected Gauge gauge;
    private float dx;
    private float dy;
    private float tx;
    private float ty;
    private RectF targetRect = new RectF();
    protected RectF boundingRect = new RectF();
    protected Fighter player;


    public WarrierMonster(float x, float y) {
        super(x, y, R.dimen.fighter_radius, R.mipmap.swordmon_idle);



    }
    private static float size, inset;
    public static void setSize(float size) {
        WarrierMonster.size = size;
        WarrierMonster.inset = size / 16;
    }
    public void update() {
        float frameTime = MainGame.getInstance().frameTime;


        //플레이어위치로 바꿔야함
        setTargetPosition(x, y);


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

        boundingRect.set(dstRect);
        boundingRect.inset(inset, inset);
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
    @Override
    public RectF getBoundingRect() {
        return boundingRect;
    }


    public boolean decreaseLife(float power) {
        life -= power;
        if (life <= 0) return true;
        gauge.setValue(life / maxLife);
        return false;
    }

}