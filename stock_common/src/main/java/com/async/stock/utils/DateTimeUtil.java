package com.async.stock.utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * 使用Joda-Time库处理股票交易日期和时间的实用工具类。
 */
@Api(tags = "日期时间工具")
public class DateTimeUtil {

    @ApiOperation(value = "获取给定日期之前的最后一个有效交易日")
    public static DateTime getPreviousTradingDay(DateTime dateTime) {
        int weekNum = dateTime.dayOfWeek().get();
        DateTime preDateTime = null;
        if (weekNum == 1) {
            // 如果是周一，则T-1为周五
            preDateTime = dateTime.minusDays(3);
        } else if (weekNum == 7) {
            // 如果是周日，则T-1为周五
            preDateTime = dateTime.minusDays(2);
        } else {
            preDateTime = dateTime.minusDays(1);
        }
        return getDateTimeWithoutSecond(preDateTime);
    }

    @ApiOperation(value = "判断指定日期是否为工作日")
    public static boolean isWorkDay(DateTime dateTime) {
        int weekNum = dateTime.dayOfWeek().get();
        return weekNum >= 1 && weekNum <= 5;
    }

    @ApiOperation(value = "获取指定日期的前一天日期")
    public static DateTime getPreDateTime(DateTime dateTime) {
        return dateTime.minusDays(1);
    }

    @ApiOperation(value = "将日期对象转换为字符串")
    public static String parseToString(DateTime dateTime, String pattern) {
        return dateTime.toString(DateTimeFormat.forPattern(pattern));
    }

    @ApiOperation(value = "获取股票日期格式的字符串")
    public static String parseToString4Stock(DateTime dateTime) {
        return parseToString(dateTime, "yyyyMMddHHmmss");
    }

    @ApiOperation(value = "获取指定日期的收盘时间")
    public static DateTime getCloseDate(DateTime dateTime) {
        return dateTime.withHourOfDay(14).withMinuteOfHour(58).withSecondOfMinute(0).withMillisOfSecond(0);
    }

    @ApiOperation(value = "获取指定日期的开盘时间")
    public static DateTime getOpenDate(DateTime dateTime) {
        return dateTime.withHourOfDay(9).withMinuteOfHour(30).withSecondOfMinute(0).withMillisOfSecond(0);
    }

    @ApiOperation(value = "获取最接近的有效股票交易时间，精确到分钟")
    public static DateTime getLastDate4Stock(DateTime target) {
        if (isWorkDay(target)) {
            if (target.isBefore(getOpenDate(target))) {
                target = getCloseDate(getPreviousTradingDay(target));
            } else if (isMarketOffTime(target)) {
                target = target.withHourOfDay(11).withMinuteOfHour(28).withSecondOfMinute(0).withMillisOfSecond(0);
            } else if (target.isAfter(getCloseDate(target))) {
                target = getCloseDate(target);
            }
        } else {
            target = getCloseDate(getPreviousTradingDay(target));
        }
        return getDateTimeWithoutSecond(target);
    }

    @ApiOperation(value = "判断当前时间是否在股市午休时间内")
    public static boolean isMarketOffTime(DateTime target) {
        DateTime start = target.withHourOfDay(11).withMinuteOfHour(28).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime end = target.withHourOfDay(13).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        return target.isAfter(start) && target.isBefore(end);
    }

    @ApiOperation(value = "将日期的秒和毫秒数归零")
    public static DateTime getDateTimeWithoutSecond(DateTime dateTime) {
        return dateTime.withSecondOfMinute(0).withMillisOfSecond(0);
    }
}