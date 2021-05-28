package com.nenu.dsms.vo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoResponseVo {

    private Integer id;

    private String name;

    private String email;

    private String phone;

    private String account;

    private String role;

    private String idNumber;

    private String avatar;

    private UserInfoResponseVo creator;

    private Long createTime;
}
