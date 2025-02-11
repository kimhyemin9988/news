package com.korea.news.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.korea.news.dto.NewsDTO;
@Mapper
public interface NewsMapper {
	 int countById(String id);
	 void insertNews(NewsDTO news);
	 NewsDTO findById(String id);
	String findsectionsById(String newsId);
	
}
