package com.smasher.study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


/**
 * @author matao
 */
public class MainActivity extends AppCompatActivity {

    @butterknife.BindView(R.id.hello)
    Button hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butterknife.ButterKnife.bind(this);

    }

    @butterknife.OnClick(R.id.hello)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setClass(this, FactoryActivity.class);
        startActivity(intent);
    }
}
