package com.androidpi.turbo.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.StringRes;

public class ToastUtils {

    public static final int TOAST_LENGTH_3S = 3000;

    public static void shortToast(Context context, String message) {
        toast(context, message, Toast.LENGTH_SHORT);
    }

    public static void shortToast(Context context, @StringRes int msgRes) {
        toast(context, msgRes, Toast.LENGTH_SHORT);
    }

    public static void longToast(Context context, String message) {
        toast(context, message, Toast.LENGTH_LONG);
    }

    public static void longToast(Context context, @StringRes int msgRes) {
        toast(context, msgRes, Toast.LENGTH_LONG);
    }

    public static void shortCenterToast(Context context, String message) {
        toast(context, message, TOAST_LENGTH_3S, Gravity.CENTER);
    }

    public static void shortCenterToast(Context context, @StringRes int msgRes) {
        toast(context, msgRes, TOAST_LENGTH_3S, Gravity.CENTER);
    }

    public static void longCenterToast(Context context, String message) {
        toast(context, message, Toast.LENGTH_LONG, Gravity.CENTER);
    }

    public static void longCenterToast(Context context, @StringRes int msgRes) {
        toast(context, msgRes, Toast.LENGTH_LONG, Gravity.CENTER);
    }

    public static void shortTopToast(Context context, String message) {
        toast(context, message, Toast.LENGTH_SHORT, Gravity.TOP);
    }

    public static void shortTopToast(Context context, @StringRes int msgRes) {
        toast(context, msgRes, Toast.LENGTH_SHORT, Gravity.TOP);
    }

    public static void longTopToast(Context context, String message) {
        toast(context, message, Toast.LENGTH_LONG, Gravity.TOP);
    }

    public static void longTopToast(Context context, @StringRes int msgRes) {
        toast(context, msgRes, Toast.LENGTH_LONG, Gravity.TOP);
    }

    public static void toast(Context context, String message, int duration) {
        if (context == null || TextUtils.isEmpty(message)) return;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public static void toast(
            Context context, @StringRes int msgRes, int duration) {
        if (context == null || msgRes == 0) return;
        Toast toast = Toast.makeText(context, msgRes, duration);
        toast.show();
    }

    public static void toast(Context context, String message, int duration, int gravity) {
        if (context == null || TextUtils.isEmpty(message)) return;
        Toast toast = Toast.makeText(context, message, duration);
        int yOffset = 0;
        if (gravity == Gravity.TOP) {
            yOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, context.getResources().getDisplayMetrics());
        }
        toast.setGravity(gravity, 0, yOffset);
        toast.show();
    }

    public static void toast(
            Context context, @StringRes int msgRes, int duration, int gravity) {
        if (msgRes == 0) return;
        Toast toast = Toast.makeText(context, msgRes, duration);
        int yOffset = 0;
        if (gravity == Gravity.TOP) {
            yOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, context.getResources().getDisplayMetrics());
        }
        toast.setGravity(gravity, 0, yOffset);
        toast.show();
    }
}