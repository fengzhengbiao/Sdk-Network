package com.leopard.network.entity;

import android.text.TextUtils;

/*********************************************
 * @author JokerFish
 * @date 2018-08-28
 * @description
 * @version v1.0
 **********************************************/
public class HttpResponse<T> {


    private int code;
    private T data;
    private String status;
    private String message;
    public static final String SUCCESS = "success";

    public boolean isSuccess() {
        return SUCCESS.equals(status) || code == 200;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
