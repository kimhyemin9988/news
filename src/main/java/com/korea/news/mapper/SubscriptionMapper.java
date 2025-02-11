package com.korea.news.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubscriptionMapper {
	

	// 구독 여부를 확인하는 메서드
    int countUserSubscription(@Param("userIdx") int userIdx, @Param("channelName") String channelName);

    // 새로운 구독을 추가하는 메서드
    void insertSubscription(@Param("userIdx") int userIdx, @Param("channelName") String channelName);

    // 사용자가 구독 중인 채널 목록을 가져오는 메서드
    List<String> findSubscribedChannelsByUserId(@Param("userIdx") int userIdx);

    // 구독을 취소하는 메서드
    void deleteSubscription(@Param("userIdx") int userIdx, @Param("channelName") String channelName);
}
