package com.androidpi.turbo.utils

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.Settings

/**
 * Created on 2019-12-13.
 */
object AppHelper {

    fun appLabel(context: Context?) : CharSequence? {
        val pm = packageManager(context)
        return pm?.getApplicationLabel(context?.applicationInfo)
    }

    fun appIcon(context: Context?) : Drawable? {
        val pm = packageManager(context)
        return pm?.getApplicationIcon(context?.applicationInfo)
    }

    private fun packageManager(context: Context?) = context?.packageManager

    fun systemSettingsIcon(context: Context?) : Drawable? {
        return packageManager(context)?.getActivityIcon(Intent(Settings.ACTION_SETTINGS))
    }

}