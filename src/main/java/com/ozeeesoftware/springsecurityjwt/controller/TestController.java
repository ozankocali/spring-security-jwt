package com.ozeeesoftware.springsecurityjwt.controller;

import com.ozeeesoftware.springsecurityjwt.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/all")
    public String allAccess(){
        return testService.allAccess();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess(){
        return testService.userAccess();
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess(){
        return testService.moderatorAccess();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess(){
        return testService.adminAccess();
    }

}
