package com.pig4cloud.pig.monitor.dao;


import com.pig4cloud.pig.common.core.entity.user.HzbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户表 Dao
 */
public interface AccountUserDao extends JpaRepository<HzbUser, Long>, JpaSpecificationExecutor<HzbUser> {

    HzbUser findByUsername(String username);
}
