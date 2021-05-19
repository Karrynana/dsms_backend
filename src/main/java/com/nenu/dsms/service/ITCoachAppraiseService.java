package com.nenu.dsms.service;

import com.nenu.dsms.entity.TCoachAppraise;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lina
 * @since 2021-05-09
 */
public interface ITCoachAppraiseService extends IService<TCoachAppraise> {

    void updateAppraiseById(TCoachAppraise appraise);

    void saveAppraise(TCoachAppraise appraise);
}
