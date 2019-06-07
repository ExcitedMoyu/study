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


public class ScrollingActivity extends AppCompatActivity {


    private static final String TAG = "ScrollingActivity";

    private Intent mIntent;
    private boolean isBind = false;


    private AppBarLayout mAppBar;
    private CollapsingToolbarLayout mToolbarLayout;
    private Toolbar mToolbar;
    private Button mBind;
    private Button mUnBind;
    private TextView mContent;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Constant.TEST_INT = 100;
        initView();
        mIntent = new Intent(this, RemoteService.class);
    }

    private void initView() {

        mAppBar = findViewById(R.id.app_bar);
        mToolbarLayout = findViewById(R.id.toolbar_layout);
        mToolbar = findViewById(R.id.toolbar);
        mBind = findViewById(R.id.bind);
        mUnBind = findViewById(R.id.unBind);
        mContent = findViewById(R.id.content);
        mFab = findViewById(R.id.fab);


        setSupportActionBar(mToolbar);

        initListener();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
