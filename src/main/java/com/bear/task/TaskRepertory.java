package com.bear.task;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.connection.ServerNodeFactory;
import com.bear.servlet.activityEffect.Main;
import com.bear.servlet.activityEffect.Player;
import com.bear.servlet.activityEffect.PlayerInfo;
import com.bear.servlet.activityEffect.ReturnData;
import com.bear.servlet.activityEffect.ServerChannel;
import com.bear.servlet.activityEffect.SingleReturn;
import com.bear.servlet.activityEffect.Voucher;
import com.bear.util.CheckOutUtil;
import com.bear.util.FileUtil;
import com.bear.util.JsonManager;
import com.bear.util.TimeUtil;
import com.bear.util.Task.CycleType;
import com.bear.util.Task.MomentTaskBase;
import com.bear.util.Task.TaskManager;

public class TaskRepertory {
	
//	2015-04-14 12:00:00 - 2015-04-14 23:59:59
//	2015-04-14 00:00:00 - 2015-04-14 23:59:59
//	2015-04-14 00:00:00 - 2015-04-14 11:30:00
//	2015-04-14 16:00:00 - 2015-04-14 23:59:59

	public static final TaskManager taskManager = new TaskManager();
	
	public static void initialize(){
		
		System.out.println("加入 <11:30:00对系统数据切片操作(00:00:00 - 11:30:00)> 任务");
		taskManager.registerRepeat(new MomentTaskBase("11:30:00对系统数据切片操作(00:00:00 - 11:30:00)", "11:30:00", 1, CycleType.DAY, MomentTaskBase.isRunLater) {
			@Override
			public void run() {
				try {
					Calendar temp = Calendar.getInstance();
					temp.set(Calendar.HOUR_OF_DAY, 0);
					temp.set(Calendar.MINUTE, 0);
					temp.set(Calendar.SECOND, 0);
					temp.set(Calendar.MILLISECOND, 0);
					String beginString = TimeUtil.fromCalendarToString(temp);
					temp.set(Calendar.HOUR_OF_DAY, 11);
					temp.set(Calendar.MINUTE, 30);
					temp.set(Calendar.SECOND, 00);
					temp.set(Calendar.MILLISECOND, 0);
					String endString = TimeUtil.fromCalendarToString(temp);
					
					System.out.println("Begin 11:30 backup! - " + TimeUtil.nowTimeString());
					System.out.println(beginString + " - " + endString);
					
					ReturnData returnSingleData = getReturnData(beginString, endString);
					
					String saveFile = Main.getViewName(beginString, endString);
					FileUtil.fromStringToFile(JsonManager.getGson().toJson(returnSingleData), saveFile, true, true);
					System.out.println("end 11:30 backup! - " + TimeUtil.nowTimeString());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		System.out.println("加入 <23:59:55对系统数据切片操作(00:00:00 - 23:59:59)> 任务");
		taskManager.registerRepeat(new MomentTaskBase("23:59:55对系统数据切片操作(00:00:00 - 23:59:59)", "23:59:55", 1, CycleType.DAY, MomentTaskBase.isRunLater) {
			@Override
			public void run() {
				try {
					Calendar temp = Calendar.getInstance();
					temp.set(Calendar.HOUR_OF_DAY, 0);
					temp.set(Calendar.MINUTE, 0);
					temp.set(Calendar.SECOND, 0);
					temp.set(Calendar.MILLISECOND, 0);
					String beginString = TimeUtil.fromCalendarToString(temp);
					temp.set(Calendar.HOUR_OF_DAY, 23);
					temp.set(Calendar.MINUTE, 59);
					temp.set(Calendar.SECOND, 59);
					temp.set(Calendar.MILLISECOND, 999);
					String endString = TimeUtil.fromCalendarToString(temp);
					
					System.out.println("Begin <23:59:55对系统数据切片操作(00:00:00 - 23:59:59)> backup! - " + TimeUtil.nowTimeString());
					System.out.println(beginString + " - " + endString);
					
					ReturnData returnSingleData = getReturnData(beginString, endString);
					
					String saveFile = Main.getViewName(beginString, endString);
					FileUtil.fromStringToFile(JsonManager.getGson().toJson(returnSingleData), saveFile, true, true);
					System.out.println("end <23:59:55对系统数据切片操作(00:00:00 - 23:59:59)> backup! - " + TimeUtil.nowTimeString());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		System.out.println("加入 <23:59:55对系统数据切片操作(12:00:00 - 23:59:59)> 任务");
		taskManager.registerRepeat(new MomentTaskBase("23:59:55对系统数据切片操作(12:00:00 - 23:59:59)", "23:59:55", 1, CycleType.DAY, MomentTaskBase.isRunLater) {
			@Override
			public void run() {
				try {
					Calendar temp = Calendar.getInstance();
					temp.set(Calendar.HOUR_OF_DAY, 12);
					temp.set(Calendar.MINUTE, 0);
					temp.set(Calendar.SECOND, 0);
					temp.set(Calendar.MILLISECOND, 0);
					String beginString = TimeUtil.fromCalendarToString(temp);
					temp.set(Calendar.HOUR_OF_DAY, 23);
					temp.set(Calendar.MINUTE, 59);
					temp.set(Calendar.SECOND, 59);
					temp.set(Calendar.MILLISECOND, 999);
					String endString = TimeUtil.fromCalendarToString(temp);
					
					System.out.println("Begin <23:59:55对系统数据切片操作(12:00:00 - 23:59:59)> backup! - " + TimeUtil.nowTimeString());
					System.out.println(beginString + " - " + endString);
					
					ReturnData returnSingleData = getReturnData(beginString, endString);
					
					String saveFile = Main.getViewName(beginString, endString);
					FileUtil.fromStringToFile(JsonManager.getGson().toJson(returnSingleData), saveFile, true, true);
					System.out.println("end <23:59:55对系统数据切片操作(12:00:00 - 23:59:59)> backup! - " + TimeUtil.nowTimeString());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		System.out.println("加入 <23:59:55对系统数据切片操作(16:00:00 - 23:59:59)> 任务");
		taskManager.registerRepeat(new MomentTaskBase("23:59:55对系统数据切片操作(16:00:00 - 23:59:59)", "23:59:55", 1, CycleType.DAY, MomentTaskBase.isRunLater) {
			@Override
			public void run() {
				try {
					Calendar temp = Calendar.getInstance();
					temp.set(Calendar.HOUR_OF_DAY, 16);
					temp.set(Calendar.MINUTE, 0);
					temp.set(Calendar.SECOND, 0);
					temp.set(Calendar.MILLISECOND, 0);
					String beginString = TimeUtil.fromCalendarToString(temp);
					temp.set(Calendar.HOUR_OF_DAY, 23);
					temp.set(Calendar.MINUTE, 59);
					temp.set(Calendar.SECOND, 59);
					temp.set(Calendar.MILLISECOND, 999);
					String endString = TimeUtil.fromCalendarToString(temp);
					
					System.out.println("Begin <23:59:55对系统数据切片操作(16:00:00 - 23:59:59)> backup! - " + TimeUtil.nowTimeString());
					System.out.println(beginString + " - " + endString);
					
					ReturnData returnSingleData = getReturnData(beginString, endString);
					
					String saveFile = Main.getViewName(beginString, endString);
					FileUtil.fromStringToFile(JsonManager.getGson().toJson(returnSingleData), saveFile, true, true);
					System.out.println("end <23:59:55对系统数据切片操作(16:00:00 - 23:59:59)> backup! - " + TimeUtil.nowTimeString());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		taskManager.start();
	}
	
	private static ReturnData getReturnData(String beginString,String endString) throws SQLException, ParseException{
		ServerNode backgroud = ConnectionManager.getBackgroud();
		Map<Integer, ServerNode> testserverNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
		
		Set<Integer> zgsy = CheckOutUtil.singleCheckOut(new ServerChannel("%zgsy%","%中国手游%"), backgroud);
		Set<Integer> yibu = CheckOutUtil.singleCheckOut(new ServerChannel("%ybh%",""), backgroud);
		Set<Integer> mingtong = CheckOutUtil.singleCheckOut(new ServerChannel("%app%",""), backgroud);
		// Map<playerID, Voucher>
		Map<Integer, Integer> voucherMap = CheckOutUtil.singleCheckOut(new Voucher(beginString, endString), backgroud);
		// Map<serverID, Map<playerID, totalVoucher>>
		Map<Integer, Map<Integer, PlayerInfo>> playerVip = CheckOutUtil.multipleCheckOutByFactory(new Player(beginString, endString), testserverNodeMap);
		ReturnData returnSingleData = SingleReturn.getSingleReturn(voucherMap, playerVip, testserverNodeMap, zgsy, yibu, mingtong);
		return returnSingleData;
	}
}
