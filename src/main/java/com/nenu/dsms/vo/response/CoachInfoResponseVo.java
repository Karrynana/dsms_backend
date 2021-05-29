package com.nenu.dsms.vo.response;

import lombok.Data;

@Data
public class CoachInfoResponseVo {

    private int id;

    private String name;

    private String email;

    private String phone;

    private String account;

    private String avatar;

    private Integer userId;

    private Integer passCount;

    private Integer stuExamCount;

    private Integer stuCount;

    private Integer curStuCount;

    private String photo;
}
