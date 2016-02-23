package com.wkl.onekeyclean.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wkl.onekeyclean.service.EndCallService;

/**
 * 来电广播接收器
 */
public class InCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 从intent意图中提取行为action
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            Intent endCallIntent = new Intent();
            endCallIntent.setAction("com.wkl.onekey.EDN_CALL_SETVICE");
            context.startService(endCallIntent);
        }
    }
}
