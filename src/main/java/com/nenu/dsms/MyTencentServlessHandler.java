package com.nenu.dsms;

import com.qcloud.scf.runtime.AbstractSpringHandler;

public class MyTencentServlessHandler extends AbstractSpringHandler {
    @Override
    public void startApp() {
        DsmsApplication.main(new String[]{""});
    }
}
