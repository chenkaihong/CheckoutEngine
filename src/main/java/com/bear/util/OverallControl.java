package com.bear.util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 设立一个全局变量,控制用户访问servlet的频率,同一个时间,
 * @author Administrator
 *
 */
public class OverallControl {
	private static AtomicBoolean isUsed;
	public static String whoUseing = "noUsing";
	
	public static void useSystem(){
		isUsed.getAndSet(true);
	}
	public static void releaseSystem(){
		isUsed.getAndSet(false);
	}
}
