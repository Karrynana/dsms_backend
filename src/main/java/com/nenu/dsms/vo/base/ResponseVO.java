package com.nenu.dsms.vo.base;

import com.nenu.dsms.def.exception.DsmsException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseVO {

    private Integer code;
    private String msg;
    private Object data;

    public ResponseVO() {
        this(0, "ok");
    }

    public ResponseVO(Object data) {
        this(0, "ok", data);
    }

    public ResponseVO(Integer code, String msg) {
        this(code, msg, null);
    }

    public ResponseVO(DsmsException me) {
        this(me.getDef().getCode(), me.getDef().getMsg());
    }
}
