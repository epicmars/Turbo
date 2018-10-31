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
package com.androidpi.turbo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import com.androidpi.turbo.activity.ActivityProfiler;

public class ProfileService extends Service {

    public static final String ACTION = "com.androidpi.turbo.service.ProfileService";
    public static final String EXTRA_COMMAND = "com.androidpi.turbo.service.ProfileService.EXTRA_COMMAND";
    public static final String CMD_SHOW_TOP_ACTIVITY = "CMD_SHOW_TOP_ACTIVITY";

    private ActivityProfiler activityProfiler;

    public static void start(Context context) {
        Intent starter = new Intent(context, ProfileService.class);
        context.startService(starter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (activityProfiler == null) {
            activityProfiler = new ActivityProfiler(this.getApplicationContext());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }
        final String cmd = intent.getStringExtra(EXTRA_COMMAND);
        if (cmd != null) {
            switch (cmd) {
                case CMD_SHOW_TOP_ACTIVITY:
                    String topActivityName = activityProfiler.getTopActivityName();
                    Toast.makeText(this, topActivityName == null ? "未发现栈顶的Activity" : topActivityName, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return START_STICKY;
    }

    public static void showTopActivity(Context context) {
        Intent intent = new Intent(context, ProfileService.class);
        intent.putExtra(ProfileService.EXTRA_COMMAND, ProfileService.CMD_SHOW_TOP_ACTIVITY);
        context.startService(intent);
    }
}
