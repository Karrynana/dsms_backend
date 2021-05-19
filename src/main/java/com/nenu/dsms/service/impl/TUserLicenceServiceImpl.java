package com.nenu.dsms.service.impl;

import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.entity.TLicenceType;
import com.nenu.dsms.entity.TUserLicence;
import com.nenu.dsms.mapper.TUserLicenceMapper;
import com.nenu.dsms.service.ITLicenceTypeService;
import com.nenu.dsms.service.ITUserLicenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lina
 * @since 2021-05-08
 */
@Service
public class TUserLicenceServiceImpl extends ServiceImpl<TUserLicenceMapper, TUserLicence> implements ITUserLicenceService {

    @Autowired
    ITLicenceTypeService licenceTypeService;

    /**
     * 当为用户创建准驾车型时创建一个新记录
     * @param uid 用户id
     * @param lid 准驾车型id
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer initNewRecord(Integer uid, Integer lid) {
        TLicenceType licence = licenceTypeService.getById(lid);
        TUserLicence userLicence = new TUserLicence();
        userLicence.setLid(lid);
        userLicence.setLicenceName(licence.getName());
        userLicence.setLicenceEnName(licence.getEnName());
        userLicence.setActiveFlag(1);
        userLicence.setUid(DsmsContext.currentUser().getId());
        userLicence.setCreateTime(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        save(userLicence);

        return userLicence.getId();
    }
}
