package com.nenu.dsms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
public class TCoachAppraise implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer stuId;

    @NotNull
    private Integer coachId;

    @NotNull
    private String msg;

    /**
     * 流水表流程id
     */
    @NotNull
    private Integer procListId;

    @NotNull
    @Min(0)
    @Max(5)
    private Integer grade;
    /**
     * proclist中的name字段
     */
    private String procName;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;
}
