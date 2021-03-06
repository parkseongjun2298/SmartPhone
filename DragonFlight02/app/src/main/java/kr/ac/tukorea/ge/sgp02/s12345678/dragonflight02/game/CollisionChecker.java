package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.CollisionHelper;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.GameObject;

public class CollisionChecker implements GameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();

    @Override
    public void update() {
        MainGame game = MainGame.getInstance();
        ArrayList<GameObject> monsters = game.objectsAt(MainGame.Layer.monster);
        ArrayList<GameObject> fireballs = game.objectsAt(MainGame.Layer.fireball);
        for (GameObject o1: monsters) {
            if (!(o1 instanceof WarrierMonster)) {
                continue;
            }
            WarrierMonster monster = (WarrierMonster) o1;
            for (GameObject o2: fireballs) {
                if (!(o2 instanceof Fire)) {
                    continue;
                }
                Fire fire = (Fire) o2;
                if (CollisionHelper.collides(monster, fire)) {
                    Log.d(TAG, "Collision !!");
                    game.remove(fire);
                    float power = fire.getPower();
                    boolean dead = monster.decreaseLife(power);
                    if (dead) {
                        game.remove(monster);
                        //game.score.add(monster.getScore());
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
