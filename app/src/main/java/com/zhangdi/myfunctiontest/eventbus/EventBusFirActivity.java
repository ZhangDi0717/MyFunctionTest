package com.zhangdi.myfunctiontest.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhangdi.myfunctiontest.MessageEvent;
import com.zhangdi.myfunctiontest.R;
import com.zhangdi.myfunctiontest.toast.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventBusFirActivity extends AppCompatActivity {

    private static final String TAG = "EventBusFirActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_fir);

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        Button mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EventBusSecActivity.class));
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBackgroundMessageEvent(MessageEvent event) {
        Log.d(TAG, "onBackgroundMessageEvent: ");
        if (event.action != R.layout.activity_event_bus_fir && event.action != 9999){
            return;
        }
        Log.d(TAG, "onBackgroundMessageEvent: event.action = "+event.action);
        ToastUtil.show(getApplicationContext(),"onBackgroundMessageEvent first", Toast.LENGTH_LONG);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}