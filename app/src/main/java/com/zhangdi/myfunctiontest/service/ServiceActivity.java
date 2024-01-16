package com.zhangdi.myfunctiontest.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hikvision.remoteservice.ITestRemoteService;
import com.zhangdi.myfunctiontest.BaseAppcompat;
import com.zhangdi.myfunctiontest.MessageEvent;
import com.zhangdi.myfunctiontest.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ServiceActivity extends BaseAppcompat {
    private static final String TAG = ServiceActivity.class.getSimpleName();
    private ITestRemoteService remoteService;
    private CounterServiceConnection conn;

    private boolean isStarted = false;
    @Override
    protected void initData() {
        mList.add("start Remote 服务");
        mList.add("stop Remote 服务");
        mList.add("bind Remote 服务");
        mList.add("unbind Remote 服务");
        mList.add("invoke Remote 服务");
    }

    @Override
    protected void onItemClicked(View view, int position) {
        Log.d(TAG, "onItemClicked: position " + position);
        switch (position) {
            case 0:
                startService();
                break;
            case 1:
                stopService();
                break;
            case 2:
                bindService();
                break;
            case 3:
                unbindService();
                break;
            case 4:
                invokeService();
                break;
        }
    }

    private void startService(){
        Intent i = new Intent();
      //  i.setClassName("com.hikvision.remoteservice", "android.intent.action.TEST_REMOTE_SERVICE"); //我的这个包里面还有层次，如*.part1、*.part2,etc
        i.setAction("android.intent.action.TEST_REMOTE_SERVICE");
        i.setPackage("com.hikvision.remoteservice");
        startService(i);
        isStarted = true;
        updateServiceStatus();
    }

    private void stopService(){
        Intent i = new Intent();
       // i.setClassName("com.hikvision.remoteservice", "android.intent.action.TEST_REMOTE_SERVICE"); //我的这个包里面还有层次，如*.part1、*.part2,etc
        i.setAction("android.intent.action.TEST_REMOTE_SERVICE");
        i.setPackage("com.hikvision.remoteservice");
        stopService(i); //触发Service的 onDestroy()[if Service存在]
        isStarted = false;
        updateServiceStatus();

    }

    private void bindService(){
        if(conn == null){
            conn = new CounterServiceConnection();
            Intent i = new Intent();
            i.setClassName("com.hikvision.remoteservice", "android.intent.action.TEST_REMOTE_SERVICE");
            bindService(i, conn, Context.BIND_AUTO_CREATE);
            updateServiceStatus();
        }
    }

    private void unbindService(){
        if(conn !=null){
            unbindService(conn); //断开连接，解除绑定
            conn = null;
            updateServiceStatus();
        }
    }

    private void invokeService(){
        if(remoteService != null){
            try{
                Integer counter = remoteService.getCount();
                showInfo(counter+"");
            }catch(RemoteException e){
                Log.e(getClass().getSimpleName(),e.toString());
            }
        }
    }


    private void updateServiceStatus() {
       showInfo("Service status: "+(conn == null ? "unbound" : "bound")+ ","+ (isStarted ? "started" : "not started"));
    }
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBackgroundMessageEvent(MessageEvent event) {}

    public class CounterServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 从连接中获得stub对象，根据我们的跟踪，remoteService就是service中的stub对象
            remoteService = ITestRemoteService.Stub.asInterface(service);
            showInfo("onServiceConnected()" + remoteService);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteService = null;
            updateServiceStatus();
            showInfo("onServiceDisconnected");
        }
    }
    private void showInfo(String s){
        Log.d(TAG, "showInfo: "+"[" +getClass().getSimpleName()+"@" + Thread.currentThread().getName()+ "] " + s);
    }

}