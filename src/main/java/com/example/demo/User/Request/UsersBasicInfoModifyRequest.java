package com.example.demo.User.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersBasicInfoModifyRequest {
    Integer id;
    String name;
    String surname;
    Integer year;
}
