package com.smasher.rejuvenation.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smasher.core.log.Logger;
import com.smasher.core.other.ApplicationContext;
import com.smasher.core.utils.NetworkUtil;
import com.smasher.rejuvenation.config.Config;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {

    private static HttpUtil ourInstance;
    private static Gson gson;

    private OkHttpClient sOkHttpClient;
    private Retrofit retrofit;
    private Retrofit gatewayRetrofit;
    private Retrofit wxRetrofit;
    private Retrofit testRetrofit;

    public static HttpUtil getInstance() {
        gson = new GsonBuilder().create();
        if (ourInstance == null) {
            ourInstance = new HttpUtil();
        }
        return ourInstance;
    }

    /**
     * 设缓存有效期为两天
     */
    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    public static final String CACHE_CONTROL_AGE = "max-age=0";


    public void update() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient())
                .build();
    }


    private HttpUtil() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient())
                .build();

        gatewayRetrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_GATEWAY_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient())
                .build();
        wxRetrofit = new Retrofit.Builder()
                .baseUrl(Config.BASEWX_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient())
                .build();


        testRetrofit = new Retrofit.Builder()
                .baseUrl(Config.TEST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient())
                .build();
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }

    public <T> T createGateWay(Class<T> service) {
        return gatewayRetrofit.create(service);
    }

    public <T> T createWX(Class<T> service) {
        return wxRetrofit.create(service);
    }

    public <T> T createTest(Class<T> service) {
        return testRetrofit.create(service);
    }


    private OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (HttpUtil.class) {
                Cache cache = new Cache(new File(ApplicationContext.getInstance().getCacheDir(), "HttpCache"),
                        1024 * 1024 * 100);
                if (sOkHttpClient == null) {
                    sOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .connectTimeout(6, TimeUnit.SECONDS)
                            .readTimeout(6, TimeUnit.SECONDS)
                            .writeTimeout(6, TimeUnit.SECONDS)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mLoggingInterceptor)
                            .build();
                }
            }
        }
        return sOkHttpClient;
    }


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = chain -> {
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
    };

    private final Interceptor mLoggingInterceptor = chain -> {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Logger.i(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        Logger.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        MediaType mediaType = null;
        String resp = "";
        if (response.body() != null) {
            mediaType = response.body().contentType();
            resp = response.body().string();
            Logger.json(resp);
        }
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, resp))
                .build();

        //return response;
    };
}
