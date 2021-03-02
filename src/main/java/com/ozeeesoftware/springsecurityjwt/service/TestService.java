package com.ozeeesoftware.springsecurityjwt.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {


    public String allAccess(){
        return "Public content";
    }

    public String userAccess(){
        return "User content";
    }

    public String moderatorAccess(){
        return "Moderator content";
    }

    public String adminAccess(){
        return "Admin content";
    }

}
