package com.nenu.dsms.controller;


import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.entity.TCoachAppraise;
import com.nenu.dsms.entity.TUserProcessList;
import com.nenu.dsms.service.ITCoachAppraiseService;
import com.nenu.dsms.service.ITUserProcessListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lina
 * @since 2021-05-09
 */
@Slf4j
@RestController
@RequestMapping("/dsms/t-coach-appraise")
public class TCoachAppraiseController {

    @Autowired
    ITUserProcessListService userProcessListService;
    @Autowired
    ITCoachAppraiseService coachAppraiseService;

    @PostMapping
    public void appraise(@Validated TCoachAppraise appraise) {
        TUserProcessList userProcessList = userProcessListService.getById(appraise.getProcListId());
        if (Objects.isNull(userProcessList)) {
            log.debug("no record in t_user_process_list's id is {}", appraise.getProcListId());
            throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
        }
        if (!userProcessList.getStuId().equals(DsmsContext.currentUser().getId())) {
            log.debug("user id {} can not match to {}", userProcessList.getStuId(), DsmsContext.currentUser().getId());
            throw new DsmsException(DsmsExceptionDef.PERMISSION_DENIED);
        }

        appraise.setProcName(userProcessList.getPrcName());
        appraise.setCreateTime(LocalDateTime.now());
        appraise.setModifyTime(LocalDateTime.now());
        appraise.setStuId(DsmsContext.currentUser().getId());
        coachAppraiseService.saveAppraise(appraise);
    }

    @GetMapping("/coach")
    public List<TCoachAppraise> getAppraiseByCoachId(Integer coachId) {
        checkCoach();
        return coachAppraiseService.lambdaQuery().eq(TCoachAppraise::getCoachId, coachId).list()
                    .stream().peek(value -> value.setStuId(null)).collect(Collectors.toList());
    }

    @GetMapping("/student")
    public List<TCoachAppraise> getAppraiseByStuId(Integer stuId) {
        checkCoach();
        return coachAppraiseService.lambdaQuery().eq(TCoachAppraise::getStuId, stuId).list()
                .stream().peek(value -> value.setStuId(null)).collect(Collectors.toList());
    }

    @PutMapping("/student")
    public void alterAppraise(Integer appraiseId, @NotBlank String msg, @Min(0) @Max(5) @NotNull Integer grade) {
        checkCoach();

        TCoachAppraise appraise = coachAppraiseService.getById(appraiseId);
        if (!appraise.getStuId().equals(DsmsContext.currentUser().getId())) {
            log.debug("user id {} can not match to {}", DsmsContext.currentUser().getId(), appraise.getStuId());
            throw new DsmsException(DsmsExceptionDef.PERMISSION_DENIED);
        }

        appraise.setMsg(msg);
        appraise.setModifyTime(LocalDateTime.now());
        appraise.setGrade(grade);
        coachAppraiseService.updateAppraiseById(appraise);
    }

    private void checkCoach() {
        String role = DsmsContext.currentUser().getRole();
        if (role.contains("coach")) {
            log.debug("coach can not access this method");
            throw new DsmsException(DsmsExceptionDef.PERMISSION_DENIED);
        }
    }
}
