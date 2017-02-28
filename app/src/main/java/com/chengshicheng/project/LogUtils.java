package com.chengshicheng.project;

import android.util.Log;

/**
 * Created by chengshicheng on 2017/2/26.
 */

public class LogUtils {
    /**
     * isDebug，是否为开发版本，否则不输出任何日志
     */
    public static final boolean isDebug = true;

    public static final String VERBOSE = "v";

    public static final String DEBUG = "d";

    public static final String INFO = "i";

    public static final String WARN = "w";

    public static final String ERROR = "E";

    public static String APPTag = "courierquery";

    public static void PrintDebug(String describe) {
        if (isDebug) {
            Log.d(APPTag, describe);
        }
    }

    public static void Print(String describe, String LogLevel) {
        if (isDebug) {
            switch (LogLevel) {
                case VERBOSE:
                    Log.v(APPTag, describe);
                    break;
                case DEBUG:
                    Log.d(APPTag, describe);
                    break;
                case INFO:
                    Log.i(APPTag, describe);
                    break;
                case WARN:
                    Log.w(APPTag, describe);
                    break;
                case ERROR:
                    Log.e(APPTag, describe);
                    break;
                default:
                    break;
            }
        }
    }

    public static void PrintError(String describe, Throwable e) {
        if (isDebug) {
            Log.e(APPTag, buildMessage(describe), e);
        }
    }

    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        return new StringBuilder().append(caller.getClassName()).append(".").append(caller.getMethodName()).append("(): ").append(msg).toString();
    }
}
