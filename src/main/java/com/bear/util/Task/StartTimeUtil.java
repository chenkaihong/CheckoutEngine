package com.bear.util.Task;

import java.text.ParseException;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * @Author Create by 梁健荣
 * @Date 2015年4月16日 下午2:08:16
 * @Description 
 */
public class StartTimeUtil {
	
	/**
	 * 获取固定时刻开启时间,格式如下: HH:mm:ss
	 * @param startTime 格式如下: HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public static long momentTime(String startTime){
		Pattern pattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
		if(!pattern.matcher(startTime).find()){
			throw new RuntimeException("Must post like this < 14:30:25 > - " + startTime);
		}
		Calendar now = Calendar.getInstance();
		String[] temp = startTime.split(":");
		now.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[0]));
		now.set(Calendar.MINUTE, Integer.parseInt(temp[1]));
		now.set(Calendar.SECOND, Integer.parseInt(temp[2]));
		return now.getTimeInMillis();
	}
	
	/**
	 * 设定多少秒以后开始执行秒循环任务
	 * @param secondLater 多少秒开始任务,如果输入为0,则代表从当前任务添加时刻开始执行,若任务较多,可能会导致任务进入下一个秒循环周期,推荐使用立即开始标志确保立即执行的任务
	 * @return
	 */
	public static long delaySeconds(int secondLater){
		return System.currentTimeMillis() + (secondLater * 1000L);
	}
	
}
