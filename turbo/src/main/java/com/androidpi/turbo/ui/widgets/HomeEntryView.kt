package com.androidpi.turbo.ui.widgets

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.androidpi.turbo.R

/**
 * Created on 2019-12-12.
 */
class HomeEntryView @TargetApi(Build.VERSION_CODES.LOLLIPOP) constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    @JvmOverloads
    constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : this(context, attrs, defStyleAttr, 0) {
       View.inflate(context, R.layout.view_home_entry, this)
        orientation = VERTICAL
    }
}