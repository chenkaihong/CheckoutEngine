package com.bear.servlet.baby;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bear.util.DataModel;

public class Upgrade extends DataModel<List<Upgrade>>{
	public int playerID;
	public int useYuanBao;
	
	public Upgrade(){
		super("SELECT log_user,SUM(log_result) AS useYuanBao FROM log_gold WHERE log_data = 1304 GROUP BY log_user");
	}
	
	public Upgrade(int playerID, int useYuanBao){
		this.playerID = playerID;
		this.useYuanBao = useYuanBao;
	}

	@Override
	public List<Upgrade> packup(ResultSet rs) throws SQLException {
		List<Upgrade> upgradeList = new ArrayList<Upgrade>();
		while(rs.next()){
			upgradeList.add(new Upgrade(rs.getInt("log_user"), rs.getInt("useYuanBao")));
		}
		return upgradeList;
	}
}