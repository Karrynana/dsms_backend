package com.nenu.dsms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class TUserProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String processName;

    private String processType;

    private Integer ulid;

    @TableField(value = "`order`")
    private Integer order;

    private Integer next;

    private Integer last;

}
