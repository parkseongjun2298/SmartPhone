package net.scgyong.and.cookierun.game;

import android.graphics.Canvas;
import android.util.Log;

import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;
import net.scgyong.and.cookierun.framework.interfaces.GameObject;
import net.scgyong.and.cookierun.framework.res.Sound;
import net.scgyong.and.cookierun.framework.util.CollisionHelper;

import java.util.ArrayList;

public class CollisionChecker implements GameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();
    private final WarrierMonster warrierMonster;


    public CollisionChecker(WarrierMonster warrierMonster) {
        this.warrierMonster = warrierMonster;
    }


    //플레이어 아이탬 충돌
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
                //Log.d(TAG, "Collision: " + item);
                if (fire instanceof Fire) {
                    Fire jelly = (Fire) fire;

                   // Sound.playEffect(jelly.soundId());

                }
                game.remove(fire);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
    }
}
