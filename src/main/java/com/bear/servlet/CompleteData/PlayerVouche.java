package com.bear.servlet.CompleteData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.bear.util.DataModel;

public class PlayerVouche extends DataModel<Map<Integer, PlayerVouche>>{
	public PlayerVouche(){
		super("SELECT pay_user,SUM(get_commonAmount(pay_type,pay_amount,pay_gold)) AS voucher FROM pay_action WHERE pay_status = 4 AND post_time BETWEEN ? AND ? ORDER BY pay_user", new String[]{});
	}
	public PlayerVouche(int i){
		
	}

	@Override
	public Map<Integer, PlayerVouche> packup(ResultSet rs) throws SQLException {
		
		return null;
	}
}
