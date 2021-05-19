package com.nenu.dsms.service;

import com.nenu.dsms.entity.TUserLicence;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lina
 * @since 2021-05-08
 */
public interface ITUserLicenceService extends IService<TUserLicence> {

    Integer initNewRecord(Integer uid, Integer lid);
}
