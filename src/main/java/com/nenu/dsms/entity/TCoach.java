package com.nenu.dsms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author chrisyxiao
 * @since 2021-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TCoach implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer passCount;

    private Integer stuExamCount;

    private Integer stuCount;

    private Integer curStuCount;

    private String photo;
}
