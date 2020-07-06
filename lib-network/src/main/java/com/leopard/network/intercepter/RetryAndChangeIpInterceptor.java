package com.cash.pinjaman.net;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cash.pinjaman.entity.java.UrlMapping;
import com.cash.pinjaman.util.StringExtentionKt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JokerFish on 2018-09-19.
 */

public class RetryAndChangeIpInterceptor implements Interceptor {
    String FirstIP;
    List<String> SERVERS = new ArrayList<>();

    public RetryAndChangeIpInterceptor() {
        FirstIP = UrlKt.BASE_ADDRESS;
    }

    public RetryAndChangeIpInterceptor(int tryCount) {
        FirstIP =UrlKt.BASE_ADDRESS;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (SERVERS == null || SERVERS.size() == 0) {
            Request post = new Request.Builder().url(UrlKt.getURL_MAPPING()).method("GET", null)
                    .build();
            Response response = chain.proceed(post);
            String urlStrs = response.body().string();
            if (!TextUtils.isEmpty(urlStrs)) {
                String decrypt = StringExtentionKt.decrypt(urlStrs, UrlKt.IV, UrlKt.KEY);
                Log.e("DECRYPT", decrypt);
                UrlMapping mapping = JSON.parseObject(decrypt, UrlMapping.class);
                if (mapping != null && mapping.getUrls().size() > 0) {
                    SERVERS = mapping.getUrls();
                }
            }
        }
        Request request = chain.request();
        if (!TextUtils.isEmpty(currentServer)) {
            switchServer(request.url(), currentServer);
        }
        Response response = doRequest(chain, request);
        int tryCount = 0;
        while (response == null && tryCount < SERVERS.size()) {
            HttpUrl url = switchServer(request.url(), tryCount);
            Request newRequest = request.newBuilder().url(url).build();
            Log.d("intercept", "Request is not successful - " + tryCount + " new Url: " + url);
            tryCount++;
            // retry the request
            response = doRequest(chain, newRequest);
            if (response != null) {
                currentServer = SERVERS.get((tryCount) % SERVERS.size());
            }
        }
        if (response == null) {
            throw new IOException();
        }
        return response;
    }

    private String currentServer;

    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    private HttpUrl switchServer(HttpUrl url, int count) {
        String newServer = SERVERS.get((count) % SERVERS.size());
        HttpUrl newServerUrl = HttpUrl.parse(newServer);
        return url.newBuilder().scheme(newServerUrl.scheme()).host(newServerUrl.host()).build();
    }

    private HttpUrl switchServer(HttpUrl url, String newServer) {
        HttpUrl newServerUrl = HttpUrl.parse(newServer);
        return url.newBuilder().scheme(newServerUrl.scheme()).host(newServerUrl.host()).build();
    }

}
