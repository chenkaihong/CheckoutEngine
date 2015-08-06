package com.bear.servlet.newActivation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.util.CheckOutUtil;
import com.bear.util.PrintUtil;
import com.bear.util.excelUtil.Excel;
import com.bear.util.excelUtil.ExcelUtil;

/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * 用于统计孩子系统上线后对全线的影响
 * @Author Create by ckh
 * @Date 2015-01-09 下午4:34:09
 * @Description
 */
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String startTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		String fileName = null;
		
		try{
			SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Calendar startTimeCal = Calendar.getInstance();
			long startTimeLong = simpleFormat.parse(startTime).getTime();
			startTimeCal.setTimeInMillis(startTimeLong);
			
			Calendar endTimeCal = Calendar.getInstance();
			long endTimeLong = simpleFormat.parse(endTime).getTime();
			endTimeCal.setTimeInMillis(endTimeLong);
			
			final long threeMonthTimeLong = 1000L * 3600L * 24L * 30L * 3L;
			
			String startTimeString = format.format(startTimeCal.getTime());
			String endTimeString = format.format(endTimeCal.getTime());
			
			if((endTimeLong-startTimeLong) > threeMonthTimeLong){
				throw new RuntimeException(String.format("@@@ The checkout time must be less three month! StartTime:%s, EndTime:%s ", 
																															startTimeString,
																															endTimeString));
			}
			
			SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMM");
			fileName = fileFormat.format(startTimeCal.getTime());
			if(fileName == null){
				throw new RuntimeException("@@@ FileName is null");
			}
			
			startTime = startTimeString;
			endTime = endTimeString;
			
			System.out.println(String.format("<p>完成日期和文件名解析: 日期[%s-%s], 文件名[%s]</p>", startTimeString, endTimeString, fileName));
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		ServerNode backgroud = ConnectionManager.getBackgroud();
		
		try {
			Map<String, Integer> incomeMap = CheckOutUtil.singleCheckOut(new NewIncome(startTime, endTime), backgroud);
			System.out.println("<p>完成收入数据统计......</p>");
			Map<String, Integer> activationMap = CheckOutUtil.singleCheckOut(new NewActivation(fileName, startTime, endTime), backgroud);
			System.out.println("<p>完成激活数据统计(激活数据为设备激活)......</p>");
			Set<String> areaName = new HashSet<String>();
			areaName.addAll(incomeMap.keySet());
			areaName.addAll(activationMap.keySet());
			
			System.out.println("<p>开始生成Excel......</p>");
			Excel excel = ExcelUtil.getInstance();
			excel.getNewSheet(String.format("Activation_%s", fileName));
			excel.insertAndMove("序列").insertAndMove("渠道").insertAndMove("新增激活").insertAndMove("收入").nextRow();
			int index = 1;
			for(String name: areaName){
				Integer newIncome = incomeMap.get(name)==null?0:incomeMap.get(name);
				Integer newActivation = activationMap.get(name)==null?0:activationMap.get(name);
				excel.insertAndMove(index).insertAndMove(name).insertAndMove(newActivation).insertAndMove(newIncome).nextRow();
				index++;
			}
			
			System.out.println("<p>开始输出Excel......</p>");
			File newFile = excel.saveExcel(String.format("Activation_%s.xlsx", fileName));
			PrintUtil.outputFileResponse(resp, newFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}