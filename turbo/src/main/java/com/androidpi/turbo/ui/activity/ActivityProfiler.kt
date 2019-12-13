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
package com.androidpi.turbo.ui.activity

import android.app.ActivityManager
import android.app.ActivityManager.RecentTaskInfo
import android.app.ActivityManager.RunningTaskInfo
import android.content.Context
import android.os.Build
import java.util.*

class ActivityProfiler(context: Context) {
    private val context: Context
    private val am: ActivityManager
    val recentTaskInfos: List<RecentTaskInfo>
        get() {
            val recentTaskInfoList: MutableList<RecentTaskInfo> =
                ArrayList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val appTaskList = am.appTasks
                for (appTask in appTaskList) {
                    recentTaskInfoList.add(appTask.taskInfo)
                }
            } else {
                val recentTaskInfos =
                    am.getRecentTasks(256, ActivityManager.RECENT_WITH_EXCLUDED)
                recentTaskInfoList.addAll(recentTaskInfos)
            }
            return recentTaskInfoList
        }

    val topRecentTaskInfo: RecentTaskInfo?
        get() {
            val recentTaskInfos: List<RecentTaskInfo>? = recentTaskInfos
            return if (recentTaskInfos == null || recentTaskInfos.isEmpty()) {
                null
            } else recentTaskInfos[0]
        }

    val runningTaskInfos: List<RunningTaskInfo>
        get() = am.getRunningTasks(256)

    val topRunningTaskInfo: RunningTaskInfo?
        get() {
            val runningTaskInfos: List<RunningTaskInfo>? = runningTaskInfos
            return if (runningTaskInfos == null || runningTaskInfos.isEmpty()) null else runningTaskInfos[0]
        }

    val topActivityName: String?
        get() {
            val recentTaskInfo = topRecentTaskInfo
            val runningTaskInfo = topRunningTaskInfo
            var activityName: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (recentTaskInfo != null && recentTaskInfo.topActivity != null) {
                    activityName = recentTaskInfo.topActivity.className
                } else if (runningTaskInfo != null && runningTaskInfo.topActivity != null) {
                    activityName = runningTaskInfo.topActivity.className
                }
            } else {
                if (runningTaskInfo != null && runningTaskInfo.topActivity != null) {
                    activityName = runningTaskInfo.topActivity.className
                }
            }
            return activityName
        }

    init {
        this.context = context.applicationContext
        am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }
}