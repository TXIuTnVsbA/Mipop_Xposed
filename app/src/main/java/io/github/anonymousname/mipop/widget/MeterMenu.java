package io.github.anonymousname.mipop.widget;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import io.github.anonymousname.mipop.Main;


public class MeterMenu extends MeterBase {
    public static final String NAME = MeterMenu.class.getSimpleName();

    public MeterMenu(Context context) {
        super(context);
        Register(NAME, this);
        setSoundEffectsEnabled(true);
        setImageResource(Main.idMenuSelector);
        setResId(Main.idMenu, Main.idMenuPressed);
    }

    public void Click() {
        Log.i("CLICK", "menu click");
        playSoundEffect(0);
        new Thread() {
            public void run() {
                try {
                    //EventBus.getDefault().post(MyAccessibilityService.MENU);
                    //Until.runRootCommand("input keyevent 82");
                    new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
                    Log.i("shenzhan", "MENU implement");
                } catch (Exception e) {
                    Log.d("HouJiong", e.toString());
                }
            }
        }.start();
    }

    public void LongClick() {
        Log.i("Suhao", "menu  long click");
    }
}
