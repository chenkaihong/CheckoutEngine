package com.bear.servlet.baby;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.bear.util.DataModel;

public class UnbirthBaby extends DataModel<Integer>{
	public UnbirthBaby(){
		super("SELECT COUNT(*) AS num FROM couple WHERE pregnantProgress > 0 AND pregnantProgress < 3");
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