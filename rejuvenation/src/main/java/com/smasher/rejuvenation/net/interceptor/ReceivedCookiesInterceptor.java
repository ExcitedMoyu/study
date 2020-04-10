package com.smasher.rejuvenation.net.interceptor;

import com.smasher.core.log.Logger;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Smasher
 * on 2020/2/28 0028
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
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
    }
}
