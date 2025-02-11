package com.korea.news.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.korea.news.dao.SubscriptionDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionDao subscriptionDao;

    // 채널 구독 처리
    public void subscribeToChannel(int userIdx, String channelName) { 
        // 이미 구독된 채널인지 확인 후 구독 처리
        if (!subscriptionDao.isChannelSubscribed(userIdx, channelName)) {
            subscriptionDao.insertSubscription(userIdx, channelName);
        }
    }

 // SubscriptionService.java
    public List<String> getSubscribedChannels(int userIdx) {
        return subscriptionDao.findSubscribedChannelsByUserId(userIdx);
    }

 // 채널 구독 취소 처리
    public void unsubscribeFromChannel(int userIdx, String channelName) {
        // 구독된 채널이 존재하면 구독을 취소함
        if (subscriptionDao.isChannelSubscribed(userIdx, channelName)) {
            subscriptionDao.deleteSubscription(userIdx, channelName);
        }
    }

}
