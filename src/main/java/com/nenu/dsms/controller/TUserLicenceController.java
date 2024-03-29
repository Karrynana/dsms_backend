package com.nenu.dsms.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.entity.TUserLicence;
import com.nenu.dsms.entity.TUserProcess;
import com.nenu.dsms.entity.TUserProcessList;
import com.nenu.dsms.service.ITLicenceTypeService;
import com.nenu.dsms.service.ITUserLicenceService;
import com.nenu.dsms.service.ITUserProcessListService;
import com.nenu.dsms.service.ITUserProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
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
@RequestMapping("/dsms/t-user-licence")
public class TUserLicenceController {

    @Autowired
    ITUserLicenceService userLicenceService;
    @Autowired
    ITLicenceTypeService licenceTypeService;
    @Autowired
    ITUserProcessService userProcessService;
    @Autowired
    ITUserProcessListService userProcessListService;

    /**
     * 查询当前用户考过的驾照列表
     * @return 驾照列表
     */
    @GetMapping
    public List<TUserLicence> queryCurrentUserLicences() {
        return userLicenceService.list(Wrappers.lambdaQuery(TUserLicence.class)
                .eq(TUserLicence::getUid, DsmsContext.currentUser().getId()));
    }

    @GetMapping("/active")
    public TUserLicence queryCurrentUserActiveLicence() {
        return userLicenceService.getOne(Wrappers.lambdaQuery(TUserLicence.class)
                .eq(TUserLicence::getActiveFlag, 1)
                .eq(TUserLicence::getUid, DsmsContext.currentUser().getId()));
    }

    /**
     * 查询某个用户考过的驾照列表
     * @return 驾照列表
     */
    @GetMapping("/id")
    public List<TUserLicence> queryCurrentUserLicencesByUserId(Integer id) {
        return userLicenceService.list(Wrappers.lambdaQuery(TUserLicence.class)
                .eq(TUserLicence::getUid, id));
    }

    /**
     * 为用户新增一个驾照学习记录
     * @param userLicence 用户id和准驾车型id
     */
    @PostMapping
    @Transactional(propagation = Propagation.REQUIRED)
    public void createNewStu(@RequestBody TUserLicence userLicence) {
        List<TUserProcessList> active = userProcessListService.lambdaQuery()
                .eq(TUserProcessList::getActiveFlag, 1).eq(TUserProcessList::getStuId, userLicence.getUid()).list();
        if (!CollectionUtils.isEmpty(active)) {
            if (active.size() == 0) {
                throw new DsmsException(DsmsExceptionDef.USER_DATA_INVALID);
            }
            throw new DsmsException(DsmsExceptionDef.PROCESS_ACTIVED);
        }
        // 分配一条代表当前用户当前准驾车型的记录
        Integer ulid = userLicenceService.initNewRecord(userLicence.getUid(), userLicence.getLid());
        // 分配驾考流程的拷贝
        TUserProcess firstProcess = userProcessService.initNewRecord(userLicence.getUid(), userLicence.getLid(), ulid);
        // 初始化用户流程状态
        userProcessListService.initNewRecord(firstProcess, userLicence.getUid());
    }

    /**
     * 完成驾考获得驾照
     * TODO 完善逻辑 校验用户是否符合条件
     */
    @PutMapping
    public void finishLicence(Integer uid) {
        userLicenceService.finishLicence(uid);
    }
}
