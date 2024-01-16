package com.zhangdi.myfunctiontest.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boshuai.li on 2017/8/16.
 */

public class PLog {

    public static final String TAG = "action_plugin";

    public static boolean mOpenLog = true;
    public static boolean mAllErrLog = true;
    private static final boolean IS_WRITE_LOG_2_FILE = false;

    public static void initPLog(boolean openLog, boolean errLog) {
    	mOpenLog = openLog;
    	mAllErrLog = errLog;
    }
    
    public static String getRunInfo() {
        StringBuffer toStringBuffer = null;
        try {
            StackTraceElement traceElement = ((new Exception()).getStackTrace())[3];
            StackTraceElement traceElement2 = ((new Exception())
                    .getStackTrace())[4];
            toStringBuffer = new StringBuffer(""
                    + Thread.currentThread().getId()).append(" | ")
                    .append(traceElement.getFileName()).append(" | ")
                    .append(traceElement.getLineNumber()).append(" | ")
                    .append(traceElement2.getMethodName()).append(" -> ")
                    .append(traceElement.getMethodName()).append("()");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toStringBuffer != null ? toStringBuffer.toString() : null;
    }

    public static String wrapLog(String msg) {
        return "[" + getRunInfo() + "] " + msg;
    }
    
    public static void i(String msg) {
    	if (!mOpenLog) {
            return;
        }
        if (mAllErrLog) {
            Log.e(TAG, wrapLog(msg));
        } else {
            Log.i(TAG, wrapLog(msg));
        }
        if (IS_WRITE_LOG_2_FILE) {
            writeLog2File(wrapLog(msg));
        }
    }
    
    public static void i(String tag, String msg) {
        if (!mOpenLog) {
            return;
        }
        if (mAllErrLog) {
            Log.e(tag, wrapLog(msg));
        } else {
            Log.i(tag, wrapLog(msg));
        }
        if (IS_WRITE_LOG_2_FILE) {
            writeLog2File(wrapLog(msg));
        }
    }

    public static void fake_tag_i(String fakeTag, String msg) {
        if (!mOpenLog) {
            return;
        }
        msg = fakeTag + " " + msg;
        if (mAllErrLog) {
            Log.e(TAG, wrapLog(msg));
        } else {
            Log.i(TAG, wrapLog(msg));
        }
        if (IS_WRITE_LOG_2_FILE) {
            writeLog2File(wrapLog( msg));
        }
    }

    public static void w(String msg) {
    	if (!mOpenLog) {
            return;
        }
        if (mAllErrLog) {
            Log.e(TAG, wrapLog(msg));
        } else {
            Log.w(TAG, wrapLog(msg));
        }
        if (IS_WRITE_LOG_2_FILE) {
            writeLog2File(wrapLog(msg));
        }
    }
    
    public static void w(String msg, Throwable tr) {
    	if (!mOpenLog) {
            return;
        }
        if (mAllErrLog) {
            Log.e(TAG, wrapLog(msg), tr);
        } else {
            Log.w(TAG, wrapLog(msg), tr);
        }
        if (IS_WRITE_LOG_2_FILE) {
            writeLog2File(wrapLog(msg) + ": " + getThrowableStr(tr));
        }
    }

    public static void e(String msg) {
    	if (!mOpenLog) {
            return;
        }
        Log.e(TAG, wrapLog(msg));
        if (IS_WRITE_LOG_2_FILE) {
            writeLog2File(wrapLog(msg));
        }
    }
    
    public static void e(String msg, Throwable tr) {
        if (!mOpenLog) {
            return;
        }
        Log.e(TAG, wrapLog(msg), tr);
        if (IS_WRITE_LOG_2_FILE) {
            writeLog2File(wrapLog(msg) + ": " + getThrowableStr(tr));
        }
    }

    private static void writeLog2File(String log) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        String path = Environment.getExternalStorageDirectory()
                + "/" + TAG + ".log";
        File file = new File(path);
        FileOutputStream fos = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        try {
            fos = new FileOutputStream(file, true);
            fos.write((sdf.format(date) + " " + log + '\n').getBytes());
        } catch (Exception e) {
            Log.w(TAG, "writeLog2File日志写入错误");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static String getThrowableStr(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }
}
