package com.bear.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bear.task.TaskRepertory;

public class Listener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		TaskRepertory.initialize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
