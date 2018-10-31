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
package com.androidpi.turbo.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class ActivityProfiler {

    private Context context;
    private ActivityManager am;

    public ActivityProfiler(Context context) {
        this.context = context.getApplicationContext();
        this.am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public List<ActivityManager.RecentTaskInfo> getRecentTaskInfos() {
        List<ActivityManager.RecentTaskInfo> recentTaskInfoList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.AppTask> appTaskList = am.getAppTasks();
            for (ActivityManager.AppTask appTask : appTaskList) {
                recentTaskInfoList.add(appTask.getTaskInfo());
            }
        } else {
            List<ActivityManager.RecentTaskInfo> recentTaskInfos =
                    am.getRecentTasks(256, ActivityManager.RECENT_WITH_EXCLUDED);
            recentTaskInfoList.addAll(recentTaskInfos);
        }
        return recentTaskInfoList;
    }

    public ActivityManager.RecentTaskInfo getTopRecentTaskInfo() {
        List<ActivityManager.RecentTaskInfo> recentTaskInfos = getRecentTaskInfos();
        if (recentTaskInfos == null || recentTaskInfos.isEmpty()) {
            return null;
        }
        return recentTaskInfos.get(0);
    }

    public List<ActivityManager.RunningTaskInfo> getRunningTaskInfos() {
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = am.getRunningTasks(256);
        return runningTaskInfos;
    }

    public ActivityManager.RunningTaskInfo getTopRunningTaskInfo() {
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = getRunningTaskInfos();
        if (runningTaskInfos == null || runningTaskInfos.isEmpty()) return null;
        return runningTaskInfos.get(0);
    }

    public String getTopActivityName() {
        ActivityManager.RecentTaskInfo recentTaskInfo = getTopRecentTaskInfo();
        ActivityManager.RunningTaskInfo runningTaskInfo = getTopRunningTaskInfo();
        String activityName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (recentTaskInfo != null && recentTaskInfo.topActivity != null) {
                activityName = recentTaskInfo.topActivity.getClassName();
            } else if (runningTaskInfo != null && runningTaskInfo.topActivity != null) {
                activityName = runningTaskInfo.topActivity.getClassName();
            }
        } else {
            if (runningTaskInfo != null && runningTaskInfo.topActivity != null) {
                activityName = runningTaskInfo.topActivity.getClassName();
            }
        }
        return activityName;
    }
}
