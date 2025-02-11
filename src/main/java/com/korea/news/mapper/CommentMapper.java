package com.korea.news.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.korea.news.vo.DaegulVO;

@Mapper
public interface CommentMapper {

	int insert(DaegulVO vo);

	List<DaegulVO> findCommentsByNewsId(String newsId);

	DaegulVO selectOne(int num);

	int update_step(DaegulVO base_vo);

	int reply(DaegulVO vo);

	int delete(DaegulVO vo);

	int updateComment(DaegulVO vo);

}
