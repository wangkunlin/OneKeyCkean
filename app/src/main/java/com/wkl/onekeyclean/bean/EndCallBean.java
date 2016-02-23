package com.wkl.onekeyclean.bean;

import java.util.Comparator;

public class EndCallBean implements Comparable<EndCallBean> {
    private int id;
    private String num;
    private String time;
    private int read;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "EndCallBean{" +
                "id=" + id +
                ", num='" + num + '\'' +
                ", time='" + time + '\'' +
                ", read=" + read +
                '}';
    }

    @Override
    public int compareTo(EndCallBean another) {
        return -this.getTime().compareTo(another.getTime());
    }
}
