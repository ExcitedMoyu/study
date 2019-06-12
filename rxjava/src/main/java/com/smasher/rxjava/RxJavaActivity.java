package com.smasher.rxjava;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smasher.oa.core.thread.ThreadPool;
import com.smasher.oa.core.utils.StatusBarUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.NewThreadScheduler;
import io.reactivex.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity {


    private static final String TAG = "RxJavaActivity";
    StringBuilder mStringBuilder;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        StatusBarUtil.setTranslucent(this);

        mTextView = findViewById(R.id.content);
        mStringBuilder = new StringBuilder();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                test();
            }
        });

    }


    public void test() {

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: " + d.isDisposed());
            }

            @Override
            public void onNext(String o) {
                Log.d(TAG, "observer onNext: " + o);
                mStringBuilder.append(o);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "observer onComplete: ");
                mStringBuilder.append("\n\n");
                String test = mStringBuilder.toString();
                mTextView.setText(test);
            }
        };


        CompletableObserver completableObserver = new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "completable onSubscribe: " + d.isDisposed());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "completable onComplete: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "completable onError: ");
            }
        };


        String just = "i'm from just\n";
        Observable<String> observable0 = Observable.just(just);

        String[] test = {"hello ", " sss", " in the world"};
        Observable<String> observable = Observable.fromArray(test);


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
            }
        };
        ThreadPool.getInstance(ThreadPool.PRIORITY_HIGH).submit(runnable);
        Completable observable2 = Completable.fromRunnable(runnable);


        ObservableOnSubscribe<String> observableOnSubscribe = new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                emitter.onNext("from Create");
                emitter.onNext("from Create");
                emitter.onNext("from Create");
                emitter.onComplete();
            }
        };
        Observable<String> observable3 = Observable.create(observableOnSubscribe);


        observable0
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        observable2.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableObserver);


        Disposable disposable = observable3
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG, "Action run: ");
            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Log.d(TAG, "Consumer accept: " + disposable.isDisposed());
            }
        });
    }
}
