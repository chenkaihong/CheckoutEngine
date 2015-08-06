package com.bear.servlet.baby;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bear.util.DataModel;

public class ItemUse extends DataModel<List<ItemUse>>{
	public int playerID;
	public int useYuanBao;
	
	public ItemUse(){
		super("SELECT log_user,SUM(f4) AS useYuanBao FROM log_buy WHERE log_data IN (17048,17049,17050) GROUP BY log_user");
	}
	
	public ItemUse(int playerID, int useYuanBao){
		this.playerID = playerID;
		this.useYuanBao = useYuanBao;
	}

	@Override
	public List<ItemUse> packup(ResultSet rs) throws SQLException {
		List<ItemUse> itemUseList = new ArrayList<ItemUse>();
		while(rs.next()){
			itemUseList.add(new ItemUse(rs.getInt("log_user"), rs.getInt("useYuanBao")));
		}
		return itemUseList;
	}
}