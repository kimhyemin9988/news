package com.korea.news.vo;

import java.util.Date;

import lombok.Data;

@Data
public class SubscriptionVO {
	private int subId;
    private int userIdx;
    private String channelName;
    private Date subscribedAt;
}
