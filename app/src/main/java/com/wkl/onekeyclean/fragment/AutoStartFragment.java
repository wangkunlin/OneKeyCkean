package com.wkl.onekeyclean.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wkl.onekeyclean.R;
import com.wkl.onekeyclean.adapter.AutoStartAdapter;
import com.wkl.onekeyclean.base.BaseFragment;
import com.wkl.onekeyclean.model.AutoStartInfo;
import com.wkl.onekeyclean.utils.BootStartUtils;
import com.wkl.onekeyclean.utils.RootUtil;
import com.wkl.onekeyclean.utils.ShellUtils;
import com.wkl.onekeyclean.utils.T;

import java.util.ArrayList;
import java.util.List;

public class AutoStartFragment extends BaseFragment {

    private Context mContext;
    public static final int REFRESH_BT = 111;
    private static final String ARG_POSITION = "position";
    private int position; // 0:普通软件，2 系统软件
    private AutoStartAdapter mAutoStartAdapter;

    private ListView listview;
    private LinearLayout bottom_lin;
    private Button disableButton;
    private TextView topText;

    private List<AutoStartInfo> isSystemAuto = null;
    private List<AutoStartInfo> noSystemAuto = null;
    private int canDisableCom;


    private Handler mHandler = new Handler() {


        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_BT:
                    refeshButoom();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_auto_start, container, false);
        mContext = getActivity();
        listview = (ListView)view.findViewById(R.id.list_view);
        bottom_lin = (LinearLayout) view.findViewById(R.id.bottom_lin);
        disableButton = (Button) view.findViewById(R.id.disable_button);
        disableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RootUtil.preparezlsu(mContext);
                disableAPP();
            }
        });
        topText = (TextView) view.findViewById(R.id.topText);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillData();
    }

    private void disableAPP() {
        List<String> mSring = new ArrayList<>();
        for (AutoStartInfo auto : noSystemAuto) {
            if (auto.isEnable()) {
                String packageReceiverList[] = auto.getPackageReceiver().toString().split(";");
                for (int j = 0; j < packageReceiverList.length; j++) {
                    String cmd = "pm disable " + packageReceiverList[j];
                    //部分receiver包含$符号，需要做进一步处理，用"$"替换掉$
                    cmd = cmd.replace("$", "\"" + "$" + "\"");
                    //执行命令
                    mSring.add(cmd);
                }
            }
        }

        ShellUtils.CommandResult mCommandResult = ShellUtils.execCommand(mSring, true, true);
        if (mCommandResult.result == 0) {
            T.showLong(mContext, "应用已经全部禁止");
            for (AutoStartInfo auto : noSystemAuto) {
                if (auto.isEnable()) {
                    auto.setEnable(false);
                }
            }
            mAutoStartAdapter.notifyDataSetChanged();
            refeshButoom();
        } else {
            T.showLong(mContext, "该功能需要获取系统root权限，请允许获取root权限");
        }
    }

    private void fillData() {

        if (position == 0) {
            topText.setText("禁止下列软件自启,可提升运行速度");

        } else {
            topText.setText("禁止系统核心软件自启,将会影响手机的正常使用,请谨慎操作");

        }

        List<AutoStartInfo> mAutoStartInfo = BootStartUtils.fetchAutoApps(mContext);

        noSystemAuto = new ArrayList<>();
        isSystemAuto = new ArrayList<>();

        for (AutoStartInfo a : mAutoStartInfo) {
            if (a.isSystem()) {

                isSystemAuto.add(a);
            } else {
                noSystemAuto.add(a);
            }
        }

        if (position == 0) {
            mAutoStartAdapter = new AutoStartAdapter(mContext, noSystemAuto, mHandler);
            listview.setAdapter(mAutoStartAdapter);
            refeshButoom();
        } else {
            mAutoStartAdapter = new AutoStartAdapter(mContext, isSystemAuto, null);
            listview.setAdapter(mAutoStartAdapter);
        }

    }

    private void refeshButoom() {
        if (position == 0) {
            canDisableCom = 0;
            for (AutoStartInfo autoS : noSystemAuto) {
                if (autoS.isEnable()) {
                    canDisableCom++;
                }
            }
            if (canDisableCom > 0) {
                bottom_lin.setVisibility(View.VISIBLE);
                disableButton.setText("可优化" + canDisableCom + "款");
            } else {
                bottom_lin.setVisibility(View.GONE);
            }
        }

    }

}
