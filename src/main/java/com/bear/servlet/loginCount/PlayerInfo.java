package com.bear.servlet.loginCount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bear.util.DataModel;
import com.bear.util.VipUtil;

public class PlayerInfo extends DataModel<List<PlayerInfo>>{
	private int id;
	private String name;
	private int level;
	private int vipLevel;
	
	public PlayerInfo(int id, String name, int level, int vipLevel) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.vipLevel = vipLevel;
	}

	public PlayerInfo(String sql, String ...parms){
		super(sql, parms);
	}

	@Override
	public List<PlayerInfo> packup(ResultSet rs) throws SQLException {
		List<PlayerInfo> list = new ArrayList<PlayerInfo>();
		while(rs.next()){
			int id = rs.getInt("id");
			String name = rs.getString("NAME");
			int level = rs.getInt("LEVEL");
			int vipLevel = VipUtil.getVipLevel(rs.getInt("voucherTotal"));
			
			PlayerInfo info = new PlayerInfo(id, name, level, vipLevel);
			list.add(info);
		}
		return list;
	}

	public int getId() {
		return id;
	}
	public int getLevel() {
		return level;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
