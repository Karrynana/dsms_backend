package com.nenu.dsms.service.impl;

import com.nenu.dsms.entity.TMessage;
import com.nenu.dsms.mapper.TMessageMapper;
import com.nenu.dsms.service.ITMessageService;
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
public class TMessageServiceImpl extends ServiceImpl<TMessageMapper, TMessage> implements ITMessageService {

}
