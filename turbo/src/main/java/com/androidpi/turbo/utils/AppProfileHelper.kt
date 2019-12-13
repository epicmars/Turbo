package com.androidpi.turbo.utils

import android.content.Context
import android.graphics.drawable.Drawable

/**
 * Created on 2018/12/5
 */
object AppProfileHelper {
    fun getIconDrawable(context: Context): Drawable {
        val iconRes = context.applicationInfo.icon
        return if (iconRes == 0) {
            context.applicationInfo.loadIcon(context.packageManager)
        } else context.resources.getDrawable(iconRes)
    }
}