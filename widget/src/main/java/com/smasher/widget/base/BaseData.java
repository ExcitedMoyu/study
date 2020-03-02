package com.smasher.widget.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author matao
 * @date 2019/3/29
 */
public class BaseData<T> implements Serializable {
    public static final long serialVersionUID = 1314L;

    @SerializedName("status")
    private boolean status;

    @SerializedName(value = "code")
    private int Result;

    private String ResultCode;

    @SerializedName(value = "message")
    private String Message;

    @SerializedName(value = "result")
    private T Data;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }


    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }
}
