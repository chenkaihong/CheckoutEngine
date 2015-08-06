package com.bear.servlet.baby;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.bear.util.DataModel;

public class BirthBaby extends DataModel<Integer>{
	public BirthBaby(){
		super("SELECT COUNT(*) AS num FROM baby");
	}
	
	@Override
	public Integer packup(ResultSet rs) throws SQLException {
		int result = 0;
		while(rs.next()){
			result = rs.getInt("num");
		}
		return result;
	}
}
