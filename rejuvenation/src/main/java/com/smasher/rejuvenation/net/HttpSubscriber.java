package com.smasher.rejuvenation.net;

import android.util.Log;


import com.smasher.widget.base.BaseData;
import com.smasher.rejuvenation.net.exception.ApiException;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author Smasher
 * on 2020/2/28 0028
 */
public class HttpSubscriber<T> implements Subscriber<BaseData<T>> {

    private static final String TAG = "HttpSubscriber";


    @Override
    public void onSubscribe(Subscription subscription) {
        Log.d(TAG, "onSubscribe: ");
        subscription.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(BaseData<T> baseData) {
        if (baseData == null) {

        } else {

        }

    }

    @Override
    public void onError(Throwable e) {
        ApiException exception = ExceptionEngine.handleException(e);
        getErrorDto(exception);
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: ");
    }


    public void getErrorDto(ApiException ex) {
        BaseData data = new BaseData();
        data.setMessage(ex.getMessage());
        data.setResultCode(ex.getStatusCode());
    }
}
