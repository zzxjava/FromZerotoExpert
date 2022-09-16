package com.zzx.FromZerotoExpert.model.dao;

import com.zzx.FromZerotoExpert.model.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
@Component
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /*根据姓名查询*/
    int selectByUser(String username,String password);
}
