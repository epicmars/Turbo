package com.androidpi.turbo.base.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.androidpi.turbo.base.utils.StatusBarUtils;
import com.androidpi.turbo.base.utils.ViewHelper;
import com.google.android.material.appbar.AppBarLayout;

public class ToolbarLayout extends AppBarLayout {

    public ToolbarLayout(Context context) {
        this(context, null);
    }

    public ToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, StatusBarUtils.getStatusBarHeight(context), 0, 0);
    }

    @Nullable
    protected Activity getActivity() {
        return ViewHelper.getActivity(getContext());
    }
}