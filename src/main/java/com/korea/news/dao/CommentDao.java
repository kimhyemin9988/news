package com.korea.news.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.korea.news.mapper.CommentMapper;
import com.korea.news.vo.DaegulVO;

import lombok.RequiredArgsConstructor;
@Repository
@RequiredArgsConstructor
public class CommentDao {
	
	final CommentMapper commentMapper;
	
	public int insert(DaegulVO vo) {
		return commentMapper.insert(vo);
	}

	public List<DaegulVO> findCommentsByNewsId(String newsId) {
		
		return commentMapper.findCommentsByNewsId(newsId);
	}

	public DaegulVO selectOne(int num) {
		return commentMapper.selectOne(num);
	}

	public int update_step(DaegulVO base_vo) {
		return commentMapper.update_step(base_vo);
	}

	public int reply(DaegulVO vo) {
		return commentMapper.reply(vo);
	}

	public int delete(DaegulVO vo) {
		return commentMapper.delete(vo);
	}

	public int updateComment(DaegulVO vo) {
		return commentMapper.updateComment(vo);
	}

}
