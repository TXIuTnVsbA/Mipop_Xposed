package io.github.anonymousname.mipop.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import io.github.anonymousname.mipop.animation.AnimationParking;

import java.util.HashMap;
import java.util.Map;

public abstract class MeterBase extends ImageView {
    public static Map<String, MeterBase> MeterMap = new HashMap();
    public static int baseX = 0;
    public static int baseY = (Until.SCREEM_HEIGHT / 2);
    public static Context mContext;
    public static int mLeftMargin = 0;
    public static boolean mTouchDown = false;
    public static Paint paint = new Paint();
    private static int pressX = 0;
    private Bitmap bmp;
    private Bitmap bmpDown;
    private int changeX = -1;
    private int changeY = -1;
    private Handler handler4LongClick = new Handler();
    private boolean hasMoved = false;
    private boolean isDown = false;
    public boolean isLongClick = false;
    private final long mTime4LongClick = 1000L;
    private int mTouchStartX = 0;
    private int mTouchStartY = 0;
    public WindowManager mWindowManager = null;
    private Rect rectDst;
    private int resId = 0;
    private int resIdPressed = 0;
    private Runnable runnable4LongClick = new Runnable() {
        public void run() {
            MeterBase.this.isLongClick = true;
            MeterBase.this.LongClick();
        }
    };
    public LayoutParams wmParams = new LayoutParams();

    protected abstract void Click();

    protected abstract void LongClick();

    public MeterBase(Context context) {
        super(context);
        mContext = context;
        this.mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.wmParams.type = 2003;
        this.wmParams.format = -2;
        LayoutParams layoutParams = this.wmParams;
        layoutParams.flags |= 8;
        layoutParams = this.wmParams;
        layoutParams.flags |= 262144;
        this.wmParams.gravity = 51;
        this.wmParams.x = baseX;
        this.wmParams.y = baseY;
        this.wmParams.height = Until.IMAGE_WIDTH;
        this.wmParams.width = Until.IMAGE_WIDTH;
        this.mWindowManager.addView(this, this.wmParams);
        this.changeX = baseX;
        this.changeY = baseY;
    }

    public static Map<String, MeterBase> getMap() {
        return MeterMap;
    }

    public void Register(String key, MeterBase value) {
        MeterMap.put(key, value);
    }

    public void setResId(int id, int idDown) {
        this.resId = id;
        this.resIdPressed = idDown;
    }

    public void setBitmap(int id, int idDown) {
        this.bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
        this.bmpDown = BitmapFactory.decodeResource(mContext.getResources(), idDown);
        this.rectDst = new Rect(0, 0, Until.IMAGE_WIDTH, Until.IMAGE_WIDTH);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = ((int) event.getRawY()) - 25;
        switch (event.getAction()) {
            default:
                break;
            //case R.styleable.EditTextExt_Text /*0*/:
            case MotionEvent.ACTION_DOWN:
                Log.i("OUT", "base DOWN" + this.hasMoved);
                setImageResource(this.resIdPressed);
                this.handler4LongClick.postDelayed(this.runnable4LongClick, mTime4LongClick);
                AnimationParking.stop();
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                Log.i("OUT", "base UP" + this.hasMoved);
                setImageResource(this.resId);
                this.handler4LongClick.removeCallbacks(this.runnable4LongClick);
                if (this.hasMoved) {
                    Log.i("Suhao.Click", "MeterBase.UP, has moved");
                } else if (this.isLongClick) {
                    Log.i("Suhao.Click", "MeterBase.UP, Long click");
                } else {
                    Log.i("Suhao.Click", "MeterBase.UP, Click");
                    Click();
                }
                this.hasMoved = false;
                this.isLongClick = false;
                AnimationParking.start();
                return true;
            case 4:
                Log.i("OUT", "base ACTION_OUTSIDE" + this.hasMoved);
                AnimationParking.shrinkStart();
                return true;
        }
        return true;
    }

    public void update(int x, int y) {
        this.wmParams.x = x;
        this.wmParams.y = y;
        this.mWindowManager.updateViewLayout(this, this.wmParams);
    }

    public void resetAlpha() {
        paint.setAlpha(255);
        invalidate();
    }

    public void moved() {
        this.hasMoved = true;
        this.handler4LongClick.removeCallbacks(this.runnable4LongClick);
    }

    public void test() {
        getWindowVisibleDisplayFrame(new Rect());
    }


}
