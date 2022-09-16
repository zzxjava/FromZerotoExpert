package com.zzx.FromZerotoExpert.service.impl;

import com.zzx.FromZerotoExpert.model.dao.UserMapper;
import com.zzx.FromZerotoExpert.model.pojo.User;
import com.zzx.FromZerotoExpert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public String check(String name, String password){
        //检查数据库中是否有数据
        int i = userMapper.selectByUser(name, password);
        if(i == 0){
            return "error";
        }else {
            return "success";
        }
    }

    @Override
    public String Insert(String name, String password){
        //插入数据到数据库中
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        int i = userMapper.insertSelective(user);//插入数据库
        if(i == 0){
            return "error";
        }else {
            return "success";
        }
    }

}
