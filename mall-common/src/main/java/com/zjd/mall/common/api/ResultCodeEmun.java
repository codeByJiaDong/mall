package com.zjd.mall.common.api;


/**
 * @author zjd
 * @description  枚举常用的API 操作码
 * @date 2020/4/15 9:25
 */

public enum ResultCodeEmun  implements IErrorCode{
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    private long code;
    private String messgae;


    ResultCodeEmun(long code, String messgae) {
        this.code = code;
        this.messgae = messgae;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return messgae;
    }
}
