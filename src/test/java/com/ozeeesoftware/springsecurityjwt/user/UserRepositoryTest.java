package com.ozeeesoftware.springsecurityjwt.user;

import com.ozeeesoftware.springsecurityjwt.constant.Authority;
import com.ozeeesoftware.springsecurityjwt.constant.Role;
import com.ozeeesoftware.springsecurityjwt.model.User;
import com.ozeeesoftware.springsecurityjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    public void testFindUserByUsername(){
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        userRepository.save(user);

        User existingUser=userRepository.findUserByUsername("test-username");

        assertThat(existingUser).isNotNull();
        assertEquals(user,existingUser);

    }

    @Test
    public void testFindUserByEmail(){
        User user =new User(1L,"test-firstName","test-lastName","test-username","test-email","test-password",
                Role.ROLE_ADMIN.name(), Authority.ADMIN_AUTHORITIES,true,true);

        userRepository.save(user);

        User existingUser=userRepository.findUserByEmail("test-email");

        assertThat(existingUser).isNotNull();
        assertEquals(user,existingUser);

    }

}
