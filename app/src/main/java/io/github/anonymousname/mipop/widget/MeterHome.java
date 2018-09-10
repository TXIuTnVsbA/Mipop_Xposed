package io.github.anonymousname.mipop.widget;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import io.github.anonymousname.mipop.Main;

public class MeterHome extends MeterBase {
    public static final String NAME = MeterHome.class.getSimpleName();

    public MeterHome(Context context) {
        super(context);
        Register(NAME, this);
        setSoundEffectsEnabled(true);
        setImageResource(Main.idHomeSelector);
        setResId(Main.idHome, Main.idHomePressed);
    }

    public void Click() {
        Log.i("CLICK", "home   click");
        playSoundEffect(0);
        new Thread() {
            public void run() {
                try {
                    //Until.runRootCommand("input keyevent 3");
                    //EventBus.getDefault().post(MyAccessibilityService.HOME);
                    new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_HOME);
                    Log.i("shenzhan", "Home implement");
                } catch (Exception e) {
                    Log.d("shenzhan", e.toString());
                }
            }
        }.start();
    }

    public void LongClick() {
        Log.i("Suhao", "home  long click");
    }
}
