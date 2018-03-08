package cn.share.jack.cyghttp;

import io.reactivex.functions.Function;

/**
 * 变换: 将服务器返回的原始数据剥离成我们最终想要的数据
 * 将BaseResponse<T> 转换成 T
 * 处理所有请求码包含解析异常
 */

public class HttpFunction<T> implements Function<BaseResponse<T>, T> {

    @Override
    public T apply(BaseResponse<T> response) throws Exception {
        //除了回调正确的成功code以外都视为请求失败
        if (!response.isRequestSuccess()) {
            throw new ApiException(response.getStatus(), String.valueOf(response.getMessage()));
        }

        return (T) response.getData();
    }
}