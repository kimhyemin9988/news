package com.korea.news.vo;

import java.util.Date;

import lombok.Data;

@Data
public class DaegulVO {
	 private int num;		//댓글 번호
	 private String userid;  // 사용자 ID
	 private String id;      // 뉴스 ID
     private String content; // 댓글 내용
     private String ip;      // 작성자 IP
     private Date regdate;   // 작성일자
     private int ref;        // 기준글 번호
     private int step;       // 댓글 순서
     private int depth;      // 댓글 깊이
}