package com.bear.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeUtil {
	private static final String translateDateFormat = "yyyy-MM-dd_HH:mm:ss";
	private static final String defaultFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String defaultFormatWithoutSecond = "yyyy-MM-dd HH:mm";
	private static final String fileName = "yyyy-MM-dd";
	
	private static ThreadLocal<Map<String,SimpleDateFormat>> threadLocal = new ThreadLocal<Map<String,SimpleDateFormat>>(){
		protected Map<String,SimpleDateFormat> initialValue() {
			Map<String,SimpleDateFormat> dataFormatMap = new HashMap<String,SimpleDateFormat>();
			
			// 可以在这加入更多格式的SimpleDateFormat
			dataFormatMap.put(translateDateFormat, new SimpleDateFormat(translateDateFormat));
			dataFormatMap.put(defaultFormat, new SimpleDateFormat(defaultFormat));
			dataFormatMap.put(defaultFormatWithoutSecond, new SimpleDateFormat(defaultFormatWithoutSecond));
			dataFormatMap.put(fileName, new SimpleDateFormat(fileName));
			// ... end
			
			return dataFormatMap;
		};
	};

	public static long fromStringToLong(String date) throws ParseException{
		return threadLocal.get().get(defaultFormat).parse(date).getTime();
	}
	
	public static Calendar fromStringToCalendar(String date) throws ParseException{
		Date temp = threadLocal.get().get(defaultFormat).parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(temp);
		return cal;
	}
	public static Calendar fromStringToCalendarWithoutSecond(String date) throws ParseException{
		Date temp = threadLocal.get().get(defaultFormatWithoutSecond).parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(temp);
		return cal;
	}
	
	public static String fromCalendarToString(Calendar date){
		return threadLocal.get().get(defaultFormat).format(date.getTime());
	}
	/**
	 * 修剪calendar 将日分秒和毫秒都去掉
	 * @param time
	 */
	public static void fixTime(Calendar time){
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * 直接返回当前时间的yyyy-MM-dd_HH:mm:ss形式String
	 */
	public static String nowTimeString(){
		return threadLocal.get().get(defaultFormat).format(new Date());
	}
	public static String fromCalendarToFileName(Calendar date){
		return threadLocal.get().get(fileName).format(date.getTime());
	}
	
	/**
	 * 直接返回当前时间Timestamp形式
	 */
	public static Timestamp nowTimeTimestamp(){
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
//	public static void main(String[] args) {
//		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while(true){
//					System.out.println(TimeUtil.nowTimeString());
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
//		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while(true){
//					System.out.println(TimeUtil.nowTimeString());
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
//		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while(true){
//					System.out.println(TimeUtil.nowTimeString());
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
//		
//	}
}
