package com.bear.servlet.rechargeCount;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.connection.ServerNodeFactory;
import com.bear.servlet.playerHero.HeroInfo;
import com.bear.util.CheckOutUtil;
import com.bear.util.DataModel;

/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * 用于统计在线玩家的上阵卡牌情况
 * @Author Create by ckh
 * @Date 2014-11-12 下午4:34:09
 * @Description
 */
public class RechargeCountSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static final Logger logger = Logger.getLogger(NewPlayerVSOldPlayerVouch.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, ServerNode> serverNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
		
		// 获取符合规则的玩家的阵容信息
		Map<Integer, Map<Integer, List<HeroInfo>>> playerHeroServerMap = CheckOutUtil.multipleCheckOut(new HeroInfo(), serverNodeMap);
		
		// 将个个服的linkID进行合并,保存最早注册的时间
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

class PlayerRecharge extends DataModel<Map<Integer,PlayerRecharge>>{

	@Override
	public Map<Integer, PlayerRecharge> packup(ResultSet rs) throws SQLException {
		
		return null;
	}
	
}

class PlayerRegistration extends DataModel<Map<Integer,PlayerRegistration>>{
	public final long registrationTime;
	public final int playerID;
	public final int linkID;
	
	public PlayerRegistration(){
		super("SELECT player_id,link_key,create_time FROM player_X");
		this.registrationTime = 0;
		this.playerID = 0;
		this.linkID = 0;
	}
	
	public PlayerRegistration(long registrationTime,int playerID,int linkID){
		this.registrationTime = registrationTime;
		this.playerID = playerID;
		this.linkID = linkID;
	}
	
	@Override
	public Map<Integer, PlayerRegistration> packup(ResultSet rs) throws SQLException {
		Map<Integer, PlayerRegistration> playerRegistrationMap = new HashMap<Integer, PlayerRegistration>();
		while(rs.next()){
			long registrationTime = rs.getLong("create_time");
			int playerID = rs.getInt("player_id");
			int linkKey = rs.getInt("link_key");
			playerRegistrationMap.put(rs.getInt("link_key"), new PlayerRegistration(registrationTime, playerID, linkKey));
		}
		return playerRegistrationMap;
	}
}