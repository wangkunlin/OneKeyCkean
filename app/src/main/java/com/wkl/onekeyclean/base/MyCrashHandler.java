package com.wkl.onekeyclean.base;

import android.content.Context;

import java.lang.Thread.UncaughtExceptionHandler;

public class MyCrashHandler implements UncaughtExceptionHandler {
    // 保证MyCrashHandler只有一个实例
    // 提供一个静态的程序变量
    private static MyCrashHandler myCrashHandler;
    private Context context;

    // 私有化构造方法
    private MyCrashHandler() {
    }

    // 暴露出来一个静态的方法 获取myCrashHandler

    public static synchronized MyCrashHandler getInstance() {
        if (myCrashHandler == null) {
            myCrashHandler = new MyCrashHandler();
        }
        return myCrashHandler;
    }

    public void init(Context context) {
        this.context = context;
    }

    // 程序发生异常的时候调用的方法

    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
