package com.example.demo.User.Service;

import com.example.demo.Auth.Token.Repository.TokenRepository;
import com.example.demo.Config.Service.JwtService;
import com.example.demo.User.Entity.User;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.User.Repository.UsersBasicInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UsersService {

    private UsersBasicInfoRepository usersBasicInfoRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public User getLoggedUser(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        var jwt = authHeader.substring(7);
        return userRepository.findByUsername(jwtService.extractUsername(jwt)).orElseThrow(
                NoSuchElementException::new
        );
    }

}
