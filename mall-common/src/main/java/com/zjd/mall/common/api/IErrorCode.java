package com.zjd.mall.common.api;


/**
 * @author zjd
 * @Description
 * @date 2020/4/15 9:21
 */

public interface IErrorCode {
    /**
     * 返回 状态码
     * @return
     */
    long getCode();

    /**
     * 返回 错误提示信息
     * @return
     */
    String getMessage();

}
