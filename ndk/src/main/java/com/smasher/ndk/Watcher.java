package com.smasher.ndk;

/**
 * @author matao
 * @date 2019/5/28
 */
public class Watcher {


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    public native void createWatcher(String userId);


    public native void connectMonitor();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
