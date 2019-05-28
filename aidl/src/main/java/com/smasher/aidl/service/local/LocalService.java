package com.smasher.aidl.service.local;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.smasher.aidl.IMyAidlInterface;
import com.smasher.aidl.activity.Constant;

/**
 * @author matao
 */
public class LocalService extends Service {
    public LocalService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    Binder mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getTestInt() throws RemoteException {
            return Constant.TEST_INT;
        }
    };
}
