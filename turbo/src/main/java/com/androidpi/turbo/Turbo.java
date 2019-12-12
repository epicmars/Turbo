/*
 * Copyright 2018 yinpinjiu@gmail.com
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
package com.androidpi.turbo;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.androidpi.turbo.service.ProfileService;
import com.androidpi.turbo.service.TurboService;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class Turbo {

    public static final int REQ_CODE_OVERLAY = 1001;
    private Context context;
    private static volatile Turbo instance;

    public static Turbo instance() {
        if (instance == null) {
            synchronized (Turbo.class) {
                if (instance == null) {
                    instance = new Turbo();
                }
            }
        }
        return instance;
    }

    public static void install(Context context) {
        instance().init(context);
    }

    private void init(Context context) {
        this.context = context;

        //

        // Turbo
        String title = context.getString(R.string.title_activity_turbo);
        shortcutDel(title);
        shortcutAdd(title);
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (!(context instanceof Application)) {
            Toast.makeText(context, R.string.not_in_application_context, Toast.LENGTH_SHORT).show();
        }
        if (Timber.treeCount() == 0) {
            Timber.plant(new Timber.DebugTree());
        }
        TurboService.start(context);
        ProfileService.start(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    if (context instanceof Activity) {
                        ((Activity) context).startActivityForResult(intent, REQ_CODE_OVERLAY);
                    } else {
                        context.startActivity(intent);
                    }
                }
            }
        }
        FloatingDotWindow window = new FloatingDotWindow(context);
        window.show();
    }

    private void shortcutAdd(String name) {
        Intent shortcutIntent = new Intent(context, TurboActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Bitmap bitmap = null;

        try {
            PackageManager pm = context.getPackageManager();

            Drawable turbo = pm.getActivityInfo(shortcutIntent.getComponent(), 0).loadIcon(pm);
            Drawable icon = context.getApplicationInfo().loadIcon(pm);

            // Create bitmap with number in it -> very default. You probably want to give it a more
            // stylish look
            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            turbo.setBounds(0, 0, 100, 100);
            turbo.draw(canvas);

            Rect bounds = new Rect();
            bounds.set(0, 0, 50, 50);
            icon.setBounds(bounds);
            icon.draw(canvas);

            // Decorate the shortcut
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);

            // Inform launcher to create shortcut
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(addIntent);

            addDynamicShortcut(name, shortcutIntent);
            addPinShortcut(name, shortcutIntent);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    private void addDynamicShortcut(String name, Intent shortcutIntent) {
        PackageManager pm = context.getPackageManager();
        Intent main = pm.getLaunchIntentForPackage(context.getPackageName());
        if (main != null && main.getComponent() != null) {
            ShortcutInfoCompat shortcutInfoCompat =
                    new ShortcutInfoCompat.Builder(context, name)
                            .setActivity(main.getComponent())
                            .setIcon(
                                    IconCompat.createWithResource(
                                            context, R.mipmap.ic_turbo_launcher))
                            .setIntent(shortcutIntent)
                            .setShortLabel(name)
                            .build();
            List<ShortcutInfoCompat> shortcutInfoCompatList = new ArrayList<>();
            shortcutInfoCompatList.add(shortcutInfoCompat);
            ShortcutManagerCompat.addDynamicShortcuts(context, shortcutInfoCompatList);
        }
    }

    private void addPinShortcut(String name, Intent shortcutIntent) {
        PackageManager pm = context.getPackageManager();
        Intent main = pm.getLaunchIntentForPackage(context.getPackageName());
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            // Assumes there's already a shortcut with the ID "my-shortcut".
            // The shortcut must be enabled.
            ShortcutInfoCompat pinShortcutInfo =
                    new ShortcutInfoCompat.Builder(context, "pin" + name)
                            .setIcon(
                                    IconCompat.createWithResource(
                                            context, R.mipmap.ic_turbo_launcher))
                            .setIntent(shortcutIntent)
                            .setShortLabel("pin" + name)
                            .build();

            // Create the PendingIntent object only if your app needs to be notified
            // that the user allowed the shortcut to be pinned. Note that, if the
            // pinning operation fails, your app isn't notified. We assume here that the
            // app has implemented a method called createShortcutResultIntent() that
            // returns a broadcast intent.
            Intent pinnedShortcutCallbackIntent =
                    ShortcutManagerCompat.createShortcutResultIntent(context, pinShortcutInfo);

            // Configure the intent so that your app's broadcast receiver gets
            // the callback successfully.For details, see PendingIntent.getBroadcast().
            PendingIntent successCallback =
                    PendingIntent.getBroadcast(
                            context, /* request code */
                            0,
                            pinnedShortcutCallbackIntent, /* flags */
                            0);

            ShortcutManagerCompat.requestPinShortcut(
                    context, pinShortcutInfo, successCallback.getIntentSender());
        }
    }

    private void shortcutDel(String name) {
        // Intent to be send, when shortcut is pressed by user ("launched")
        Intent shortcutIntent = new Intent(context, TurboActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        // Decorate the shortcut
        Intent delIntent = new Intent();
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // Inform launcher to remove shortcut
        delIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.sendBroadcast(delIntent);
    }
}
