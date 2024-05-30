package com.example.springjwtDeep.repository;

import com.example.springjwtDeep.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    //refresh token 존재 여부 확인
    Boolean existsByRefresh(String refresh);

    //refresh token 지우기
    @Transactional
    void deleteByRefresh(String refresh);
}
