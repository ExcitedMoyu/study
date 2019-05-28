package com.smasher.ndk;

/**
 * @author matao
 * @date 2019/5/28
 */
public class Java2CJNI {


    static {
        System.loadLibrary("Java2C");
    }

    public native String java2C();
}
