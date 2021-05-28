package com.nenu.dsms.service;

import com.nenu.dsms.entity.TUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nenu.dsms.vo.response.UserInfoResponseVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chrisyxiao
 * @since 2021-04-19
 */
public interface ITUserService extends IService<TUser> {

    String getVisibleUsers();

    UserInfoResponseVo getCurUserInfo();

    void updateAvatar(MultipartFile avatarFile);

    void registerUser(TUser user, MultipartFile avatarFile);
}
