package com.zhangdi.myfunctiontest.fingerprint;







import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.gsls.gt.GT;
import com.zhangdi.myfunctiontest.MainActivity;
import com.zhangdi.myfunctiontest.R;

public class FingerPrintActivity extends AppCompatActivity {
    private GT.DarknessMagic.FingerprintUtils fingerprint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
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
        fingerprint.stopListening();
    }
}