package com.example.springjwtDeep.repository;

import com.example.springjwtDeep.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByUsername(String username);

    //DB table에서 회원 조화
    UserEntity findByUsername(String username);
}
