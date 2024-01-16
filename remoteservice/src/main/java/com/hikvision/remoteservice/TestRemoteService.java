package com.hikvision.remoteservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class TestRemoteService extends Service {
    private static final String TAG = TestRemoteService.class.getSimpleName();
    private Handler serviceHandler = null;
    private int counter = 0;
    private TestCounterTask myTask = new TestCounterTask();

    private ITestRemoteService.Stub stub = new ITestRemoteService.Stub(){

        @Override
        public int getCount() throws RemoteException {
            return counter;
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
       showInfo("remote service onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceHandler=new Handler();
        serviceHandler.postDelayed(myTask, 1000);
        showInfo("remote service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        showInfo("remote onBind");
        return stub;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    /* 用Runnable使用定时计数器，每10秒计数器加1。 */
    private class TestCounterTask implements Runnable{
        public void run() {
            ++ counter;
            serviceHandler.postDelayed(myTask,10000);
            showInfo("running " + counter);
        }
    }

    /* showInfo( ) 帮助我们进行信息跟踪，更好了解Service的运行情况 */
    private void showInfo(String s){
        Log.d(TAG, "showInfo: "+"[" +getClass().getSimpleName()+"@" + Thread.currentThread().getName()+ "] " + s);
    }
}