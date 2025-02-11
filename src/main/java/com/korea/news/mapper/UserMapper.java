package com.korea.news.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.korea.news.vo.UserVO;
@Mapper
public interface UserMapper {
	UserVO loginCheck(String userid);
	UserVO idCheck(String userid);
	int insert(UserVO vo);
	

	

}
