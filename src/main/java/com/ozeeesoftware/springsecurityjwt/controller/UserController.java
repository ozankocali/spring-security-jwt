package com.ozeeesoftware.springsecurityjwt.controller;

import com.ozeeesoftware.springsecurityjwt.constant.SecurityConstant;
import com.ozeeesoftware.springsecurityjwt.exception.ExceptionHandling;
import com.ozeeesoftware.springsecurityjwt.exception.model.EmailExistsException;
import com.ozeeesoftware.springsecurityjwt.exception.model.NotAnImageFileException;
import com.ozeeesoftware.springsecurityjwt.exception.model.UserNotFoundException;
import com.ozeeesoftware.springsecurityjwt.exception.model.UsernameExistsException;
import com.ozeeesoftware.springsecurityjwt.model.HttpResponse;
import com.ozeeesoftware.springsecurityjwt.model.User;
import com.ozeeesoftware.springsecurityjwt.model.UserPrincipal;
import com.ozeeesoftware.springsecurityjwt.service.UserService;
import com.ozeeesoftware.springsecurityjwt.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({"/","/api/v1/user"})
public class UserController extends ExceptionHandling {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UsernameExistsException, UserNotFoundException, EmailExistsException{

        User newUser = userService.register(user.getFirstName(),user.getLastName(),user.getUsername(),user.getEmail());
        return new ResponseEntity<User>(newUser, HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){

        authenticate(user.getUsername(),user.getPassword());

        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal=new UserPrincipal(loginUser);
        HttpHeaders jwtHeader=getJwtHeader(userPrincipal);
        return new ResponseEntity<User>(loginUser,jwtHeader, HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestParam("firstName")String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam("isNonLocked") String isNonLocked,
                                           @RequestParam(value = "profileImage",required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, NotAnImageFileException, NotAnImageFileException {

        User newUser=userService.addNewUser(firstName,lastName,username,email,role,Boolean.parseBoolean(isNonLocked),Boolean.parseBoolean(isActive),profileImage);
        return new ResponseEntity<User>(newUser,HttpStatus.OK);

    }

    @PostMapping("/update")
    public ResponseEntity<User> update(@RequestParam("currentUsername")String currentUsername,
                                       @RequestParam("firstName")String firstName,
                                       @RequestParam("lastName") String lastName,
                                       @RequestParam("username") String username,
                                       @RequestParam("email") String email,
                                       @RequestParam("role") String role,
                                       @RequestParam("isActive") String isActive,
                                       @RequestParam("isNonLocked") String isNonLocked,
                                       @RequestParam(value = "profileImage",required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException, NotAnImageFileException {

        User updatedUser=userService.updateUser(currentUsername,firstName,lastName,username,email,role,Boolean.parseBoolean(isNonLocked),Boolean.parseBoolean(isActive),profileImage);
        return new ResponseEntity<User>(updatedUser,HttpStatus.OK);

    }

    @GetMapping("/find/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable("username") String username){

        User existingUser=userService.findUserByUsername(username);

        return new ResponseEntity<User>(existingUser,HttpStatus.OK);

    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers(){

        List<User> users=userService.getUsers();

        return new ResponseEntity<List<User>>(users,HttpStatus.OK);

    }


    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws IOException {
        userService.deleteUser(username);
        return response(HttpStatus.OK,"User deleted successfully");
    }



    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<HttpResponse>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }


    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(SecurityConstant.JWT_TOKEN_HEADER,jwtTokenProvider.generateJwtToken(userPrincipal));
        return httpHeaders;

    }

    private void authenticate(String username, String password) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

    }

}
