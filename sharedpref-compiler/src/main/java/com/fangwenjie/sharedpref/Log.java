package com.fangwenjie.sharedpref;

/**
 * Created by fangwenjie on 2020/4/15
 */
public class Log {
    public static void d(String tag, String content) {
        System.out.println(String.format("--%s--:::%s", tag, content));
    }
}
