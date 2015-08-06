package com.bear.servlet.vipVoucher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bear.util.DataModel;

public class VoucherModel extends DataModel<List<VoucherModel>>{
	
	private int playerID;
	private int payGold;
	
	public VoucherModel(int playerID, int payGold) {
		super();
		this.playerID = playerID;
		this.payGold = payGold;
	}
	public VoucherModel(String sql, String ...parmList){
		super(sql, parmList);
	}
	
	@Override
	public List<VoucherModel> packup(ResultSet rs) throws SQLException {
		List<VoucherModel> list = new ArrayList<VoucherModel>();
		while(rs.next()){
			list.add(new VoucherModel(
										rs.getInt("pay_user"), 
										rs.getInt("pay_gold")));
		}
		return list;
	}

	public int getPlayerID() {
		return playerID;
	}
	public int getPayGold() {
		return payGold;
	}
}