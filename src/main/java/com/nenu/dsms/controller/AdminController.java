package com.nenu.dsms.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.def.RoleDef;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.entity.TUser;
import com.nenu.dsms.service.ITUserService;
import com.nenu.dsms.vo.base.CreatorVO;
import com.nenu.dsms.vo.base.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("/dsms/admin")
public class AdminController {

    @Autowired
    ITUserService userService;

    @GetMapping("teacher")
    public List<UserInfo> getTeachers() {
        List<TUser> teachers = userService.list(Wrappers.lambdaQuery(TUser.class).eq(TUser::getRole, RoleDef.TEACHER));

        List<TUser> creators = userService.list(Wrappers.lambdaQuery(TUser.class)
                .in(TUser::getId, teachers.stream().map(TUser::getCreator)
                        .collect(Collectors.toList())));

        Map<Integer, CreatorVO> teacherMap = creators.stream().collect(Collectors.toMap(TUser::getId,
                (creator) -> {
                    CreatorVO creatorVO = new CreatorVO();
                    BeanUtils.copyProperties(creator, creatorVO);
                    return creatorVO;
                }));

        return teachers.stream().map((teacher -> {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(teacher, userInfo);
            userInfo.setCreator(teacherMap.get(teacher.getCreator()));
            return userInfo;
        })).collect(Collectors.toList());
    }

    @PostMapping("user")
    public void addUser(@RequestBody TUser user) {
        user.setCreator(DsmsContext.currentUser().getId());
        user.setCreateTime(System.currentTimeMillis());
        userService.save(user);
    }

    @PutMapping("teacher")
    public void updateTeacher(TUser user) {
        if (!Objects.isNull(user.getPassword())) {
            throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
        }
        userService.updateById(user);
    }

    @PutMapping("coach")
    public void updateCoach(TUser user) {
        if (!Objects.isNull(user.getPassword())) {
            throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
        }
        userService.updateById(user);
    }

    @PutMapping("student")
    public void updateStu(TUser user) {
        if (!Objects.isNull(user.getPassword())) {
            throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
        }
        userService.updateById(user);
    }

    @DeleteMapping("user")
    public void deleteUser(@NotNull @Min(1) Integer id) {
        TUser user = userService.getById(id);
        if (user.getRole().equals(RoleDef.STUDENT)) {
            log.debug("can not delete the user which role is student");
            throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
        }

        userService.removeById(id);
    }

    @PatchMapping("pwd")
    public void resetPwd(Integer id) {
        TUser user = userService.getById(id);
        if (user.getRole().equals(RoleDef.STUDENT)) {
            log.debug("can not delete the user which role is student");
            throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
        }

        TUser pwdWrapper = new TUser();
        String idNumber = user.getIdNumber();
        pwdWrapper.setPassword(idNumber.substring(idNumber.length() - 6));
    }
}
