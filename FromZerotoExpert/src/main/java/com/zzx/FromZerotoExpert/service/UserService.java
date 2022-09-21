package com.zzx.FromZerotoExpert.service;

import com.zzx.FromZerotoExpert.common.Result;
import com.zzx.FromZerotoExpert.model.pojo.User;

public interface UserService {

    Result<User> check(String username, String password);
}
