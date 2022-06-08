package net.scgyong.and.cookierun.game;

import android.view.MotionEvent;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.game.Scene;
import net.scgyong.and.cookierun.framework.objects.Button;
import net.scgyong.and.cookierun.framework.objects.HorzScrollBackground;
import net.scgyong.and.cookierun.framework.res.Metrics;
import net.scgyong.and.cookierun.framework.res.Sound;


public class MainScene extends Scene {
    public static final String PARAM_STAGE_INDEX = "stage_index";
    private static final String TAG = MainScene.class.getSimpleName();
    private Player player;
    private WarrierMonster warrierMonster;
    private static MainScene singleton;
    public static MainScene get() {
        if (singleton == null) {
            singleton = new MainScene();
        }
        return singleton;
    }
    public enum Layer {
        bg, platform, item, obstacle, player,fire,monster, ui, touchUi, controller, COUNT;
    }

    public float size(float unit) {
        return Metrics.height / 9.5f * unit;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }

    protected int mapIndex;

    public void init() {
        super.init();

        initLayers(Layer.COUNT.ordinal());

//        Sprite player = new Sprite(
//                size(2), size(7),
//                size(2), size(2),
//                R.mipmap.cookie);
        player = new Player(100,600);
        add(Layer.player.ordinal(), player);

        warrierMonster = new WarrierMonster(2000,1000);
        add(Layer.monster.ordinal(), warrierMonster);

        add(Layer.bg.ordinal(), new HorzScrollBackground(R.mipmap.cookie_run_bg_1, Metrics.size(R.dimen.bg_scroll_1)));
        add(Layer.bg.ordinal(), new HorzScrollBackground(R.mipmap.cookie_run_bg_2, Metrics.size(R.dimen.bg_scroll_2)));
        add(Layer.bg.ordinal(), new HorzScrollBackground(R.mipmap.cookie_run_bg_3, Metrics.size(R.dimen.bg_scroll_3)));

//        MapLoader mapLoader = MapLoader.get();
//        mapLoader.init(mapIndex);
//        add(Layer.controller.ordinal(), mapLoader);
        add(Layer.controller.ordinal(), new CollisionChecker(warrierMonster));

        float btn_x = size(1.5f);
        float btn_y = size(8.75f);
        float btn_w = size(8.0f / 3.0f);
        float btn_h = size(1.0f);

        add(Layer.touchUi.ordinal(), new Button(
                Metrics.width - btn_x, btn_y, btn_w, btn_h, R.mipmap.btn_slide_n, R.mipmap.btn_slide_p,
                new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
//                if (action != Button.Action.pressed) return false;
                player.slide(action == Button.Action.pressed);
                return true;
            }
        }));



        add(Layer.touchUi.ordinal(), new Button(
                Metrics.width - btn_x, btn_y-200, btn_w, btn_h, R.mipmap.btn_slide_n, R.mipmap.btn_slide_p,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                  if (action != Button.Action.pressed) return false;
                        player.fire(action == Button.Action.pressed);
                        return true;
                    }
                }));


        add(Layer.touchUi.ordinal(), new Button(
                btn_x, btn_y, btn_w, btn_h, R.mipmap.left_btn, R.mipmap.left_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action != Button.Action.pressed) return false;
                        player.left(frameTime);
                        return true;
                    }
                }));
        add(Layer.touchUi.ordinal(), new Button(
                btn_x+btn_x+520, btn_y, btn_w, btn_h, R.mipmap.right_btn, R.mipmap.right_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action != Button.Action.pressed) return false;
                        player.right(frameTime);
                        return true;
                    }
                }));


        add(Layer.touchUi.ordinal(), new Button(
                btn_x+btn_x+150, btn_y-200, btn_w, btn_h, R.mipmap.up_btn, R.mipmap.up_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action != Button.Action.pressed) return false;
                        player.up(frameTime);
                        return true;
                    }
                }));

        add(Layer.touchUi.ordinal(), new Button(
                btn_x+btn_x+150, btn_y, btn_w, btn_h, R.mipmap.down_btn, R.mipmap.down_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action != Button.Action.pressed) return false;
                        player.down(frameTime);
                        return true;
                    }
                }));




    }


    @Override
    public boolean handleBackKey() {
        push(PausedScene.get());
        return true;
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touchUi.ordinal();
    }

    @Override
    public void start() {
        Sound.playMusic(R.raw.main);
    }

    @Override
    public void pause() {
        Sound.pauseMusic();
    }

    @Override
    public void resume() {
        Sound.resumeMusic();
    }

    @Override
    public void end() {
        Sound.stopMusic();
    }
}
