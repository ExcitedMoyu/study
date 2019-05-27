package com.smasher.dagger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.smasher.dagger.DaggerApplication;
import com.smasher.dagger.R;
import com.smasher.dagger.entity.HelloWorld;
import com.smasher.dagger.entity.SellMoe;
import com.smasher.dagger.injection.component.OkHttpComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;


/**
 * @author matao
 */
public class DaggerActivity extends AppCompatActivity {

    private static final String TAG = "DaggerActivity";
    @BindView(R.id.hello)
    Button hello;

    @Inject
    HelloWorld mHelloWorld;

    @Inject
    SellMoe mProduct;

    @Inject
    OkHttpClient mOkHttpClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butterknife.ButterKnife.bind(this);

        DaggerApplication application = (DaggerApplication) getApplication();
        OkHttpComponent okHttpComponent = application.getOkHttpComponent();
        okHttpComponent.getActivityComponent().inject(this);

        mHelloWorld.sayHello(this);
        mProduct.setName("測試");

    }

    @OnClick(R.id.hello)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setClass(this, AlarmActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            Log.d(TAG, "onResume: " + mOkHttpClient.toString());
            Log.d(TAG, "onResume: " + mProduct.getName());
            Log.d(TAG, "onResume: mHelloWorld:" + mHelloWorld.getSellMoe().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
