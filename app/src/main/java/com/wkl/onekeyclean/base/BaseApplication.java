package com.wkl.onekeyclean.base;

import android.app.Application;


import android.app.Application;
import android.content.Context;

import com.wkl.onekeyclean.db.DBManager;

public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    private Context mContext;
    // private Gson mG;
    public static BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        DBManager.getInstance(this).open().close();
        MyCrashHandler myCrashHandler = MyCrashHandler.getInstance();
        myCrashHandler.init(getApplicationContext());
        Thread.currentThread().setUncaughtExceptionHandler(myCrashHandler);
    }

    @Override
    public void onLowMemory() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();

    }

}
