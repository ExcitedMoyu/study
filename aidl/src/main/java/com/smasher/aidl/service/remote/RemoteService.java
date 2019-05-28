package com.smasher.aidl.service.remote;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.smasher.aidl.activity.Constant;
import com.smasher.aidl.entity.Book;
import com.smasher.aidl.listener.ObserverListener;
import com.smasher.aidl.manager.IBookManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author matao
 */
public class RemoteService extends Service {

    public static final String TAG = "RemoteService";

    private List<Book> mBookList;

    private List<ObserverListener> mListeners;


    public RemoteService() {
        mBookList = new ArrayList<>();
        mListeners = new ArrayList<>();

    }


    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            Log.d(TAG, "addBook: " + book.toString());
            Log.d(TAG, "addBook: " + mBookList.toString());

        }

        @Override
        public void registerListener(ObserverListener listener) throws RemoteException {
            if (!mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }

        @Override
        public void unregisterListener(ObserverListener listener) throws RemoteException {
            if (mListeners.contains(listener)) {
                mListeners.remove(listener);
            }
        }

        @Override
        public int getTestInt() throws RemoteException {
            return Constant.TEST_INT;
        }
    };
}
