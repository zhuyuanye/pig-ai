package com.pig4cloud.pig.monitor.service.impl;

import com.pig4cloud.pig.common.core.entity.user.HzbUser;
import com.pig4cloud.pig.monitor.mapper.AccountUserMapper;
import com.pig4cloud.pig.monitor.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Objects;


@Service
public class AccountUserServiceImpl implements AccountUserService {

    @Autowired
    private AccountUserMapper accountUserMapper;

    // 新增用户
    @Override
    public HzbUser addUser(HzbUser hzbUser) {
        HzbUser hzbUserByUsername = getUserByUsername(hzbUser.getUsername());
        if (null != hzbUserByUsername) {
            throw new IllegalArgumentException("用户名重复");
        }
        accountUserMapper.insert(hzbUser);
        return hzbUser;
    }

    // 根据 ID 查询用户
    @Override
    public HzbUser getUserById(Long userId) {
        return accountUserMapper.selectById(userId);
    }

    // 根据用户名查询用户
    @Override
    public HzbUser getUserByUsername(String username) {
        return accountUserMapper.selectOne(new QueryWrapper<HzbUser>().eq("username", username));
    }

    // 修改用户信息
    @Override
    public HzbUser updateUser(HzbUser hzbUser) {
        HzbUser hzbUserByUsername = getUserByUsername(hzbUser.getUsername());
        if (null != hzbUserByUsername && !Objects.equals(hzbUserByUsername.getId(), hzbUser.getId())) {
            throw new IllegalArgumentException("用户名重复");
        }
        accountUserMapper.updateById(hzbUser);
        return hzbUser;
    }

    // 根据 ID 删除用户
    @Override
    public void deleteUser(Long userId) {
        accountUserMapper.deleteById(userId);
    }

    //根据用户名称模糊分页查询用户
    @Override
    public IPage<HzbUser> getUsersByPage(String username, int page, int size) {
        LambdaQueryWrapper<HzbUser> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(username) && !username.isBlank()) {
            queryWrapper.like(HzbUser::getUsername, username);
        }
        queryWrapper.orderByDesc(HzbUser::getId);
        Page<HzbUser> pageResult = new Page<>(page, size);
        return accountUserMapper.selectPage(pageResult, queryWrapper);
    }


}
