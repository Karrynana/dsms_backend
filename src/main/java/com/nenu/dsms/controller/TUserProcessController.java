package com.nenu.dsms.controller;


import com.nenu.dsms.entity.TUserProcess;
import com.nenu.dsms.service.ITUserProcessListService;
import com.nenu.dsms.service.ITUserProcessService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/dsms/t-user-process")
public class TUserProcessController {

    @Autowired
    ITUserProcessService userProcessService;

    @GetMapping
    public List<TUserProcess> queryByUserLicenceId(Integer id) {
        return userProcessService.lambdaQuery().eq(TUserProcess::getUlid, id).list();
    }

}
