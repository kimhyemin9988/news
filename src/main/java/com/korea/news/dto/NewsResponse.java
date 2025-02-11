package com.korea.news.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsResponse {
    private List<NewsItem> data; // 'data'라는 필드에 List<NewsItem>이 포함됨
}
