package com.nenu.dsms.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.nenu.dsms.vo.base.UserInfo;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

// 口令工具类
public class JWTUtil {

    private static final String secret = "join666";
    private static final Algorithm algorithm = Algorithm.HMAC256(secret);
    private static final JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer("dsms").withClaimPresence("user").build();

    public static String createUserToken(UserInfo userInfo) {
        Gson gson = new Gson();
        return JWT.create()
                .withIssuer("dsms")
                .withClaim("user", gson.toJson(userInfo))
                .withExpiresAt(Date.from(LocalDateTime.now().plusMinutes(120).atZone(ZoneId.systemDefault()).toInstant()))
                .sign(algorithm);
    }

    public static DecodedJWT getJWTFromString(String str) {
        assert !StringUtils.hasLength(str) : "str witch format to JWT can't be null or empty";
        return jwtVerifier.verify(str);
    }
}
