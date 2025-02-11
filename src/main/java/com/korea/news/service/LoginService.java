package com.korea.news.service;

import org.springframework.stereotype.Service;

import com.korea.news.dao.UserDao;
import com.korea.news.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	final UserDao userDao;
	
	public UserVO loginCheck(String userid) {
		return userDao.loginCheck(userid);
	}
	
	public int insert(UserVO vo) {		
		return userDao.insert(vo);
	}

	public UserVO idCheck(String userid) {
		return userDao.idCheck(userid);
	}

	


}
