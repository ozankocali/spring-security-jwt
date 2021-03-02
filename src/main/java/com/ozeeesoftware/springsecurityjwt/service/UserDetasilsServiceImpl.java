package com.ozeeesoftware.springsecurityjwt.service;

import com.ozeeesoftware.springsecurityjwt.model.User;
import com.ozeeesoftware.springsecurityjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetasilsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(
                        "User Not Found with username: " + username
                ));

        return UserDetailsImpl.build(user);
    }
}
