package com.nenu.dsms;

import com.qcloud.scf.runtime.AbstractSpringHandler;

public class MyHandler extends AbstractSpringHandler {
    @Override
    public void startApp() {
        DsmsApplication.main(new String[]{""});
    }
}
