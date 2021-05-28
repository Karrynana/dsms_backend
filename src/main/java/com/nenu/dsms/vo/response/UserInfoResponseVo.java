package com.nenu.dsms.vo.response;

import com.nenu.dsms.entity.TUser;
import com.nenu.dsms.vo.base.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserInfoResponseVO {

    private Integer id;

    private String name;

    private String email;

    private String phone;

    private String account;

    private String role;

    private String idNumber;

    private String avatar;

    private UserInfoResponseVO creator;

    private Long createTime;
}
