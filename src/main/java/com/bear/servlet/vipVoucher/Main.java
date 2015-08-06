package com.bear.servlet.vipVoucher;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.util.CheckOutUtil;
import com.bear.util.VipUtil;
import com.bear.util.excelUtil.Excel;
import com.bear.util.excelUtil.ExcelUtil;

public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int onceLimit = 200;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp){
		try{
			ServerNode backgroud = ConnectionManager.getBackgroud();
			
			List<String> yearAndMonthList = Arrays.asList("2014,8",
														  "2014,9",
														  "2014,10",
														  "2014,11",
														  "2014,12",
														  "2015,1",
														  "2015,2",
														  "2015,3",
														  "2015,4",
														  "2015,5",
														  "2015,6");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Excel excel = ExcelUtil.getInstance();
			
			for(String yearAndMonth : yearAndMonthList){
				
				Calendar time = Calendar.getInstance();
				String[] info = yearAndMonth.split(",");
				time.set(Integer.parseInt(info[0]), Integer.parseInt(info[1])-1, 1, 0, 0, 0);
				time.set(Calendar.MILLISECOND, 0);
				String startTime = sf.format(time.getTime());
				time.add(Calendar.MONTH, 1);
				String endTime = sf.format(time.getTime());
				
				List<VoucherModel> playerList = CheckOutUtil.singleCheckOut(
						new VoucherModel("SELECT DISTINCT pay_user,SUM(pay_gold) as pay_gold FROM pay_action WHERE pay_status = 4 AND (post_time BETWEEN ? AND ?) GROUP BY pay_user",
										 startTime,
										 endTime), 
										 backgroud);
				
				Map<Integer, Integer> vipLevelMap = new HashMap<Integer, Integer>();
				List<VoucherModel> copyPlayerList = new ArrayList<VoucherModel>(playerList);
				while(copyPlayerList.size() > 0){
					// 整理这次需要查询的人员名单
					StringBuilder sb = new StringBuilder();
					Iterator<VoucherModel> itr = copyPlayerList.iterator();
					for(int i = 0;i < onceLimit && itr.hasNext();i++){
						VoucherModel playerID = itr.next();
						sb.append(playerID.getPlayerID()).append(",");
						itr.remove();
					}
					sb.deleteCharAt(sb.length()-1);
					System.out.println("@@@ playerList: " + sb.toString());
					
					String sql = String.format("SELECT pay_user,SUM(pay_gold) pay_gold FROM pay_action WHERE pay_status = 4 AND pay_user IN (%s) AND (post_time BETWEEN ? AND ?) GROUP BY pay_user", sb.toString());
					List<VoucherModel> vipLvelModel = CheckOutUtil.singleCheckOut(
							new VoucherModel(sql,
											 "2010-01-01 00:00:00",
											 endTime),
											 backgroud);
					for(VoucherModel s : vipLvelModel){
						vipLevelMap.put(s.getPlayerID(), VipUtil.getVipLevel(s.getPayGold()));
					}
				}
				
				Map<Integer,Integer> playerVoucher = new LinkedHashMap<Integer,Integer>();
				Map<Integer,Integer> playerNum = new HashMap<Integer,Integer>();
				for(int i =0;i <= 14;i++){
					playerVoucher.put(i, 0);
					playerNum.put(i, 0);
				}
				for(VoucherModel s : playerList){
					int vipLevel = vipLevelMap.get(s.getPlayerID());
					int voucherNum = playerVoucher.get(vipLevel);
					playerVoucher.put(vipLevel, voucherNum+s.getPayGold());
					int num = playerNum.get(vipLevel);
					playerNum.put(vipLevel, num+1);
				}
				
				excel.getNewSheet(info[0] + "年" + info[1] + "月");
				excel.insertAndMove("vipLevel").insertAndMove("playerNum").insertAndMove("voucherNum").nextRow();
				for(Entry<Integer, Integer> e : playerVoucher.entrySet()){
					int vipLevel = e.getKey();
					int voucherNum = e.getValue();
					int num = playerNum.get(vipLevel);
					excel.insertAndMove(vipLevel).insertAndMove(num).insertAndMove(voucherNum).nextRow();
				}
			}
			
			File newFile = excel.saveExcel("voucher.xlsx");
			System.out.println("@@@ OutFile: " + newFile);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}