package com.smasher.caidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.smasher.caidlserver.BookController;

import java.util.List;

import com.smasher.caidlserver.Book;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Client";

    private BookController bookController;

    private boolean connected;

    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bookController = BookController.Stub.asInterface(iBinder);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connected = false;
        }
    };

    public void bindService() {
        Intent intent = new Intent();
        intent.setPackage("com.smasher.caidlserver");
        intent.setAction("com.smasher.caidlserver.action");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connected) {
            unbindService(serviceConnection);
        }
    }
}
