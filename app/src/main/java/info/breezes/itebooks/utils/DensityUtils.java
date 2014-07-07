package info.breezes.itebooks.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * liubinjiang
 */
public class DensityUtils {
    public static int dp2px(Context context, float dpValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (dpValue * dm.density + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (pxValue / dm.density + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        window.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        window.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
