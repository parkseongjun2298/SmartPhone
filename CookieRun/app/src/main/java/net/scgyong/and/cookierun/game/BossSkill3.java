package net.scgyong.and.cookierun.game;




import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.JsonReader;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.res.BitmapPool;
import net.scgyong.and.cookierun.framework.res.Metrics;
import net.scgyong.and.cookierun.framework.objects.Sprite;
import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;

import net.scgyong.and.cookierun.framework.view.GameView;

public class BossSkill3 extends Sprite implements BoxCollidable {

    private static final String TAG = WarrierMonster.class.getSimpleName();
    protected float x, y;

    private float dx;
    private float dy;
    private float tx;
    private float ty;
    private float power;
    protected RectF boundingRect = new RectF();
    public BossSkill3(float x, float y,float power) {
        super(x, y, R.dimen.fire_speed, R.mipmap.bossskill3);
        this.power=power;
        this.x=x;
        this.y=y;

    }

    private static float size, inset;
    public static void setSize(float size) {
        BossSkill3.size = size;
        BossSkill3.inset = size / 16;
    }




    public float createtime=0.f;
    public void update(float frameTime) {
        MainScene game = MainScene.get();

        createtime+=1;


        if (createtime>=50) {

            game.remove(this);
            //recycleBin.add(this);
        }
        float dx = this.x * frameTime;

        dx-=5;
        float dy = this.y * frameTime;

        dy+=5;

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
