package com.nenu.dsms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nenu.dsms.entity.TLicenceProcess;
import com.nenu.dsms.entity.TUserProcess;
import com.nenu.dsms.mapper.TUserProcessMapper;
import com.nenu.dsms.service.ITLicenceProcessService;
import com.nenu.dsms.service.ITUserProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lina
 * @since 2021-05-08
 */
@Service
public class TUserProcessServiceImpl extends ServiceImpl<TUserProcessMapper, TUserProcess> implements ITUserProcessService {

    @Autowired
    ITLicenceProcessService licenceProcessService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public TUserProcess initNewRecord(Integer uid, Integer lid, Integer ulid) {
        List<TLicenceProcess> lpList = licenceProcessService.list(Wrappers.lambdaQuery(TLicenceProcess.class)
                .eq(TLicenceProcess::getLicenseId, lid))
                .stream().sorted(Comparator.comparingInt(TLicenceProcess::getOrder))
                .collect(Collectors.toList());

        List<TUserProcess> newRecords = new ArrayList<>();
        lpList.forEach(lp -> {
            TUserProcess tUserProcess = new TUserProcess();
            BeanUtils.copyProperties(lp, tUserProcess);
            tUserProcess.setUlid(ulid);
            newRecords.add(tUserProcess);
        });

        saveBatch(newRecords);
        return newRecords.get(0);
    }
}
