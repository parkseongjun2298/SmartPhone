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
    private MageMonster mageMonster;
    private Boss boss;
    private PlayerUi playerUi;
    private static MainScene singleton;
    public static MainScene get() {
        if (singleton == null) {
            singleton = new MainScene();
        }
        return singleton;
    }
    public enum Layer {
        bg, platform, item, obstacle, player,fire,monster,monatt, ui, touchUi, controller, COUNT;
    }
    public boolean nextstage=false;
    public float size(float unit) {
        return Metrics.height / 9.5f * unit;
    }

    public void setMapIndex(int mapIndex) {
        this.mapIndex = mapIndex;
    }
    public Player GetPlayer(){
        return  player;
    }
    public void SetStage(){nextstage=true;}
    protected int mapIndex;

    public void init() {
        super.init();

        initLayers(Layer.COUNT.ordinal());

//        Sprite player = new Sprite(
//                size(2), size(7),
//                size(2), size(2),
//                R.mipmap.cookie);
        player = new Player(100,1000);
        add(Layer.player.ordinal(), player);

        warrierMonster = new WarrierMonster(2000,1000);
        add(Layer.monster.ordinal(), warrierMonster);
        add(Layer.controller.ordinal(), new CollisionChecker(warrierMonster));


//            mageMonster = new MageMonster(2000, 500);
//            add(Layer.monster.ordinal(), mageMonster);
//            add(Layer.controller.ordinal(), new CollisionChecker2(mageMonster));
//
//
//
//            boss = new Boss(1800, 750);
//            add(Layer.monster.ordinal(), boss);
//            add(Layer.controller.ordinal(), new CollisionChecker3(boss));




        add(Layer.bg.ordinal(), new HorzScrollBackground(R.mipmap.map02, Metrics.size(R.dimen.bg_scroll_1)));


        int mipmapResId = R.mipmap.playerui;
        float marginTop = Metrics.size(R.dimen.score_margin_top);
        float marginRight = Metrics.size(R.dimen.score_margin_right);
        float charWidth = Metrics.size(R.dimen.score_digit_width);
        playerUi = new PlayerUi(mipmapResId, marginTop, marginRight, charWidth);
//        score.set(123456);
        add(Layer.ui.ordinal(), playerUi);


        float btn_x = size(1.5f);
        float btn_y = size(8.75f);
        float btn_w = size(8.0f / 3.0f);
        float btn_h = size(1.0f);

        add(Layer.touchUi.ordinal(), new Button(
                Metrics.width - btn_x, btn_y, btn_w, btn_h, R.mipmap.btn_att_n, R.mipmap.btn_att_n,
                new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                if (action != Button.Action.pressed) return false;
                player.att(action == Button.Action.pressed);
                return true;
            }
        }));



        add(Layer.touchUi.ordinal(), new Button(
                Metrics.width - btn_x, btn_y-200, btn_w, btn_h, R.mipmap.btn_skill_n, R.mipmap.btn_skill_n,
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
                        if (action == Button.Action.pressed) {
                            player.ChangeMoveState(1);

                            return true;
                        }
                        if (action == Button.Action.released) {
                            player.ChangeMoveState(0);
                            return true;
                        }
                        else return  false;
                    }
                }));
        add(Layer.touchUi.ordinal(), new Button(
                btn_x+btn_x+520, btn_y, btn_w, btn_h, R.mipmap.right_btn, R.mipmap.right_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action == Button.Action.pressed) {
                            player.ChangeMoveState(2);

                            return true;
                        }
                        if (action == Button.Action.released) {
                            player.ChangeMoveState(0);
                            return true;
                        }
                        else return  false;
                    }
                }));


        add(Layer.touchUi.ordinal(), new Button(
                btn_x+btn_x+150, btn_y-200, btn_w, btn_h, R.mipmap.up_btn, R.mipmap.up_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action == Button.Action.pressed) {
                            player.ChangeMoveState(3);

                            return true;
                        }
                        if (action == Button.Action.released) {
                            player.ChangeMoveState(0);
                            return true;
                        }
                        else return  false;
                    }
                }));

        add(Layer.touchUi.ordinal(), new Button(
                btn_x+btn_x+150, btn_y, btn_w, btn_h, R.mipmap.down_btn, R.mipmap.down_btn,
                new Button.Callback() {
                    @Override
                    public boolean onTouch(Button.Action action) {
                        if (action == Button.Action.pressed) {
                            player.ChangeMoveState(4);

                            return true;
                        }
                        if (action == Button.Action.released) {
                            player.ChangeMoveState(0);
                            return true;
                        }
                        else return  false;
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
