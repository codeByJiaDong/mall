package com.zjd.mall.common.exception;


import com.zjd.mall.common.api.IErrorCode;

/**
 * @author zjd
 * @Description 断言处理类，用于抛出各种异常
 * @date 2020/4/15 10:20
 */

public class Asserts
{
    public static void fail(String message){
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode){
        throw new ApiException(errorCode);
    }

}
