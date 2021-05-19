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
public class TProcessTypeState implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String type;

    private String state;

    @TableField("`order`")
    private Integer order;

    // 是否为检查状态
    private Integer checked;

    // 允许用户下一步
    private Integer nextFlag;

    // 允许用户回退
    private Integer backFlag;

    private String name;
}
