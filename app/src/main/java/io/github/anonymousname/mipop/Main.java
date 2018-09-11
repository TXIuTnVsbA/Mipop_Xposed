package io.github.anonymousname.mipop;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XModuleResources;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import io.github.anonymousname.mipop.widget.MeterBack;
import io.github.anonymousname.mipop.widget.MeterHome;
import io.github.anonymousname.mipop.widget.MeterMenu;
import io.github.anonymousname.mipop.widget.MeterRecent;
import io.github.anonymousname.mipop.widget.Until;

public class Main implements IXposedHookLoadPackage,IXposedHookZygoteInit, IXposedHookInitPackageResources {
    private static String MODULE_PATH = null; //模块所在路径
    public static int idBack = 0;
    public static int idBackPressed=0;
    public static int idHome = 0;
    public static int idHomePressed = 0;
    public static int idHomeSelector = 0;
    public static int idMenu = 0;
    public static int idMenuSelector = 0;
    public static int idMenuPressed = 0;
    public static int idRecent = 0;
    public static int idRecentSelector = 0;
    public static int idRecentPressed = 0;
    private static final String appName = "com.android.systemui";
    private static final String packageName = "com.android.systemui";
    private static final String className = "SystemBars";
    private static final String methodName = "start";
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        //XposedBridge.log("initZygote");
        MODULE_PATH = startupParam.modulePath;//获取模块apk文件在储存中的位置
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        if(resparam.packageName.equals(appName)){
            //XposedBridge.log("handleInitPackageResources");
            XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
            idBack = resparam.res.addResource(modRes, R.drawable.back);
            idBackPressed = resparam.res.addResource(modRes, R.drawable.back_pressed);
            idHome = resparam.res.addResource(modRes, R.drawable.home);
            idHomeSelector = resparam.res.addResource(modRes, R.drawable.home_selector);
            idHomePressed = resparam.res.addResource(modRes, R.drawable.home_pressed);
            idMenu = resparam.res.addResource(modRes, R.drawable.menu);
            idMenuSelector = resparam.res.addResource(modRes, R.drawable.menu_selector);
            idMenuPressed = resparam.res.addResource(modRes, R.drawable.menu_pressed);
            idRecent = resparam.res.addResource(modRes, R.drawable.recent);
            idRecentPressed = resparam.res.addResource(modRes, R.drawable.recent_pressed);
            idRecentSelector = resparam.res.addResource(modRes, R.drawable.recent_selector);
        }

    }
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(lpparam.packageName.equals(appName)){
            Class clazz = null;
            clazz = XposedHelpers.findClassIfExists(packageName + "." + className,lpparam.classLoader);
            if(clazz == null){
                clazz = XposedHelpers.findClassIfExists(packageName + ".statusbar." + className,lpparam.classLoader);
            }
//            final Class clazz = lpparam.classLoader.loadClass(packageName + "." + className);
            XposedHelpers.findAndHookMethod(clazz, methodName,new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                    Until.initialPop(context);
                    new MeterMenu(context);
                    new MeterRecent(context);
                    new MeterHome(context);
                    new MeterBack(context);
                }
            });
        }
    }
}
