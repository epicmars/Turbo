package com.androidpi.turbo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by yinhang on 2018/12/5
 */
public class AppProfileHelper {

    public static Drawable getIconDrawable(Context context) {
        int iconRes = context.getApplicationInfo().icon;
        if (iconRes == 0) {
            return context.getApplicationInfo().loadIcon(context.getPackageManager());
        }
        return context.getResources().getDrawable(iconRes);
    }
}
