package com.nenu.dsms.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nenu.dsms.def.DsmsContext;
import com.nenu.dsms.def.exception.DsmsException;
import com.nenu.dsms.def.exception.DsmsExceptionDef;
import com.nenu.dsms.entity.TMessage;
import com.nenu.dsms.service.ITMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
@RequestMapping("/dsms/t-message")
public class TMessageController {

    @Autowired
    ITMessageService messageService;

    /**
     * 发送消息
     * @param msg 消息
     */
    @PostMapping
    public void sendMsg(@RequestBody TMessage msg) {
        if (!StringUtils.hasText(msg.getMsg())) {
            log.debug("msg body can not be blank");
            throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
        }
        if (msg.getReceiver().equals(msg.getSender())) {
            log.debug("can not send msg to self");
            throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
        }
        msg.setSender(DsmsContext.currentUser().getId());
        msg.setCreateTime(LocalDateTime.now());
        messageService.save(msg);
    }

    @GetMapping
    public List<TMessage> queryMyMsg() {
        return messageService.lambdaQuery()
                .eq(TMessage::getReceiver, DsmsContext.currentUser().getId())
                .eq(TMessage::getSenderDeleteFlag, 0)
                .list();
    }

    @DeleteMapping
    public void delMsg(Integer msgId) {
        TMessage msg = messageService.getById(msgId);
        if (msg.getReceiver().equals(DsmsContext.currentUser().getId())) {
            msg.setReceiverDeleteFlag(1);
        }
        else if (msg.getSender().equals(DsmsContext.currentUser().getId())) {
            msg.setSenderDeleteFlag(1);
        } else {
            log.debug("can not remove msg which did not associate with current user");
            throw new DsmsException(DsmsExceptionDef.PERMISSION_DENIED);
        }
        if (msg.getReceiverDeleteFlag().equals(msg.getSenderDeleteFlag())) {
            messageService.removeById(msgId);
            return;
        }
        messageService.updateById(msg);
    }

    /**
     * 更改消息状态
     * @param msgId 信息id
     * @param flag 1代表已读 0代表未读
     */
    @PatchMapping
    public void changeReadFlag(Integer msgId, Integer flag) {
        if (flag != 0 && flag != 1) {
            throw new DsmsException(DsmsExceptionDef.INVALID_PARAM);
        }
        TMessage tMessage = new TMessage();
        tMessage.setReadFlag(flag);
        boolean update = messageService.update(tMessage, Wrappers.lambdaQuery(TMessage.class)
                .eq(TMessage::getReceiver, DsmsContext.currentUser().getId())
                .eq(TMessage::getId, msgId));
        if (!update) {
            log.debug("用户无权更改该消息状态");
            throw new DsmsException(DsmsExceptionDef.PERMISSION_DENIED);
        }
    }

    /**
     * 将所有消息设为已读
     */
    @PutMapping
    public void readAllMsg() {
        TMessage tMessage = new TMessage();
        tMessage.setReadFlag(1);
        boolean update = messageService.update(tMessage, Wrappers.lambdaQuery(TMessage.class)
                .eq(TMessage::getReceiver, DsmsContext.currentUser().getId()));
        if (!update) {
            throw new DsmsException(DsmsExceptionDef.NO_MSG_FOUND);
        }
    }

    /**
     * 查询我发送的消息
     * @return 消息列表
     */
    @GetMapping("/sender")
    public List<TMessage> queryMyMsgAsSender() {
        return messageService.lambdaQuery()
                .eq(TMessage::getSender, DsmsContext.currentUser().getId())
                .eq(TMessage::getSenderDeleteFlag, 0)
                .list();
    }

}
