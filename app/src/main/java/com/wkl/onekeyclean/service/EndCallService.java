package com.wkl.onekeyclean.service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.internal.telephony.ITelephony;
import com.wkl.onekeyclean.R;
import com.wkl.onekeyclean.bean.EndCallBean;
import com.wkl.onekeyclean.db.DBManager;
import com.wkl.onekeyclean.ui.EndCallActivity;
import com.wkl.onekeyclean.ui.SplishActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * 挂断电话服务
 */
public class EndCallService extends Service {

    private Intent intent;
    private TelephonyManager telephonyManager;
    private ITelephony iTelephony;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        startService(intent);
    }

    // 创建一个内部类
    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            if (state == TelephonyManager.CALL_STATE_RINGING) {
                try { // 挂断电话的核心代码
                    Class<TelephonyManager> telephonyManagerClass = TelephonyManager.class;
                    Method getITelephony = telephonyManagerClass.getDeclaredMethod("getITelephony",
                            (Class[]) null);
                    getITelephony.setAccessible(true);
                    iTelephony = (ITelephony) getITelephony.invoke(telephonyManager, (Object[]) null);
                    if (!isInContact(incomingNumber)) {
                        // 挂断电话
                        iTelephony.endCall();
                        EndCallBean bean = new EndCallBean();
                        DBManager db = DBManager.getInstance(EndCallService.this);
                        bean.setNum(incomingNumber);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        bean.setTime(sdf.format(new Date()));
                        showNotify(incomingNumber);
                        db.open().insert(bean);
                        db.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void showNotify(String num) {
        NotificationManager manager = null;
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(101);
        Notification notify = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon).setContentText("号码:" + num)
                .setContentTitle("电话拦截").setContentInfo("点击查看").build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notify.when = System.currentTimeMillis();
        notify.icon = R.mipmap.app_icon;
        notify.tickerText = "电话拦截";
        notify.sound = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6");
        long[] vibrate = {0, 100, 200, 300};
        notify.vibrate = vibrate;
        notify.ledARGB = 0xff00ff00;
        notify.ledOnMS = 400;
        notify.ledOffMS = 1000;
        notify.flags |= Notification.FLAG_SHOW_LIGHTS;
        Intent intent = new Intent(this, EndCallActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notify.contentIntent = pi;
        manager.notify(101, notify);
    }

    private boolean isInContact(String num) {
        boolean isIn = false;
        ContentResolver resolver = getContentResolver();
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME};
        String[] args = {num};
        String selection = Phone.NUMBER + "=?";
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, projection, selection, args, null);

        if (phoneCursor != null) {
            if (phoneCursor.getCount() > 0) {
                isIn = true;
            }
            phoneCursor.close();
        }
        return isIn;
    }
}