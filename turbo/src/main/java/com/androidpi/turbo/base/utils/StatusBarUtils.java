package com.androidpi.turbo.base.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import com.androidpi.turbo.common.RomUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*
 * Copyright 2019 yinpinjiu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class StatusBarUtils {

    public static int getStatusBarHeight(Context context) {
        int result =
                Math.round(
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                24,
                                context.getResources().getDisplayMetrics()));
        int resourceId =
                context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private final static int VERSION_7 = 7;

    public static void setLightStatusBar(Activity activity, boolean dark) {
        setLightStatusBar(activity, false, false, false, dark);
    }

    public static void setLightStatusBar(Activity activity, boolean isMarginStatusBar
            , boolean isMarginNavigationBar, boolean isTransStatusBar, boolean dark) {
        switch (RomUtils.getLightStatusBarAvailableRomType()) {
            case RomUtils.AvailableRomType.MIUI:
                if (RomUtils.getMIUIVersionCode() >= VERSION_7) {
                    setAndroidNativeLightStatusBar(activity, isMarginStatusBar, isMarginNavigationBar, isTransStatusBar, dark);
                } else {
                    setMIUILightStatusBar(activity, isMarginStatusBar, isMarginNavigationBar, isTransStatusBar, dark);
                }
                break;

            case RomUtils.AvailableRomType.FLYME:
                setFlymeLightStatusBar(activity, isMarginStatusBar, isMarginNavigationBar, isTransStatusBar, dark);
                break;

            case RomUtils.AvailableRomType.ANDROID_NATIVE:
                setAndroidNativeLightStatusBar(activity, isMarginStatusBar, isMarginNavigationBar, isTransStatusBar, dark);
                break;

            case RomUtils.AvailableRomType.NA:
                // N/A do nothing
                break;
        }
    }


    private static boolean setMIUILightStatusBar(Activity activity, boolean isMarginStatusBar
            , boolean isMarginNavigationBar, boolean isTransStatusBar, boolean darkmode) {
        initStatusBarStyle(activity, isMarginStatusBar, isMarginNavigationBar);

        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            setAndroidNativeLightStatusBar(activity, isMarginStatusBar, isMarginNavigationBar, isTransStatusBar, darkmode);
        }
        return false;
    }

    private static boolean setFlymeLightStatusBar(Activity activity, boolean isMarginStatusBar
            , boolean isMarginNavigationBar, boolean isTransStatusBar, boolean dark) {
        boolean result = false;
        if (activity != null) {
            initStatusBarStyle(activity, isMarginStatusBar, isMarginNavigationBar);
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
                setAndroidNativeLightStatusBar(activity, isMarginStatusBar, isMarginNavigationBar, isTransStatusBar, dark);
            }
        }
        return result;
    }

    @TargetApi(11)
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean isMarginStatusBar
            , boolean isMarginNavigationBar, boolean isTransStatusBar, boolean isDarkStatusBarIcon) {

        try {
            if (isTransStatusBar) {
                Window window = activity.getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isMarginStatusBar && isMarginNavigationBar) {
                        //5.0版本及以上
                        if (isDarkStatusBarIcon && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        } else {
                            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                        }
                    } else if (!isMarginStatusBar && !isMarginNavigationBar) {

                        if (isDarkStatusBarIcon && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        } else {
                            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                        }


                    } else if (!isMarginStatusBar && isMarginNavigationBar) {
                        if (isDarkStatusBarIcon && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        } else {
                            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                        }


                    } else {
                        //留出来状态栏 不留出来导航栏 没找到办法。。
                        return;
                    }
                }
            } else {
                View decor = activity.getWindow().getDecorView();
                if (isDarkStatusBarIcon && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    // We want to change tint color to white again.
                    // You can also record the flags in advance so that you can turn UI back completely if
                    // you have set other flags before, such as translucent or full screen.
                    decor.setSystemUiVisibility(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initStatusBarStyle(Activity activity, boolean isMarginStatusBar
            , boolean isMarginNavigationBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (isMarginStatusBar && isMarginNavigationBar) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else if (!isMarginStatusBar && !isMarginNavigationBar) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else if (!isMarginStatusBar && isMarginNavigationBar) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    public static void clearLightStatusBar(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }
}
