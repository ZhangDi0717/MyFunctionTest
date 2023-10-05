package com.zhangdi.myfunctiontest.eventbus;

import androidx.appcompat.app.AppCompatActivity;

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

public class EventBusSecActivity extends AppCompatActivity {
    private static final String TAG = "EventBusSecActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_sec);
        Log.d(TAG, "onCreate: EventBus.getDefault().isRegistered(this) = " +EventBus.getDefault().isRegistered(this));
        if (!EventBus.getDefault().isRegistered(this)){
            Log.d(TAG, "onCreate: registration");
            EventBus.getDefault().register(this);
        }
       Button mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(9999,0));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBackgroundMessageEvent(MessageEvent event) {
        if (event.action != R.layout.activity_event_bus_sec && event.action != 9999){
            return;
        }
        Log.d(TAG, "onBackgroundMessageEvent: event.action = "+event.action);
        ToastUtil.show(getApplicationContext(),"onBackgroundMessageEvent sec", Toast.LENGTH_LONG);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            Log.d(TAG, "onDestroy: ");
            EventBus.getDefault().unregister(this);
        }
    }
}