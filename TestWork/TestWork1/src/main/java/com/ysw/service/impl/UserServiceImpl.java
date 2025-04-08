package com.ysw.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ysw.dao.UserMapper;
import com.ysw.entity.User;
import com.ysw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;


import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Override
	public User login(String name, String password) {
	   return userMapper.login(name, password);
	}

	@Override
	public List<User> getUserList() {
		return userMapper.selectAll();
	}

	@Override
	@Transactional
	public int deleteUser(int id) {
		int n = userMapper.deleteByPrimaryKey(id);
		return n;
	}

	@Override
	public User selectUserById(int id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public int updateUser(User user) {
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public int insertUser(User user) {
		return userMapper.insertSelective(user);
	}

	@Override
	public void resetAutoIncrement() throws Exception {
		userMapper.resetAutoIncrement();
	}

	@Override
	public PageInfo<User> getPage(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		List<User> list = userMapper.selectAll();
		PageInfo<User> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

//	@Override
//	public List<User> findByNameContaining(String name) {
//		return userMapper.findByNameContaining(name);
//	}

	@Override
	public List<User> findByNameContaining(String name,String sex,Integer age){
		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.hasText(name)){
			criteria.andLike("name","%"+name+"%");
		}
		if (StringUtils.hasText(sex)){
			criteria.orEqualTo("sex",sex);
		}
		if(StringUtils.hasText(String.valueOf(age))){
			criteria.andEqualTo("age",age);
		}
		List<User> users = userMapper.selectByExample(example);
		return users;
	}







	@Override
	public User findByName(String name) {
		return userMapper.findByName(name);
	}

}