package com.bear.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TreadUtil {
	/**
	 * 将任务进行多线程处理,并返回Future对象作为返回
	 * @param callableAgentList
	 * @return
	 */
	public static<T> List<Future<T>> getFutureList(List<Callable<T>> callableAgentList){
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Future<T>> results = new ArrayList<Future<T>>();
		try{
			for(Callable<T> callabl : callableAgentList)
				results.add(exec.submit(callabl));
		}finally{
			exec.shutdown();
		}
		return results;
	}
}
