package com.ysw.dao;

import com.github.pagehelper.Page;
import com.ysw.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {
    @Select("select * from m_user where name=#{name} and password=#{password}")
    User login(@Param("name") String name,@Param("password")String password);

    @Select("select * from m_user where name=#{name}")
    User findByName(@Param("name") String name);

    @Update("ALTER TABLE m_user AUTO_INCREMENT = 1")
    void resetAutoIncrement() throws Exception;

    @Select("SELECT * FROM m_user u WHERE u.name LIKE CONCAT('%', #{name}, '%')")
    List<User> findByNameContaining(@Param("name") String name);
}
