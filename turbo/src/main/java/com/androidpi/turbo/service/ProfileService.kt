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
package com.androidpi.turbo.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.androidpi.turbo.ui.activity.ActivityProfiler

class ProfileService : Service() {
    private var activityProfiler: ActivityProfiler? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (activityProfiler == null) {
            activityProfiler = ActivityProfiler(this.applicationContext)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent == null) {
            return START_STICKY
        }
        val cmd = intent.getStringExtra(EXTRA_COMMAND)
        if (cmd != null) {
            when (cmd) {
                CMD_SHOW_TOP_ACTIVITY -> {
                    val topActivityName = activityProfiler?.topActivityName
                    Toast.makeText(
                        this,
                        topActivityName ?: "未发现栈顶的Activity",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return START_STICKY
    }

    companion object {
        const val ACTION = "com.androidpi.turbo.service.ProfileService"
        const val EXTRA_COMMAND =
            "com.androidpi.turbo.service.ProfileService.EXTRA_COMMAND"
        const val CMD_SHOW_TOP_ACTIVITY = "CMD_SHOW_TOP_ACTIVITY"
        fun start(context: Context) {
            val starter = Intent(context, ProfileService::class.java)
            context.startService(starter)
        }

        fun showTopActivity(context: Context) {
            val intent = Intent(context, ProfileService::class.java)
            intent.putExtra(
                EXTRA_COMMAND,
                CMD_SHOW_TOP_ACTIVITY
            )
            context.startService(intent)
        }
    }
}