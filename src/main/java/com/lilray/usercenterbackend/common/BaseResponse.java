package com.lilray.usercenterbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回对象
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 1879729317660543482L;

    private int code;

    private String message;

    private T data;

    private String description;

    public BaseResponse(int code, T data ,String message,String description) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code,  data, "","");
    }

    public BaseResponse(int code, T data,String message) {
        this(code,  data, message,"");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public BaseResponse(int code,String message,String description) {
        this(code, null, message, description);
    }
}
