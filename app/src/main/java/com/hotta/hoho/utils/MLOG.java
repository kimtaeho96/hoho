package com.hotta.hoho.utils;

import android.util.Log;

import com.hotta.hoho.Const_Ho;


public class MLOG {

    public static void v(String tag, String msg) {
        if (Const_Ho.USE_LOG) {
            Log.v(tag, buildLogMsg(msg));
        }
    }

    public static void d(String tag, String msg) {
        if (Const_Ho.USE_LOG) {
            Log.d(tag, buildLogMsg(msg));
        }
    }

    public static void i(String tag, String msg) {
        if (Const_Ho.USE_LOG) {
            Log.i(tag, buildLogMsg(msg));
        }
    }

    public static void w(String tag, String msg) {
        if (Const_Ho.USE_LOG) {
            Log.w(tag, buildLogMsg(msg));
        }
    }

    public static void e(String tag, String msg) {
        if (Const_Ho.USE_LOG) {
            Log.e(tag, buildLogMsg(msg));
        }
    }

    public static String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append("]");
        sb.append(message);
        return sb.toString();
    }


    public static void LogLineLog(String tag, String str) {
        if (Const_Ho.USE_LOG) {
            if (str.length() > 3000) {    // 텍스트가 3000자 이상이 넘어가면 줄
                Log.d(tag, str.substring(0, 3000));
                LogLineLog(tag, str.substring(3000));
            } else {
                Log.d(tag, str);
            }
        }
    }


}
