package io.github.anonymousname.mipop.widget;

import android.app.Instrumentation;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import io.github.anonymousname.mipop.Main;
import io.github.anonymousname.mipop.animation.AnimationParking;

public class MeterBack extends MeterBase {
    public static final String NAME = MeterBack.class.getSimpleName();
    private final boolean LEFT = true;
    private final boolean RIGHT = false;
    private int changeX = 0;
    private int changeY = 0;
    private boolean hasMoved = false;
    private boolean isDown = false;
    private int mBackX = 0;
    private int mBackY = 0;
    private int mTouchStartX = 0;
    private int mTouchStartY = 0;
    private int pressX = 0;

    public MeterBack(Context context) {
        super(context);
        Register(NAME, this);
        setSoundEffectsEnabled(true);
        setImageResource(Main.idBack);
        setResId(Main.idBack, Main.idBackPressed);
    }

    public void Click() {
        Log.i("Suhao.Click", "back click");
        playSoundEffect(0);
        new Thread() {
            public void run() {
                try {
                    //EventBus.getDefault().post(MyAccessibilityService.BACK);
                    //Until.runRootCommand("input keyevent 4");
                    new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    Log.i("shenzhan", "Back implement");
                } catch (Exception e) {
                    Log.d("shenzhan", e.toString());
                }
            }
        }.start();
    }

    public void LongClick() {
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Until.initialPop(mContext);
        AnimationParking.land();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = ((int) event.getRawY()) - 25;
        switch (event.getAction()) {
            //case R.styleable.EditTextExt_Text /*0*/:
            case 0:
                Log.i("OUT", "back ACTION_DOWN" + this.hasMoved);
                mTouchDown = true;
                this.changeX = x;
                this.changeY = y;
                this.mTouchStartX = x;
                this.mTouchStartY = y;
                this.isDown = true;
                break;
            case 1:
                Log.i("Suhao.Click", "MeterBacd.UP, MOVE_MAX_SIZE/baseX= " + Until.MOVE_MAX_SIZE + " / " + baseX);
                if (!this.hasMoved) {
                    mTouchDown = false;
                    this.hasMoved = false;
                    this.isDown = false;
                    break;
                }
                mTouchDown = false;
                this.hasMoved = false;
                this.isDown = false;
            //case R.styleable.MySeekBar_decr /*2*/:
            case 2:
                int offsetX = x - this.changeX;
                int offsetY = y - this.changeY;
                if (Math.abs(offsetX) > 3 || Math.abs(offsetY) > 3) {
                    Log.i("MBack", "baseX/offsetX = " + baseX + "/" + offsetX);
                    baseX += offsetX;
                    baseY += offsetY;
                    AnimationParking.updateAll(baseX, baseY);
                    this.changeX = x;
                    this.changeY = y;
                }
                if (Math.abs(x - this.mTouchStartX) <= Until.MOVE_MAX_SIZE && Math.abs(y - this.mTouchStartY) <= Until.MOVE_MAX_SIZE) {
                    return true;
                }
                moved();
                return true;
            case 4:
                Log.i("OUT", "back ACTION_OUTSIDE");
                break;
        }
        return super.onTouchEvent(event);
    }
}
