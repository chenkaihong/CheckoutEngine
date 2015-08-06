package com.bear.servlet.activityEffect;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.connection.ServerNodeFactory;
import com.bear.util.CheckOutUtil;
import com.bear.util.FileUtil;
import com.bear.util.JsonManager;
import com.bear.util.TimeUtil;

public class Main extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public static final String savePath = "/data/activityEffect/";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Calendar beginTimeA = null;
		Calendar endTimeA = null;
		Calendar beginTimeB = null;
		Calendar endTimeB = null;
		String beginTimeAInput = req.getParameter("beginTimeA");
		String endTimeAInput = req.getParameter("endTimeA");
		String beginTimeBInput = req.getParameter("beginTimeB");
		String endTimeBInput = req.getParameter("endTimeB");
		try {
			beginTimeA = TimeUtil.fromStringToCalendarWithoutSecond(beginTimeAInput);
			endTimeA = TimeUtil.fromStringToCalendarWithoutSecond(endTimeAInput);
			beginTimeB = TimeUtil.fromStringToCalendarWithoutSecond(beginTimeBInput);
			endTimeB = TimeUtil.fromStringToCalendarWithoutSecond(endTimeBInput);
			if(beginTimeA == null || endTimeA == null || beginTimeB == null || endTimeB == null){
				throw new Exception("BeginTime or endTime is null!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(String.format("Please insert the correct format time! - beginTimeA: %s, endTimeA: %s | beginTimeB: %s, endTimeB: %s", 
					beginTimeA, endTimeA, beginTimeB, endTimeB),e);
		}
		
		ServerNode backgroud = ConnectionManager.getBackgroud();
		Map<Integer, ServerNode> testserverNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
		
		ReturnData returnDataA = null;
		ReturnData returnDataB = null;
		
		try {
			String beginStringA = TimeUtil.fromCalendarToString(beginTimeA);
			String endStringA = TimeUtil.fromCalendarToString(endTimeA);
			String beginStringB = TimeUtil.fromCalendarToString(beginTimeB);
			String endStringB = TimeUtil.fromCalendarToString(endTimeB);
			
			System.out.println("CheckTime: " + beginStringA + " - " + endStringA + " | " +  beginStringB + " - " + endStringB);
			// Set<serverID>
			Set<Integer> zgsy = CheckOutUtil.singleCheckOut(new ServerChannel("%zgsy%","%中国手游%"), backgroud);
			Set<Integer> yibu = CheckOutUtil.singleCheckOut(new ServerChannel("%ybh%",""), backgroud);
			Set<Integer> mingtong = CheckOutUtil.singleCheckOut(new ServerChannel("%app%",""), backgroud);
			
			// 因为运营需求,日活跃人数和活跃充值人数是按照日滚动累计的,也就是说某些数据是在当日排重而且跨日不排重的,所以只能先按日计算出数据,然后再进行累加
			returnDataA = getReturn(beginTimeA, endTimeA, testserverNodeMap, backgroud, zgsy, yibu, mingtong);
			returnDataB = getReturn(beginTimeB, endTimeB, testserverNodeMap, backgroud, zgsy, yibu, mingtong);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("System Err!",e);
		}
		
		req.setCharacterEncoding("UTF-8");
		req.setAttribute("beginTimeAInput", beginTimeAInput);
		req.setAttribute("endTimeAInput", endTimeAInput);
		req.setAttribute("beginTimeBInput", beginTimeBInput);
		req.setAttribute("endTimeBInput", endTimeBInput);
		req.setAttribute("returnDataA", returnDataA);
		req.setAttribute("returnDataB", returnDataB);
		req.getRequestDispatcher("jsp/activityVoucher.jsp").forward(req, resp);
	}
	
	// 将每天的returnData合并成一个符合运营要求的returnData
	private ReturnData getReturn(Calendar beginTime, Calendar endTime,
								 Map<Integer, ServerNode> testserverNodeMap,
								 ServerNode backgroud,
								 Set<Integer> zgsy,
								 Set<Integer> yibu,
								 Set<Integer> mingtong) throws SQLException, ParseException{
		
		List<ReturnData> returnDataTemp = new ArrayList<ReturnData>();
		// 将输入的天数划分为每天进行数据导出
		while(beginTime.before(endTime)){
			String beginString = TimeUtil.fromCalendarToString(beginTime);
			beginTime.set(Calendar.HOUR_OF_DAY, 23);
			beginTime.set(Calendar.MINUTE, 59);
			beginTime.set(Calendar.SECOND, 59);
			beginTime.set(Calendar.MILLISECOND, 999);
			String endString = null;
			if(beginTime.after(endTime)){
				endString = TimeUtil.fromCalendarToString(endTime);
			}else{
				endString = TimeUtil.fromCalendarToString(beginTime);
			}
			beginTime.add(Calendar.DAY_OF_MONTH, 1);
			beginTime.set(Calendar.HOUR_OF_DAY, 0);
			beginTime.set(Calendar.MINUTE, 0);
			beginTime.set(Calendar.SECOND, 0);
			beginTime.set(Calendar.MILLISECOND, 0);
			
			
			ReturnData returnSingleData = null;
			try {
				// 使用json进行缓存,文件名为年-月-日_时-分-秒,在下次数据库查询之前先从json进行寻找
				returnSingleData = JsonManager.getGson().fromJson(FileUtil.fromFileToString(getViewName(beginString, endString)), ReturnData.class);
			} catch (Exception e) {
				e.printStackTrace();
				returnSingleData = null;
			}
			if(returnSingleData == null){
				// 此处为IO密集,可以使用多线程加速程序
				// Map<playerID, Voucher>
				Map<Integer, Integer> voucherMap = CheckOutUtil.singleCheckOut(new Voucher(beginString, endString), backgroud);
				// Map<serverID, Map<playerID, totalVoucher>>
				Map<Integer, Map<Integer, PlayerInfo>> playerVip = CheckOutUtil.multipleCheckOutByFactory(new Player(beginString, endString), testserverNodeMap);
				returnSingleData = SingleReturn.getSingleReturn(voucherMap, playerVip, testserverNodeMap, zgsy, yibu, mingtong);
				
				// 将已经查询过的数据进行json保存
				String saveFile = getViewName(beginString, endString);
				try {
					FileUtil.fromStringToFile(JsonManager.getGson().toJson(returnSingleData), saveFile, true, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			returnDataTemp.add(returnSingleData);
		}
		
		// 聚合统计数据
		int activityPlayerNum = 0;
		int voucherNum = 0;
		int voucherPlayerNum = 0;
		Map<String,Integer> channelVoucher = new LinkedHashMap<String, Integer>();
		Map<String,Integer> openVoucher = new LinkedHashMap<String, Integer>();
		Map<Integer,Integer> vipVoucher = new LinkedHashMap<Integer, Integer>();
		Map<String,Integer> levelVoucher = new LinkedHashMap<String, Integer>();
		Map<String,Integer> serverVoucher = new LinkedHashMap<String, Integer>();
		for(ReturnData s : returnDataTemp){
			activityPlayerNum += s.activityPlayerNum;
			voucherNum += s.voucherNum;
			voucherPlayerNum += s.voucherPlayerNum;
			combineMap(channelVoucher, s.channelVoucher);
			combineMap(openVoucher, s.openVoucher);
			combineMap(vipVoucher, s.vipVoucher);
			combineMap(levelVoucher, s.levelVoucher);
			combineMap(serverVoucher, s.serverVoucher);
		}
		
		// 日均活跃用户 需要总活跃用户数/天数
		activityPlayerNum /= returnDataTemp.size();
		
		return new ReturnData(activityPlayerNum, voucherNum, voucherPlayerNum, channelVoucher, openVoucher, vipVoucher, levelVoucher, serverVoucher);
	}
	
	// 用于让两个value是Integer类型的Map合并,value值相加,并返回目标Map
	private <T> void combineMap(Map<T,Integer> targetMap,Map<T,Integer> sourceMap){
		for(Entry<T, Integer> s : sourceMap.entrySet()){
			T sourceKey = s.getKey();
			Integer targetValue = targetMap.get(sourceKey);
			if(targetValue != null){
				targetMap.put(sourceKey, targetValue + s.getValue());
			}else{
				targetMap.put(sourceKey, s.getValue());
			}
		}
	}
	
	/**
	 * 通过beginString和endString生成视图文件路径,支持缓存功能
	 * @param beginString
	 * @param endString
	 * @return
	 * @throws ParseException
	 */
	public static String getViewName(String beginString,String endString) throws ParseException{
		Calendar beginTime = TimeUtil.fromStringToCalendar(beginString);
		Calendar endTime = TimeUtil.fromStringToCalendar(endString);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return String.format("%s%s_%s.txt", savePath, format.format(beginTime.getTime()), format.format(endTime.getTime()));
	}
}