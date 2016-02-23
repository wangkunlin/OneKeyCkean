package com.wkl.onekeyclean.widget.textcounter.formatters;

import com.wkl.onekeyclean.widget.textcounter.Formatter;

public class NoFormatter implements Formatter {

    @Override
    public String format(String prefix, String suffix, float value) {
        return prefix + value + suffix;
    }
}
