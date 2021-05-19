package com.nenu.dsms.def;

import com.nenu.dsms.vo.base.UserInfo;

public class DsmsContext {

    private static ThreadLocal<UserInfo> userInfo = new ThreadLocal<>();

    public static UserInfo currentUser() {
        return userInfo.get();
    }

    public static void setUser(UserInfo user) {
        userInfo.set(user);
    }
}
