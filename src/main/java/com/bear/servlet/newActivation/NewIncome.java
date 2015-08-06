package com.bear.servlet.newActivation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.bear.util.DataModel;

public class NewIncome extends DataModel<Map<String, Integer>>{
	public NewIncome(String startTime, String endTime){
		super("SELECT gs.name,SUM(get_commonAmount(lp.pay_type,lp.pay_amount,lp.pay_gold) ) AS result "
				+ "FROM pay_action lp,groups gs "
				+ "WHERE lp.pay_status ='4' AND "
				+ "channel_id IN (SELECT id FROM channel WHERE child_group_id IN(SELECT id FROM child_groups WHERE groupId=gs.id)) AND "
				+ "lp.post_time BETWEEN ? AND ? GROUP BY gs.id", startTime, endTime);
	}
	
	@Override
	public Map<String, Integer> packup(ResultSet rs) throws SQLException {
		Map<String, Integer> incomeMap = new HashMap<String, Integer>();
		while(rs.next()){
			String area = rs.getString("name");
			int income = rs.getInt("result");
			incomeMap.put(area, income);
		}
		return incomeMap;
	}
}