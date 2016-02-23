package com.wkl.onekeyclean.ui;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import com.wkl.onekeyclean.R;
import com.wkl.onekeyclean.base.BaseSwipeBackActivity;
import com.wkl.onekeyclean.utils.AppUtil;

public class AboutActivity extends BaseSwipeBackActivity {

    private TextView subVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        subVersion = (TextView) findViewById(R.id.subVersion);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("关于");
        TextView tv = (TextView) findViewById(R.id.app_information);
        Linkify.addLinks(tv, Linkify.ALL);
        subVersion.setText("V"+ AppUtil.getVersion(mContext));

    }

}
