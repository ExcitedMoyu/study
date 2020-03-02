package com.smasher.widget.net.interceptor;

import com.smasher.core.log.Logger;
import com.smasher.core.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 云端响应头拦截器，用来配置缓存策略
 * Dangerous interceptor that rewrites the server's cache-control header.
 *
 * @author Smasher
 * on 2020/2/28 0028
 */
public class AddCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request newRequest;
        if (!NetworkUtil.isNetworkAvailable()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Logger.d("no network");
        }

        if (NetworkUtil.isNetworkAvailable()) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            //UserManager.getInstance().getUserToken()
            String token = "Bearer ";
            String cacheControl = request.cacheControl().toString();
            newRequest = request.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .header("Authorization", token)
                    .removeHeader("Pragma")
                    .build();
            return chain.proceed(newRequest);
        } else {
            newRequest = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 0)
                    .removeHeader("Pragma")
                    .build();
            return chain.proceed(newRequest);
        }
    }
}
