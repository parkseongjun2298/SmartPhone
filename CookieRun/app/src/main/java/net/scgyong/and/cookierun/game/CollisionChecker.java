package net.scgyong.and.cookierun.game;

import android.graphics.Canvas;
import android.util.Log;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;
import net.scgyong.and.cookierun.framework.interfaces.GameObject;
import net.scgyong.and.cookierun.framework.res.Sound;
import net.scgyong.and.cookierun.framework.util.CollisionHelper;

import java.util.ArrayList;

public class CollisionChecker implements GameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();
    private  WarrierMonster warrierMonster;


    public CollisionChecker(WarrierMonster warrierMonster) {
        this.warrierMonster = warrierMonster;
    }

    private  MageMonster mageMonster;


    public CollisionChecker(MageMonster mageMonster) {
        this.mageMonster = mageMonster;
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

            if (CollisionHelper.collides(warrierMonster, (BoxCollidable) fire)) {

                boolean dead = warrierMonster.decreaseLife(10.f);
                    Sound.playEffect(R.raw.jelly_coin);
                if (dead) {

                    Player p=MainScene.get().GetPlayer();
                    p.kill+=1;
                    game.remove(warrierMonster);


                }


                game.remove(fire);
            }

//            if (CollisionHelper.collides(mageMonster, (BoxCollidable) fire)) {
//
//                boolean dead = mageMonster.decreaseLife(10.f);
//                // Sound.playEffect(jelly.soundId());
//                if (dead) {
//                    game.remove(mageMonster);
//
//                }
//
//
//                game.remove(fire);
//            }

        }
    }

    @Override
    public void draw(Canvas canvas) {
    }
}
