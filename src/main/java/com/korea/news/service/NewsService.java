package com.korea.news.service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.korea.news.dto.NewsDTO;
import com.korea.news.dto.NewsItem;
import com.korea.news.dto.NewsResponse;
import com.korea.news.mapper.NewsMapper;
import com.korea.news.util.TimeUtils;

@Service
public class NewsService {

    private final RestTemplate restTemplate;
    private final NewsMapper newsMapper;

    @Value("${deepsearch.api.url}")
    private String apiUrl;

    @Value("${deepsearch.api.key}")
    private String apiKey;

    @Autowired
    public NewsService(RestTemplate restTemplate, NewsMapper newsMapper) {
        this.restTemplate = restTemplate;
        this.newsMapper = newsMapper;
    }

    public List<NewsDTO> fetchNews(String keyword) {
        URI uri = UriComponentsBuilder
                .fromUriString(apiUrl)
                .path("/v1/articles/" + keyword)
                .queryParam("api_key", apiKey)
                .encode()
                .build()
                .toUri();

        try {
            // NewsResponse로 응답을 받음
            NewsResponse response = restTemplate.getForObject(uri, NewsResponse.class);
            if (response != null && response.getData() != null) {
                return response.getData().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.out.println("Error fetching news: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    public List<NewsDTO> fetchPublisher(String publisher) {
        URI uri = UriComponentsBuilder
                .fromUriString(apiUrl)
                .path("/v1/articles")
                .queryParam("keyword", "publisher:" + publisher)
                .queryParam("api_key", apiKey)
                .encode()
                .build()
                .toUri();

        try {
            // NewsResponse로 응답을 받음
            NewsResponse response = restTemplate.getForObject(uri, NewsResponse.class);
            if (response != null && response.getData() != null) {
                return response.getData().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.out.println("Error fetching news: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    @Transactional
    public void saveAllNews(List<NewsDTO> newsList, String keyword) {
        for (NewsDTO news : newsList) {
            news.setKeyword(keyword);  
            int count = newsMapper.countById(news.getId());
            if (count == 0) {
                newsMapper.insertNews(news);  
            }
        }
    }

    public NewsDTO findById(String newsId) {
        return newsMapper.findById(newsId);
    }

    private NewsDTO convertToDTO(NewsItem item) {
        NewsDTO dto = new NewsDTO();
        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setPublisher(item.getPublisher());
        dto.setAuthor(item.getAuthor());
        dto.setSummary(item.getSummary());
        dto.setImageUrl(item.getImageUrl());
        dto.setContentUrl(item.getContentUrl());
        dto.setPublishedAt(item.getPublishedAt());

        // API에서 받은 시간을 9시간 전으로 변환
        LocalDateTime publishedAtMinus9Hours = TimeUtils.subtract9Hours(item.getPublishedAt());

        // 변환된 시간을 저장 (DB에 저장할 시간)
        dto.setPublishedAt(Date.from(publishedAtMinus9Hours.atZone(ZoneId.systemDefault()).toInstant()));

        // 시간 계산 로직을 유틸리티 클래스로 위임
        String publishedAtAgo = TimeUtils.calculateTimeAgo(dto.getPublishedAt());
        dto.setPublishedAtAgo(publishedAtAgo);

        dto.setSections(item.getSections());
        
        return dto;
    }


	public String findsectionsById(String newsId) {
		return newsMapper.findsectionsById(newsId);
	}
}
