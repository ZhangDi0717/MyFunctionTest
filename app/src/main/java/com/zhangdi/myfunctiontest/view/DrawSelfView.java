package com.zhangdi.myfunctiontest.view;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zhangdi.myfunctiontest.BaseAppcompat;
import com.zhangdi.myfunctiontest.MessageEvent;
import com.zhangdi.myfunctiontest.excel.ExcelManager;
import com.zhangdi.myfunctiontest.excel.Student;
import com.zhangdi.myfunctiontest.json.JsonUtil;
import com.zhangdi.myfunctiontest.utils.PLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * author: zhangdi45
 * Date: 14:18 2024/1/18
 */
public class DrawSelfView  extends BaseAppcompat {
    private static final String TAG = "DrawSelfView";
    @Override
    protected void initData() {
        mList.add("绘制流程");
    }

    @Override
    protected void onItemClicked(View view, int position) {
        EventBus.getDefault().post(new MessageEvent(position));
    }




    private String jsonString;
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBackgroundMessageEvent(MessageEvent event) {
        Log.d(TAG, "onBackgroundMessageEvent: ");
        switch (event.what) {
            case 0:
                startActivity(new Intent(this, PersonViewActivity.class));
                break;
        }
    }
}
