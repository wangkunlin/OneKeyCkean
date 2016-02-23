package com.wkl.onekeyclean.widget.textcounter;

public interface Formatter {

    /**
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @param value 值
     * @return
     */
    String format(String prefix, String suffix, float value);

}
