package com.zhangdi.myfunctiontest.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.hikvision.mylibrary.dialog.DialogManager;
import com.zhangdi.myfunctiontest.BaseAppcompat;
import com.zhangdi.myfunctiontest.MessageEvent;
import com.zhangdi.myfunctiontest.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DialogActivity extends BaseAppcompat {
    private Handler mHandler = new Handler();

    @Override
    protected void initData() {
        mList.add("展示加载");
    }

    @Override
    protected void onItemClicked(View view, int position) {
        switch (position) {
            case 0:
                DialogManager.showIconMessageDialogWithoutProgress(this);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogManager.hideIconMessageDialogWithoutProgress();
                    }
                },3000);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBackgroundMessageEvent(MessageEvent event) {}
}