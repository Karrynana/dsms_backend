package com.nenu.dsms.def.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public class DsmsException extends RuntimeException{

    private final DsmsExceptionDef def;

    public DsmsException(DsmsExceptionDef def) {
        this.def = def;
    }

    @Override
    public String toString() {
        return "DsmsException{" +
                "def=" + def +
                '}';
    }
}
