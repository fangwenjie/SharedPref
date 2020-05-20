package com.fangwenjie.sharedpref.internal.exception;

/**
 * 检查Api版本 和 编译器的版本 是否匹配
 *
 * Created by fangwenjie on 2020/4/8
 */
public class VersionMismatchException extends Exception {
    private static final long serialVersionUID = 1457334941140141471L;

    public VersionMismatchException(){
        super();
    }

    public VersionMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionMismatchException(String message) {
        super(message);
    }

    public VersionMismatchException(Throwable cause) {
        super(cause);
    }
}
