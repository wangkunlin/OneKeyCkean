package com.wkl.onekeyclean.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.wkl.onekeyclean.model.AutoStartInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BootStartUtils {

    private static final String BOOT_START_PERMISSION = "android.permission.RECEIVE_BOOT_COMPLETED";

    private BootStartUtils() {
    }

    /**
     * 获取Android开机启动列表
     */
    public static List<AutoStartInfo> fetchInstalledApps(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> appInfo = pm.getInstalledApplications(0);
        Iterator<ApplicationInfo> appInfoIterator = appInfo.iterator();
        List<AutoStartInfo> appList = new ArrayList<AutoStartInfo>(appInfo.size());

        while (appInfoIterator.hasNext()) {
            ApplicationInfo app = appInfoIterator.next();
            int flag = pm.checkPermission(BOOT_START_PERMISSION, app.packageName);
            if (flag == PackageManager.PERMISSION_GRANTED) {
                AutoStartInfo appMap = new AutoStartInfo();
                String label = pm.getApplicationLabel(app).toString();
                Drawable icon = pm.getApplicationIcon(app);
                String packageName = app.packageName;
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    appMap.setSystem(true);
                } else {
                    appMap.setSystem(false);
                }

                appMap.setIcon(icon);
                appMap.setPackageName(packageName);
                appMap.setLabel(label);

                appList.add(appMap);
            }
        }
        return appList;
    }


    public static List<AutoStartInfo> fetchAutoApps(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_BOOT_COMPLETED);
        List<ResolveInfo> resolveInfoList = pm.queryBroadcastReceivers(intent, PackageManager.GET_DISABLED_COMPONENTS);
        List<AutoStartInfo> appList = new ArrayList<AutoStartInfo>();
        String appName = null;
        String packageReceiver = null;
        Drawable icon = null;
        boolean isSystem = false;
        boolean isenable = true;
        if (resolveInfoList.size() > 0) {

            appName = resolveInfoList.get(0).loadLabel(pm).toString();
            packageReceiver = resolveInfoList.get(0).activityInfo.packageName + "/" + resolveInfoList.get(0).activityInfo.name;
            icon = resolveInfoList.get(0).loadIcon(pm);
            ComponentName mComponentName1 = new ComponentName(resolveInfoList.get(0).activityInfo.packageName, resolveInfoList.get(0).activityInfo.name);

            if (pm.getComponentEnabledSetting(mComponentName1) == 2) {

                isenable = false;
            } else {
                isenable = true;
            }
            if ((resolveInfoList.get(0).activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                isSystem = true;
            } else {
                isSystem = false;
            }
            for (int i = 1; i < resolveInfoList.size(); i++) {
                AutoStartInfo mAutoStartInfo = new AutoStartInfo();
                if (appName.equals(resolveInfoList.get(i).loadLabel(pm).toString())) {
                    packageReceiver = packageReceiver + ";" + resolveInfoList.get(i).activityInfo.packageName + "/" + resolveInfoList.get(i).activityInfo.name;
                } else {
                    mAutoStartInfo.setLabel(appName);
                    mAutoStartInfo.setSystem(isSystem);
                    mAutoStartInfo.setEnable(isenable);
                    mAutoStartInfo.setIcon(icon);
                    mAutoStartInfo.setPackageReceiver(packageReceiver);

                    appList.add(mAutoStartInfo);
                    appName = resolveInfoList.get(i).loadLabel(pm).toString();
                    packageReceiver = resolveInfoList.get(i).activityInfo.packageName + "/" + resolveInfoList.get(i).activityInfo.name;
                    icon = resolveInfoList.get(i).loadIcon(pm);
                    ComponentName mComponentName2 = new ComponentName(resolveInfoList.get(i).activityInfo.packageName, resolveInfoList.get(i).activityInfo.name);
                    if (pm.getComponentEnabledSetting(mComponentName2) == 2) {

                        isenable = false;
                    } else {
                        isenable = true;
                    }

                    if ((resolveInfoList.get(i).activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        isSystem = true;
                    } else {
                        isSystem = false;
                    }

                }

            }
            AutoStartInfo mAutoStartInfo = new AutoStartInfo();
            mAutoStartInfo.setLabel(appName);
            mAutoStartInfo.setSystem(isSystem);
            mAutoStartInfo.setEnable(isenable);
            mAutoStartInfo.setIcon(icon);
            mAutoStartInfo.setPackageReceiver(packageReceiver);
            appList.add(mAutoStartInfo);

        }

        return appList;
    }
}