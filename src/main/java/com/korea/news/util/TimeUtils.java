package com.korea.news.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.ui.Model;

public class TimeUtils {

	// KST (Korea Standard Time) ZoneId
    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    /**
     * Date 객체에서 9시간을 빼고 LocalDateTime으로 변환합니다.
     *
     * @param date 변환할 Date 객체
     * @return 9시간이 빠진 LocalDateTime 객체
     */
    public static LocalDateTime subtract9Hours(Date date) {
        ZonedDateTime kstZonedDateTime = date.toInstant().atZone(KST_ZONE);
        ZonedDateTime minus9Hours = kstZonedDateTime.minusHours(9);
        return minus9Hours.toLocalDateTime();
    }	
    // 모델에 현재 날짜를 추가하는 메서드
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static void CurrentDate(Model model) {
        LocalDate now = LocalDate.now();
        String formattedDate = now.format(formatter);
        model.addAttribute("currentDate", formattedDate);
    }

    // 시간 간격을 "X일 전", "X시간 전", "X분 전"으로 계산하는 메서드
    public static String calculateTimeAgo(Date publishedAt) {
        Instant now = Instant.now();
        Instant publishedInstant = publishedAt.toInstant();
        Duration duration = Duration.between(publishedInstant, now);

        long days = Math.abs(duration.toDays());
        long hours = Math.abs(duration.toHours()) % 24;
        long minutes = Math.abs(duration.toMinutes()) % 60;

        if (days > 0) {
            return days + "일 전";
        } else if (hours > 0) {
            return hours + "시간 전";
        } else {
            return minutes + "분 전";
        }
    }
}
