package cn.share.jack.cyghttp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 服务器返回的json基类
 */
public class BaseResponse<T> implements Serializable {

    @SerializedName("status")
    private long status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private HttpData<T> data;


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

    public HttpData<T> getData() {
        return data;
    }

    public void setData(HttpData<T> data) {
        this.data = data;
    }
}
