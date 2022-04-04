package com.gougou.ib.company.util;

import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeUtil {

    public static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DF_TIME = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DF_MINUTE = DateTimeFormatter.ofPattern("mm");
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MM-dd");
    public static final DateTimeFormatter YYMMDD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Long diffOfTimeMinute(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return Math.abs(duration.toMinutes());
    }

    public static Long diffOfTimeDay(LocalDate start, LocalDate end) {
        return end.toEpochDay() - start.toEpochDay();
    }


    /**
     * 以周为单位返回两个该周内的全部日期
     *
     * @param start YYYY-MM-dd格式的字符串
     * @param end
     * @return
     */
    public static List<List<String>> weekList(String start, String end) {
        if (end.compareTo(start) < 0) {
            String tmp = end;
            end = start;
            start = tmp;
        }
        org.joda.time.LocalDate startDate = org.joda.time.LocalDate.parse(start, DateTimeFormat.forPattern("yyyy-MM-dd"));
        org.joda.time.LocalDate endDate = org.joda.time.LocalDate.parse(end, DateTimeFormat.forPattern("yyyy-MM-dd"));
        List<List<String>> weekList = new ArrayList<>();
        //转换成joda-time的对象
        org.joda.time.LocalDate firstDay = startDate.dayOfWeek().withMinimumValue();
        org.joda.time.LocalDate lastDay = endDate.dayOfWeek().withMaximumValue();
        //计算两日期间的区间天数
        org.joda.time.Period p = new org.joda.time.Period(firstDay, lastDay, PeriodType.days());
        int days = p.getDays();
        if (days > 0) {
            int weekLength = 7;
            for (int i = 0; i < days; i = i + weekLength) {
                List<String> tmp = new ArrayList<>();
                for (int j = 0; j <= 6; j++) {
                    tmp.add(firstDay.plusDays(i + j).toString("yyyy-MM-dd"));
                }
                weekList.add(tmp);
            }
        }
        return weekList;
    }

}
