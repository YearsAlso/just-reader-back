package com.yearsalso.common.api;

/**
 * 错误展示类型，用于前端显示
 */
public enum ErrorShowType {
    SILENT(0, "不显示"),
    WARNING(1, "警告提示"),
    ERROR(2, "错误提示"),
    NOTIFICATION(3, "通知提示"),
    REDIRECT(9, "重定向");

    private Integer code;
    private String message;

    private ErrorShowType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
