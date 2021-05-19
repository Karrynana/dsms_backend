package com.nenu.dsms.service;

import com.nenu.dsms.entity.TUserProcess;
import com.nenu.dsms.entity.TUserProcessList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lina
 * @since 2021-05-08
 */
public interface ITUserProcessListService extends IService<TUserProcessList> {

    void initNewRecord(TUserProcess firstProcess, Integer uid);

    void nextStep(Integer uid);

    void lastStep(Integer uid);
}
