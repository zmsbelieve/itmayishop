package com.itmayi.dao;

import com.itmayi.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    @Select("select  id,username,password,phone,email,created,updated from mb_user where id =#{userId}")
    UserEntity findByID(@Param("userId") Long userId);

    @Insert("INSERT  INTO `mb_user`  (username,password,phone,email,created,updated) VALUES (#{username}, #{password},#{phone},#{email},#{created},#{updated});")
    Integer insertUser(UserEntity userEntity);
    @Select("select * from mb_user where username=#{userName} and password=#{password}")
    UserEntity login(@Param("userName") String userName,@Param("password") String password);
    @Select("select * from mb_user where id =#{id}")
    UserEntity findByUserId(String userId);
}
