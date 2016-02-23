package com.wkl.onekeyclean.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wkl.onekeyclean.R;
import com.wkl.onekeyclean.model.AppInfo;
import com.wkl.onekeyclean.utils.ShellUtils;
import com.wkl.onekeyclean.utils.StorageUtil;
import com.wkl.onekeyclean.utils.T;
import com.wkl.onekeyclean.views.RippleView;

import java.util.ArrayList;
import java.util.List;

public class SoftwareAdapter extends BaseAdapter {

    public List<AppInfo> mlistAppInfo;
    LayoutInflater infater = null;
    private Context mContext;
    public static List<Integer> clearIds;

    public SoftwareAdapter(Context context, List<AppInfo> apps) {
        infater = LayoutInflater.from(context);
        mContext = context;
        clearIds = new ArrayList<Integer>();
        mlistAppInfo = apps;
    }

    @Override
    public int getCount() {
        return mlistAppInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mlistAppInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = infater.inflate(R.layout.listview_software, null);
            holder = new ViewHolder();
            holder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.appName = (TextView) convertView.findViewById(R.id.app_name);
            holder.size = (TextView) convertView.findViewById(R.id.app_size);
            holder.ripleUninstall = (RippleView) convertView.findViewById(R.id.riple_uninstall);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AppInfo item = (AppInfo) getItem(position);
        if (item != null) {
            holder.appIcon.setImageDrawable(item.getAppIcon());
            holder.appName.setText(item.getAppName());
            holder.size.setText(StorageUtil.convertStorage(item.getUid()));
        }
        holder.ripleUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + item.getPackname()));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView appIcon;
        TextView appName;
        TextView size;
        RippleView ripleUninstall;
        String packageName;
    }

}
