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
package com.androidpi.turbo

import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.androidpi.turbo.service.ProfileService
import com.androidpi.turbo.service.TurboService
import com.androidpi.turbo.ui.activity.TurboActivity
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.*

class Turbo {
    var context: Context? = null
    var handler = Handler()
    private fun init(context: Context) {
        this.context = context.applicationContext
        // Turbo
        val title = context.getString(R.string.title_activity_turbo)
        shortcutDel(title)
        shortcutAdd(title)
        if (!BuildConfig.DEBUG) {
            return
        }
        if (context !is Application) {
            Toast.makeText(context, R.string.not_in_application_context, Toast.LENGTH_SHORT).show()
        } else {
            (context as Application).registerActivityLifecycleCallbacks(lifecycleCallbacks)
        }
        if (Timber.treeCount() == 0) {
            Timber.plant(DebugTree())
        }
        handler.post {
            TurboService.start(context)
            ProfileService.start(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(context)) {
                    NavCenter.navToFloatingWindowManager(context, REQ_CODE_OVERLAY)
                }
            }
            val window = FloatingDotWindow(context)
            window.show()
        }
    }



    private fun shortcutAdd(name: String) {
        val shortcutIntent = Intent(context, TurboActivity::class.java)
        shortcutIntent.action = Intent.ACTION_MAIN
        var bitmap: Bitmap? = null
        try {
            val pm = context!!.packageManager
            val turbo =
                pm.getActivityInfo(shortcutIntent.component, 0).loadIcon(pm)
            val icon = context!!.applicationInfo.loadIcon(pm)
            // Create bitmap with number in it -> very default. You probably want to give it a more
            // stylish look
            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            turbo.setBounds(0, 0, 100, 100)
            turbo.draw(canvas)
            val bounds = Rect()
            bounds[0, 0, 50] = 50
            icon.bounds = bounds
            icon.draw(canvas)
            // Decorate the shortcut
            val addIntent = Intent()
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name)
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap)
            // Inform launcher to create shortcut
            addIntent.action = "com.android.launcher.action.INSTALL_SHORTCUT"
            context!!.sendBroadcast(addIntent)
            addDynamicShortcut(name, shortcutIntent)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e)
        } finally {
            bitmap?.recycle()
        }
    }

    private fun addDynamicShortcut(name: String, shortcutIntent: Intent) {
        val pm = context!!.packageManager
        val main = pm.getLaunchIntentForPackage(context!!.packageName)
        if (main != null && main.component != null) {
            val shortcutInfoCompat = ShortcutInfoCompat.Builder(context!!, name)
                .setActivity(main.component)
                .setIcon(
                    IconCompat.createWithResource(
                        context, R.mipmap.ic_turbo_launcher
                    )
                )
                .setIntent(shortcutIntent)
                .setShortLabel(name)
                .build()
            val shortcutInfoCompatList: MutableList<ShortcutInfoCompat> =
                ArrayList()
            shortcutInfoCompatList.add(shortcutInfoCompat)
            ShortcutManagerCompat.addDynamicShortcuts(context!!, shortcutInfoCompatList)
        }
    }

    private fun addPinShortcut(name: String, shortcutIntent: Intent) {
        val pm = context!!.packageManager
        val main = pm.getLaunchIntentForPackage(context!!.packageName)
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context!!)) { // Assumes there's already a shortcut with the ID "my-shortcut".
            // The shortcut must be enabled.
            val pinShortcutInfo =
                ShortcutInfoCompat.Builder(context!!, "pin$name")
                    .setIcon(
                        IconCompat.createWithResource(
                            context, R.mipmap.ic_turbo_launcher
                        )
                    )
                    .setIntent(shortcutIntent)
                    .setShortLabel("pin$name")
                    .build()
            // Create the PendingIntent object only if your app needs to be notified
            // that the user allowed the shortcut to be pinned. Note that, if the
            // pinning operation fails, your app isn't notified. We assume here that the
            // app has implemented a method called createShortcutResultIntent() that
            // returns a broadcast intent.
            val pinnedShortcutCallbackIntent =
                ShortcutManagerCompat.createShortcutResultIntent(context!!, pinShortcutInfo)
            // Configure the intent so that your app's broadcast receiver gets
            // the callback successfully.For details, see PendingIntent.getBroadcast().
            val successCallback = PendingIntent.getBroadcast(
                context,  /* request code */
                0,
                pinnedShortcutCallbackIntent,  /* flags */
                0
            )
            ShortcutManagerCompat.requestPinShortcut(
                context!!, pinShortcutInfo, successCallback.intentSender
            )
        }
    }

    private fun shortcutDel(name: String) { // Intent to be send, when shortcut is pressed by user ("launched")
        val shortcutIntent = Intent(context, TurboActivity::class.java)
        shortcutIntent.action = Intent.ACTION_MAIN
        // Decorate the shortcut
        val delIntent = Intent()
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name)
        // Inform launcher to remove shortcut
        delIntent.action = "com.android.launcher.action.UNINSTALL_SHORTCUT"
        context!!.sendBroadcast(delIntent)
    }

    val lifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityPaused(activity: Activity?) {

        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }
    }

    companion object {
        const val REQ_CODE_OVERLAY = 1001
        @Volatile
        private var instance: Turbo? = null

        fun instance(): Turbo? {
            if (instance == null) {
                synchronized(Turbo::class.java) {
                    if (instance == null) {
                        instance = Turbo()
                    }
                }
            }
            return instance
        }

        fun install(context: Context) {
            instance()!!.init(context)
        }
    }
}