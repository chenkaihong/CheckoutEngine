package com.bear.servlet.newPlayerVSOldPlayerVouch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.bear.util.DataModel;

public class PlayerVouch extends DataModel<Map<Integer,PlayerVouch>>{
	private int playerID;
	private int yuanBao;
	private int money;
	
	public PlayerVouch(){
		super("SELECT pay_user,SUM(get_actual_amount(pay_type,pay_amount,pay_gold,currency)) AS total_money,"
				+ "SUM(get_commonGold(pay_type,pay_amount,pay_gold)) AS total_gold "
				+ "FROM pay_action WHERE pay_status = 4 AND post_time "
				+ "BETWEEN '2015-06-05 10:00:00' AND '2015-07-07 23:59:59' "
				+ "GROUP BY pay_user", new String[]{});
	}
	public PlayerVouch(int playerID, int yuanBao,int money){
		this.playerID = playerID;
		this.yuanBao = yuanBao;
		this.money = money;
	}
	@Override
	public Map<Integer, PlayerVouch> packup(ResultSet rs) throws SQLException {
		Map<Integer,PlayerVouch> vouchMap = new HashMap<Integer,PlayerVouch>();
		while(rs.next()){
			vouchMap.put(rs.getInt("pay_user"), new PlayerVouch(rs.getInt("pay_user"), rs.getInt("total_gold"), rs.getInt("total_money")));
		}
		return vouchMap;
	}
	public int getYuanBao() {
		return yuanBao;
	}
	public int getMoney() {
		return money;
	}
	public int getPlayerID() {
		return playerID;
	}
}
