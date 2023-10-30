package com.example.demo.User.Controller;

import com.example.demo.Shared.Response.ResponseWithMessage;
import com.example.demo.User.Entity.User;
import com.example.demo.User.Entity.UsersBasicInfo;
import com.example.demo.User.Repository.UsersBasicInfoRepository;
import com.example.demo.User.Request.UsersBasicInfoModifyRequest;
import com.example.demo.User.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {


    private final UsersBasicInfoRepository usersBasicInfoRepository;
    private final UserService usersService;

    @GetMapping("/user/{userId}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Map<String, String>> getUserInfo(@PathVariable Integer userId) {
        Map<String, String> map = new HashMap<>();
        UsersBasicInfo userInfo = usersBasicInfoRepository.findByUserId(userId);

        map.put("firstname", userInfo.getFirstname());
        map.put("surname", userInfo.getSurname());

        return ResponseEntity.ok(map);
    }

    @GetMapping("/user/currentUser")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Map<String, String>> getCurrentUserInfo(HttpServletRequest request) {
        User user = usersService.getLoggedUser();

        Map<String, String> map = new HashMap<>();
        UsersBasicInfo userInfo = usersBasicInfoRepository.findByUserId(user.getId());

        map.put("username", user.getUsername());
        map.put("firstname", userInfo.getFirstname());
        map.put("surname", userInfo.getSurname());

        return ResponseEntity.ok(map);
    }

    @PutMapping("/user")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<ResponseWithMessage> updateUserBasicInfo(@RequestBody UsersBasicInfoModifyRequest request) {
        try {
            UsersBasicInfo usersBasicInfo = usersBasicInfoRepository.findByUserId(request.getId());

            if (usersBasicInfo == null) {
                throw new NoSuchElementException();
            }

            if (request.getName() != null) {
                usersBasicInfo.setFirstname(request.getName());
            }
            if (request.getSurname() != null) {
                usersBasicInfo.setSurname(request.getSurname());
            }

            usersBasicInfoRepository.save(usersBasicInfo);
            return ResponseEntity.ok(new ResponseWithMessage("UserBasicInfo modified"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWithMessage("UserBasicInfo to modify not found"));
        }
    }
}
