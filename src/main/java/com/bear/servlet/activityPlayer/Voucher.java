package com.bear.servlet.activityPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.bear.util.DataModel;

public class Voucher extends DataModel<Map<Integer,Integer>>{
	public Voucher(String beginTime,String endTime){
		super("SELECT pay_user,SUM(get_commonAmount(pay_type,pay_amount,pay_gold)) AS pay_value "
				+ "FROM pay_action "
				+ "WHERE pay_status = 4 AND (post_time BETWEEN ? AND ?) GROUP BY pay_user", beginTime, endTime);
	}
	
	@Override
	public Map<Integer, Integer> packup(ResultSet rs) throws SQLException {
		Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
		while(rs.next()){
			resultMap.put(rs.getInt("pay_user"),rs.getInt("pay_value"));
		}
		return resultMap;
	}
}