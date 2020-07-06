package com.leopard.network.intercepter

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

class ReplaceInterceptor : Interceptor {

    val EMPTYSTRING = ":\"\"";
    val EMPTYOBJECT = ":{}";
    val EMPTYARRAY = ":[]";
    val NEWCHARS = ":null";


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request);
        val responseBody = response.body;
        if (responseBody != null) {
            val json = responseBody.string();
            val contentType = responseBody.contentType();
            if (!json.contains(EMPTYSTRING)) {
                val body = ResponseBody.create(contentType, json);
                return response.newBuilder().body(body).build();
            } else {
                val replace = json.replace(EMPTYSTRING, NEWCHARS);
                val replace1 = replace.replace(EMPTYOBJECT, NEWCHARS);
                val replace2 = replace1.replace(EMPTYARRAY, NEWCHARS);
                val body = ResponseBody.create(contentType, replace2);
                return response.newBuilder().body(body).build();
            }
        }
        return response;
    }
}