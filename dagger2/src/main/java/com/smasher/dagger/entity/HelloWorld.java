package com.smasher.dagger.entity;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * @author matao
 * @date 2019/5/27
 */
public class HelloWorld {


    public SellMoe mSellMoe;

    public HelloWorld() {
    }


    @Inject
    public HelloWorld(SellMoe sellMoe) {
        mSellMoe = sellMoe;
    }

    public void sayHello(Context context) {
        Toast.makeText(context, "Hello World !", Toast.LENGTH_SHORT).show();
    }


    public SellMoe getSellMoe() {
        return mSellMoe;
    }

    public void setSellMoe(SellMoe sellMoe) {
        mSellMoe = sellMoe;
    }
}
