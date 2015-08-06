package com.bear.servlet.activityEffect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.bear.util.DataModel;

public class ServerChannel  extends DataModel<Set<Integer>>{
	public ServerChannel(String ...parm){
		super("SELECT id FROM servers WHERE game_addr LIKE (?) OR NAME LIKE (?)", parm);
	}
	
	@Override
	public Set<Integer> packup(ResultSet rs) throws SQLException {
		Set<Integer> resultSet = new HashSet<Integer>();
		while(rs.next()){
			resultSet.add(rs.getInt("id"));
		}
		return resultSet;
	}
}
