package com.nenu.dsms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.nenu.dsms.mapper")
@SpringBootApplication
public class DsmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsmsApplication.class, args);
    }

}
