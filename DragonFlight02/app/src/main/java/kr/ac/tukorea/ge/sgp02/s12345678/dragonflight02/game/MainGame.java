package kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.BoxCollidable;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Metrics;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.R;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.GameObject;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.GameView;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Recyclable;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.RecycleBin;
import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Button;

import kr.ac.tukorea.ge.sgp02.s12345678.dragonflight02.framework.Touchable;
public class MainGame {
    private static final String TAG = MainGame.class.getSimpleName();
    private Paint collisionPaint;
    Score score;


    public static MainGame getInstance() {
        if (singleton == null) {
            singleton = new MainGame();
        }
        return singleton;
    }

    public float frameTime;

    private MainGame() {
    }

    private static MainGame singleton;

    private static final int BALL_COUNT = 10;
//    private ArrayList<GameObject> objects = new ArrayList<>();
    private ArrayList<ArrayList<GameObject>> layers;
    public enum Layer {
        bg1, fireball, enemy,monster, player,touchUi, bg2, ui, controller,COUNT,
    }
    private Fighter fighter;
    private WarrierMonster warrierMonster;
    public static void clear() {
        singleton = null;
    }
    public float size(float unit) {
        return Metrics.height / 9.5f * unit;
    }
    public void init() {

//        objects.clear();
        initLayers(Layer.COUNT.ordinal());

       // add(Layer.controller, new EnemyGenerator());
        add(Layer.controller, new CollisionChecker());

        float fighterY = Metrics.height - Metrics.size(R.dimen.fighter_y_offset);
        fighter = new Fighter(Metrics.width / 2, fighterY);
        add(Layer.player, fighter);
        warrierMonster=new WarrierMonster(Metrics.width-200,Metrics.height-200);
        add(Layer.monster,warrierMonster);
        score = new Score();
//        score.set(12345);
        add(Layer.ui, score);

        add(Layer.bg1, new VertScrollBackground(R.mipmap.map01, Metrics.size(R.dimen.bg_speed_city)));


        float btn_x = size(1.5f);
        float btn_y = size(8.75f);
        float btn_w = size(8.0f / 3.0f);
        float btn_h = size(1.0f);

        add(Layer.touchUi, new Button(
                Metrics.width - btn_x, btn_y, btn_w, btn_h, R.mipmap.btn_slide_n, R.mipmap.btn_slide_p,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action)
                    {
//                if (action != Button.Action.pressed) return falsw1sz2e;
                        fighter.fire();
                        Log.d(TAG,"click");
                        return true;
                    }
                }));


        add(Layer.touchUi, new Button(
                Metrics.width - btn_x, btn_y-200, btn_w, btn_h, R.mipmap.btn_slide_n, R.mipmap.btn_slide_p,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                     //   if (action != Button.Action.pressed) return false;
                        fighter.fire();
                        return true;
                    }
                }));


        add(Layer.touchUi, new Button(
                btn_x, btn_y, btn_w, btn_h, R.mipmap.left_btn, R.mipmap.left_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action != Button.Action.pressed) return false;
                        fighter.left(frameTime);
                        return true;
                    }
                }));
        add(Layer.touchUi, new Button(
                btn_x+btn_x+520, btn_y, btn_w, btn_h, R.mipmap.right_btn, R.mipmap.right_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action != Button.Action.pressed) return false;
                        fighter.right(frameTime);
                        return true;
                    }
                }));


        add(Layer.touchUi, new Button(
                btn_x+btn_x+150, btn_y-200, btn_w, btn_h, R.mipmap.up_btn, R.mipmap.up_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action != Button.Action.pressed) return false;
                        fighter.up(frameTime);
                        return true;
                    }
                }));

        add(Layer.touchUi, new Button(
                btn_x+btn_x+150, btn_y, btn_w, btn_h, R.mipmap.down_btn, R.mipmap.down_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action != Button.Action.pressed) return false;
                        fighter.down(frameTime);
                        return true;
                    }
                }));


        collisionPaint = new Paint();
        collisionPaint.setColor(Color.RED);
        collisionPaint.setStyle(Paint.Style.STROKE);
    }

    private void initLayers(int count) {
        layers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            layers.add(new ArrayList<>());
        }
    }

    public void update(int elapsedNanos) {
        frameTime = elapsedNanos * 1e-9f; // 1_000_000_000.0f;
        for (ArrayList<GameObject> objects : layers) {
            for (GameObject gobj : objects) {
                gobj.update();
            }
        }

//        checkCollision();
    }

//    private void checkCollision() {
//    }

    public ArrayList<GameObject> objectsAt(Layer layer) {
        return layers.get(layer.ordinal());
    }

    public void draw(Canvas canvas) {
        for (ArrayList<GameObject> objects : layers) {
            for (GameObject gobj : objects) {
                gobj.draw(canvas);
                if (gobj instanceof BoxCollidable) {
                    RectF rect = ((BoxCollidable) gobj).getBoundingRect();
                    canvas.drawRect(rect, collisionPaint);
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int touchLayer = getTouchLayerIndex();
        if (touchLayer < 0) return false;
        ArrayList<GameObject> gameObjects = layers.get(touchLayer);
        for (GameObject gobj : gameObjects) {
            if (!(gobj instanceof Touchable)) {
                continue;
            }
            boolean processed = ((Touchable) gobj).onTouchEvent(event);
            if (processed) return true;
        }
        return false;
    }
    protected int getTouchLayerIndex() {
        return -1;
    }

    public void add(Layer layer, GameObject gameObject) {
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<GameObject> objects = layers.get(layer.ordinal());
                objects.add(gameObject);
            }
        });
    }

    public void remove(GameObject gameObject) {
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                for (ArrayList<GameObject> objects : layers) {
                    boolean removed = objects.remove(gameObject);
                    if (!removed) continue;
                    if (gameObject instanceof Recyclable) {
                        RecycleBin.add((Recyclable) gameObject);
                    }
                    break;
                }
            }
        });
    }

    public int objectCount() {
        int count = 0;
        for (ArrayList<GameObject> objects : layers) {
            count += objects.size();
        }
        return count;
    }
}
