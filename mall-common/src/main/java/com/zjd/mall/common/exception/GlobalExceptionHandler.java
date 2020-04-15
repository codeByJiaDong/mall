package com.zjd.mall.common.exception;


import com.zjd.mall.common.api.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zjd
 * @Description 全局异常处理
 * @date 2020/4/15 10:23
 */
@ControllerAdvice
public class GlobalExceptionHandler
{
    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResult handle(ApiException e){
        if (e.getErrorCode() != null){
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getErrorCode());
    }
}
