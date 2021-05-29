package com.nenu.dsms.filter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.util.JWTUtil;
import com.nenu.dsms.vo.base.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Component
@Slf4j
// 用户上下文拦截器
public class UserCtxInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (!Objects.isNull(((HandlerMethod)handler).getMethod().getAnnotation(NoSignIn.class))) {
            return true;
        }

        String token = request.getHeader("token");
        if (StringUtils.hasLength(token)) {
            DecodedJWT jwtFromString = JWTUtil.getJWTFromString(token);
            Claim user = jwtFromString.getClaim("user");
            if (user.isNull()) {
                throw new DsmsException(DsmsExceptionDef.USER_NOT_LOGIN);
            }

            UserInfo userInfo;
            try {
                Gson gson = new Gson();
                userInfo = gson.fromJson(user.asString(), UserInfo.class);

                if (jwtFromString.getExpiresAt().before(Date.from(LocalDateTime.now().minusHours(1).atZone(ZoneId.of("+8")).toInstant()))) {
                    String userToken = JWTUtil.createUserToken(userInfo);
                    response.addHeader("token", userToken);
                    response.addHeader("Access-Control-Expose-Headers","token");
                }
            } catch (JsonSyntaxException e) {
                throw new DsmsException(DsmsExceptionDef.USER_LOGIN_STATUS_ERR);
            }
            DsmsContext.setUser(userInfo);
            return true;
        }
        throw new DsmsException(DsmsExceptionDef.USER_NOT_LOGIN);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        DsmsContext.setUser(null);
    }
}
