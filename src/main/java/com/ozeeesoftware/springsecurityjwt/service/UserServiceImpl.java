package com.ozeeesoftware.springsecurityjwt.service;

import com.ozeeesoftware.springsecurityjwt.constant.Role;
import com.ozeeesoftware.springsecurityjwt.exception.model.EmailExistsException;
import com.ozeeesoftware.springsecurityjwt.exception.model.UserNotFoundException;
import com.ozeeesoftware.springsecurityjwt.exception.model.UsernameExistsException;
import com.ozeeesoftware.springsecurityjwt.model.User;
import com.ozeeesoftware.springsecurityjwt.model.UserPrincipal;
import com.ozeeesoftware.springsecurityjwt.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import javax.transaction.Transactional;

import java.io.IOException;

import java.util.List;

import static com.ozeeesoftware.springsecurityjwt.constant.Role.ROLE_USER;
import static com.ozeeesoftware.springsecurityjwt.constant.UserImplConstant.*;


@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private Logger LOGGER= LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findUserByUsername(username);
        if(user==null){
            LOGGER.error(NO_USER_FOUND_BY_USERNAME+username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME+username);
        }else {
            userRepository.save(user);
            UserPrincipal userPrincipal=new UserPrincipal(user);
            LOGGER.info("Returning found user by username: "+username);
            return userPrincipal;
        }
    }



    @Override
    public User register(String firstName, String lastName, String username, String email) throws UsernameExistsException, UserNotFoundException, EmailExistsException {
        validateNewUsernameAndEmail(StringUtils.EMPTY,username,email);

        User user=new User();
        String password=generatePassword();
        String encodedPassword=encodePassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        userRepository.save(user);
        LOGGER.info("New user password: "+password);
        return user;
    }



    @Override
    public User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UsernameExistsException, UserNotFoundException, EmailExistsException, IOException{
        validateNewUsernameAndEmail(StringUtils.EMPTY,username,email);
        User user=new User();
        String password=generatePassword();
        user.setPassword(encodePassword(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setActive(isActive);
        user.setNotLocked(isNonLocked);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(user);
        return user;
    }



    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UsernameExistsException, UserNotFoundException, EmailExistsException, IOException {
        User currentUser=validateNewUsernameAndEmail(currentUsername,newUsername,newEmail);
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        return currentUser;
    }

    @Override
    public void deleteUser(String username) throws IOException {
        User user= userRepository.findUserByUsername(username);
        userRepository.deleteById(user.getId());
    }


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }



    private String encodePassword(String password) {

        return bCryptPasswordEncoder.encode(password);

    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {

        return RandomStringUtils.randomNumeric(10);

    }

    private User validateNewUsernameAndEmail(String currentUsername,String newUsername,String newEmail) throws EmailExistsException, UsernameExistsException, UserNotFoundException {

        User userByNewUsername=findUserByUsername(newUsername);
        User userByNewEmail=findUserByEmail(newEmail);

        if(StringUtils.isNotBlank(currentUsername)){
            User currentUser=findUserByUsername(currentUsername);
            if(currentUser==null){
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME +currentUsername);
            }
            if (userByNewUsername!=null && !currentUser.getId().equals(userByNewUsername.getId())){
                throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail!=null && !currentUser.getId().equals(userByNewEmail.getId())){
                throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        }else {
            if (userByNewUsername!=null ){
                throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail!=null ){
                throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }


    }


    private Role getRoleEnumName(String role) {
        return  Role.valueOf(role.toUpperCase());
    }


}
