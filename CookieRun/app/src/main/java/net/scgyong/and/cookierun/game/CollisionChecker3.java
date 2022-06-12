package net.scgyong.and.cookierun.game;

import android.graphics.Canvas;
import android.util.Log;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;
import net.scgyong.and.cookierun.framework.interfaces.GameObject;
import net.scgyong.and.cookierun.framework.res.Sound;
import net.scgyong.and.cookierun.framework.util.CollisionHelper;

import java.util.ArrayList;

public class CollisionChecker3 implements GameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();


    private  Boss boss;


    public CollisionChecker3(Boss boss) {
        this.boss = boss;
    }





    //몬스터   파이어
    @Override
    public void update(float frameTime) {
        MainScene game = MainScene.get();

        ArrayList<GameObject> fires = game.objectsAt(MainScene.Layer.fire.ordinal());
        for (GameObject fire: fires) {
            if (!(fire instanceof BoxCollidable)) {
                continue;
            }



            if (CollisionHelper.collides(boss, (BoxCollidable) fire)) {

                boolean dead = boss.decreaseLife(10.f);
                Sound.playEffect(R.raw.jelly_coin);
                if (dead) {
                    Player p=MainScene.get().GetPlayer();
                    p.fin+=1;
                    game.remove(boss);

                }


                game.remove(fire);
            }

        }
    }

    @Override
    public void draw(Canvas canvas) {
    }
}