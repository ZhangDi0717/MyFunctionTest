package com.zhangdi.utils.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;

public class NfcUtils {

    private static String TAG = "ZDNfcUtils";

    //nfc
    //public static NfcAdapter mNfcAdapter;
    public static IntentFilter[] mIntentFilter = null;
    public static PendingIntent mPendingIntent = null;
    public static String[][] mTechList = null;

    /**
     * 构造函数，用于初始化nfc
     */
    public NfcUtils(Activity activity,int flags) {
        //   mNfcAdapter = NfcCheck(activity);
        NfcInit(activity,flags);
    }

    /**
     * 检查NFC是否打开
     */
//    public static NfcAdapter NfcCheck(Activity activity) {
//     mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
//     if (mNfcAdapter == null) {
//             Log.d(TAG,"mNfcAdapter is null !!!! error");
//         return null;
//     } else {
//
//       if (!mNfcAdapter.isEnabled()) {
//           Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
//           activity.startActivity(setNfc);
//       }
//     }
//     return mNfcAdapter;
//    }

    /**
     * 初始化nfc设置
     */
    public static void NfcInit(Activity activity,int flags) {
        mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), flags);
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        Log.d(TAG,"NfcInit");
        try {
            filter.addDataType("*/*");
            tech.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Log.d(TAG,"NfcInit  exception");
            e.printStackTrace();
        }
        mIntentFilter = new IntentFilter[]{filter,tech, filter2};
        mTechList =  new String[][] {
                new String[]{NfcF.class.getName()},
                new String[]{NfcA.class.getName()},
                new String[]{NfcB.class.getName()},
                new String[]{NfcV.class.getName()},
                new String[]{Ndef.class.getName()},
                new String[]{IsoDep.class.getName()}
        };
    }

    /**
     * 读取NFC的数据
     */
    public static String readNFCFromTag(Intent intent) throws Exception {
        String result = "";
        boolean hasResult = false;

        Parcelable pTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (pTag != null) {
            Tag tag = (Tag) pTag;

            if (tag != null){
                byte[] uidBytes = tag.getId();
                result += "\r\nUid="+ByteArrayToHexString(uidBytes)+ "\r\n"+tag.toString() ;
                hasResult = true;
            }
        }

        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        //String
        if (rawArray != null) {
            Log.d(TAG,"readNFCFromTag  get raw Array");
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                Log.d(TAG,"readNFCFromTag  get record " );
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                //
                result += "\r\nNDEF_MESSAGES="+readResult;
                hasResult=true;
            }
        }

        if (!hasResult){
            result += "Empty";
        }

        return result;
    }


    /**
     * 往nfc写入数据
     */
    public static void writeNFCToTag(String data, Intent intent) throws IOException, Exception {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        NdefRecord ndefRecord = NdefRecord.createTextRecord(null, data);
        NdefRecord[] records = {ndefRecord};
        NdefMessage ndefMessage = new NdefMessage(records);
        ndef.writeNdefMessage(ndefMessage);
    }

    /**
     * 读取nfcID
     */
    public static String readNFCId(Intent intent) throws Exception {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String id = ByteArrayToHexString(tag.getId());
        return id;
    }

    /**
     * 将字节数组转换为字符串
     */
    private static String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
}

