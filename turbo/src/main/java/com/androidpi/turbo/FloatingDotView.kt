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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.androidpi.turbo.utils.AppProfileHelper
import timber.log.Timber

class FloatingDotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private val STATE_IDLE = 0
    private val STATE_DRAGGING = 1
    private val fab: ImageButton
    private var listener: OnDragListener? = null
    private val state = STATE_IDLE
    private var checked = false

    interface OnDragListener {
        fun onMove(x: Float, y: Float)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val actionMasked = ev.action and MotionEvent.ACTION_MASK
        when (actionMasked) {
            MotionEvent.ACTION_DOWN -> return false
            MotionEvent.ACTION_MOVE -> return true
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val actionMasked = ev.action and MotionEvent.ACTION_MASK
        when (actionMasked) {
            MotionEvent.ACTION_DOWN -> return false
            MotionEvent.ACTION_MOVE -> {
                val x = ev.rawX
                val y = ev.rawY
                Timber.i("raw: x %.2f y %.2f", x, y)
                if (listener != null) {
                    listener!!.onMove(x, y)
                }
                return true
            }
        }
        return super.onTouchEvent(ev)
    }

    fun setListener(listener: OnDragListener?) {
        this.listener = listener
    }

    init {
        View.inflate(context, R.layout.floating_dot, this)
        fab = findViewById(R.id.fab)
        val icon = AppProfileHelper.getIconDrawable(context)
        if (icon != null) {
            fab.setImageDrawable(icon)
        }
        fab.setOnClickListener {
            checked = if (checked) {
                false
            } else {
                navToAppDetailSettings(getContext())
                true
            }
        }
        fab.setOnLongClickListener { false }
    }

    private fun navToAppDetailSettings(context: Context) {
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(
                    Uri.fromParts(
                        "package",
                        getContext().packageName,
                        null
                    )
                )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}