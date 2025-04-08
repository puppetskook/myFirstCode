package com.ysw.service;

import com.github.pagehelper.PageInfo;
import com.ysw.entity.User;

import java.util.List;

public interface UserService{
    User login(String name, String password);
    List<User> getUserList();
    int deleteUser(int id);
    User selectUserById(int id);
    int updateUser(User user);
    int insertUser(User user);
    void resetAutoIncrement() throws Exception;
    PageInfo<User> getPage(Integer pageNum, Integer pageSize);
    List<User> findByNameContaining(String name,String sex,Integer age);
    User findByName(String name);
}
