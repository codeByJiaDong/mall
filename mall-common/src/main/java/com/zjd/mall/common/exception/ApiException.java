package com.zjd.mall.common.exception;


import com.zjd.mall.common.api.IErrorCode;

/**
 * @author zjd
 * @Description
 * @date 2020/4/15 10:17
 */

public class ApiException extends RuntimeException {
    private IErrorCode iErrorCode;

    public ApiException( IErrorCode iErrorCode) {
        super(iErrorCode.getMessage());
        this.iErrorCode = iErrorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return iErrorCode;
    }
}
