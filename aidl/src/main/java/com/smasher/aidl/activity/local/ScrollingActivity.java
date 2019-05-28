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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ScrollingActivity extends AppCompatActivity {


    private static final String TAG = "ScrollingActivity";
    @BindView(R.id.bind)
    Button bind;
    @BindView(R.id.unBind)
    Button unBind;
    @BindView(R.id.content)
    TextView content;

    private Unbinder mUnbinder;
    private Intent mIntent;
    private boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Constant.TEST_INT = 100;
        mUnbinder = ButterKnife.bind(this);
        initView();
        mIntent = new Intent(this, RemoteService.class);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        mUnbinder.unbind();

        try {
            mIBookManager.unregisterListener(mObserverListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.bind, R.id.unBind, R.id.content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bind:
                isBind = bindMservice();
                break;
            case R.id.unBind:
                unBindMService();
                break;
            case R.id.content:
                addBooks();
                break;
            default:
                break;
        }
    }

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
