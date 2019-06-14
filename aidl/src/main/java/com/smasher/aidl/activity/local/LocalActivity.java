package com.smasher.aidl.activity.local;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smasher.aidl.R;
import com.smasher.aidl.activity.Constant;
import com.smasher.aidl.activity.remote.ui.login.LoginActivity;
import com.smasher.aidl.entity.Book;
import com.smasher.aidl.listener.ObserverListener;
import com.smasher.aidl.manager.IBookManager;
import com.smasher.aidl.service.remote.RemoteService;

import java.util.List;


public class LocalActivity extends AppCompatActivity {


    private static final String TAG = "LocalActivity";

    private Intent mIntent;
    private boolean isBind = false;


    private Toolbar mToolbar;
    private Button mBind;
    private Button mUnBind;
    private TextView mContent;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        Constant.TEST_INT = 100;
        initView();
        initListener();
        mIntent = new Intent(this, RemoteService.class);
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mBind = findViewById(R.id.bind);
        mUnBind = findViewById(R.id.unBind);
        mContent = findViewById(R.id.content);
        mFab = findViewById(R.id.fab);


        setSupportActionBar(mToolbar);


    }

    private void initListener() {
        mBind.setOnClickListener(mOnClickListener);
        mUnBind.setOnClickListener(mOnClickListener);
        mContent.setOnClickListener(mOnClickListener);
        mFab.setOnClickListener(mOnClickListener);
    }


    @Override
    protected void onStart() {
        super.onStart();
        startService(mIntent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        stopService(mIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            mIBookManager.unregisterListener(mObserverListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    View.OnClickListener mOnClickListener = view -> {
        int i = view.getId();
        if (i == R.id.bind) {
            isBind = bindMservice();
        } else if (i == R.id.unBind) {
            unBindMService();
        } else if (i == R.id.content) {
            addBooks();
        } else if (i == R.id.fab) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };


    private void addBooks() {
        try {


            int testInt = mIBookManager.getTestInt();
            Log.d(TAG, "addBooks: testInt=" + testInt);

            if (mIBookManager == null) {
                Log.d(TAG, "addBooks: mIbookManager is null");
                return;
            }
            mIBookManager.addBook(new Book("Android进阶之光"));
            mIBookManager.addBook(new Book("Android群英传"));
            mIBookManager.addBook(new Book("Android开发艺术探索"));
            Log.d(TAG, "addBooks: " + mIBookManager.getBookList().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean bindMservice() {
        return bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    private void unBindMService() {
        Log.d(TAG, "unBindMService: isBind " + isBind);
        try {
            if (isBind) {
                unbindService(mConnection);
                isBind = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IBookManager mIBookManager;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            try {
                mIBookManager = IBookManager.Stub.asInterface(service);
                mIBookManager.registerListener(mObserverListener);
                List<Book> bookList = mIBookManager.getBookList();
                Log.d(TAG, "全部书籍: " + bookList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        /**
         * 当service被kill
         * @param name name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: " + isBind);
            mIBookManager = null;
        }
    };

    ObserverListener mObserverListener = new ObserverListener.Stub() {

        @Override
        public void onNewBookAdd(Book book) throws RemoteException {
            //mIBookManager.addBook(book);
            Log.d("onNewBookAdd", "onNewBookAdd: ");

        }

        @Override
        public void onAllBook() throws RemoteException {
            List<Book> bookList = mIBookManager.getBookList();
            Log.i("onAllBook", "onAllBook: " + bookList.toString());

        }
    };
}
