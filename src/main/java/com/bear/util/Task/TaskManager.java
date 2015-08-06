package com.bear.util.Task;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * @Author Create by ckh
 * @Date 2015年4月15日 上午10:41:54
 * @Description 
 */
public class TaskManager {
	
	public final boolean isRunNow = true;					// 马上执行
	public final boolean isRunLater = false;				// 稍后执行
	
	private final int threadSize = 5;
	private final List<MomentTaskBase> taskList = new CopyOnWriteArrayList<MomentTaskBase>();		// 任务列表
	private ExecutorService guardPool = Executors.newSingleThreadExecutor();						// 守护线程,维持workerPool和taskPool的驱动
	private ExecutorService workerPool = Executors.newFixedThreadPool(threadSize);					// 工作线程,作为消费者来消耗到时任务
	private final DelayQueue<MomentTaskBase> taskPool = new DelayQueue<MomentTaskBase>();			// 任务延迟优先队列,负责任务调度
	
	/**
	 * 注册循环执行和的任务
	 * @param task
	 */
	public void registerRepeat(MomentTaskBase task){
		taskList.add(task);
	}
	/**
	 * 注册只执行一次的任务
	 * @param task
	 */
	public void registerOnce(MomentTaskBase task){
		
	}
	
	public void start(){
		guardPool.execute(new Runnable() {
			@Override
			public void run() {
				// 加入所有注册任务
				for(MomentTaskBase s : taskList){
					taskPool.offer(s);
				}
				while(true){
					MomentTaskBase temp = null;
					try {
						temp = taskPool.take();
					} catch (InterruptedException e) {
						printERR(e);
						e.printStackTrace();
						shutdownProc(temp);
					}
					if(temp != null){
						workerPool.execute(temp);
						taskPool.offer(temp.reloadTime());
					}
				}
			}
		});
	}
	
	public synchronized void shutdown () {
		if (guardPool != null && !guardPool.isShutdown()) {
			guardPool.shutdown();
			guardPool = null;
		}
		
		if (workerPool != null && !workerPool.isShutdown()) {
			workerPool.shutdown();
			workerPool = null;
		}
	}
	
	// 打印详细报错情况
	private void printERR(Exception e){
		
	}
	// 关闭时的处理方案
	private void shutdownProc(MomentTaskBase task){
		
	}
}