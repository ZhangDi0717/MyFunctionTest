package com.zhangdi.myfunctiontest.broadcast;

import android.content.Intent;
import android.view.View;

import com.zhangdi.myfunctiontest.BaseAppcompat;
import com.zhangdi.myfunctiontest.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author: zhangdi45
 * Date: 14:27 2023/11/24
 */
public class BroadcastActivity extends BaseAppcompat {
    private static final String TAG = "BroadcastActivity";
    static final String UHF_TEST="android.intent.action.UHF_START_TEST";//android.intent.action.UHF_START_TEST

    @Override
    protected void initData() {
        mList.add("uhf启动测试");
    }

    @Override
    protected void onItemClicked(View view, int position) {
        switch (position) {
            case 0:
//                sendBroadcast(new Intent(Intent.ACTION_CONNECTION_STATE_CHANGED));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBackgroundMessageEvent(MessageEvent event) {}
}
