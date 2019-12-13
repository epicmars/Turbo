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
import android.graphics.PixelFormat
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager

class FloatingDotWindow(context: Context) :
    FloatingDotView.OnDragListener {
    private val context: Context
    private val windowManager: WindowManager
    private val floatingDotView: FloatingDotView
    private var params: WindowManager.LayoutParams? = null
    fun show() {
        params = WindowManager.LayoutParams()
        params!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        params!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        params!!.gravity = Gravity.START or Gravity.TOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params!!.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        params!!.flags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        params!!.format = PixelFormat.TRANSLUCENT
        windowManager.addView(floatingDotView, params)
        // xxx dispatch touch event
        floatingDotView.setListener(this)
    }

    inner class ViewHandler {
        fun onClick(view: View?) {}
    }

    override fun onMove(x: Float, y: Float) {
        val statusBarHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 24f,
            context.resources.displayMetrics
        )
        params!!.x = x.toInt() - floatingDotView.width / 2
        params!!.y = (y - floatingDotView.height / 2).toInt()
        windowManager.updateViewLayout(floatingDotView, params)
    }

    init {
        this.context = context.applicationContext
        windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        floatingDotView = FloatingDotView(context)
    }
}