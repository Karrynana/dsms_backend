package com.nenu.dsms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class TUserLicence implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    private Integer lid;

    private String licenceName;

    private String licenceEnName;

    private Long createTime;

    private Integer activeFlag;
}
