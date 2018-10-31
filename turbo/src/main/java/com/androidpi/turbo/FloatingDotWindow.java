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

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class FloatingDotWindow implements FloatingDotView.OnDragListener {

    private Context context;
    private WindowManager windowManager;
    private FloatingDotView floatingDotView;
    private WindowManager.LayoutParams params;

    public FloatingDotWindow(Context context) {
        this.context = context.getApplicationContext();
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.floatingDotView = new FloatingDotView(context);
    }

    public void show() {
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.START | Gravity.TOP;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.format = PixelFormat.TRANSLUCENT;
        windowManager.addView(floatingDotView, params);
        // xxx dispatch touch event
        floatingDotView.setListener(this);
    }

    public class ViewHandler {
        public void onClick(View view) {

        }
    }

    @Override
    public void onMove(float x, float y) {
        float statusBarHeight =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        context.getResources().getDisplayMetrics());
        params.x = (int) x - floatingDotView.getWidth() / 2;
        params.y = (int) (y - floatingDotView.getHeight() / 2);
        windowManager.updateViewLayout(floatingDotView, params);
    }
}
