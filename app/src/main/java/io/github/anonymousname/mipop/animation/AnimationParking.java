package io.github.anonymousname.mipop.animation;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import io.github.anonymousname.mipop.widget.MeterBack;
import io.github.anonymousname.mipop.widget.MeterBase;
import io.github.anonymousname.mipop.widget.MeterHome;
import io.github.anonymousname.mipop.widget.MeterMenu;
import io.github.anonymousname.mipop.widget.MeterRecent;
import io.github.anonymousname.mipop.widget.Until;

public class AnimationParking {
    public static final boolean LEFT = true;
    public static final boolean RIGHT = false;
    private static String TAG = "Parking";
    public static int baseX = MeterBase.baseX;
    public static int baseY = MeterBase.baseY;
    private static Handler handler4Parking = new Handler();
    private static Handler handler4PosCheck = new Handler();
    private static Handler handler4Shrink = new Handler();
    private static Handler handler4Turning = new Handler();
    private static int homeX = 0;
    private static int homeY = 0;
    public static boolean mAreaChanged = false;
    private static long mAutoUpdatePeriod = 10;
    public static boolean mOriginSide = true;
    private static long mParking2Shrink = 2000;
    private static int mStep = 10;
    private static boolean mTimeOut = false;
    private static long mVelocityTime = 300;
    private static int menuX = 0;
    private static int menuY = 0;
    private static int recentX = 0;
    private static int recentY = 0;
    private static Runnable runnable4Parking = new Runnable() {
        public void run() {
            AnimationParking.parking();
        }
    };
    private static Runnable runnable4PosCheck = new Runnable() {
        public void run() {
            AnimationParking.velocityCheck = true;
            AnimationParking.mTimeOut = true;
            if (AnimationParking.mAreaChanged) {
                AnimationParking.velocityCheck = false;
            }
        }
    };
    private static Runnable runnable4Shrink = new Runnable() {
        public void run() {
            AnimationParking.shrinking();
        }
    };
    private static Runnable runnable4Turning = new Runnable() {
        public void run() {
            AnimationParking.turning();
        }
    };
    private static boolean velocityCheck = false;

    private static void initial() {
        MeterBase meterBase = (MeterBase) MeterBase.MeterMap.get(MeterBack.NAME);
        baseX = MeterBase.baseX;
        meterBase = (MeterBase) MeterBase.MeterMap.get(MeterBack.NAME);
        baseY = MeterBase.baseY;
    }

    private static void quickSlide() {
        handler4PosCheck.removeCallbacks(runnable4PosCheck);
        if (!mTimeOut) {
            velocityCheck = true;
        }
        if (mAreaChanged) {
            velocityCheck = false;
        }
    }

    public static void start() {
        Log.i("Suhao", "AnimationParking start()");
        quickSlide();
        initial();
        if (baseX <= 0) {
            Log.i("Suhao.TransParent", "AnimationParking.start(), baseX<0");
            mOriginSide = true;
            mAreaChanged = false;
            velocityCheck = false;
            baseX = 0;
            AnimationTransparent.start();
        } else if (baseX >= Until.SCREEM_WIDTH - Until.IMAGE_WIDTH) {
            Log.i("Suhao.TransParent", "AnimationParking.start(), baseX>SCREEN_WIDTH-IMAGE_WIDTH");
            mOriginSide = false;
            mAreaChanged = false;
            velocityCheck = false;
            baseX = Until.SCREEM_WIDTH - Until.IMAGE_WIDTH;
            AnimationTransparent.start();
        } else {
            updateTop(baseX, baseY);
            updateBottom(baseX, baseY);
            if (mAreaChanged) {
                Log.i("Suhao", "else");
                handler4Parking.removeCallbacks(runnable4Parking);
                handler4Shrink.postDelayed(runnable4Shrink, mAutoUpdatePeriod);
            } else if (baseX < Until.PARKING_LINE || baseX > Until.PARKING_LINE_RIGHT) {
                Log.i("Suhao", "LEFT && > MID_LINE");
                handler4Parking.removeCallbacks(runnable4Parking);
                handler4Shrink.postDelayed(runnable4Shrink, mAutoUpdatePeriod);
            } else if (true == mOriginSide && baseX > Until.MID_LINE) {
                Log.i("Suhao", "LEFT && > MID_LINE");
                handler4Parking.removeCallbacks(runnable4Parking);
                handler4Shrink.postDelayed(runnable4Shrink, mAutoUpdatePeriod);
            } else if (mOriginSide || baseX >= Until.MID_LINE) {
                handler4Shrink.removeCallbacks(runnable4Shrink);
                handler4Parking.postDelayed(runnable4Parking, mAutoUpdatePeriod);
            } else {
                Log.i("Suhao", "LEFT && > MID_LINE");
                handler4Parking.removeCallbacks(runnable4Parking);
                handler4Shrink.postDelayed(runnable4Shrink, mAutoUpdatePeriod);
            }
        }
    }

    public static void shrinkStart() {
        handler4Parking.removeCallbacks(runnable4Parking);
        handler4Shrink.postDelayed(runnable4Shrink, 180);
        Log.i("Suhao.Shrink", "shrinkStart() delay = " + 180);
    }

    public static void stop() {
        Log.i("Suhao.TransParent", "AnimationParking.stop()");
        AnimationTransparent.stop();
        handler4Parking.removeCallbacks(runnable4Parking);
        handler4Shrink.removeCallbacks(runnable4Shrink);
        handler4Turning.removeCallbacks(runnable4Turning);
        mTimeOut = false;
        handler4PosCheck.postDelayed(runnable4PosCheck, mVelocityTime);
    }

    private static void parking() {
        Log.d("Suhao", "parking baseX = " + baseX);
        if (baseX < Until.MID_LINE) {
            parking2Margin(true);
        } else {
            parking2Margin(false);
        }
        Log.d("MBack", "baseX = " + baseX);
    }

    private static void parking2Margin(boolean side) {
        int margin = Until.EXPEND_LINE;
        if (!side) {
            margin = (Until.SCREEM_WIDTH - Until.IMAGE_WIDTH) - Until.EXPEND_LINE;
        }
        int step = mStep;
        if (baseX > margin) {
            step = -mStep;
        }
        baseX += step;
        updateAll(baseX, baseY);
        if (Math.abs(baseX - margin) <= 10) {
            baseX = margin;
            updateAll(baseX, baseY);
            handler4Parking.removeCallbacks(runnable4Parking);
            handler4Turning.postDelayed(runnable4Turning, mParking2Shrink);
            return;
        }
        handler4Shrink.removeCallbacks(runnable4Shrink);
        handler4Parking.postDelayed(runnable4Parking, mAutoUpdatePeriod);
    }

    public static void updateAll(int x, int y) {
        if (x < 0) {
            x = 0;
        }
        if (x > Until.SCREEM_WIDTH - Until.IMAGE_WIDTH) {
            x = Until.SCREEM_WIDTH - Until.IMAGE_WIDTH;
        }
        if (y < 0) {
            y = 0;
        }
        ((MeterBase) MeterBase.MeterMap.get(MeterBack.NAME)).update(x, y);
        MeterBase meterBase = (MeterBase) MeterBase.MeterMap.get(MeterBack.NAME);
        MeterBase.baseX = x;
        meterBase = (MeterBase) MeterBase.MeterMap.get(MeterBack.NAME);
        MeterBase.baseY = y;
        if (true == mOriginSide) {
            updateAllLeft(x, y);
        } else {
            updateAllRight(x, y);
        }
    }

    private static void showOrHide(int x) {
        if (velocityCheck) {
            showSub();
        } else {
            hideSub();
        }
        if (mAreaChanged) {
            hideSub();
        }
        if (x > Until.SCREEM_WIDTH - Until.IMAGE_WIDTH || x < 0) {
            hideSub();
        }
    }

    private static void updateTop(int x, int y) {
        if (y < ((int) (0.707d * ((double) Until.IMAGE_WIDTH)))) {
            if (true == mOriginSide) {
                if (x >= Until.PARKING_LINE && x < Until.MID_LINE) {
                    y = Until.EXPEND_LINE / 2;
                    baseX = x;
                    baseY = y;
                    updateAll(x, y);
                }
            } else if (x <= Until.SCREEM_WIDTH - Until.PARKING_LINE && x > Until.MID_LINE) {
                y = Until.EXPEND_LINE / 2;
                baseX = x;
                baseY = y;
                updateAll(x, y);
            }
        }
    }

    private static void updateBottom(int x, int y) {
        if (y <= Until.BOTTOM_LINE) {
            Log.i("Bottom", "return");
        } else if (true == mOriginSide) {
            if (x >= Until.PARKING_LINE && x < Until.MID_LINE) {
                Log.i("Bottom", "LEFT bar = " + Until.STATUS_HEIGHT);
                y = Until.BOTTOM_LINE;
                baseX = x;
                baseY = y;
                updateAll(x, y);
            }
        } else if (x <= Until.SCREEM_WIDTH - Until.PARKING_LINE && x > Until.MID_LINE) {
            Log.i("Bottom", "RIGHT");
            y = Until.BOTTOM_LINE;
            baseX = x;
            baseY = y;
            updateAll(x, y);
        }
    }

    private static void updateAllRight(int x, int y) {
        int offset = Until.IMAGE_WIDTH / 40;
        showOrHide(x);
        if (x > Until.EXPEND_LINE_RIGHT) {
            recentX = Until.EXPEND_LINE + x;
            recentY = y;
            homeX = ((Until.SCREEM_WIDTH + x) - Until.IMAGE_WIDTH) / 2;
            homeY = y - (((Until.SCREEM_WIDTH - Until.IMAGE_WIDTH) - x) / 2);
            menuX = ((Until.SCREEM_WIDTH + x) - Until.IMAGE_WIDTH) / 2;
            menuY = (((Until.SCREEM_WIDTH - Until.IMAGE_WIDTH) - x) / 2) + y;
        } else if (x > Until.MID_LINE) {
            recentX = Until.EXPEND_LINE + x;
            recentY = y;
            homeX = (Until.EXPEND_LINE / 2) + x;
            homeY = y - (Until.EXPEND_LINE / 2);
            menuX = (Until.EXPEND_LINE / 2) + x;
            menuY = (Until.EXPEND_LINE / 2) + y;
        } else if (x > Until.MID_LINE - Until.SHRINK_LINE) {
            recentX = (Until.EXPEND_LINE + x) + ((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE));
            recentY = y;
            homeX = ((Until.EXPEND_LINE / 2) + x) + (((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE)) / 2);
            homeY = (y - (Until.EXPEND_LINE / 2)) - (((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE)) / 2);
            menuX = ((Until.EXPEND_LINE / 2) + x) + (((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE)) / 2);
            menuY = ((Until.EXPEND_LINE / 2) + y) + (((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE)) / 2);
        } else if (!mOriginSide) {
            mAreaChanged = true;
            recentX = x;
            recentY = y;
            homeX = x;
            homeY = y;
            menuX = x;
            menuY = y;
            hideSub();
        }
        ((MeterBase) MeterBase.MeterMap.get(MeterRecent.NAME)).update(recentX, recentY);
        ((MeterBase) MeterBase.MeterMap.get(MeterHome.NAME)).update(homeX, homeY);
        ((MeterBase) MeterBase.MeterMap.get(MeterMenu.NAME)).update(menuX, menuY);
    }

    private static void posCalculateLeftX(int x, int y) {
        if (x < Until.EXPEND_LINE) {
            recentX = x - Until.EXPEND_LINE;
            recentY = y;
            homeX = x / 2;
            homeY = y - (x / 2);
            menuX = x / 2;
            menuY = (x / 2) + y;
        } else if (x < Until.MID_LINE) {
            recentX = x - Until.EXPEND_LINE;
            recentY = y;
            homeX = x - (Until.EXPEND_LINE / 2);
            homeY = y - (Until.EXPEND_LINE / 2);
            menuX = x - (Until.EXPEND_LINE / 2);
            menuY = (Until.EXPEND_LINE / 2) + y;
        } else if (x < Until.MID_LINE + Until.SHRINK_LINE) {
            Log.i("Park", "Left shrink x=" + x);
            recentX = (x - Until.EXPEND_LINE) + ((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE));
            recentY = y;
            homeX = (x - (Until.EXPEND_LINE / 2)) + (((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE)) / 2);
            homeY = (y - (Until.EXPEND_LINE / 2)) + (((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE)) / 2);
            menuX = (x - (Until.EXPEND_LINE / 2)) + (((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE)) / 2);
            menuY = ((Until.EXPEND_LINE / 2) + y) - (((Until.EXPEND_LINE / Until.SHRINK_LINE) * (x - Until.MID_LINE)) / 2);
        } else if (true == mOriginSide) {
            mAreaChanged = true;
            recentX = x;
            recentY = y;
            homeX = x;
            homeY = y;
            menuX = x;
            menuY = y;
            hideSub();
        }
    }

    private static void updateAllLeft(int x, int y) {
        showOrHide(x);
        posCalculateLeftX(x, y);
        ((MeterBase) MeterBase.MeterMap.get(MeterRecent.NAME)).update(recentX, recentY);
        ((MeterBase) MeterBase.MeterMap.get(MeterHome.NAME)).update(homeX, homeY);
        ((MeterBase) MeterBase.MeterMap.get(MeterMenu.NAME)).update(menuX, menuY);
    }

    private static void hideSub() {
        ((MeterBase) MeterBase.MeterMap.get(MeterRecent.NAME)).setVisibility(View.INVISIBLE);
        ((MeterBase) MeterBase.MeterMap.get(MeterHome.NAME)).setVisibility(View.INVISIBLE);
        ((MeterBase) MeterBase.MeterMap.get(MeterMenu.NAME)).setVisibility(View.INVISIBLE);
    }

    private static void showSub() {
        ((MeterBase) MeterBase.MeterMap.get(MeterRecent.NAME)).setVisibility(View.VISIBLE);
        ((MeterBase) MeterBase.MeterMap.get(MeterHome.NAME)).setVisibility(View.VISIBLE);
        ((MeterBase) MeterBase.MeterMap.get(MeterMenu.NAME)).setVisibility(View.VISIBLE);
    }

    private static void turning() {
        handler4Shrink.postDelayed(runnable4Shrink, mAutoUpdatePeriod);
        handler4Turning.removeCallbacks(runnable4Turning);
    }

    private static void shrinking() {
        int step = 10;
        if (baseX < Until.MID_LINE) {
            step = -10;
        }
        baseX += step;
        Log.i("Suhao", "shrinking x= " + baseX);
        updateAll(baseX, baseY);
        if (baseX > Until.SCREEM_WIDTH - Until.IMAGE_WIDTH) {
            baseX = Until.SCREEM_WIDTH - Until.IMAGE_WIDTH;
            updateAll(baseX, baseY);
            velocityCheck = false;
            mOriginSide = false;
            mAreaChanged = false;
            handler4Shrink.removeCallbacks(runnable4Shrink);
            Log.i("Suhao.TransParent", "AnimationParking.shrinking(), baseX>SCREEN_WIDTH-IMAGE_WIDTH");
            AnimationTransparent.start();
        } else if (baseX <= 1) {
            baseX = 0;
            updateAll(baseX, baseY);
            velocityCheck = false;
            mOriginSide = true;
            mAreaChanged = false;
            handler4Shrink.removeCallbacks(runnable4Shrink);
            Log.i("Suhao.TransParent", "AnimationParking.shrinking(), baseX<1");
            AnimationTransparent.start();
        } else {
            handler4Shrink.postDelayed(runnable4Shrink, mAutoUpdatePeriod);
        }
    }

    public static void land() {
        if (!mOriginSide) {
            stop();
            baseX = Until.SCREEM_WIDTH - Until.IMAGE_WIDTH;
            updateAll(baseX, baseY);
        }
        MeterBase meterBase = (MeterBase) MeterBase.MeterMap.get(MeterBack.NAME);
        if (MeterBase.baseY > Until.SCREEM_HEIGHT) {
            updateBottom(baseX, baseY);
        }
    }
}
