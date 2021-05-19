package com.nenu.dsms.controller;


import com.nenu.dsms.filter.NoSignIn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chrisyxiao
 * @since 2021-04-19
 */
@RestController
@RequestMapping("/health")
public class TProcessController {

    @NoSignIn
    @GetMapping
    public String get() {
        return "hello world";
    }
}
