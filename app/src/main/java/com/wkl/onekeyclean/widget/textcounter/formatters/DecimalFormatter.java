package com.wkl.onekeyclean.widget.textcounter.formatters;

import com.wkl.onekeyclean.widget.textcounter.Formatter;

import java.text.DecimalFormat;

public class DecimalFormatter implements Formatter {

    private final DecimalFormat format = new DecimalFormat("#.0");

    @Override
    public String format(String prefix, String suffix, float value) {
        return prefix + format.format(value) + suffix;
    }
}
