package com.androidpi.turbo

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RECENT_IGNORE_UNAVAILABLE
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.androidpi.app.common.utils.AppUtils
import com.androidpi.app.common.utils.ToastUtils

/**
 * Created on 2019-12-12.
 */

object NavCenter {

    fun navToApp(context: Context?) {
        val mainIntent = launchIntent(context)
        val mainComponent = launchIntent(context)?.component
        val am = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var infos = ArrayList<ActivityManager.RecentTaskInfo>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            am.appTasks.forEach {
                infos.add(it.taskInfo)
            }
        } else {
            val tasks = am.getRecentTasks(Int.MAX_VALUE, RECENT_IGNORE_UNAVAILABLE)
            infos.addAll(tasks)
        }
        infos.forEach {
            if (AppUtils.has23()) {
                if (it.baseActivity.equals(mainComponent)) {
                    am.moveTaskToFront(it.id, 0)
                    return@forEach
                }
            } else {
                if (it.baseIntent.equals(mainIntent)) {
                    am.moveTaskToFront(it.id, 0)
                    return@forEach
                }
            }
        }
    }

    private fun launchIntent(context: Context?) =
        packageManager(context)?.getLaunchIntentForPackage(context?.packageName)

    fun navToAppDetailSettings(context: Context?) {
        if (context == null) {
            ToastUtils.shortToast(Turbo.instance()?.context, "空的")
            return
        }
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(
                    Uri.fromParts(
                        "package",
                        context.packageName,
                        null
                    )
                )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun navToFloatingWindowManager(context: Context, requestCode: Int) {
        val intent =
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:" + context.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (intent.resolveActivity(context.packageManager) != null) {
            if (context is Activity) {
                context.startActivityForResult(
                    intent,
                    requestCode
                )
            } else {
                context.startActivity(intent)
            }
        }
    }

    fun navToDeveloperOptions(context: Context?) {
        val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
        context?.startActivity(intent)
    }

    fun navToSystemSettings(context: Context?) {
        val pm = packageManager(context)
        val intent = Intent(Settings.ACTION_SETTINGS)
        context?.startActivity(intent)
        pm?.getActivityIcon(intent)
    }

    private fun packageManager(context: Context?) = context?.packageManager
}