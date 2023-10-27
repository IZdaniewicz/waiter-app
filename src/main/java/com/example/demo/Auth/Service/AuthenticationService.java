package com.example.demo.Auth.Service;

import com.example.demo.Auth.Request.LoginRequest;
import com.example.demo.Auth.Request.RegisterRequest;
import com.example.demo.Auth.Response.AuthenticationResponse;
import com.example.demo.Auth.Token.Entity.Token;
import com.example.demo.Auth.Token.Enum.TokenType;
import com.example.demo.Auth.Token.Repository.TokenRepository;
import com.example.demo.Config.Service.JwtService;
import com.example.demo.User.Entity.User;
import com.example.demo.User.Entity.UsersBasicInfo;
import com.example.demo.User.Enum.Role;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.User.Repository.UsersBasicInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UsersBasicInfoRepository usersBasicInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest request) {

        User user = User
                .builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        user.setRole(Role.USER);
        userRepository.save(user);

        UsersBasicInfo ubi = UsersBasicInfo
                .builder()
                .name(request.getName())
                .surname(request.getSurname())
                .user(user)
                .build();
        usersBasicInfoRepository.save(ubi);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authorize(LoginRequest request) throws CredentialNotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new CredentialNotFoundException("Given credentials doesn't match any user")
        );
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
