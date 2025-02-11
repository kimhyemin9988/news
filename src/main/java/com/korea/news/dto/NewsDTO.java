package com.korea.news.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsDTO {
    private String id;

    // sections 필드를 List<String>으로 변경
    private List<String> sections;

    private String title;
    private String publisher;
    private String author;
    private String summary;
    private String imageUrl;
    private String contentUrl;
    private Date publishedAt;
    private String publishedAtAgo;
    private String keyword;
}
