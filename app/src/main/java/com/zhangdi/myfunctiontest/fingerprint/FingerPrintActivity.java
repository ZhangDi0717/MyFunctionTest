package com.zhangdi.myfunctiontest.fingerprint;







import androidx.appcompat.app.AppCompatActivity;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gsls.gt.GT;
import com.zhangdi.myfunctiontest.MainActivity;
import com.zhangdi.myfunctiontest.R;

public class FingerPrintActivity extends AppCompatActivity {
    private static final String TAG = FingerPrintActivity.class.getSimpleName();
    private GT.DarknessMagic.FingerprintUtils fingerprint;

    private FingerUtil mFingerUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        mFingerUtil = new FingerUtil(this);
        // 使用指纹监听监听
        mFingerUtil.startFingerListen(mFingerListen);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerprint = GT.DarknessMagic.FingerprintUtils.getInstance().init(FingerPrintActivity.this, new GT.OnListener<Integer>() {
                    @Override
                    public void onListener(Integer... integers) {
                        GT.logt("指纹验证状态:" + integers[0]);
                        switch (integers[0]) {
                            case GT.DarknessMagic.FingerprintUtils.TYPE_0://正常可以 指纹功能
                            case GT.DarknessMagic.FingerprintUtils.TYPE_1://您的手机不支持指纹功能
                            case GT.DarknessMagic.FingerprintUtils.TYPE_2://您还未设置锁屏，请先设置锁屏并添加一个指纹
                            case GT.DarknessMagic.FingerprintUtils.TYPE_3://您至少需要在系统设置中添加一个指纹
                            case GT.DarknessMagic.FingerprintUtils.TYPE_cancel://用户手动取消了
                            case GT.DarknessMagic.FingerprintUtils.TYPE_SUCCESS://指纹认证成功
                            case GT.DarknessMagic.FingerprintUtils.TYPE_ERROR2://尝试次数过多，请稍后重试。
                                break;
                            case GT.DarknessMagic.FingerprintUtils.TYPE_ERROR://指纹认证失败，请再试一次
                                break;
                        }
                    }
                });

                fingerprint.startListening();//开启指纹验证




            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFingerUtil != null && mFingerListen != null) {
            mFingerUtil.startFingerListen(mFingerListen);
        }
    }



    /**
     * 检测是否有指纹模块
     *
     * @return 是否有指纹模块
     */
    private boolean checkFingerModule() {
        try {
            FingerprintManager fingerManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            return fingerManager.isHardwareDetected();
        } catch (Exception e) {
            return false;
        }
    }


    // 实例化指纹监听
    private FingerprintManagerCompat.AuthenticationCallback mFingerListen = new FingerprintManagerCompat.AuthenticationCallback() {

        /**
         * 指纹识别成功
         */
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
//            tv_log.setText("指纹识别成功");
            Log.d(TAG, "onAuthenticationSucceeded: ");
        }

        /**
         * 识别失败
         */
        @Override
        public void onAuthenticationFailed() {
//            tv_log.setText("指纹识别失败");
            Log.d(TAG, "onAuthenticationFailed: 指纹识别失败");
        }

        /**
         * Msg监听
         * @param helpMsgId Msg码
         * @param helpString Msg文案
         */
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Log.d(TAG, "onAuthenticationHelp: helpMsgId = "+helpMsgId);
//            if (tv_log.getTag() != null && false == (Boolean) tv_log.getTag()) {
//                return;
//            }
//            switch (helpMsgId) {
//                case 1001:      // 等待按下手指
//                    tv_log.setText("请按下手指");
//                    break;
//                case 1002:      // 手指按下
//                    tv_log.setText("正在识别…");
//                    break;
//                case 1003:      // 手指抬起
//                    tv_log.setText("手指抬起，请重新按下");
//                    break;
//            }
        }

        /**
         * 多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
         * @param errMsgId 错误码
         * @param errString 剩余禁用时间
         */
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Log.d(TAG, "onAuthenticationError: errMsgId = "+errMsgId);
//            switch (errMsgId) {
//                case 5:      // 可以进行识别
//                    tv_log.setTag(true);
//                    break;
//                case 7:      // 失败次数过多，禁用倒计时未结束
//                    tv_log.setText("失败次数过多！请" + errString + "秒后再试");
//                    break;
//            }
        }
    };
    // 实例化工具类



    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        int type = data.getIntExtra(GT.DarknessMagic.FingerprintDialogFragment.TYPE, -1);
        GT.logt("type:" + type);
        switch (type){
            case GT.DarknessMagic.FingerprintUtils.TYPE_0://正常可以 指纹功能
            case GT.DarknessMagic.FingerprintUtils.TYPE_1://您的手机不支持指纹功能
            case GT.DarknessMagic.FingerprintUtils.TYPE_2://您还未设置锁屏，请先设置锁屏并添加一个指纹
            case GT.DarknessMagic.FingerprintUtils.TYPE_3://您至少需要在系统设置中添加一个指纹
            case GT.DarknessMagic.FingerprintUtils.TYPE_cancel://用户手动取消了
            case GT.DarknessMagic.FingerprintUtils.TYPE_SUCCESS://指纹认证成功
            case GT.DarknessMagic.FingerprintUtils.TYPE_ERROR2://尝试次数过多，请稍后重试。
                break;
            case GT.DarknessMagic.FingerprintUtils.TYPE_ERROR://指纹认证失败，请再试一次
                break;
        }
    }




    @Override
    protected void onStop() {
        super.onStop();
//        fingerprint.stopListening();
        if (mFingerUtil != null) {
            mFingerUtil.stopsFingerListen();
        }
    }
}