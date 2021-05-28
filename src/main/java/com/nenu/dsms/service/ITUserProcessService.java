package com.nenu.dsms.service;

import com.nenu.dsms.entity.TUserProcess;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lina
 * @since 2021-05-08
 */
public interface ITUserProcessService extends IService<TUserProcess> {

    TUserProcess initNewRecord(Integer uid, Integer lid, Integer ulid);
}
