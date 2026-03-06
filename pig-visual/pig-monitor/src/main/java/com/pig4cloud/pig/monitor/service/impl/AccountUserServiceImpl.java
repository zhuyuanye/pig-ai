package com.pig4cloud.pig.monitor.service.impl;

import com.pig4cloud.pig.common.core.entity.user.HzbUser;
import com.pig4cloud.pig.monitor.dao.AccountUserDao;
import com.pig4cloud.pig.monitor.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class AccountUserServiceImpl implements AccountUserService {

    @Autowired
    private AccountUserDao accountUserDao;

    // 新增用户
    @Override
    public HzbUser addUser(HzbUser hzbUser) {
        HzbUser hzbUserByUsername = getUserByUsername(hzbUser.getUsername());
        if (null != hzbUserByUsername) {
            throw new IllegalArgumentException("用户名重复");
        }
        return accountUserDao.save(hzbUser);
    }

    // 根据 ID 查询用户
    @Override
    public HzbUser getUserById(Long userId) {
        return accountUserDao.findById(userId).orElse(null);
    }

    // 根据用户名查询用户
    @Override
    public HzbUser getUserByUsername(String username) {
        return accountUserDao.findByUsername(username);
    }

    // 修改用户信息
    @Override
    public HzbUser updateUser(HzbUser hzbUser) {
        HzbUser hzbUserByUsername = getUserByUsername(hzbUser.getUsername());
        if (null != hzbUserByUsername && !Objects.equals(hzbUserByUsername.getId(), hzbUser.getId())) {
            throw new IllegalArgumentException("用户名重复");
        }
        return accountUserDao.save(hzbUser);
    }

    // 根据 ID 删除用户
    @Override
    public void deleteUser(Long userId) {
        accountUserDao.deleteById(userId);
    }

    //根据用户名称模糊分页查询用户
    @Override
    public Page<HzbUser> getUsersByPage(String username, int page, int size) {
        Specification<HzbUser> spec = (root, query, cb) -> {
            if (Objects.nonNull(username) && !username.isBlank()) {
                return cb.like(root.get("username"), "%" + username + "%");
            }
            return cb.conjunction(); // 无条件
        };
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return accountUserDao.findAll(spec, pageable);
    }


}
