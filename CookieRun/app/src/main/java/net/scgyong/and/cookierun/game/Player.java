package net.scgyong.and.cookierun.game;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.JsonReader;

import net.scgyong.and.cookierun.R;
import net.scgyong.and.cookierun.framework.game.Scene;
import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;
import net.scgyong.and.cookierun.framework.interfaces.GameObject;
import net.scgyong.and.cookierun.framework.objects.Button;
import net.scgyong.and.cookierun.framework.objects.SheetSprite;
import net.scgyong.and.cookierun.framework.res.BitmapPool;
import net.scgyong.and.cookierun.framework.res.Metrics;
import net.scgyong.and.cookierun.framework.view.GameView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Player extends SheetSprite implements BoxCollidable {

    private static final float FRAMES_PER_SECOND = 8f;
    private static final String TAG = Player.class.getSimpleName();
    private float dx;
    private float dy;

    public void changeBitmap() {
        int nextIndex = (cookieIndex + 1) % cookieInfos.size();
        selectCookie(nextIndex);
        setState(state);
    }
    public  enum MoveState{
        left,right, stop, up,down, shot;
    }
    public MoveState moveState;
    private enum State {
        run, idle,att,slide, COUNT;
        Rect[] srcRects() {
            return rectsArray[this.ordinal()];
        }
        void applyInsets(RectF dstRect) {
            float[] inset = insets[this.ordinal()];
            float w = dstRect.width();
            float h = dstRect.height();
            dstRect.left += w * inset[0];
            dstRect.top += h * inset[1];
            dstRect.right -= w * inset[2];
            dstRect.bottom -= h * inset[3];
        }
        static Rect[][] rectsArray;
        static void initRects(CookieInfo info) {
            int[][] indices = {
                    new int[] { 100, 101, 102, 103 }, // run
                    new int[] { 7, 8 }, // jump
                    new int[] { 1, 2, 3, 4 }, // doubleJump
                    new int[] { 0 }, // falling
                    new int[] { 9, 10 },
            };
            rectsArray = new Rect[indices.length][];
            for (int r = 0; r < indices.length; r++) {
                int[] ints = indices[r];
                Rect[] rects = new Rect[ints.length];
                for (int i = 0; i < ints.length; i++) {
                    int idx = ints[i];
                    int l = 2 + (idx % 100) * (2 + info.size);
                    int t = 2 + (idx / 100) * (2 + info.size);
                    Rect rect = new Rect(l, t, l + info.size, t + info.size);
                    rects[i] = rect;
                }
                rectsArray[r] = rects;
            }
        }
        static float[][] insets = {
                new float[] { 85/270f, 135/270f, 80/270f, 0.00f }, // run
                new float[] { 85/270f, 158/270f, 80/270f, 0.00f }, // jump
                new float[] { 85/270f, 150/270f, 80/270f, 0.00f }, // doubleJump
                new float[] { 85/270f, 125/270f, 80/270f, 0.00f }, // falling
                new float[] { 80/270f, 204/270f, 50/270f, 0.00f }, // slide
        };
    }
    private State state = State.run;
    private final float jumpPower;
    private final float gravity;
    private float jumpSpeed;
    protected RectF collisionBox = new RectF();

    public float kill=0;
    public Player(float x, float y) {
        super(0, FRAMES_PER_SECOND);
        this.x = x;
        this.y = y;
        setDstRect(0, 0);
        loadCookiesInfo();
        selectCookie(0);
        jumpPower = Metrics.size(R.dimen.player_jump_power);
        gravity = Metrics.size(R.dimen.player_gravity);
        setState(State.run);
        moveState = MoveState.stop;

    }
    public float Get_Player_PosX()
    {
        return x;
    }
    public float Get_Player_PosY()
    {
        return y;
    }


    private void selectCookie(int cookieIndex) {
        this.cookieIndex = cookieIndex;
        CookieInfo info = cookieInfos.get(cookieIndex);
        State.initRects(info);
        AssetManager assets = GameView.view.getContext().getAssets();
        try {
            String filename = "cookies/" + info.id + "_sheet.png";
            InputStream is = assets.open(filename);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        float bottom = dstRect.bottom;
        float size = MainScene.get().size(info.size * 3.85f / 270);
        dstRect.set(x - size / 2, bottom - size, x + size / 2, bottom);
    }

    private class CookieInfo {
        int id;
        String name;
        int size;
        int xcount;
        int ycount;
    }
    private ArrayList<CookieInfo> cookieInfos;
    private int cookieIndex;
    private void loadCookiesInfo() {
        cookieInfos = new ArrayList<>();
        AssetManager assets = GameView.view.getContext().getAssets();
        try {
            InputStream is = assets.open("cookies.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                CookieInfo info = new CookieInfo();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("id")) {
                        info.id = reader.nextInt();
                    } else if (name.equals("name")) {
                        info.name = reader.nextString();
                    } else if (name.equals("size")) {
                        info.size = reader.nextInt();
                    } else if (name.equals("xcount")) {
                        info.xcount = reader.nextInt();
                    } else if (name.equals("ycount")) {
                        info.ycount = reader.nextInt();
                    }
                }
                reader.endObject();
                cookieInfos.add(info);
            }
            reader.endArray();

            cookieIndex = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public float size(float unit) {
        return Metrics.height / 9.5f * unit;
    }
    @Override
    public RectF getBoundingRect() {
        return collisionBox;
    }

    private  MageMonster mageMonster;
    private  Boss boss;
    public boolean isCheck = false;
    public boolean isCheck2 = false;
    boolean moncreatecheck=false;
    boolean moncreatecheck2=false;
    float fin=0;
    @Override
    public void update(float frameTime) {
        float foot = collisionBox.bottom;
        MainScene game = MainScene.get();

        if(this.kill==1&& !moncreatecheck) {

            mageMonster = new MageMonster(2000, 500);
            game.add(MainScene.Layer.monster.ordinal(), mageMonster);
            game.add(MainScene.Layer.controller.ordinal(), new CollisionChecker2(mageMonster));
            moncreatecheck=true;
        }
        if(this.kill==2 && !moncreatecheck2) {

            boss = new Boss(2000, 500);
            game.add(MainScene.Layer.monster.ordinal(), boss);
            game.add(MainScene.Layer.controller.ordinal(), new CollisionChecker3(boss));
            moncreatecheck2=true;
        }
        if(this.fin==1)
        {
            float btn_x = size(1.5f);
            float btn_y = size(8.75f);
            float btn_w = size(8.0f / 3.0f);
            float btn_h = size(1.0f);

            game.add(MainScene.Layer.touchUi.ordinal(), new Button(
                    Metrics.width/2, Metrics.height/2, btn_w, btn_h, R.mipmap.gameclear, R.mipmap.gameclear,
                    new Button.Callback() {
                        @Override
                        public boolean onTouch(Button.Action action) {
                            if (action != Button.Action.pressed) return false;
                            Scene.clear();
                            return true;
                        }
                    }));

        }




        switch (state) {

            case run:
                if(moveState == MoveState.stop)
                {
                    isCheck = false;
                    isCheck2=false;
                }
                else if(moveState == MoveState.left)
                {
                    left(frameTime);
                    isCheck = false;
                }
                else if(moveState == MoveState.right)
                {
                    right(frameTime);
                }
                else if(moveState == MoveState.up)
                {
                    up(frameTime);
                    isCheck2 = false;
                }
                else if(moveState == MoveState.down)
                {
                    down(frameTime);
                }


                break;
        }





    }

    private float findNearestPlatformTop(float foot) {
        Platform platform = findNearestPlatform(foot);
        if (platform == null) return Metrics.height;
        return platform.getBoundingRect().top;
    }

    private Platform findNearestPlatform(float foot) {
        Platform nearest = null;
        MainScene game = MainScene.get();

        ArrayList<GameObject> platforms = game.objectsAt(MainScene.Layer.platform.ordinal());
        float top = Metrics.height;
        for (GameObject obj: platforms) {
            Platform platform = (Platform) obj;
            RectF rect = platform.getBoundingRect();
            if (rect.left > x || x > rect.right) {
                continue;
            }
//            Log.d(TAG, "foot:" + foot + " platform: " + rect);
            if (rect.top < foot) {
                continue;
            }
            if (top > rect.top) {
                top = rect.top;
                nearest = platform;
            }
//            Log.d(TAG, "top=" + top + " gotcha:" + platform);
        }
        return nearest;
    }


    public void left(float frameTime) {

        float dx=-this.x*frameTime;


        x+=dx;
        dstRect.offset(dx, 0);
        collisionBox.offset(dx, dy);
        dx=0;
    }
    public void right(float frameTime) {

        float dx=this.x*frameTime;

        x+=dx;
        dstRect.offset(dx, 0);
        collisionBox.offset(dx, dy);
        dx=0;

    }
    public void up(float frameTime) {

        float dy=-this.y*frameTime;

        y+=dy;
        dstRect.offset(0, dy);
        collisionBox.offset(dx, dy);

    }
    public void down(float frameTime) {

        float  dy=this.y*frameTime;


        y+=dy;
        dstRect.offset(0, dy);
        collisionBox.offset(dx, dy);

    }

    public void slide(boolean startsSlide) {
        if (state == State.run && startsSlide) {
            setState(State.slide);
            return;
        }
        if (state == State.slide && !startsSlide) {
            setState(State.run);
            return;
        }
    }
    public void fire(boolean startsSlide) {

        float power = 10 ;
        Fire bullet = new Fire(x,y-250,power);
        MainScene.get().add(MainScene.Layer.fire.ordinal(),bullet);

    }
    public void att(boolean startsSlide) {

        float power = 5 ;
        PlayerAtt bullet = new PlayerAtt(x+50,y-250,power);
        MainScene.get().add(MainScene.Layer.fire.ordinal(),bullet);

    }
    public void ChangeMoveState(int state)
    {
        if(state == 0)
            moveState = MoveState.stop;
        if(state == 1)
            moveState = MoveState.left;
        if(state == 2)
            moveState = MoveState.right;
        if(state == 3)
            moveState = MoveState.up;
        if(state == 4)
            moveState = MoveState.down;
    }




    private void setState(State state) {
        this.state = state;
        srcRects = state.srcRects();
        collisionBox.set(dstRect);
        state.applyInsets(collisionBox);
    }
}
