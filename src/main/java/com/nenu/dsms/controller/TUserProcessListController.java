package com.nenu.dsms.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.entity.TUserProcessList;
import com.nenu.dsms.service.ITUserLicenceService;
import com.nenu.dsms.service.ITUserProcessListService;
import com.nenu.dsms.vo.request.NextStepRequestVo;
import com.nenu.dsms.vo.response.StateInfoResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lina
 * @since 2021-05-08
 */
@RestController
@RequestMapping("/dsms/t-user-process-list")
public class TUserProcessListController {

    @Autowired
    ITUserProcessListService userProcessListService;

    @PostMapping
    @Transactional
    public void nextStep(Boolean flag) {
        if (flag) {
            userProcessListService.nextStep(DsmsContext.currentUser().getId());
        } else {
            userProcessListService.lastStep(DsmsContext.currentUser().getId());
        }
    }

    @PostMapping("/teacher")
    @Transactional
    public void nextStep(@RequestBody NextStepRequestVo requestVo) {
        if (requestVo.getFlag()) {
            userProcessListService.nextStep(requestVo.getUid());
        } else {
            userProcessListService.lastStep(requestVo.getUid());
        }
    }

    @GetMapping
    public List<TUserProcessList> queryRecordsByProcessListId(Integer id) {
        return userProcessListService.list(Wrappers.lambdaQuery(TUserProcessList.class)
            .eq(TUserProcessList::getPrcId, id));
    }

    @GetMapping("/next/info")
    public StateInfoResponseVo getNextStateInfo() {
        return userProcessListService.getNextStateInfo(DsmsContext.currentUser().getId());
    }

    @GetMapping("/teacher/stu")
    public List<TUserProcessList> getActiveByUserid(Integer uid, Integer ulid) {
        return userProcessListService.lambdaQuery().eq(TUserProcessList::getStuId, uid)
                .eq(TUserProcessList::getUlid, ulid).list();
    }
}
