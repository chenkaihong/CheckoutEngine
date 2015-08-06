package com.bear.servlet.activityCost;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.bear.util.DataModel;
import com.bear.util.excelUtil.Excel;
import com.bear.util.excelUtil.ExcelUtil;

public class ActivityCost extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, ServerNode> dataServerNodeMap = ConnectionManager.getDataDB(ServerNodeFactory.onlineServerNode);
		Map<Integer, ServerNode> testServerNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
		
		Map<Integer, Integer> yuanbaoMap = CheckOutUtil.multipleCheckOut(new activityYuanBao(), dataServerNodeMap);
		Map<Integer, Integer> playerNumMap = CheckOutUtil.multipleCheckOut(new activityPlayerNum(), testServerNodeMap);
		
		Excel excel = ExcelUtil.getInstance();
		excel.getNewSheet("yuanbao");
		excel.insertAndMove("serverID").insertAndMove("serverName").insertAndMove("yuanbao").insertAndMove("joinNum").nextRow();
		for(Entry<Integer, Integer> yuanbaoEntry : yuanbaoMap.entrySet()){
			int serverID = yuanbaoEntry.getKey();
			int yuanbao = yuanbaoEntry.getValue();
			int joinNum = playerNumMap.get(serverID);
			ServerNode serverNode = dataServerNodeMap.get(serverID);
			excel.insertAndMove(serverID).insertAndMove(serverNode.getServerName()).insertAndMove(yuanbao).insertAndMove(joinNum).nextRow();
		}
		excel.saveExcel("ExchangeActivityStat.xlsx");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}

class activityYuanBao extends DataModel<Integer>{
	public activityYuanBao(){
		super("SELECT SUM(log_result) AS yuanbao "
				+ "FROM log_gold "
				+ "WHERE log_data = 50 AND log_time BETWEEN '2014-11-12 12:00:00' AND '2014-11-13 11:30:00'");
	}
	@Override
	public Integer packup(ResultSet rs) throws SQLException {
		int result = 0;
		while(rs.next()){
			result += rs.getInt("yuanbao");
		}
		return result;
	}
}

class activityPlayerNum extends DataModel<Integer>{
	public activityPlayerNum(){
		super("SELECT COUNT(*) AS num FROM player_dynamic_activity WHERE activityId IN (4675,4676,4677)");
	}
	@Override
	public Integer packup(ResultSet rs) throws SQLException {
		int result = 0;
		while(rs.next()){
			result += rs.getInt("num");
		}
		return result;
	}
}
