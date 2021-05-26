package com.nenu.dsms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.Gson;
import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.entity.TUser;
import com.nenu.dsms.entity.TUserProcessList;
import com.nenu.dsms.mapper.TUserMapper;
import com.nenu.dsms.service.ITUserProcessListService;
import com.nenu.dsms.service.ITUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nenu.dsms.util.CosUtil;
import com.nenu.dsms.util.FileUtil;
import com.nenu.dsms.vo.base.UserInfo;
import com.nenu.dsms.vo.response.UserInfoResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chrisyxiao
 * @since 2021-04-19
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

    @Autowired
    ITUserProcessListService userProcessService;

    @Override
    public String getVisibleUsers() {

        String role = DsmsContext.currentUser().getRole();
        Gson gson = new Gson();
        List<TUser> users = new ArrayList<>();

        if (role.equals("admin")) {
            users = this.list();
        }

        if (role.equals("teacher")) {
            users = this.list(Wrappers.lambdaQuery(TUser.class).in(TUser::getRole, "student", "coach"));
        }

        if (role.equals("coach")) {
            List<TUserProcessList> records = userProcessService.list(Wrappers.lambdaQuery(TUserProcessList.class)
                .eq(TUserProcessList::getCoachId, DsmsContext.currentUser().getId()));

            users = list(Wrappers.lambdaQuery(TUser.class)
                    .in(TUser::getId, records.stream().map(TUserProcessList::getStuId)));

            HashMap<String, List<TUser>> map = new HashMap<>();
            map.put("student", users);
            return gson.toJson(map);
        }

        return gson.toJson(listToRoleMap(users));
    }

    @Override
    public UserInfoResponseVO getCurUserInfo() {
        UserInfoResponseVO userInfoResponseVO = new UserInfoResponseVO();
        UserInfo userInfo = DsmsContext.currentUser();

        // 当前用户
        TUser u = getById(userInfo.getId());
        BeanUtils.copyProperties(u, userInfoResponseVO);
        userInfoResponseVO.setAvatar(CosUtil.INSTANCE.getDownloadSign(u.getAvatar()));

        // 如果是管理员不要添加creator信息
        if (u.getRole().equals("admin")) {
            return userInfoResponseVO;
        }

        // 当前用户的创建者
        TUser creator = getById(u.getCreator());
        UserInfoResponseVO creatorRespVO = new UserInfoResponseVO();
        BeanUtils.copyProperties(creator, creatorRespVO);
        // 创建者的创建者置为空
        creatorRespVO.setCreator(null);
        userInfoResponseVO.setCreator(creatorRespVO);
        return userInfoResponseVO;
    }

    @Override
    public void updateAvatar(MultipartFile avatarFile) {
        try {
            String path = CosUtil.INSTANCE.put(DsmsContext.currentUser().getAccount(), FileUtil.multipartFileToFile(avatarFile));
            TUser user = new TUser();
            user.setAvatar(DsmsContext.currentUser().getAccount());
            updateById(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerUser(TUser user, MultipartFile avatarFile) {
        // 记录创建时间
        user.setCreateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        // 记录创建者
        user.setCreator(DsmsContext.currentUser().getId());

        // 如果有头像
        if (!Objects.isNull(avatarFile)) {
            try {
                // 上传头像
                String put = CosUtil.INSTANCE.put(user.getAccount(), FileUtil.multipartFileToFile(avatarFile));
                // 记录头像key
                user.setAvatar(user.getAccount());
            } catch (IOException e) {
                throw new DsmsException(DsmsExceptionDef.FILE_SAVE_ERR);
            }
        }

        // 真正保存用户
        save(user);
    }

    private Map<String, List<TUser>> listToRoleMap(List<TUser> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }

        return list.stream().collect(Collectors.toMap(TUser::getRole,
                (user) -> {
                    List<TUser> tUsers = new ArrayList<>();
                    tUsers.add(user);
                    return tUsers;
                },
                (v1, v2) -> {
                    v1.addAll(v2);
                    return v1;
                }));
    }
}
