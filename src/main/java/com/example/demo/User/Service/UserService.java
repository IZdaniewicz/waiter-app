package com.example.demo.User.Service;

import com.example.demo.Config.Service.JwtService;
import com.example.demo.Permission.Entity.Permission;
import com.example.demo.Role.Entity.Role;
import com.example.demo.User.Entity.User;
import com.example.demo.User.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;


    public User getLoggedUser() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        var jwt = authHeader.substring(7);
        return userRepository.findByUsername(jwtService.extractUsername(jwt)).orElseThrow(
                NoSuchElementException::new
        );
    }

    public boolean hasPermission(User user, Permission permission) {
        Role role = user.getRole();
        return role.getPermissions().contains(permission);
    }
}
