package com.bear.servlet.activityEffect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.bear.connection.ServerNode;
import com.bear.util.DataModel;
import com.bear.util.PackupSelfFactory;
import com.bear.util.TimeUtil;

public class Player extends DataModel<Map<Integer,PlayerInfo>>{
	private String beginTime;
	private String endTime;
	
	public Player(String beginTime, String endTime){
		this.beginTime = beginTime;
		this.endTime = endTime;
	}
	private Player(String serverID, String beginTime, String endTime, String levelLimit){
		super(String.format("SELECT userId,voucherTotal,level "
				+ "FROM condor_test_%s.player "
				+ "WHERE userId IN "
				+ "(SELECT DISTINCT log_user FROM condor_data_%s.log_check_user WHERE log_time BETWEEN ? AND ?) AND LEVEL >= ?", serverID, serverID), beginTime, endTime, levelLimit);
	}

	@Override
	public Map<Integer, PlayerInfo> packup(ResultSet rs) throws SQLException {
		Map<Integer,PlayerInfo> resultMap = new HashMap<Integer,PlayerInfo>();
		while(rs.next()){
			resultMap.put(rs.getInt("userId"),new PlayerInfo(rs.getInt("level"), rs.getInt("voucherTotal")));
		}
		return resultMap;
	}
	@Override
	public PackupSelfFactory<Map<Integer, PlayerInfo>> getModel(ServerNode serverNode) {
		String levelMinLimit = null;
		try {
			levelMinLimit = getLevelMinLimit(beginTime, serverNode.getCreateTime());
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return new Player(String.valueOf(serverNode.getServerId()), beginTime, endTime, levelMinLimit);
	}
	
	private static String getLevelMinLimit(String beginTime, String createTime) throws ParseException{
		long yue4 = 3600 * 24 * 30 * 4 * 1000L;
		long yue2 = 3600 * 24 * 30 * 2 * 1000L;
		long yue1 = 3600 * 24 * 30 * 1 * 1000L;
		String levelMinLimit = "0";
		long openTime = TimeUtil.fromStringToLong(beginTime) - TimeUtil.fromStringToLong(createTime);
		if(openTime > yue4){
			levelMinLimit = "150";
		}else if(openTime > yue2){
			levelMinLimit = "70";
		}else if(openTime > yue1){
			levelMinLimit = "55";
		}
		return levelMinLimit;
	}
}