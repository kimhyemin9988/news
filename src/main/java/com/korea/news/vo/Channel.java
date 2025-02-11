package com.korea.news.vo;

import java.util.List;

import com.korea.news.dto.NewsDTO;

import lombok.Data;

@Data
public class Channel {
	private String name;
    private String image;
    private List<NewsDTO> relatedNews;
    
    public Channel(String name, String image) {
        this.name = name;
        this.image = image;
    }

		
	
}
