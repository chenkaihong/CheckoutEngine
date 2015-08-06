package com.bear.util.Task;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * @Author Create by ckh
 * @Date 2015年4月15日 下午6:27:44
 * @Description 
 */
public abstract class MomentTaskBase implements Delayed,Runnable{
	// 暂时只能使用这set当中的任务循环类别,因为Calendar中的枚举值具有重复性,所以只能自定义映射加以限制
	private static final Map<CycleType,Integer> typeMap = new HashMap<CycleType,Integer>();
	static{
		typeMap.put(CycleType.YEAR, Calendar.YEAR);
		typeMap.put(CycleType.MONTH, Calendar.MONTH);
		typeMap.put(CycleType.WEEK, Calendar.WEEK_OF_MONTH);
		typeMap.put(CycleType.DAY, Calendar.DAY_OF_MONTH);
		typeMap.put(CycleType.HOUR, Calendar.HOUR_OF_DAY);
		typeMap.put(CycleType.MINUTE, Calendar.MINUTE);
		typeMap.put(CycleType.SECOND, Calendar.SECOND);
	}
	private static final long SECOND = 1000L;				// 秒的毫秒表示
	private static final long MINUTE = SECOND * 60L;		// 分钟的毫秒表示
	private static final long HOUR = MINUTE * 60L;			// 小时的毫秒表示
	private static final long DAY = HOUR * 24L;				// 天的毫秒表示
	private static final long WEEK = DAY * 7L;				// 星期的毫秒表示
	
	public static final boolean isRunNow = true;					// 马上执行
	public static final boolean isRunLater = false;				// 稍后执行
	
	private final String TaskName;		// 任务名称
	private volatile long runTime;		// 当第一次执行任务时,由工具类计算最近一次任务时间,之后执行任务时则直接按照cycleTime进行执行(使用TimeUnit.MILLISECONDS)
	private volatile long runTimeTemp;	// 当立即执行任务时,负责暂时存储runTime的值(使用TimeUnit.MILLISECONDS)
	private final int Step;				// 循环周期步长,例如: 每2秒执行一次,则2则为步长. 每3周执行一次,则3则为步长
	private final int CalendarType;		// 使用CalendarType做时间类型
	
	/**
	 * 定时在周期的某个时刻执行
	 * @param taskName		任务名称
	 * @param startTime		具体的开始时间(例如: 2015-04-16 14:30:25)
	 * @param step			周期间隔(例如: cycleType为DAY, step为2, 则2天执行一次, 并且首次执行时间为当天的开始时刻,如果已经过了开始时刻,则需要下个周期的开始时刻再执行)
	 * @param cycleType		周期类型(YEAR,MONTH,WEEK,DAY,HOUR,MINUTE,SECOND)
	 * @param isRunNow		是否马上执行一次
	 */
	public MomentTaskBase(String taskName, long startTime, int step, CycleType cycleType, boolean isRunNow){
		Integer CalendarType = typeMap.get(cycleType);
		if(CalendarType == null){
			throw new RuntimeException("CycleType is missing! - " + cycleType);
		}
		
		this.TaskName = taskName;
		this.CalendarType = CalendarType;
		this.Step = step;
		if(isRunNow){
			this.runTime = System.currentTimeMillis();
			this.runTimeTemp = getFirstRunTime(startTime);
		}else{
			this.runTime = getFirstRunTime(startTime);
			this.runTimeTemp = 0L;
		}
	}
	/**
	 * 定时在周期的某个时刻执行
	 * @param taskName		任务名称
	 * @param startMoment	开始时刻(格式: HH:mm:ss)
	 * @param step			周期间隔(例如: cycleType为DAY, step为2, 则2天执行一次, 并且首次执行时间为当天的开始时刻,如果已经过了开始时刻,则需要下个周期的开始时刻再执行)
	 * @param cycleType		周期类型(YEAR,MONTH,WEEK,DAY,HOUR,MINUTE,SECOND)
	 * @param isRunNow		是否马上执行一次
	 */
	public MomentTaskBase(String taskName, String startMoment, int step, CycleType cycleType, boolean isRunNow){
		this(taskName, StartTimeUtil.momentTime(startMoment), step, cycleType, isRunNow);
	}
	/**
	 * 延迟N秒之后开始首次执行任务,并进入执行周期
	 * @param taskName		任务名称
	 * @param delaySeconds	延迟多少秒以后开始执行()
	 * @param step			周期间隔(例如: cycleType为DAY, step为2, 则2天执行一次, 并且首次执行时间为当天开始任务并等待完延迟时间后开始执行)
	 * @param cycleType		周期类型(YEAR,MONTH,WEEK,DAY,HOUR,MINUTE,SECOND)
	 * @param isRunNow		是否马上执行一次
	 */
	public MomentTaskBase(String taskName, int delaySeconds, int step, CycleType cycleType, boolean isRunNow){
		this(taskName, StartTimeUtil.delaySeconds(delaySeconds), step, cycleType, isRunNow);
	}
	
	public synchronized MomentTaskBase reloadTime(){
		if(runTimeTemp == 0){
			runTime = System.currentTimeMillis() + getCycleTime();
		}else{
			runTime = runTimeTemp;
			runTimeTemp = 0L;
		}
		return this;
	}
	
	/**
	 * 通过步长计算出第2,第3...第n次的循环时间,例如day类型的循环
	 * @param Step 步长,例如: step = 1,type = year 则代表1年循环一次, step = 1,type = day 则代表1天循环一次
	 * @return 第2,第3...第n次的等待时间(循环时间)
	 */
	private long getCycleTime(){
		switch(CalendarType){
			case Calendar.SECOND:
				return SECOND * Step;
			case Calendar.MINUTE:
				return MINUTE * Step;
			case Calendar.HOUR_OF_DAY:
				return HOUR * Step;
			case Calendar.DAY_OF_MONTH:
				return DAY * Step;
			case Calendar.WEEK_OF_MONTH:
				return WEEK * Step;
			default:
				Calendar runTime = Calendar.getInstance();
				long runLong = runTime.getTimeInMillis();
				runTime.add(CalendarType, Step);
				long nextLong = runTime.getTimeInMillis();
				return nextLong-runLong;
		}
	}
	/**
	 * 根据StartTime计算出第一次循环所延迟的时间,
	 * @return 若开始时间大于当前时间,则返回延迟等待时间.若开始时间小于当前时间,则先反复计算下次开始时间直到下次开始时间比当前时间大,再使用下次开始时间减去当前时间1 
	 */
	private long getFirstRunTime(long startTime){
		long now = System.currentTimeMillis();
		while(startTime < now){
			startTime += getCycleTime();
		}
		return startTime;
	}
	
	@Override
	public int compareTo(Delayed other) {
		long diff = 0L;
		if (other == this){
			diff = 0L;
		}else if(other instanceof MomentTaskBase){
			MomentTaskBase otherTask = (MomentTaskBase)other;
			diff = runTime - otherTask.runTime;
		}else{
			diff = getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS);
		}
        return (diff == 0) ? 0 : ((diff < 0) ? -1 : 1);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long diff = unit.convert(runTime - System.currentTimeMillis(), TimeUnit.NANOSECONDS);
        return diff;
	}

	public String getTaskName() {
		return TaskName;
	}
}
