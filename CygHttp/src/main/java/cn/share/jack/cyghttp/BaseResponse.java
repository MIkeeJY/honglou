package cn.share.jack.cyghttp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 服务器返回的json基类
 */
public class BaseResponse<T> implements Serializable {

    @SerializedName("code")
    private long status;

    @SerializedName("msg")
    private String message;

    @SerializedName("data")
    T data;


    /**
     * API是否请求成功
     *
     * @return 成功返回true, 失败返回false
     */
    public boolean isRequestSuccess() {
        return status == ConstantCode.REQUEST_SUCCESS || status == ConstantCode.REQUEST_SUCCESS_LOGIN;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
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
}
