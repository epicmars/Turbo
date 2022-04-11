package com.androidpi.turbo.base.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.view.PixelCopy;
import android.view.View;
import android.view.Window;

import timber.log.Timber;

/*
 * Copyright 2019 yinpinjiu@gmail.com
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
public class BitmapUtils {

    public interface BitmapCreateCallback {
        void onBitmapCreated(Bitmap bitmap);
    }

    public static void createBitmapFromView(
            View view, Window window, BitmapCreateCallback callback) {
        createBitmapFromView(view, window, null, callback);
    }

    public static void createBitmapFromView(
            View view, Window window, Rect dest, BitmapCreateCallback callback) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int[] locationInWindow = new int[2];
            view.getLocationInWindow(locationInWindow);
            Rect rect =
                    new Rect(
                            locationInWindow[0],
                            locationInWindow[1],
                            locationInWindow[0] + width,
                            locationInWindow[1] + height);
            if (dest != null) {
                rect = dest;
            }
            try {
                PixelCopy.request(
                        window,
                        rect,
                        bitmap,
                        new PixelCopy.OnPixelCopyFinishedListener() {
                            @Override
                            public void onPixelCopyFinished(int copyResult) {
                                switch (copyResult) {
                                    case PixelCopy.SUCCESS:
                                        callback.onBitmapCreated(bitmap);
                                        break;
                                    default:
                                        Timber.e("PixCopy failed: %d", copyResult);
                                        break;
                                }
                            }
                        },
                        new Handler());
            } catch (IllegalArgumentException e) {

            }
        } else {
            view.buildDrawingCache();
            Bitmap bitmap = view.getDrawingCache();
            if (dest == null) {
                callback.onBitmapCreated(bitmap);
                return;
            }
            Bitmap result =
                    Bitmap.createBitmap(bitmap, dest.left, dest.top, dest.width(), dest.height());
            bitmap.recycle();
            callback.onBitmapCreated(result);
        }
    }
}
