package com.zzx.FromZerotoExpert.service.impl;

import com.zzx.FromZerotoExpert.common.Result;
import com.zzx.FromZerotoExpert.model.dao.UserMapper;
import com.zzx.FromZerotoExpert.model.pojo.User;
import com.zzx.FromZerotoExpert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    /**
     * 登录检查接口
     * @param
     * @return
     */
    @Override
    public Result<User> check(String username, String password){
        //插入数据到数据库中
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        System.out.println(user);
        //检查是否传入数据
        if(user.getUsername() == null || user.getPassword() == null){
            return Result.error("-1", "缺少必要参数");
        }
        //检查传入的参数在数据库中是否存在
        User dbUser = userMapper.selectByUsernameAndPassword(user);
        if(dbUser == null){
            return Result.error("-1", "账号密码错误");
        }
        return Result.success(dbUser);
    }



}
