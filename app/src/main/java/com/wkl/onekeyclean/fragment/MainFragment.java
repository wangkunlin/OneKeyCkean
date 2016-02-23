package com.wkl.onekeyclean.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;
import com.wkl.onekeyclean.R;
import com.wkl.onekeyclean.base.BaseFragment;
import com.wkl.onekeyclean.model.SDCardInfo;
import com.wkl.onekeyclean.ui.AutoStartManageActivity;
import com.wkl.onekeyclean.ui.MemoryCleanActivity;
import com.wkl.onekeyclean.ui.RubbishCleanActivity;
import com.wkl.onekeyclean.ui.SoftwareManageActivity;
import com.wkl.onekeyclean.utils.AppUtil;
import com.wkl.onekeyclean.utils.StorageUtil;
import com.wkl.onekeyclean.widget.circleprogress.ArcProgress;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页面
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {

    private ArcProgress arcStore;
    private ArcProgress arcProcess;
    private TextView capacity;

    private Context mContext;

    private Timer timer;
    private Timer timer2;
    private View card1;
    private View card2;
    private View card3;
    private View card4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = getActivity();
        arcStore = (ArcProgress) view.findViewById(R.id.arc_store);
        arcProcess = (ArcProgress) view.findViewById(R.id.arc_process);
        capacity = (TextView) view.findViewById(R.id.capacity);
        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);
        card4 = view.findViewById(R.id.card4);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UmengUpdateAgent.update(getActivity());
    }

    private void fillData() {
        timer = null;
        timer2 = null;
        timer = new Timer();
        timer2 = new Timer();


        long l = AppUtil.getAvailMemory(mContext) / 1024;
        long y = AppUtil.getTotalMemory(mContext);
        final double x = (y - l) / (y * 1.0) * 100;

        arcProcess.setProgress(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (arcProcess.getProgress() >= (int) x) {
                            timer.cancel();
                        } else {
                            arcProcess.setProgress(arcProcess.getProgress() + 1);
                        }

                    }
                });
            }
        }, 100, 20);

        SDCardInfo mSDCardInfo = StorageUtil.getSDCardInfo();
        SDCardInfo mSystemInfo = StorageUtil.getSystemSpaceInfo(mContext);

        long nAvailaBlock;
        long TotalBlocks;
        if (mSDCardInfo != null) {
            nAvailaBlock = mSDCardInfo.free + mSystemInfo.free;
            TotalBlocks = mSDCardInfo.total + mSystemInfo.total;
        } else {
            nAvailaBlock = mSystemInfo.free;
            TotalBlocks = mSystemInfo.total;
        }

        final double percentStore = (TotalBlocks - nAvailaBlock) / (TotalBlocks * 1.0) * 100;

        capacity.setText(StorageUtil.convertStorage(TotalBlocks - nAvailaBlock) +
                "/" + StorageUtil.convertStorage(TotalBlocks));
        arcStore.setProgress(0);

        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcStore.getProgress() >= (int) percentStore) {
                            timer2.cancel();
                        } else {
                            arcStore.setProgress(arcStore.getProgress() + 1);
                        }

                    }
                });
            }
        }, 100, 20);


    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timer2.cancel();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card1:
                startActivity(MemoryCleanActivity.class);
                break;
            case R.id.card2:
                startActivity(RubbishCleanActivity.class);
                break;
            case R.id.card3:
                startActivity(AutoStartManageActivity.class);
                break;
            case R.id.card4:
                startActivity(SoftwareManageActivity.class);
                break;
        }
    }
}
