package org.naik.auth_service.service;

import java.util.Set;

import org.naik.auth_service.dto.AuthResponse;
import org.naik.auth_service.dto.SignInRequest;
import org.naik.auth_service.dto.SignUpRequest;
import org.naik.auth_service.dto.TokenValidationResponse;
import org.naik.auth_service.model.User;
import org.naik.auth_service.repository.UserRepository;
import org.naik.auth_service.security.JwtUtil;
import org.naik.common.exception.AuthenticationException;
import org.naik.common.exception.UserAlreadyExistsException;
import org.naik.common.security.JwtValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passEncoder;

    private final JwtUtil jwtUtil;

    private final JwtValidator jwtValidator;

    public AuthService(UserRepository userRepository, PasswordEncoder passEncoder, JwtUtil jwtUtil, JwtValidator jwtValidator) {
        this.userRepository = userRepository;
        this.passEncoder = passEncoder;
        this.jwtUtil = jwtUtil;
        this.jwtValidator = jwtValidator;
    }

    public AuthResponse signUp(SignUpRequest request) throws Exception{
        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("Email " + request.getEmail() + " alredy registered!");
        }

        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserAlreadyExistsException("Username " + request.getUsername() + " is not available!");
        }
        try{
            User user = new User(
                request.getUsername(),
                passEncoder.encode(request.getPassword()),
                request.getEmail(),
                request.getName()
            );

            user.setPhoneNumber(request.getPhoneNumber());

            // System.out.println(user);

            user = userRepository.save(user);

            String token = jwtUtil.generateToken(user.getId(), user.getRoles());

            user.setCurrentToken(token);

            user = userRepository.save(user);

            return new AuthResponse(token, user.getUsername(), user.getEmail(),user.getRoles());

        } catch(Exception e){
            throw new Exception("Unable to register user!");
        }
    }

    public AuthResponse signIn(SignInRequest request) throws Exception{
        
        User user = userRepository.findByEmail(request.getUsername())
            .or(() -> userRepository.findByUsername(request.getUsername()))
            .orElseThrow(() -> new AuthenticationException("Incorrect user details. Please try again!"));

        if (!passEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        try{

            String token = jwtUtil.generateToken(user.getId(), user.getRoles());
            user.setCurrentToken(token);
            userRepository.save(user);

            return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRoles());

        } catch(Exception e){
            throw new Exception("Something went wrong!");
        }

    }

    public TokenValidationResponse validateToken(String token){
        if(!jwtValidator.validateToken(token)){
            return new TokenValidationResponse(false, null, null);
        }

        String userId = jwtValidator.getUserIdFromToken(token);

        Set<String> roles = jwtValidator.getRolesFromToken(token);

        return new TokenValidationResponse(true, userId, roles);
    }
}