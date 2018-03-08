package cn.share.jack.cyghttp;

/**
 * 异常处理的一个类
 */
public class  ApiException extends RuntimeException {

    private long mErrorCode;

    public ApiException(long errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExpried() {
        return mErrorCode == ConstantCode.EXCEPTION_TOKEN;
    }
}
