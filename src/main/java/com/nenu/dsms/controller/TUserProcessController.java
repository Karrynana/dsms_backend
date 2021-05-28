package com.nenu.dsms.controller;


import com.nenu.dsms.entity.TUserProcess;
import com.nenu.dsms.service.ITUserProcessListService;
import com.nenu.dsms.service.ITUserProcessService;
import com.nenu.dsms.vo.request.UpdateStudyTimeRequestVo;
import com.nenu.dsms.vo.response.StateInfoResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/dsms/t-user-process")
public class TUserProcessController {

    @Autowired
    ITUserProcessService userProcessService;

    @GetMapping
    public List<TUserProcess> queryByUserLicenceId(Integer id) {
        return userProcessService.lambdaQuery().eq(TUserProcess::getUlid, id).list();
    }

    @PutMapping("/time")
    public void updateStudyTime(@RequestBody UpdateStudyTimeRequestVo requestVo) {
        TUserProcess tUserProcess = new TUserProcess();
        TUserProcess origin = userProcessService.lambdaQuery().eq(TUserProcess::getId, requestVo.getUpid()).one();
        tUserProcess.setCurTime(origin.getCurTime() + requestVo.getTime());
        tUserProcess.setId(requestVo.getUpid());

        userProcessService.updateById(tUserProcess);
    }

}
