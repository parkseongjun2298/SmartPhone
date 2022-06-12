package net.scgyong.and.cookierun.game;

import android.graphics.Canvas;
import android.util.Log;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;
import net.scgyong.and.cookierun.framework.interfaces.GameObject;
import net.scgyong.and.cookierun.framework.res.Sound;
import net.scgyong.and.cookierun.framework.util.CollisionHelper;

import java.util.ArrayList;

public class CollisionChecker4 implements GameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();


    private  Player player;


    public CollisionChecker4(Player player) {
        this.player = player;
    }





    //몬스터   파이어
    @Override
    public void update(float frameTime) {
        MainScene game = MainScene.get();

        ArrayList<GameObject> fires = game.objectsAt(MainScene.Layer.monatt.ordinal());
        for (GameObject fire: fires) {
            if (!(fire instanceof BoxCollidable)) {
                continue;
            }



            if (CollisionHelper.collides(player, (BoxCollidable) fire)) {


                Sound.playEffect(R.raw.jelly_alphabet);



                game.remove(fire);
            }

        }
    }

    @Override
    public void draw(Canvas canvas) {
    }
}