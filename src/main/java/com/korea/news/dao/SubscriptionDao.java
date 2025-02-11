package com.korea.news.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.korea.news.mapper.SubscriptionMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubscriptionDao {

    private final SubscriptionMapper subscriptionMapper;

    // 사용자가 이미 채널을 구독했는지 확인
    public boolean isChannelSubscribed(int userIdx, String channelName) {
        return subscriptionMapper.countUserSubscription(userIdx, channelName) > 0;
    }

    // 새로운 구독을 추가
    public void insertSubscription(int userIdx, String channelName) {
        subscriptionMapper.insertSubscription(userIdx, channelName);
    }
    
 // SubscriptionDao.java
    public List<String> findSubscribedChannelsByUserId(int userIdx) {
        return subscriptionMapper.findSubscribedChannelsByUserId(userIdx);
    }

 // 구독을 취소
    public void deleteSubscription(int userIdx, String channelName) {
        subscriptionMapper.deleteSubscription(userIdx, channelName);
    }

}
