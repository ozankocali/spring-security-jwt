package com.ozeeesoftware.springsecurityjwt.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozeeesoftware.springsecurityjwt.constant.Authority;
import com.ozeeesoftware.springsecurityjwt.constant.Role;
import com.ozeeesoftware.springsecurityjwt.controller.UserController;
import com.ozeeesoftware.springsecurityjwt.exception.model.EmailExistsException;
import com.ozeeesoftware.springsecurityjwt.exception.model.UserNotFoundException;
import com.ozeeesoftware.springsecurityjwt.exception.model.UsernameExistsException;
import com.ozeeesoftware.springsecurityjwt.filter.JwtAccessDeniedHandler;
import com.ozeeesoftware.springsecurityjwt.filter.JwtAuthenticationEntryPoint;
import com.ozeeesoftware.springsecurityjwt.model.User;
import com.ozeeesoftware.springsecurityjwt.repository.UserRepository;
import com.ozeeesoftware.springsecurityjwt.service.UserServiceImpl;
import com.ozeeesoftware.springsecurityjwt.utility.JWTTokenProvider;
import lombok.With;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    String url="/api/v1/user";


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsers()throws Exception{
        List<User> userList=new ArrayList<>();
        userList.add(new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true));
        userList.add(new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true));


        when(userService.getUsers()).thenReturn(userList);


        MvcResult mvcResult=mockMvc.perform(get(url+"/list")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        String actualJsonResponse=mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse=objectMapper.writeValueAsString(userList);

        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetUserByUsername()throws Exception{
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        when(userService.findUserByUsername("test-username"))
                .thenReturn(user);

        MvcResult mvcResult=mockMvc.perform(get(url+"/find/test-username")
                ).andExpect(status().isOk()).andReturn();

        String actualJsonResponse=mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse=objectMapper.writeValueAsString(user);

        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);

        System.out.println(actualJsonResponse);
    }



    @Test
    public void testRegister() throws Exception {
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        when(userService.register(user.getFirstName(),user.getLastName(),user.getUsername(),user.getEmail()))
                .thenReturn(user);

        MvcResult mvcResult=mockMvc.perform(post(url+"/register")
                .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andReturn();

        String actualJsonResponse=mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse=objectMapper.writeValueAsString(user);

        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);


    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddNewUser()throws Exception{
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        when(userService.addNewUser(user.getFirstName(),user.getLastName(),user.getUsername(),user.getEmail()
        ,user.getRole(),user.isNotLocked(),user.isActive(),null)).thenReturn(user);


        MvcResult mvcResult=mockMvc.perform(post(url+"/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName","test-firstName")
        .param("lastName","test-lastName")
        .param("email","test-email")
        .param("role",Role.ROLE_ADMIN.name())
        .param("isActive","true")
        .param("isNonLocked","true")
        .param("username","test-username")).andExpect(status().isOk()).andReturn();

        String actualJsonResponse=mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse=objectMapper.writeValueAsString(user);

        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);


    }

    @Test
    public void testLoginUser()throws Exception{
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);


        when(userService.findUserByUsername("test-username")).thenReturn(user);

        MvcResult mvcResult=mockMvc.perform(post(url+"/login").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andReturn();

        String actualJsonResponse=mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse=objectMapper.writeValueAsString(user);

        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);


    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUser()throws Exception{
        User existingUser =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        User updatedUser =new User(1L,"test-firstName2","test-lastName2","test-username2","test-email2","test-password2",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        when(userService.updateUser(existingUser.getUsername(),updatedUser.getFirstName(),updatedUser.getLastName(),
                updatedUser.getUsername(),updatedUser.getEmail(),updatedUser.getRole(),updatedUser.isNotLocked(),
                updatedUser.isActive(),null)).thenReturn(updatedUser);

        MvcResult mvcResult=mockMvc.perform(post(url+"/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName","test-firstName2")
                .param("lastName","test-lastName2")
                .param("email","test-email2")
                .param("role",Role.ROLE_ADMIN.name())
                .param("isActive","true")
                .param("isNonLocked","true")
                .param("username","test-username2")
                .param("currentUsername","test-username")).andExpect(status().isOk()).andReturn();

        String actualJsonResponse=mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse=objectMapper.writeValueAsString(updatedUser);

        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);

        assertThat(actualJsonResponse).isNotEqualToIgnoringWhitespace(objectMapper.writeValueAsString(existingUser));

    }

    @Test
    @WithMockUser(authorities = {"user:delete"})
    public void testDeleteUser()throws Exception{
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        userService.deleteUser("test-username");


        mockMvc.perform(delete(url+"/delete/test-username")).andExpect(status().isOk());
    }


}
