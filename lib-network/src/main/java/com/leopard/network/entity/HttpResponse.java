package com.leopard.network.entity;

/*********************************************
 * @author JokerFish
 * @date 2018-08-28
 * @description
 * @version v1.0
 **********************************************/
public class HttpResponse<T> {
    private boolean isSuccess;
    private int code;
    private String message;
    private T data;
    private boolean canRetry;
    private String debugInfo;


    public boolean isSuccess() {
        return code == 0;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isCanRetry() {
        return canRetry;
    }

    public void setCanRetry(boolean canRetry) {
        this.canRetry = canRetry;
    }

    public String getDebugInfo() {
        return debugInfo;
    }

    public void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }
}
