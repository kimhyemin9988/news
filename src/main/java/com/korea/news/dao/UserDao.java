package com.korea.news.dao;

import org.springframework.stereotype.Repository;

import com.korea.news.mapper.UserMapper;
import com.korea.news.vo.UserVO;

import lombok.RequiredArgsConstructor;
@Repository
@RequiredArgsConstructor
public class UserDao {
	
	final UserMapper userMapper;
	
	public UserVO loginCheck(String userid) {
		return userMapper.loginCheck(userid);
	}
	
	public int insert(UserVO vo) {
		 return userMapper.insert(vo);
	}

	public UserVO idCheck(String userid) {
		return userMapper.idCheck(userid);
	}

	

	
}
