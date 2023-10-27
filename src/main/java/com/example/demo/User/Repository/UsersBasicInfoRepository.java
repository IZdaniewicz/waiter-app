package com.example.demo.User.Repository;

import com.example.demo.User.Entity.UsersBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersBasicInfoRepository extends JpaRepository<UsersBasicInfo,Integer> {
    UsersBasicInfo findByUserId(Integer userId);
}
