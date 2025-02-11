package com.korea.news.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.korea.news.dao.CommentDao;
import com.korea.news.vo.DaegulVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	@Autowired
    private CommentDao commentDao;

	public int insert(DaegulVO vo) {
		
        return commentDao.insert(vo);
	}

	public List<DaegulVO> findCommentsByNewsId(String newsId) {
		
		return commentDao.findCommentsByNewsId(newsId);
	}

	public DaegulVO selectOne(int num) {
		return commentDao.selectOne(num);
	}

	public int update_step(DaegulVO base_vo) {		
		return commentDao.update_step(base_vo);
	}

	public int reply(DaegulVO vo) {
		return commentDao.reply(vo);
	}

	public int delete(DaegulVO vo) {
		return commentDao.delete(vo);
	}

	public int updateComment(DaegulVO vo) {
		return commentDao.updateComment(vo);
	}
}
