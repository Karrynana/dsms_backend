package com.nenu.dsms.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.vo.base.ResponseVO;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
// 返回值和异常处理拦截器
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return Objects.isNull(methodParameter.getMethodAnnotation(DontFormat.class));
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (Objects.isNull(o)) {
            return new ResponseVO();
        }

        if (o instanceof LinkedHashMap) {
            Map<String, Object> m = (LinkedHashMap<String, Object>) o;
            Integer status = (Integer) m.get("status");
            if (status != 200) {
                return new Gson().toJson(new ResponseVO(status, (String) m.get("error"), null));
            }
            throw new DsmsException(DsmsExceptionDef.SYSTEM_BUSY);
        }

        if (o instanceof ResponseVO) {
            return o;
        }

        return new Gson().toJson(new ResponseVO(o));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DsmsException.class)
    public ResponseVO DsmsExceptionHandle(DsmsException dsmsException) {
        dsmsException.printStackTrace();
        return new ResponseVO(dsmsException);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseVO TokenExpiredExceptionHandler(TokenExpiredException tokenExpiredException) {
        tokenExpiredException.printStackTrace();
        return new ResponseVO(new DsmsException(DsmsExceptionDef.TOKEN_EXPIRED));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseVO ConstrainViolationExceptionHandler(ConstraintViolationException constraintViolationException) {
        constraintViolationException.printStackTrace();
        return new ResponseVO(new DsmsException(DsmsExceptionDef.INVALID_PARAM));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseVO DefaultExceptionHandler(Exception e) {
        e.printStackTrace();
        return new ResponseVO(new DsmsException(DsmsExceptionDef.SYSTEM_BUSY));
    }
}
