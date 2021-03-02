package com.ozeeesoftware.springsecurityjwt.service;

import com.ozeeesoftware.springsecurityjwt.jwt.JwtUtils;
import com.ozeeesoftware.springsecurityjwt.model.Role;
import com.ozeeesoftware.springsecurityjwt.model.RoleEnum;
import com.ozeeesoftware.springsecurityjwt.model.User;
import com.ozeeesoftware.springsecurityjwt.payload.request.LoginRequest;
import com.ozeeesoftware.springsecurityjwt.payload.request.SignupRequest;
import com.ozeeesoftware.springsecurityjwt.payload.response.JwtResponse;
import com.ozeeesoftware.springsecurityjwt.payload.response.MessageResponse;
import com.ozeeesoftware.springsecurityjwt.repository.RoleRepository;
import com.ozeeesoftware.springsecurityjwt.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        List<String> roles=userDetails.getAuthorities().stream()
                .map(role->role.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        ));
    }



    public ResponseEntity<?> registerUser(SignupRequest signupRequest){

        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }


        User user=new User(signupRequest.getUsername(),
                            signupRequest.getEmail(),
                            encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles=signupRequest.getRole();
        Set<Role> roles=new HashSet<>();

        if (strRoles==null){
            Role userRole=roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }else {
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Role adminRole=roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(()-> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;

                    case "mod":
                        Role modRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;

                    default:
                        Role userRole=roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));


    }

}
