package com.wkl.onekeyclean.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wkl.onekeyclean.R;
import com.wkl.onekeyclean.bean.EndCallBean;

import java.util.List;

public class EndCallAdapter extends BaseAdapter {

    private List<EndCallBean> data;
    private LayoutInflater inflater;

    public EndCallAdapter(Context context, List<EndCallBean> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.end_call_item, parent, false);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.num = (TextView) convertView.findViewById(R.id.item_num);
            holder.time = (TextView) convertView.findViewById(R.id.item_time);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        EndCallBean bean = data.get(position);
        holder.num.setText(bean.getNum());
        holder.time.setText(bean.getTime());
        if (bean.getRead() == 0) {
            holder.icon.setImageResource(R.mipmap.call_unread);
        } else {
            holder.icon.setImageResource(R.mipmap.call_read);
        }
        return convertView;
    }

    private class Holder {
        ImageView icon;
        TextView num;
        TextView time;
    }

}
