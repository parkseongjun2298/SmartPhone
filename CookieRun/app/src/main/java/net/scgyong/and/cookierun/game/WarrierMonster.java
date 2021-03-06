package net.scgyong.and.cookierun.game;

import android.content.res.AssetManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Rect;
        import android.graphics.RectF;
        import android.util.JsonReader;

        import net.scgyong.and.cookierun.R;
        import net.scgyong.and.cookierun.framework.interfaces.BoxCollidable;
        import net.scgyong.and.cookierun.framework.interfaces.GameObject;
        import net.scgyong.and.cookierun.framework.objects.SheetSprite;
        import net.scgyong.and.cookierun.framework.res.BitmapPool;
        import net.scgyong.and.cookierun.framework.res.Metrics;
        import net.scgyong.and.cookierun.framework.view.GameView;
        import net.scgyong.and.cookierun.framework.util.Gauge;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;

public class WarrierMonster extends SheetSprite implements BoxCollidable {
    protected float life, maxLife;
    private static final float FRAMES_PER_SECOND = 8f;
    private static final String TAG = Player.class.getSimpleName();
    private float dx;
    private float dy;

    private float Mx;
    private float My;

    public void changeBitmap() {
//        int nextIndex = (cookieIndex + 1) % cookieInfos.size();
//        selectCookie(nextIndex);
//        setState(state);
    }
    private float fx;
    public void Set_posX(float fx){
        this.fx=x-fx;
        x= this.fx;
        dstRect.offset( this.fx, 0);
        collisionBox.offset( this.fx, 0);

    }
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

    private Gauge gauge;
    public WarrierMonster(float x, float y) {
        super(0, FRAMES_PER_SECOND);
        this.x = x;
        this.y = y;
        setDstRect(0, 0);
        loadCookiesInfo();
        selectCookie(0);
        jumpPower = Metrics.size(R.dimen.player_jump_power);
        gravity = Metrics.size(R.dimen.player_gravity);
        setState(State.run);
        maxLife = 100;
        life = maxLife;


//        gauge = new Gauge(
//                Metrics.size(R.dimen.map_gauge_y),
//                R.color.black,
//                Metrics.size(R.dimen.map_gauge_y),
//                R.color.white,

    }

    private void selectCookie(int cookieIndex) {
        this.cookieIndex = cookieIndex;
        CookieInfo info = cookieInfos.get(cookieIndex);
        State.initRects(info);
        AssetManager assets = GameView.view.getContext().getAssets();
        try {
            String filename = "monster/" + info.id + "_sheet.png";
            InputStream is = assets.open(filename);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        float bottom = dstRect.bottom;
        float size = MainScene.get().size(info.size * 3.85f / 200);
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
            InputStream is = assets.open("monster.json");
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

    @Override
    public RectF getBoundingRect() {
        return collisionBox;
    }


    public float count=0;
    boolean attcheck=false;
    boolean first=false;
    @Override
    public void update(float frameTime) {
        Player p=MainScene.get().GetPlayer();
        double dX = x - p.Get_Player_PosX(); // x????????? ?????????
        double dY = y - p.Get_Player_PosY(); // y????????? ?????????
        double distance=Math.sqrt(dX*dX+dY*dY);

//        if(p.kill==1 && !first)
//        {
//            Set_posX(1700);
//            first=true;
//        }
        if(attcheck)
        {
            count++;
            if(count>=40)
            {
                attcheck=false;
                count=0;
            }
        }
        if(distance<700.f&& !attcheck) {
            //??????????????????
            att();
            attcheck=true;
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

    public void att() {

        float power = 5 ;
        MonAtt bullet = new MonAtt(x-30,y-250,power);
        MainScene.get().add(MainScene.Layer.monatt.ordinal(),bullet);

    }

    public void left(float frameTime) {

        dx=-this.x*frameTime*10;

        dstRect.offset(dx, 0);
        collisionBox.offset(dx, 0);
    }
    public void right(float frameTime) {

        dx=this.x*frameTime*10;

        dstRect.offset(dx, 0);
        collisionBox.offset(dx, 0);
    }
    public void up(float frameTime) {

        dy=-this.y*frameTime*5;

        dstRect.offset(0, dy);
        collisionBox.offset(0, dy);
    }
    public void down(float frameTime) {

        dy=this.y*frameTime*5;

        dstRect.offset(0, dy);
        collisionBox.offset(0, dy);
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

    public boolean decreaseLife(float power) {
        life -= power;
        if (life <= 0) return true;

        //gauge.setValue((float)life / maxLife);
        return false;
    }



    private void setState(State state) {
        this.state = state;
        srcRects = state.srcRects();
        collisionBox.set(dstRect);
        state.applyInsets(collisionBox);
    }



}