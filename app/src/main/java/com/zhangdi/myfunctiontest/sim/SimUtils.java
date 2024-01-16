package com.zhangdi.myfunctiontest.sim;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

public class SimUtils {
    private static final String TAG = SimUtils.class.getSimpleName();
    private static Context mContext;
    private SimUtils(Context context){
        mContext = context;
    }

    private static SimUtils instance;

    public static SimUtils getInstance(Context context){
        if (instance == null || mContext != context){
            synchronized (SimUtils.class){
                if (instance == null || mContext != context){
                    instance = new SimUtils(context);
                }
            }
        }
        return instance;
    }


    /**
     * 通过slot改变手机上网
     * @param slotId 0 1 2 ...
     * @throws InterruptedException
     */
    public boolean changeSimDataBySlot(int slotId){
        SubscriptionInfo subscriptionInfo = getPhoneSubscriptionInfo(slotId);
        if (subscriptionInfo ==null){
            Log.d(TAG, "changeSimDataBySlot: subscriptionInfo ==null");
            return false;
        }
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        TelephonyManager tm = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).createForSubscriptionId(subscriptionId);
        final SubscriptionManager subscriptionManager = SubscriptionManager.from(mContext);
        subscriptionManager.setDefaultDataSubId(subscriptionInfo.getSubscriptionId());
        tm.setDataEnabled(true);
        if (checkSelfPermission(mContext,Manifest.permission.MODIFY_PHONE_STATE) != PermissionChecker.PERMISSION_GRANTED) {
            return false;
        }
        if(!tm.isDataEnabled()){
            Log.d(TAG, "onBackgroundMessage: 启动数据失败 : "+slotId);
            return false;
        }
        return true;

    }

    /**
     * 是否SIM卡联网
     * @param times 循环检查次数
     * @return
     * @throws InterruptedException
     */
    public boolean isDataEnabled(int times) throws InterruptedException {
        Log.d(TAG, "isDataEnabled: ");
        int count = 0;
        while (count < times) {
            if(NetworkUtil.isConnected(mContext)){
                break;
            }
            count++;
            Thread.sleep(500);
        }
        Log.d(TAG, "isDataEnabled: sim1 count = "+count);
        return count<times;
    }

    private SubscriptionInfo getPhoneSubscriptionInfo(int slotId) {
        /// M: Get subscription info of the specified slotId. @{

        @SuppressLint("MissingPermission")
        SubscriptionInfo subInfo = SubscriptionManager.from(mContext).getActiveSubscriptionInfoForSimSlotIndex(slotId);
        Log.d(TAG, "getPhoneSubscriptionInfo, slotId=" + slotId
                + ", subInfo=" + (subInfo == null ? "null" : subInfo));
        return subInfo;
        /// @}
    }
}
