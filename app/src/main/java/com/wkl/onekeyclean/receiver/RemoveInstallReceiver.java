package com.wkl.onekeyclean.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.wkl.onekeyclean.R;

/**
 * 应用安装卸载广播接收器
 */
public class RemoveInstallReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("发现应用被卸载").setMessage("是否搜索垃圾并清理?");
            builder.setPositiveButton("清理",null).setNegativeButton("取消",null);
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            Window window = alert.getWindow();
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alert.show();
        }
    }
}
