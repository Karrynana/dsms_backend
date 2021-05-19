package com.nenu.dsms.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.entity.TUserProcessList;
import com.nenu.dsms.service.ITUserProcessListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
    public void nextStep(Boolean flag, Integer uid) {
        if (flag) {
            userProcessListService.nextStep(uid);
        } else {
            userProcessListService.lastStep(uid);
        }
    }

    @GetMapping
    public List<TUserProcessList> queryRecordsByProcessListId(Integer id) {
        return userProcessListService.list(Wrappers.lambdaQuery(TUserProcessList.class)
            .eq(TUserProcessList::getPrcId, id));
    }
}
