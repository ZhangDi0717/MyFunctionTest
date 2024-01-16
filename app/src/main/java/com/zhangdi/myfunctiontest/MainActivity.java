package com.zhangdi.myfunctiontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemProperties;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhangdi.myfunctiontest.broadcast.BroadcastActivity;
import com.zhangdi.myfunctiontest.calendar.CalendarHaiBinActivity;
import com.zhangdi.myfunctiontest.camera.CameraActivity;
import com.zhangdi.myfunctiontest.compass.Compass3Activity;
import com.zhangdi.myfunctiontest.dialog.DialogActivity;
import com.zhangdi.myfunctiontest.eventbus.EventBusFirActivity;
import com.zhangdi.myfunctiontest.excel.ExcelActivity;
import com.zhangdi.myfunctiontest.fingerprint.FingerPrintActivity;
import com.zhangdi.myfunctiontest.input.InputActivity;
import com.zhangdi.myfunctiontest.jni.JNIActivity;
import com.zhangdi.myfunctiontest.json.JSONActivity;
import com.zhangdi.myfunctiontest.seekbar.SeekBarActivity;
import com.zhangdi.myfunctiontest.service.ServiceActivity;
import com.zhangdi.myfunctiontest.sim.SimActivity;
import com.zhangdi.myfunctiontest.sound.SoundActivity;
import com.zhangdi.myfunctiontest.toast.ToastActivity;
import com.zhangdi.myfunctiontest.toast.ToastUtil;
import com.zhangdi.myfunctiontest.touch.TouchActivity;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class MainActivity extends BasePermissionActivity {
    private static final String TAG = "MainActivity";
    private Context mContext;
    private List<String> mList;
    private HomeAdapter mHomeAdaper;
    private RecyclerView mRecyclerView;
    private StaggeredHomeAdapter mStaggeredHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        request(new OnPermissionsResult() {
            @Override
            public void OnSuccess() {
                Log.d(TAG, "OnSuccess: ");
            }

            @Override
            public void OnFail(List<String> noPermissions) {
                for (int i = 0; noPermissions!=null && i < noPermissions.size(); i++) {
                    Log.d(TAG, "OnFail: permission = "+noPermissions.get(i));
                }
            }
        });
        mContext = this;
        initData();
        initView();
//        testFunctions();

//        finish();

    }

    private void testFunctions() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(TAG, "testFunctions: telephonyManager: " + telephonyManager);
        Log.d(TAG, "testFunctions: getPhoneType = " + telephonyManager.getPhoneType());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d(TAG, "testFunctions: getNetworkType " + telephonyManager.getDataNetworkType());

//        telephonyManager.listen(listener, 290);
        Log.d(TAG, "testFunctions: getPsdnIp = "+getPsdnIp());
    }



    /**
     * 测试sim卡的网络连接
     */
    public void testSIMNetwork(){

    }



    /**
     * 十六进制数组转十六进制字符串
     * @param hexArray
     * @return
     */
    public static String hexArrayArrayToHexLogString(byte[] hexArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hexArray) {
            sb.append(String.format("%02x", b & 0xFF)+" ");
        }

        return sb.toString();
    }

    /**
     * 用来获取手机拨号上网(包括CTWAP和CTNET)时由PDSN分配给手机终端的源IP地址。
     * //fe80::4095:9eff:fefb:bd2%dummy0
     * //fe80::4832:86ff:fef9:aa49%dummy0
     * @return
     */
    public static String getPsdnIp() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Log.d(TAG, "getPsdnIp: HardwareAddress = "+hexArrayArrayToHexLogString(intf.getHardwareAddress()));

                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    Log.d(TAG, "getPsdnIp:HostAddress "+inetAddress.getHostAddress());
                   // Log.d(TAG, "getPsdnIp:Address = "+Arrays.toString(inetAddress.getAddress()));
//                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
//                        return inetAddress.getHostAddress().toString();
//                    }

                }

            }

        } catch (Exception e) {
        }

        return "";

    }



    String getProvider(@NonNull TelephonyManager mTelephonyManager) {
        String IMSI = "";
        @SuppressLint("MissingPermission")
        String subscriberid = mTelephonyManager.getSubscriberId();
        if (subscriberid != null) {
            IMSI = subscriberid;
        } else {
            IMSI = mTelephonyManager.getSimOperator();
        }

        String ProvidersName = "Unknown";
        if (IMSI != null && 0 != IMSI.compareTo("")) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007") || IMSI.startsWith("46004")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003") || IMSI.startsWith("46011")) {
                ProvidersName = "中国电信";
            } else
                ProvidersName = IMSI;
        } else {
        }

        return ProvidersName;
    }

    String getDataActivity(@NonNull TelephonyManager mTelephonyManager) {
        int activity = mTelephonyManager.getDataActivity();
        if (activity == TelephonyManager.DATA_ACTIVITY_NONE) {
            return "数据连接状态：活动，但无数据发送和接受\n";
        } else if (activity == TelephonyManager.DATA_ACTIVITY_IN) {
            return "数据连接状态：活动，正在接受数据\n";
        } else if (activity == TelephonyManager.DATA_ACTIVITY_OUT) {
            return "数据连接状态：活动，正在发送数据\n";
        } else if (activity == TelephonyManager.DATA_ACTIVITY_INOUT) {
            return "数据连接状态：活动，正在接受和发送数据\n";
        } else {
            return "数据连接状态：未知\n";
        }

    }

    String getCellularType(@NonNull TelephonyManager mTelephonyManager) {
        String cellularType = "";


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "TODO";
        }
        int nSubType = mTelephonyManager.getNetworkType();
        if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                || nSubType == TelephonyManager.NETWORK_TYPE_1xRTT
                || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                || nSubType == TelephonyManager.NETWORK_TYPE_GSM
                || nSubType == TelephonyManager.NETWORK_TYPE_IDEN) {
            cellularType = "2G";
        } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                || nSubType == TelephonyManager.NETWORK_TYPE_TD_SCDMA
                || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_B
                || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                || nSubType == TelephonyManager.NETWORK_TYPE_HSUPA
                || nSubType == TelephonyManager.NETWORK_TYPE_HSPA
                || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_A
                || nSubType == TelephonyManager.NETWORK_TYPE_HSPAP) {
            cellularType = "3G";
        } else if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                || nSubType == TelephonyManager.NETWORK_TYPE_IWLAN) {
            cellularType = "4G";
//        } else if (nSubType == TelephonyManager.NETWORK_TYPE_NR) {
//            cellularType= "5G";
        } else if (nSubType == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
            cellularType = "0G";
        } else
            cellularType = String.valueOf(nSubType);

        return cellularType;
    }


    private PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onSignalStrengthsChanged(@NonNull SignalStrength signalStrength) {

            Log.i("alderaan", "---------------------------\r\n");

            TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            String providername = getProvider(mTelephonyManager);
            Log.i("alderaan", "运营商为：" + providername);

            String cellulartype = getCellularType(mTelephonyManager);
            Log.i("alderaan", "网络类型为：" + cellulartype);

            String activity = getDataActivity(mTelephonyManager);
            Log.i("alderaan", activity);

            //通过方法反射调用，获取出和系统一样的信号格数
            Method method1 = null;
            try {
                method1 = signalStrength.getClass().getMethod("getLteLevel");
                int level = (int) method1.invoke(signalStrength);

                if (level != 0) {
                    Log.i("alderaan", "当前手机的LTE信号格数：" + level);
                } else {
                    // LTE信号为0时，获取GSM信号强度
                    Method method2 = signalStrength.getClass().getMethod("getGsmLevel");
                    int level2 = (int) method2.invoke(signalStrength);

                    Log.i("alderaan", "当前手机的GSM信号格数：" + level2);
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            super.onSignalStrengthsChanged(signalStrength);
        }
    };



    private void initView() {
        mRecyclerView= (RecyclerView) this.findViewById(R.id.recycler);
        //设置GridView
        setGridView();
        //设置ListView
//        setListView();
        //设置瀑布流
//        setWaterfallView();
    }
    public void setListView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mHomeAdaper=new HomeAdapter(this, mList);
        setLister();
        mRecyclerView.setAdapter(mHomeAdaper);
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
    public void setWaterfallView(){
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mStaggeredHomeAdapter=new StaggeredHomeAdapter(this, mList);
        mRecyclerView.setAdapter(mStaggeredHomeAdapter);
    }

    private void initData() {

        mList=new ArrayList<String>();
        mList.add("读取excel某一列内容");
        mList.add("跳转到指南针界面");
        mList.add("jni");
        mList.add("Sim");
        mList.add("Toast");
        mList.add("加载图片");
        mList.add("EventBus的多activity响应");
        mList.add("EventBusc测试");
        mList.add("指纹");
        mList.add("相机");
        mList.add("preferences");
        mList.add("java语法");
        mList.add("音频");
        mList.add("触屏");
        mList.add("excel");
        mList.add("seekBar");
        mList.add("日历");
        mList.add("dialog");
        mList.add("service");
        mList.add("输入框");
        mList.add("广播");
        mList.add("fastjson");
    }
    /**
     * 获取文件内容
     *
     * @param inputStream
     * @return
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        //创建字符缓冲流
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            //读取每行路由数据
            while ((line = reader.readLine()) != null) {
                //添加到字符缓冲流中
                sb.append(line);
                //一条一行
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回路由字符串
        return sb.toString();
    }
    private void setLister(){
        mHomeAdaper.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                try {
//                    Toast.makeText(mContext, "点击第" + (position + 1) + "条", Toast.LENGTH_SHORT).show();
//                    //获取输入流
//                    InputStream inputStream = getResources().getAssets().open("text.txt");//这里的名字是你的txt 文本文件名称,在App,main文件夹下建一个assets文件夹，放入txt文本。
//                    //获取路由名单
//                    String str = getString(inputStream);
//                    Log.d(TAG, "onItemClick: "+str);
//
//                }catch (Exception e) {
//                    Log.d(TAG, "onItemClick: ");
//                }

                switch (position){
                    case 0:
//                        ExcelPoiUtils.readRowListFromFile(mContext,"factorytest_item.xlsx",0);
//                        List<String> strings = ExcelPoiUtils.readColumnListFromFile(mContext,"factorytest_item.xlsx",0);
//                        strings.remove(0);

//                        Log.d(TAG, "onItemClick: "+ Arrays.toString(strings.toArray()));
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d(TAG, "onItemClick: 1");
//                                Map<String, Integer> map = ExcelPoiUtils.readMapFromFile(mContext, "factorytest_item.xlsx", "4-64");
//                                Log.d(TAG, "onItemClick: 2");
//                            }
//                        }).start();
                        break;
                    case 1:
                        startActivity(new Intent(mContext, Compass3Activity.class));
//                        finish();
                        break;
                    case 2:
                        startActivity(new Intent(mContext, JNIActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(mContext, SimActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(mContext, ToastActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(mContext, EventBusFirActivity.class));
                        break;
                    case 7:
                       EventBus.getDefault().post(new MessageEvent(R.layout.activity_main,1));
                       break;
                   case 8:
                       startActivity(new Intent(mContext, FingerPrintActivity.class));
                       break;
                   case 9:
                       startActivity(new Intent(mContext, CameraActivity.class));
                       break;
                    case 11:
                        startActivity(new Intent(mContext, JavaActivity.class));
                        break;
                    case 12:
                        startActivity(new Intent(mContext, SoundActivity.class));
                        break;
                    case 13:
                        startActivity(new Intent(mContext, TouchActivity.class));
                        break;
                    case 14:
                        startActivity(new Intent(mContext, ExcelActivity.class));
                        break;
                    case 15:
                        startActivity(new Intent(mContext, SeekBarActivity.class));
                        break;
                    case 16:
                        startActivity(new Intent(mContext, CalendarHaiBinActivity.class));
                        break;
                    case 17:
                        startActivity(new Intent(mContext, DialogActivity.class));
                        break;
                    case 18:
                        startActivity(new Intent(mContext, ServiceActivity.class));
                        break;
                    case 19:
                        startActivity(new Intent(mContext, InputActivity.class));
                        break;
                    case 20:
                        startActivity(new Intent(mContext, BroadcastActivity.class));
                        break;
                    case 21:
                        startActivity(new Intent(mContext, JSONActivity.class));
                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onBackgroundMessageEvent(MessageEvent event) {
        Log.d(TAG, "onBackgroundMessageEvent: ");
        if (event.action != R.layout.activity_main && event.action != 9999){
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