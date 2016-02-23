package com.wkl.onekeyclean.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wkl.onekeyclean.service.CoreService;

/**
 * 监听开机启动
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, CoreService.class);
        context.startService(i);

        Intent endCallIntent = new Intent();
        endCallIntent.setAction("com.wkl.onekey.EDN_CALL_SETVICE");
        context.startService(endCallIntent);
    }

}
