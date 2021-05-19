package com.nenu.dsms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TUserProcessList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer stuId;

    private Integer prcId;

    @TableField("`order`")
    private Integer order;

    private Integer parent;

    private Integer coachId;

    private Integer prcStatus;

    private Long createTime;

    private String prcName;

    private String prcType;

    private Integer activeFlag;

    // 大状态的next状态
    private Integer next;

    // 大状态的last状态
    private Integer last;

    // 是否为检查状态
    private Integer checked;

    // 允许用户下一步
    private Integer nextFlag;

    // 允许用户回退
    private Integer backFlag;

    private String stateName;
}
