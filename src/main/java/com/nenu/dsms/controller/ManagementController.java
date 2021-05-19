package com.nenu.dsms.controller;

import com.nenu.dsms.service.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/dsms/management")
public class ManagementController {

    @Autowired
    private ITUserService userService;

    @GetMapping("/user")
    public String visibleUsers() {
        return userService.getVisibleUsers();
    }
}
