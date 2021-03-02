package com.ozeeesoftware.springsecurityjwt.controller;


import com.ozeeesoftware.springsecurityjwt.payload.request.LoginRequest;
import com.ozeeesoftware.springsecurityjwt.payload.request.SignupRequest;
import com.ozeeesoftware.springsecurityjwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        return authService.registerUser(signupRequest);
    }

}
