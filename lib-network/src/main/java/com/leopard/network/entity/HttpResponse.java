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
    private String code;
    private T data;
    private String status;
    private String message;


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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
