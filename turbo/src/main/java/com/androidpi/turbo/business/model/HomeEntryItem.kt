package com.androidpi.turbo.business.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes

/**
 * Created on 2019-12-12.
 */
class HomeEntryItem(var context: Context?, var command: Runnable?) : Runnable {
    var title: CharSequence? = null
    @StringRes
    var iconRes = 0
    var icon: Drawable? = null

    override fun run() {
        command?.run()
    }
}