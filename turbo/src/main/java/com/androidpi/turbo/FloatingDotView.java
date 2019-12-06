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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.androidpi.turbo.utils.AppProfileHelper;

import timber.log.Timber;

public class FloatingDotView extends ConstraintLayout {

    private int STATE_IDLE = 0;
    private int STATE_DRAGGING = 1;

    private ImageButton fab;
    private OnDragListener listener;
    private int state = STATE_IDLE;
    private boolean checked = false;

    public interface OnDragListener {
        void onMove(float x, float y);
    }

    public FloatingDotView(Context context) {
        this(context, null);
    }

    public FloatingDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingDotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.floating_dot, this);

        fab = findViewById(R.id.fab);

        Drawable icon = AppProfileHelper.getIconDrawable(context);
        if (icon != null) {
            fab.setImageDrawable(icon);
        }
        fab.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checked) {
                            checked = false;
                        } else {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.fromParts("package", getContext().getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);
                            checked = true;
                        }
                    }
                });

        fab.setOnLongClickListener(
                new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                return false;
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                return false;
            case MotionEvent.ACTION_MOVE:
                float x = ev.getRawX();
                float y = ev.getRawY();
                Timber.i("raw: x %.2f y %.2f", x, y);
                if (listener != null) {
                    listener.onMove(x, y);
                }
                return true;
        }

        return super.onTouchEvent(ev);
    }

    public void setListener(OnDragListener listener) {
        this.listener = listener;
    }
}
