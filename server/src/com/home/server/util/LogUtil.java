package com.home.server.util;

import java.util.Date;
import java.text.SimpleDateFormat;

public class LogUtil {

    public static int ERROR = 0;
    public static int INFO = 1;
    public static int DEBUG = 2;

    public static int LEVEL = 1;

    public static void printLog(String str, int level) {
        if (level <= LEVEL) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            System.out.println(format.format(date) + " " + str);
        }
    }

}
