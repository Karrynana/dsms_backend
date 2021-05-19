package com.nenu.dsms.vo.base;

import lombok.Data;

@Data
public class UserInfo {
    private Integer id;

    private String name;

    private String email;

    private String phone;

    private String account;

    private String role;

    private String idNumber;

    private String remark;

    private String avatar;

    private CreatorVO creator;

    private Long createTime;

}
