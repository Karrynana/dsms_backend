package com.nenu.dsms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lina
 * @since 2021-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发送方id
     */
    private Integer sender;

    /**
     * 接收方id
     */
    private Integer receiver;

    /**
     * 信息体
     */
    private String msg;

    private Integer readFlag;

    private LocalDateTime createTime;

    private Integer receiverDeleteFlag;

    private Integer senderDeleteFlag;
}
