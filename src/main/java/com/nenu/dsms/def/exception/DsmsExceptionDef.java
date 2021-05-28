package com.nenu.dsms.def.exception;

import lombok.Getter;

@Getter
public enum DsmsExceptionDef {

    SYSTEM_BUSY(-1, "系统繁忙"),

    // 授权异常
    PERMISSION_DENIED(15001, "无权访问"),
    USER_NOT_LOGIN(15010, "用户未登录"),
    NO_SUCH_USER(15011, "用户名不存在"),
    INVALID_PASSWORD(15011, "密码错误"),
    USER_LOGIN_STATUS_ERR(15012, "用户登录态异常, 可能是口令被篡改, 请重新登录或联系管理员"),
    TOKEN_EXPIRED(15013, "用户身份过期 请重新登录"),

    // 参数异常
    INVALID_PARAM(15051, "入参校验失败"),

    // 数据异常
    USER_DATA_INVALID(15060, "用户数据异常，请联系管理员"),
    NO_MSG_FOUND(15061, "消息不存在"),
    SYSTEM_DATA_ERR(15062, "系统数据异常，请联系管理员"),
    PROCESS_ACTIVED(15063, "已存在流程中的学习记录，请完成学习后再试"),

    // 文件异常
    FILE_SAVE_ERR(15071, "文件上传异常");

    private final Integer code;
    private final String msg;

    DsmsExceptionDef(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
