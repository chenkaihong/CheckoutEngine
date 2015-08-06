package com.bear.servlet.newActivation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.bear.util.DataModel;

public class NewActivation extends DataModel<Map<String, Integer>>{
	public NewActivation(String fileName, String startTime, String endTime){
		super(String.format("SELECT gs.name,SUM(result) AS result "
				+ "FROM result_%s,groups gs "
				+ "WHERE statistic_id=1 AND "
				+ "channel_id IN (SELECT id FROM channel WHERE child_group_id IN(SELECT id FROM child_groups WHERE groupId=gs.id)) AND "
				+ "result_time BETWEEN ? AND ? GROUP BY gs.id", fileName), startTime, endTime);
	}
	
	@Override
	public Map<String, Integer> packup(ResultSet rs) throws SQLException {
		Map<String, Integer> activationMap = new HashMap<String, Integer>();
		while(rs.next()){
			String area = rs.getString("name");
			int activation = rs.getInt("result");
			activationMap.put(area, activation);
		}
		return activationMap;
	}
}