package com.wkl.onekeyclean.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.wkl.onekeyclean.R;

/**
 * 短信拦截接收器
 */
public class SMSReceiver extends BroadcastReceiver {

    NotificationManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            // 获得一个对象数组类型的“协议数组单元数组”
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] sms = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                // 获取短信息对象
                sms[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            StringBuilder sb = new StringBuilder();
            for (SmsMessage msg: sms) {
                sb.append("from:" + msg.getDisplayOriginatingAddress());
                sb.append("content:" + msg.getMessageBody());
            }
            Toast.makeText(context,sb.toString(),Toast.LENGTH_SHORT).show();
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            this.abortBroadcast();
            Notification notify = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.app_icon)
                    .setTicker("一键清理提示").setContentText("点击查看").setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle("content title").setContentInfo("content info").build();
            manager.notify(1,notify);
        }
    }
}
