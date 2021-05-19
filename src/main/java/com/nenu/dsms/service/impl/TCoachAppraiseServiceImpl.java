package com.nenu.dsms.service.impl;

import com.nenu.dsms.entity.TCoachAppraise;
import com.nenu.dsms.mapper.TCoachAppraiseMapper;
import com.nenu.dsms.service.ITCoachAppraiseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lina
 * @since 2021-05-09
 */
@Service
public class TCoachAppraiseServiceImpl extends ServiceImpl<TCoachAppraiseMapper, TCoachAppraise> implements ITCoachAppraiseService {

    @Override
    public void updateAppraiseById(TCoachAppraise appraise) {
        // @TODO 计算并更新教练评分
        updateById(appraise);
    }

    @Override
    public void saveAppraise(TCoachAppraise appraise) {
        // @TODO 计算并更新教练评分
        save(appraise);
    }
}
