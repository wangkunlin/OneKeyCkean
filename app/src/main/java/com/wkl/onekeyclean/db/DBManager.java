package com.wkl.onekeyclean.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wkl.onekeyclean.bean.EndCallBean;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private SQLiteDatabase db;
    private boolean isOpen = false;
    private static DBManager instance;
    private DBHelper helper;

    private DBManager(Context context) {
        helper = new DBHelper(context);
    }

    public DBManager open() {
        if (!isOpen) {
            isOpen = true;
            db = helper.getWritableDatabase();
        }
        return instance;
    }

    public void insert(EndCallBean bean) {
        if (!isOpen) {
            throw new IllegalStateException("需要先调用open()方法");
        }
        ContentValues cv = new ContentValues();
        cv.put("read", 0);
        cv.put("phone_num", bean.getNum());
        cv.put("time", bean.getTime());
        db.insert("call", null, cv);
    }

    public void update(int id) {
        if (!isOpen) {
            throw new IllegalStateException("需要先调用open()方法");
        }
        ContentValues cv = new ContentValues();
        cv.put("read", 1);
        db.update("call", cv, "_id = ?", new String[]{id + ""});
    }

    public List<EndCallBean> getEndCalls() {
        if (!isOpen) {
            throw new IllegalStateException("需要先调用open()方法");
        }
        List<EndCallBean> list = new ArrayList<EndCallBean>();
        String sql = "select * from call";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                EndCallBean bean = new EndCallBean();
                bean.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                bean.setNum(cursor.getString(cursor.getColumnIndex(DBHelper.PHONE_NUM)));
                bean.setTime(cursor.getString(cursor.getColumnIndex(DBHelper.TIME)));
                bean.setRead(cursor.getInt(cursor.getColumnIndex(DBHelper.READ)));
                list.add(bean);
            }
        }
        return list;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void close() {
        if (isOpen && db != null) {
            db.close();
        }
        isOpen = false;
    }

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }
}
