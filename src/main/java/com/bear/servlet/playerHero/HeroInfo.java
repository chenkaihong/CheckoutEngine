package com.bear.servlet.playerHero;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bear.util.DataModel;

public class HeroInfo extends DataModel<Map<Integer,List<HeroInfo>>>{
	private int playerID;
	private int formationType;
	private int cardRefID;
	private int level;
	private int jieshu;
	
	public HeroInfo(){
		super("SELECT a.playerID,a.formationType,b.cardRefID,b.level,b.jieShu "
				+ "FROM formation_hero a JOIN card_bag b ON a.cardID = b.id "
				+ "WHERE a.playerID IN (SELECT id FROM player WHERE voucherTotal > 500000)");
	}
	public HeroInfo(int playerID, int formationType, int cardRefID, int level, int jieshu){
		this.playerID = playerID;
		this.formationType = formationType;
		this.cardRefID = cardRefID;
		this.level = level;
		this.jieshu = jieshu;
	}
	
	@Override
	public Map<Integer, List<HeroInfo>> packup(ResultSet rs) throws SQLException {
		Map<Integer, List<HeroInfo>> heroInfoMap = new HashMap<Integer, List<HeroInfo>>();
		List<HeroInfo> heroInfoList = null;
		while(rs.next()){
			int playerID = rs.getInt("playerID");
			int formationType = rs.getInt("formationType");
			int cardRefID = rs.getInt("cardRefID");
			int level = rs.getInt("level");
			int jieshu = rs.getInt("jieShu");
			
			heroInfoList = heroInfoMap.get(playerID);
			if(heroInfoList == null){
				heroInfoList = new ArrayList<HeroInfo>();
				heroInfoMap.put(playerID, heroInfoList);
			}
			heroInfoList.add(new HeroInfo(playerID, formationType, cardRefID, level, jieshu));
		}
		return heroInfoMap;
	}

	public int getPlayerID() {
		return playerID;
	}
	public int getFormationType() {
		return formationType;
	}
	public int getCardRefID() {
		return cardRefID;
	}
	public int getLevel() {
		return level;
	}
	public int getJieshu() {
		return jieshu;
	}
}
