package com.zhangdi.myfunctiontest.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

/**
 * 本地提示音播放器
 * SoundPool用于播放声音短，文件小的音频，延时短
 * @author: heiyulong
 * @date: 2021/4/16
 */
public class SoundPlayer {
    private static final String TAG = "SoundPlayer";
    private static final int MAX_SOUNDS = 1;
    private Context mContext;
    private SoundPool soundPool;
    private HashMap<Integer,Integer> soundMap = new HashMap<>();

    private static SoundPlayer instance;
    public static SoundPlayer getInstance(Context context) {
        if (instance == null){
            synchronized (SoundPlayer.class){
                if (instance == null){
                    instance = new SoundPlayer(context);
                }
            }
        }
        return instance;
    }
    public SoundPlayer(Context appContext) {
        // 版本兼容
        this.mContext = appContext;
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(MAX_SOUNDS);
        AudioAttributes attr = new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build();
        builder.setAudioAttributes(attr);
        soundPool = builder.build();
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                int streamId = soundPool.play(sampleId, 1, 1, 1, 0, 1);
                Log.d(TAG, "onLoadComplete: sampleId: " + sampleId + "; streamId: " + streamId);
                soundMap.put(sampleId,streamId);
            }
        });
    }



    /**
     * 播放音频
     * @param resId 音频文件 R.raw.xxx
     * @param repeatTime  循环模式：0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次
     */
    public void play(int resId, int repeatTime) {
        int soundID = soundPool.load(mContext, resId, 1);
        // 该方法防止sample not ready错误
    }
    /**
     * 播放音频
     * @param resId 音频文件 R.raw.xxx
     */
    public void play(int resId) {

//        try {
//            AssetFileDescriptor assetFileDescriptor = mContext.openFd(assetPath);
//            mSoundPool.load(assetFileDescriptor,0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void load(){

    }



    /**
     * 播放音频
     * @param assetPath 加载自assets 的地址:beep_60ms.wav
     */
    public void play(String assetPath){
        try {
            AssetFileDescriptor assetFileDescriptor = mContext.getAssets().openFd(assetPath);
            soundPool.load(assetFileDescriptor,0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停
     * @param resId
     */
    public void pause(int resId) {
        if (soundPool != null) {
            Integer mStreamID = soundMap.get(resId);
            if(mStreamID != null){
                soundPool.pause(mStreamID);
            }
        }
    }

    /**
     * 继续
     * @param resId
     */
    public void resume(int resId) {
        if (soundPool != null) {
            Integer mStreamID = soundMap.get(resId);
            if(mStreamID != null){
                soundPool.resume(mStreamID);
            }
        }
    }

    /**
     * 停止
     * @param resId
     */
    public void stop(int resId) {
        if (soundPool != null) {
            Integer mStreamID = soundMap.get(resId);
            if(mStreamID != null){
                soundPool.stop(mStreamID);
            }
        }
    }


    /**
     * 资源释放
     */
    public void release() {
        Log.d(TAG, "Cleaning resources..");
        if (soundPool != null) {
            soundPool.autoPause();
            soundPool.release();
        }
    }

}
