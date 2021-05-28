package com.nenu.dsms.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.entity.TUser;
import com.nenu.dsms.filter.NoSignIn;
import com.nenu.dsms.service.ITUserService;
import com.nenu.dsms.util.JWTUtil;
import com.nenu.dsms.vo.base.ResponseVO;
import com.nenu.dsms.vo.base.UserInfo;
import com.nenu.dsms.vo.response.UserInfoResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chrisyxiao
 * @since 2021-04-19
 */
@CrossOrigin
@RestController
@RequestMapping("/dsms/t-user")
public class TUserController {

    @Autowired
    ITUserService userService;

    @NoSignIn
    @GetMapping
    public ResponseVO login(String account, String password, HttpServletResponse resp) {

        // 查询用户信息
        TUser user = userService.getOne(Wrappers.lambdaQuery(TUser.class).eq(TUser::getAccount, account));

        // 用户不存在
        if (Objects.isNull(user)) {
            throw new DsmsException(DsmsExceptionDef.NO_SUCH_USER);
        }

        // 用户存在 对比密码
        if (user.getPassword().equals(password)) {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user, userInfo);
            String userToken = JWTUtil.createUserToken(userInfo);
            resp.addHeader("token", userToken);
            resp.addHeader("Access-Control-Expose-Headers","token");

            // 返回一个默认结果 ok
            return new ResponseVO();
        }
        // 返回密码错误
        throw new DsmsException(DsmsExceptionDef.INVALID_PASSWORD);
    }

    @GetMapping("/my")
    public UserInfoResponseVo getUserInfo() {
        return userService.getCurUserInfo();
    }

    /**
     * 用户注册
     * @param user 用户实体
     * @param avatarFile 用户头像
     */
    @PostMapping
    public void register(TUser user, MultipartFile avatarFile) {
        userService.registerUser(user, avatarFile);
    }

    @PostMapping("/avatar")
    public void updateAvatar(MultipartFile avatarFile) {

        if (Objects.isNull(avatarFile)) {
            throw new DsmsException(DsmsExceptionDef.SYSTEM_BUSY);
        }

        userService.updateAvatar(avatarFile);
    }

    @PutMapping
    public void updateUserInfo(@RequestBody TUser user) {
        userService.updateById(user);
    }
}
