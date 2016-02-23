package com.wkl.onekeyclean.utils;

import android.content.Context;

import com.wkl.onekeyclean.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RootUtil {

    /**
     * 调用su获取root权限再把zlsu写到system/bin目录下
     * 以便永久获取root权限（一旦获取成功，下次不再调用su）
     *
     * @param ctx
     */
    public static void preparezlsu(Context ctx) {
        try {
            File zlsu = new File("/system/bin/" + Constants.ROOT_SU);

            InputStream suStream = ctx.getResources().openRawResource(R.raw.zlsu);
            /**
             * 如果zlsu存在，则和raw目录下的zlsu比较大小，大小相同则不替换
             */
            if (zlsu.exists()) {
                if (zlsu.length() == suStream.available()) {
                    suStream.close();
                    return;
                }
            }

            /*
             * 先把zlsu 写到/system/bin目录下
             */
            byte[] bytes = new byte[suStream.available()];
            DataInputStream dis = new DataInputStream(suStream);
            dis.readFully(bytes);
            String pkgPath = ctx.getApplicationContext().getPackageName();
            String zlsuPath = "/data/data/" + pkgPath + File.separator + Constants.ROOT_SU;
            File zlsuFileInData = new File(zlsuPath);
            if (!zlsuFileInData.exists()) {
                System.out.println(zlsuPath + " not exist! ");
                try {
                    System.out.println("creating " + zlsuPath + "......");
                    zlsuFileInData.createNewFile();
                } catch (IOException e) {
                    System.out.println("create " + zlsuPath + " failed ! ");
                }
                System.out.println("create " + zlsuPath + " successfully ! ");
            }
            FileOutputStream suOutStream = new FileOutputStream(zlsuPath);
            suOutStream.write(bytes);
            suOutStream.close();

            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            //重新挂载
            os.writeBytes("mount -o remount,rw /dev/block/mtdblock3 /system\n");
            //拷贝文件
            os.writeBytes("busybox cp " + zlsuPath + " /system/bin/" + Constants.ROOT_SU + "\n");
            //改变所有者
            os.writeBytes("busybox chown 0:0 /system/bin/" + Constants.ROOT_SU + "\n");
            //修改权限
            os.writeBytes("chmod 4755 /system/bin/" + Constants.ROOT_SU + "\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (Exception e) {
            System.out.println("RootUtil preparezlsu: error");
            e.printStackTrace();
        }
    }
}
