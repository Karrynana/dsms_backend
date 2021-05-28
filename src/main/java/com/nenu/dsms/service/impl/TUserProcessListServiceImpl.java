package com.nenu.dsms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.entity.TProcessTypeState;
import com.nenu.dsms.entity.TUserProcess;
import com.nenu.dsms.entity.TUserProcessList;
import com.nenu.dsms.mapper.TUserProcessListMapper;
import com.nenu.dsms.service.ITProcessTypeStateService;
import com.nenu.dsms.service.ITUserProcessListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nenu.dsms.service.ITUserProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lina
 * @since 2021-05-08
 */
@Slf4j
@Service
public class TUserProcessListServiceImpl extends ServiceImpl<TUserProcessListMapper, TUserProcessList> implements ITUserProcessListService {

    @Autowired
    ITProcessTypeStateService processTypeStateService;
    @Autowired
    ITUserProcessService userProcessService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void initNewRecord(TUserProcess firstProcess, Integer uid) {
        TUserProcessList entity = new TUserProcessList();
        entity.setCreateTime(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        entity.setOrder(0);
        entity.setPrcStatus(0);
        entity.setPrcId(firstProcess.getId());
        entity.setPrcName(firstProcess.getProcessName());
        entity.setActiveFlag(1);
        entity.setStuId(uid);
        entity.setPrcType(firstProcess.getProcessType());

        List<TProcessTypeState> stateList = processTypeStateService.lambdaQuery().eq(TProcessTypeState::getType, firstProcess.getProcessType()).list()
                .stream()
                .sorted(Comparator.comparingInt(TProcessTypeState::getOrder))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(stateList)) {
            throw new DsmsException(DsmsExceptionDef.SYSTEM_DATA_ERR);
        }

        entity.setLast(-1);
        entity.setNext(firstProcess.getNext());
        entity.setChecked(stateList.get(0).getChecked());
        entity.setBackFlag(stateList.get(0).getBackFlag());
        entity.setNextFlag(stateList.get(0).getNextFlag());
        entity.setStateName(stateList.get(0).getName());
        // 暂不启用以下字段
        entity.setParent(0);

        save(entity);
    }

    @Override
    public void nextStep(Integer uid) {
        TUserProcessList active = getActive(uid);

        // 判断当前流程的子状态是否是终态 如果是则进入下一个大状态 否则进入下一个子状态
        List<TProcessTypeState> orderedState = processTypeStateService.list(Wrappers.lambdaQuery(TProcessTypeState.class)
                .eq(TProcessTypeState::getType, active.getPrcType()))
                .stream()
                .sorted(Comparator.comparingInt(TProcessTypeState::getOrder))
                .collect(Collectors.toList());

        // 创建需要写库的实体
        TUserProcessList entity = new TUserProcessList();
        // 是子状态的终态
        if (active.getPrcStatus().equals(orderedState.get(orderedState.size() - 1).getOrder())) {
            // 主状态也达到终态
            if (active.getNext().equals(-1)) {
                active.setActiveFlag(0);
                updateById(active);
                return;
            }
            // 主状态不是终态 流转到下一个大状态的第一个小状态
            TUserProcess mainPrc = userProcessService.getById(active.getPrcId());
            TUserProcess mainNextState = userProcessService.lambdaQuery()
                    .eq(TUserProcess::getOrder, active.getNext())
                    .eq(TUserProcess::getUlid, mainPrc.getUlid())
                    .list().get(0);
            TProcessTypeState nextState = processTypeStateService.getOne(Wrappers.lambdaQuery(TProcessTypeState.class)
                    .eq(TProcessTypeState::getType, mainNextState.getProcessType()).eq(TProcessTypeState::getOrder, 0));
            BeanUtils.copyProperties(active, entity);
            entity.setOrder(active.getOrder() + 1);
            entity.setPrcStatus(0);
            entity.setStateName(nextState.getName());
            entity.setPrcId(mainNextState.getId());
            entity.setPrcName(mainNextState.getProcessName());
            entity.setPrcType(mainNextState.getProcessType());
            entity.setNext(mainNextState.getNext());
            entity.setLast(mainPrc.getOrder());
            List<TProcessTypeState> stateList = marginSetChildStateFields(entity, mainNextState);
            entity.setChecked(stateList.get(0).getChecked());
            entity.setBackFlag(stateList.get(0).getBackFlag());
            entity.setNextFlag(stateList.get(0).getNextFlag());
            // 暂不启用此字段
            entity.setParent(0);

            save(entity);
            // 将上一个状态置为非激活
            active.setActiveFlag(0);
            updateById(active);
            return;
        }

        // 不是子状态的终态 只对order字段自增
        BeanUtils.copyProperties(active, entity);
        entity.setOrder(active.getOrder() + 1);
        int nextState = active.getPrcStatus() + 1;
        entity.setPrcStatus(active.getPrcStatus() + 1);
        normalSetChildStateFields(active, orderedState, entity, nextState);
    }

    @Override
    public void lastStep(Integer uid) {
        TUserProcessList active = getActive(uid);

        // 判断当前流程的子状态是否是初始态 如果是则进入上一个大状态 否则进入上一个子状态
        List<TProcessTypeState> orderedState = processTypeStateService.list(Wrappers.lambdaQuery(TProcessTypeState.class)
                .eq(TProcessTypeState::getType, active.getPrcType()))
                .stream()
                .sorted(Comparator.comparingInt(TProcessTypeState::getOrder))
                .collect(Collectors.toList());

        // 创建需要写库的实体
        TUserProcessList entity = new TUserProcessList();
        entity.setActiveFlag(1);
        // 是子状态的初始态 则需要将大状态流转到上一个状态
        if (active.getPrcStatus().equals(orderedState.get(0).getOrder())) {
            // 主状态也达到初态 没有上一个大状态
            if (active.getLast().equals(-1)) {
                log.debug("不能删除初态");
                throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
            }
            TProcessTypeState nextState = processTypeStateService.getOne(Wrappers.lambdaQuery(TProcessTypeState.class)
                    .eq(TProcessTypeState::getType, active.getPrcType()).eq(TProcessTypeState::getOrder, 0));
            TUserProcess lastProc = userProcessService.getById(active.getLast());
            Integer stateCount = processTypeStateService.lambdaQuery().eq(TProcessTypeState::getType, lastProc.getProcessType()).count();
            // 主状态不是初态
            TUserProcess mainLastState = userProcessService.getById(active.getLast());
            BeanUtils.copyProperties(active, entity);
            entity.setOrder(active.getOrder() + 1);
            entity.setPrcStatus(stateCount);
            entity.setNextFlag(nextState.getNextFlag());
            entity.setBackFlag(nextState.getBackFlag());
            entity.setStateName(nextState.getName());
            entity.setPrcId(mainLastState.getId());
            entity.setPrcName(mainLastState.getProcessName());
            entity.setPrcType(mainLastState.getProcessType());
            entity.setLast(mainLastState.getLast());
            List<TProcessTypeState> stateList = marginSetChildStateFields(entity, mainLastState);
            entity.setChecked(stateList.get(stateList.size()-1).getChecked());
            entity.setBackFlag(stateList.get(stateList.size()-1).getBackFlag());
            entity.setNextFlag(stateList.get(stateList.size()-1).getNextFlag());
            // 暂不启用此字段
            entity.setParent(0);

            save(entity);
            active.setActiveFlag(0);
            updateById(active);
            return;
        }

        // 不是子状态的初态
        BeanUtils.copyProperties(active, entity);
        entity.setPrcStatus(active.getPrcStatus() - 1);
        int lastState = active.getPrcStatus() - 1;
        normalSetChildStateFields(active, orderedState, entity, lastState);
    }

    private void normalSetChildStateFields(TUserProcessList active, List<TProcessTypeState> orderedState, TUserProcessList entity, int lastState) {
        entity.setChecked(orderedState.get(lastState).getChecked());
        entity.setNextFlag(orderedState.get(lastState).getNextFlag());
        entity.setBackFlag(orderedState.get(lastState).getBackFlag());
        entity.setStateName(orderedState.get(lastState).getName());
        entity.setStateName(orderedState.get(lastState).getName());

        save(entity);
        active.setActiveFlag(0);
        updateById(active);
    }

    private List<TProcessTypeState> marginSetChildStateFields(TUserProcessList entity, TUserProcess mainLastState) {
        entity.setCreateTime(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));

        List<TProcessTypeState> stateList = processTypeStateService.lambdaQuery().eq(TProcessTypeState::getType, mainLastState.getProcessType()).list()
                .stream()
                .sorted(Comparator.comparingInt(TProcessTypeState::getOrder))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(stateList)) {
            throw new DsmsException(DsmsExceptionDef.SYSTEM_DATA_ERR);
        }
        return stateList;
    }

    private TUserProcessList getActive(Integer uid) {
        // 找到当前用户处于激活状态的流程 并找到其下一步流程
        List<TUserProcessList> list = list(Wrappers.lambdaQuery(TUserProcessList.class)
                .eq(TUserProcessList::getStuId, uid).eq(TUserProcessList::getActiveFlag, 1));
        if (CollectionUtils.isEmpty(list) || list.size() > 1) {
            throw new DsmsException(DsmsExceptionDef.USER_DATA_INVALID);
        }

        return list.get(0);
    }
}
