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
    private boolean status;
    private String message;

    public boolean isSuccess(){
        return code==200;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
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
