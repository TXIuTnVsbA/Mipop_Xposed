package io.github.anonymousname.mipop.animation;

import android.os.Handler;
import android.util.Log;

import io.github.anonymousname.mipop.widget.MeterBack;
import io.github.anonymousname.mipop.widget.MeterBase;
import io.github.anonymousname.mipop.widget.MeterHome;
import io.github.anonymousname.mipop.widget.MeterMenu;
import io.github.anonymousname.mipop.widget.MeterRecent;

public class AnimationTransparent {
    private static int currentAlpha = 255;
    private static int endAlpha = 100;
    private static Handler handler4Transparent = new Handler();
    private static int periodTime = 10;
    private static Runnable runnable4Transparent = new Runnable() {
        public void run() {
            AnimationTransparent.transparenting();
        }
    };
    private static int startAlpha = 255;
    private static int steps = 0;
    private static long time4Trans = 2000;

    public static void start() {
        Log.i("Suhao.TransParent", "AnimationTransparent.start()");
        periodTime = (int) (time4Trans / ((long) (endAlpha - startAlpha)));
        handler4Transparent.postDelayed(runnable4Transparent, 1);
        ((MeterBase) MeterBase.MeterMap.get(MeterHome.NAME)).setVisibility(8);
        ((MeterBase) MeterBase.MeterMap.get(MeterMenu.NAME)).setVisibility(8);
        ((MeterBase) MeterBase.MeterMap.get(MeterRecent.NAME)).setVisibility(8);
    }

    public static void stop() {
        Log.i("Suhao.TransParent", "AnimationTransparent.stop()");
        currentAlpha = startAlpha;
        handler4Transparent.removeCallbacks(runnable4Transparent);
        ((MeterBase) MeterBase.MeterMap.get(MeterBack.NAME)).setAlpha(startAlpha);
        ((MeterBase) MeterBase.MeterMap.get(MeterHome.NAME)).setVisibility(0);
        ((MeterBase) MeterBase.MeterMap.get(MeterMenu.NAME)).setVisibility(0);
        ((MeterBase) MeterBase.MeterMap.get(MeterRecent.NAME)).setVisibility(0);
    }

    private static void transparenting() {
        Log.i("Suhao.TransParent", "AnimationTransparent.transparenting(), alpha = " + currentAlpha);
        if (currentAlpha <= endAlpha) {
            Log.i("Suhao.TransParent", "AnimationTransparent.transparenting(), removeCallbacks");
            handler4Transparent.removeCallbacks(runnable4Transparent);
            return;
        }
        currentAlpha--;
        ((MeterBase) MeterBase.MeterMap.get(MeterBack.NAME)).setAlpha(currentAlpha);
        handler4Transparent.postDelayed(runnable4Transparent, (long) periodTime);
    }
}
