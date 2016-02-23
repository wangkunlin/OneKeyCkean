package com.wkl.onekeyclean.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wkl.onekeyclean.R;
import com.wkl.onekeyclean.adapter.EndCallAdapter;
import com.wkl.onekeyclean.base.BaseSwipeBackActivity;
import com.wkl.onekeyclean.bean.EndCallBean;
import com.wkl.onekeyclean.db.DBManager;

import java.util.Collections;
import java.util.List;

public class EndCallActivity extends BaseSwipeBackActivity {

    private ListView listView;
    private List<EndCallBean> data;
    private EndCallAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_call_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        listView = (ListView) findViewById(R.id.end_call_list);
        data = DBManager.getInstance(this).open().getEndCalls();
        Collections.sort(data);
        adapter = new EndCallAdapter(this, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EndCallBean bean = data.get(position);
                if (bean.getRead() == 0) {
                    showDialog(bean);
                }
            }
        });
    }

    private void showDialog(final EndCallBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tips).setMessage(getString(R.string.select_mode) +
                getString(R.string.num_new_line) + bean.getNum());
        builder.setPositiveButton(R.string.sms, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bean.setRead(1);
                adapter.notifyDataSetChanged();
                Uri smsToUri = Uri.parse("smsto:" + bean.getNum());
                Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
                DBManager.getInstance(EndCallActivity.this).open().update(bean.getId());
                startActivity(mIntent);
            }
        }).setNegativeButton(R.string.call, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bean.setRead(1);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + bean.getNum()));
                DBManager.getInstance(EndCallActivity.this).open().update(bean.getId());
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        DBManager.getInstance(this).close();
        super.onDestroy();
    }
}
