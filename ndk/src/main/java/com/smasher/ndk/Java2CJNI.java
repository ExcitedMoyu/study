package com.smasher.ndk;

/**
 * @author matao
 * @date 2019/5/28
 */
public class Java2CJNI {



    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


//    public native String java2C();
//
//
//    public native void close();


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


//    static {
//        System.loadLibrary("Java2C-jni");
//    }

}
