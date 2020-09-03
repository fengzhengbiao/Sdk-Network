package com.leopard.network.entity;

import android.text.TextUtils;

/*********************************************
 * @author JokerFish
 * @date 2018-08-28
 * @description
 * @version v1.0
 **********************************************/
public class HttpResponse<T> {

    public static final String SUCCESS = "success";
    private String status;
    private T data;


    public boolean isSuccess() {
        return SUCCESS.equals(status);
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
