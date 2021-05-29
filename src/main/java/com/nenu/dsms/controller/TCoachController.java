package com.nenu.dsms.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.entity.TCoach;
import com.nenu.dsms.entity.TUser;
import com.nenu.dsms.filter.NoSignIn;
import com.nenu.dsms.service.ITCoachService;
import com.nenu.dsms.service.ITUserService;
import com.nenu.dsms.util.CosUtil;
import com.nenu.dsms.util.FileUtil;
import com.nenu.dsms.vo.response.CoachInfoResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chrisyxiao
 * @since 2021-04-19
 */
@RestController
@RequestMapping("/dsms/t-coach")
public class TCoachController {

    @Autowired
    private ITCoachService coachService;
    @Autowired
    private ITUserService userService;

    @NoSignIn
    @GetMapping
    public List<CoachInfoResponseVo> getAllCoach() {
        List<TCoach> coaches = coachService.list();
        List<TUser> coachesUserInfos = userService.lambdaQuery().in(TUser::getId, coaches.stream().map(TCoach::getUserId).collect(Collectors.toSet()))
                .list();
        Map<Integer, TUser> userIdMap = coachesUserInfos.stream().collect(Collectors.toMap(TUser::getId, (v) -> v));
        List<CoachInfoResponseVo> result = new ArrayList<>();
        coaches.forEach(item -> {
            int originCoachId = item.getId();
            CoachInfoResponseVo coachInfoResponseVo = new CoachInfoResponseVo();
            BeanUtils.copyProperties(userIdMap.get(item.getUserId()), coachInfoResponseVo);
            BeanUtils.copyProperties(item, coachInfoResponseVo);
            coachInfoResponseVo.setId(originCoachId);
            result.add(coachInfoResponseVo);
        });

        return result;
    }

    @PatchMapping("/photo")
    public void uploadCoachPhoto(MultipartFile file) {
        String key = DsmsContext.currentUser().getAccount() + "_coach_photo";
        try {
            String put = CosUtil.INSTANCE.put(key
                    , FileUtil.multipartFileToFile(file));
        } catch (IOException e) {
            throw new DsmsException(DsmsExceptionDef.FILE_SAVE_ERR);
        }
        TCoach tCoach = new TCoach();
        tCoach.setPhoto(key);
        tCoach.setUserId(DsmsContext.currentUser().getId());
        coachService.update(Wrappers.lambdaQuery(TCoach.class).eq(TCoach::getUserId, tCoach.getUserId()));
    }

}
