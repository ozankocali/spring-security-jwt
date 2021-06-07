package com.ozeeesoftware.springsecurityjwt.user;

import com.ozeeesoftware.springsecurityjwt.constant.Authority;
import com.ozeeesoftware.springsecurityjwt.constant.Role;
import com.ozeeesoftware.springsecurityjwt.exception.model.EmailExistsException;
import com.ozeeesoftware.springsecurityjwt.exception.model.UserNotFoundException;
import com.ozeeesoftware.springsecurityjwt.exception.model.UsernameExistsException;
import com.ozeeesoftware.springsecurityjwt.model.User;
import com.ozeeesoftware.springsecurityjwt.repository.UserRepository;
import com.ozeeesoftware.springsecurityjwt.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    public void testGetAllUsers(){

       List<User> userList=new ArrayList<User>();
       userList.add(new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
               Role.ROLE_USER.name(), Authority.USER_AUTHORITIES,true,true));
       userList.add(new User(2L,"test-firstName2","test-lastName2","test-username2","test-email2","test-password2",
               Role.ROLE_USER.name(), Authority.USER_AUTHORITIES,true,true));

       when(userRepository.findAll()).thenReturn(userList);

       assertEquals(2,userService.getUsers().size());

       assertEquals(userList,userService.getUsers());

   }

    @Test
    public void testGetUserByUsername(){
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_USER.name(), Authority.USER_AUTHORITIES,true,true);

        when(userRepository.findUserByUsername("test-username")).thenReturn(user);

        assertEquals(user,userService.findUserByUsername("test-username"));
    }

    @Test
    public void testGetUserByEmail(){
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_USER.name(), Authority.USER_AUTHORITIES,true,true);

        when(userRepository.findUserByEmail("test-email")).thenReturn(user);

        assertEquals(user,userService.findUserByEmail("test-email"));
    }

    @Test
    public void testRegisterUser() throws UsernameExistsException, UserNotFoundException, EmailExistsException {
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_USER.name(), Authority.USER_AUTHORITIES,true,true);


        when(userRepository.save(user)).thenReturn(user);

        User registeredUser=userService.register("test-firstName","test-lastName",
                "test-username","test-email");
        registeredUser.setId(1L);
        registeredUser.setPassword("test-password");

        assertEquals(user,registeredUser);


    }


    @Test
    public void testAddNewUser() throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException {
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        when(userRepository.save(user)).thenReturn(user);

        User newUser=userService.addNewUser(user.getFirstName(),user.getLastName(),user.getUsername(),
                user.getEmail(),user.getRole(),user.isNotLocked(),user.isActive(),null);
        newUser.setId(1L);
        newUser.setPassword("test-password");

        assertEquals(user,newUser);


    }

    @Test
    public void testDeleteUser() throws IOException {
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        when(userRepository.findUserByUsername("test-username")).thenReturn(user);

        userService.deleteUser("test-username");

        verify(userRepository,times(1)).deleteById(1L);
    }

}
