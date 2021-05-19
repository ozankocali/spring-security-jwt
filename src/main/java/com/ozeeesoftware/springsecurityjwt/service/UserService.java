package com.ozeeesoftware.springsecurityjwt.service;


import com.ozeeesoftware.springsecurityjwt.exception.model.*;
import com.ozeeesoftware.springsecurityjwt.model.User;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;


public interface UserService {

    User register(String firstName, String lastName, String username, String email) throws UsernameExistsException, UserNotFoundException, EmailExistsException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked,
                    boolean isActive, MultipartFile profileImage) throws UsernameExistsException, UserNotFoundException, EmailExistsException, IOException, NotAnImageFileException;

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked,
                    boolean isActive, MultipartFile profileImage) throws UsernameExistsException, UserNotFoundException, EmailExistsException, IOException, NotAnImageFileException;

    void deleteUser(String username) throws IOException;

}
