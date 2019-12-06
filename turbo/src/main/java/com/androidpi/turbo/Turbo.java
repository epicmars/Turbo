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

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import com.androidpi.turbo.service.ProfileService;
import com.androidpi.turbo.service.TurboService;
import timber.log.Timber;

public class Turbo {

    public static final int REQ_CODE_OVERLAY = 1001;

    public static void install(Context context) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (!(context instanceof Application)) {
            Toast.makeText(context, R.string.not_in_application_context, Toast.LENGTH_SHORT).show();
        }
        if (Timber.treeCount() == 0) {
            Timber.plant(new Timber.DebugTree());
        }
        TurboService.start(context);
        ProfileService.start(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    if (context instanceof Activity) {
                        ((Activity)context).startActivityForResult(intent, REQ_CODE_OVERLAY);
                    } else {
                        context.startActivity(intent);
                    }
                }
            }
        }
        FloatingDotWindow window = new FloatingDotWindow(context);
        window.show();
    }
}
