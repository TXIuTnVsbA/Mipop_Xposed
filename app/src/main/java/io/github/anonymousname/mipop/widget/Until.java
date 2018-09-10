package io.github.anonymousname.mipop.widget;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class Until {
    public static int BOTTOM_LINE;
    public static int EXPEND_LINE;
    public static int EXPEND_LINE_RIGHT;
    public static int EXPEND_OFFSET ;
    public static int IMAGE_WIDTH;
    public static int MID_LINE;
    public static int MOVE_MAX_SIZE;
    public static int PARKING_LINE;
    public static int PARKING_LINE_RIGHT;
    public static int SCREEM_HEIGHT = 840;
    public static int SCREEM_WIDTH = 480;
    public static int SHRINK_LINE;
    public static int STATUS_HEIGHT = 10;
    private static int expend_offset_factor = 10;
    private static int image_height_factor = 6;
    private static int mid_line_factor = 2;
    private static int move_max_factor = 5;
    private static float parking_factor = 0.707f;
    private static int shrink_factor = 2;
    static {
        BOTTOM_LINE = (((SCREEM_HEIGHT - STATUS_HEIGHT) - (EXPEND_LINE / 2)) - (IMAGE_WIDTH / 2));
        EXPEND_LINE = (((int) (1.414d * ((double) IMAGE_WIDTH))) + EXPEND_OFFSET);
        EXPEND_LINE_RIGHT = ((SCREEM_WIDTH - IMAGE_WIDTH) - EXPEND_LINE);
        EXPEND_OFFSET = (IMAGE_WIDTH / expend_offset_factor);
        IMAGE_WIDTH = (SCREEM_WIDTH / image_height_factor);
        MID_LINE = ((SCREEM_WIDTH - IMAGE_WIDTH) / mid_line_factor);
        MOVE_MAX_SIZE = (IMAGE_WIDTH / move_max_factor);
        PARKING_LINE = ((int) (parking_factor * ((float) IMAGE_WIDTH)));
        PARKING_LINE_RIGHT = ((SCREEM_WIDTH - IMAGE_WIDTH) - PARKING_LINE);
        SHRINK_LINE = (IMAGE_WIDTH / shrink_factor);
    }
    public static void init(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEM_WIDTH = dm.widthPixels;
        SCREEM_HEIGHT = dm.heightPixels;
        IMAGE_WIDTH = (Math.min(SCREEM_WIDTH, SCREEM_HEIGHT) * 125) / 720;
    }

    public static void initialPop(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        SCREEM_WIDTH = dm.widthPixels;
        SCREEM_HEIGHT = dm.heightPixels;
        setStatusBarHeight(context);
        IMAGE_WIDTH = Math.min(SCREEM_WIDTH, SCREEM_HEIGHT) / image_height_factor;
        MID_LINE = (SCREEM_WIDTH - IMAGE_WIDTH) / mid_line_factor;
        MOVE_MAX_SIZE = IMAGE_WIDTH / move_max_factor;
        EXPEND_OFFSET = IMAGE_WIDTH / expend_offset_factor;
        EXPEND_LINE = ((int) (1.414d * ((double) IMAGE_WIDTH))) + EXPEND_OFFSET;
        SHRINK_LINE = IMAGE_WIDTH / shrink_factor;
        PARKING_LINE = (int) (parking_factor * ((float) IMAGE_WIDTH));
        BOTTOM_LINE = ((SCREEM_HEIGHT - STATUS_HEIGHT) - IMAGE_WIDTH) - (EXPEND_LINE / 2);
        EXPEND_LINE_RIGHT = (SCREEM_WIDTH - IMAGE_WIDTH) - EXPEND_LINE;
        PARKING_LINE_RIGHT = (SCREEM_WIDTH - IMAGE_WIDTH) - PARKING_LINE;
    }

    private static void setStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            STATUS_HEIGHT = context.getResources().getDimensionPixelSize(Integer.parseInt(c.getField("status_bar_height").get(c.newInstance()).toString()));
            Log.i("BAR", "sbar = " + STATUS_HEIGHT);
        } catch (Exception e1) {
            Log.i("BAR", "get status bar height fail");
            e1.printStackTrace();
        }
    }
}
