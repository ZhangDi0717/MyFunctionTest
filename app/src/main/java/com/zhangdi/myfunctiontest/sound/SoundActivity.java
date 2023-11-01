package com.zhangdi.myfunctiontest.sound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhangdi.myfunctiontest.DividerGridItemDecoration;
import com.zhangdi.myfunctiontest.HomeAdapter;
import com.zhangdi.myfunctiontest.JavaActivity;
import com.zhangdi.myfunctiontest.MessageEvent;
import com.zhangdi.myfunctiontest.R;
import com.zhangdi.myfunctiontest.StaggeredHomeAdapter;
import com.zhangdi.myfunctiontest.compass.Compass3Activity;
import com.zhangdi.myfunctiontest.eventbus.EventBusFirActivity;
import com.zhangdi.myfunctiontest.fingerprint.FingerPrintActivity;
import com.zhangdi.myfunctiontest.jni.JNIActivity;
import com.zhangdi.myfunctiontest.sim.SimActivity;
import com.zhangdi.myfunctiontest.toast.ToastActivity;
import com.zhangdi.myfunctiontest.toast.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoundActivity extends AppCompatActivity {
    private static final String TAG = "SoundActivity";
    private Context mContext;
    private static final int MAX_SOUNDS = 3;
    private List<String> mList;
    private HomeAdapter mHomeAdaper;
    private RecyclerView mRecyclerView;
    private StaggeredHomeAdapter mStaggeredHomeAdapter;

    private SoundPlayer soundPlayer;
    private SoundPool soundPool;
    private boolean loop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        Log.d(TAG, "onCreate: ");
        mContext = this;
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        soundPlayer = new SoundPlayer(mContext);
        initData();
        initView();
    }
    private int streamId;
    private void initData() {
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(MAX_SOUNDS);
        AudioAttributes attr = new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_SYSTEM).build();
        builder.setAudioAttributes(attr);
        soundPool = builder.build();

        try {
            AssetFileDescriptor assetFileDescriptor = mContext.getAssets().openFd("beep_60ms.wav");
            streamId = soundPool.load(assetFileDescriptor, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mList=new ArrayList<String>();
        mList.add("播放短音频");
        mList.add("循环播放短音频");
        mList.add("停止循环播放短音频");


    }


    private void initView() {
        mRecyclerView= (RecyclerView) this.findViewById(R.id.recycler);
        //设置GridView
        setGridView();
        //设置ListView
//        setListView();
        //设置瀑布流
//        setWaterfallView();
    }

    public void setGridView(){
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mHomeAdaper=new HomeAdapter(this, mList);
        setLister();
        mRecyclerView.setAdapter(mHomeAdaper);
    }


    private long lastBeepTime = System.currentTimeMillis();
    private void setLister(){
        mHomeAdaper.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (position){
                    case 0:
//                        try {
//                            soundPlayer.play(getAssets().openFd("beep_60ms.wav"));
//
////
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
                        EventBus.getDefault().post(new MessageEvent(R.layout.activity_sound,1));
                        break;
                        case 1:
                            loop  = true;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (loop){
                                        long currentBeepTime = System.currentTimeMillis();
                                        if (currentBeepTime - lastBeepTime > 150){
                                            Log.d(TAG, "onItemClick: currentBeepTime-lastBeepTime = "+(currentBeepTime-lastBeepTime));
//                                    SoundPlayer.getInstance(mContext).play("beep_60ms.wav");
                                            Log.d(TAG, "onItemClick: play start ");
                                            EventBus.getDefault().post(new MessageEvent(R.layout.activity_sound,1));
                                            lastBeepTime = currentBeepTime;
                                        }
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                    }
//                                }).start();
                                        try {
                                            Thread.sleep(10);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }).start();

                        break;
                    case 2:
                        loop = false;
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {

            }
        });
    }



    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBackgroundMessageEvent(MessageEvent event) {
        Log.d(TAG, "onBackgroundMessageEvent: ");
        switch (event.what) {
            case 1:
//                SoundPlayer.getInstance(mContext).play("beep_60ms.wav");
                Log.d(TAG, "onBackgroundMessageEvent: play start");
                soundPool.play(streamId, 1, 1, 1, 0, 1);
                Log.d(TAG, "onBackgroundMessageEvent: play end");


                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.autoPause();
        soundPool.release();
    }
}