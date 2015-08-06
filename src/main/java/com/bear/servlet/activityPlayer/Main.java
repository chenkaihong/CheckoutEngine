package com.bear.servlet.activityPlayer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.connection.ServerNodeFactory;
import com.bear.util.CheckOutUtil;
import com.bear.util.PrintUtil;
import com.bear.util.TimeUtil;
import com.bear.util.VipUtil;
import com.bear.util.excelUtil.Excel;
import com.bear.util.excelUtil.ExcelUtil;

public class Main extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Calendar beginTime = null;
		Calendar endTime = null;
		int maxVipLevel = 14;
		try {
			beginTime = TimeUtil.fromStringToCalendar(req.getParameter("beginTime"));
			endTime = TimeUtil.fromStringToCalendar(req.getParameter("endTime"));
			if(beginTime == null || endTime == null){
				throw new Exception("BeginTime or endTime is null!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(String.format("Please insert the correct format time! - beginTime: %s, endTime: %s", beginTime, endTime),e);
		}
		// 修剪时间
		TimeUtil.fixTime(beginTime);
		TimeUtil.fixTime(endTime);
		
		ServerNode backgroud = ConnectionManager.getBackgroud();
		Map<Integer, ServerNode> testserverNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
		
		Excel excel = ExcelUtil.getInstance();
		
		while(beginTime.before(endTime)){
			String timeA = TimeUtil.fromCalendarToString(beginTime);
			excel.getNewSheet(TimeUtil.fromCalendarToFileName(beginTime));
			beginTime.add(Calendar.DAY_OF_MONTH, 1);
			String timeB = TimeUtil.fromCalendarToString(beginTime);
			
			System.out.println("CheckTime: " + timeA + " - " + timeB);
			
			try {
				// Map<playerID, Voucher>
				Map<Integer, Integer> voucherMap = CheckOutUtil.singleCheckOut(new Voucher(timeA, timeB), backgroud);
				// Map<serverID, Map<playerID, totalVoucher>>
				Map<Integer, Map<Integer, PlayerInfo>> playerVip = CheckOutUtil.multipleCheckOutByFactory(new Player(timeA, timeB), testserverNodeMap);
				
				
				excel.insertAndMove("服务器ID")
					 .insertAndMove("服务器名称")
					 .insertAndMove("VIP等级")
					 .insertAndMove("活跃人数")
					 .insertAndMove("活跃充值金额")
					 .insertAndMove("活跃充值人数")
					 .insertAndMove("活跃剩余元宝总数")
					 .insertAndMove("活跃人均剩余元宝量").nextRow();
				for(Entry<Integer, Map<Integer, PlayerInfo>> s : playerVip.entrySet()){
					int serverID = s.getKey();
					Map<Integer, Integer> vipNum = new HashMap<Integer,Integer>();
					Map<Integer, Integer> voucherNum = new HashMap<Integer,Integer>();
					Map<Integer, Integer> voucherPlayerNum = new HashMap<Integer,Integer>();
					Map<Integer, Integer> yuanbaoCount = new HashMap<Integer,Integer>();
					for(Entry<Integer, PlayerInfo> t : s.getValue().entrySet()){
						int playerID = t.getKey();
						PlayerInfo info = t.getValue();
						int vipLevel = VipUtil.getVipLevel(info.voucherTotal);
						int yuanBao = info.yuanbao;
						
						vipNum.put(vipLevel, vipNum.get(vipLevel) == null?1:(vipNum.get(vipLevel)+1));
						yuanbaoCount.put(vipLevel, yuanbaoCount.get(vipLevel) == null?yuanBao:(yuanbaoCount.get(vipLevel)+yuanBao));
						Integer voucher = voucherMap.get(playerID);
						if(voucher != null){
							voucherNum.put(vipLevel, voucherNum.get(vipLevel) == null?1:(voucherNum.get(vipLevel)+voucher));
							voucherPlayerNum.put(vipLevel, voucherPlayerNum.get(vipLevel) == null?1:(voucherPlayerNum.get(vipLevel)+1));
						}
					}
					for(int i = 0;i <= maxVipLevel;i++){
						excel.insertAndMove(serverID)
						     .insertAndMove(testserverNodeMap.get(serverID).getServerName())
						     .insertAndMove(i)
						     .insertAndMove(vipNum.get(i) == null?0:vipNum.get(i))
						     .insertAndMove(voucherNum.get(i) == null?0:voucherNum.get(i))
						     .insertAndMove(voucherPlayerNum.get(i) == null?0:voucherPlayerNum.get(i))
						     .insertAndMove(yuanbaoCount.get(i) == null?0:yuanbaoCount.get(i));
						if(vipNum.get(i) != null && vipNum.get(i) > 0){
							int yuanbao = yuanbaoCount.get(i) == null?0:yuanbaoCount.get(i);
							excel.insertAndMove(yuanbao/vipNum.get(i));
						}else{
							excel.insertAndMove(0);
						}
						excel.nextRow();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("System Err!",e);
			}
			
		}
		File newFile = excel.saveExcel("ActivityCount.xlsx");
		PrintUtil.outputFileResponse(resp, newFile);
	}
	
	public static void main(String[] args) {
		System.out.println(VipUtil.getVipLevel(55000));
	}
}