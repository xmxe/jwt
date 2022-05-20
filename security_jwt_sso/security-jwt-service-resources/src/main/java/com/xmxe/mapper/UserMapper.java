package com.xmxe.mapper;


import com.xmxe.pojo.UserPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMapper {
    //@Select("select * from t_user where username = #{userName}")
    UserPojo queryByUserName(@Param("userName") String userName);
}
