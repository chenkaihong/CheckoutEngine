package com.bear.servlet.newPlayerVSOldPlayerVouch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.connection.ServerNodeFactory;
import com.bear.util.CheckOutUtil;

/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * 用于统计新用户和滚服用户消费比的数据(滚服用户：使用同一个linkID非第一次创建帐号的用户)
 * 数据样式：
 * 
 *	ServerID : 963, ServerName : H16.如日方升-------------------------------------------------
 *	人民币  元宝    注册人数        付费人数
 *	1292    12920   22122   23      新用户
 *	25861   258611  18698   123     滚服用户
 *	27153   271531  40820   146     总和
 *	
 *	-------------------------------------------------------------------------------
 * @Author Create by ckh
 * @Date 2014-11-10 下午4:34:09
 * @Description
 */
public class NewPlayerVSOldPlayerVouch extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static final Logger logger = Logger.getLogger(NewPlayerVSOldPlayerVouch.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 获取Data数据库serverNode信息
		Map<Integer,ServerNode> serverNodeMap = ConnectionManager.getDataDB(ServerNodeFactory.onlineServerNode);
		
		// 定义容器 ----> 
		// linkMap : 通过linkID聚合的用户信息
		// serverMap : 通过serverID聚合的用户信息
		// newPlayerID : 确认为新用户的聚合
		Map<String,List<Player>> linkMap = new HashMap<String,List<Player>>();
		Set<Integer> newPlayerID = new HashSet<Integer>();
		Map<Integer,List<Player>> serverMap = new HashMap<Integer,List<Player>>();
		
		// 通过多线程获取全服player信息
		Map<Integer, List<Player>> playerMap = CheckOutUtil.multipleCheckOut(new Player(), serverNodeMap);
		
		// 对player信息产生区分
		for(Entry<Integer, List<Player>> playerEntry : playerMap.entrySet()){
			List<Player> playerList = playerEntry.getValue();
			for(Player player : playerList){
				// 通过LinkID 区分用户
				List<Player> linkIDPlayerList = linkMap.get(player.getLinkID());
				if(linkIDPlayerList == null){
					linkIDPlayerList = new ArrayList<Player>();
					linkMap.put(player.getLinkID(), linkIDPlayerList);
				}
				linkIDPlayerList.add(player);
			}
		}
		serverMap = playerMap;
		
		// 分辨出全服的新建playerID
		for(Entry<String, List<Player>> s : linkMap.entrySet()){
			List<Player> playerList = s.getValue();
			long mixTime = playerList.get(0).getCreate_time();
			int mixTimePlayerID = playerList.get(0).getPlayerID();
			for(Player player : playerList){
				if(player.getCreate_time() < mixTime){
					mixTimePlayerID = player.getPlayerID();
					mixTime = player.getCreate_time();
				}
			}
			newPlayerID.add(mixTimePlayerID);
		}
		// 从后台抽取出玩家充值记录
		Map<Integer, PlayerVouch> PlayerVouch;
		try {
			PlayerVouch = CheckOutUtil.singleCheckOut(new PlayerVouch(), ConnectionManager.getBackgroud());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Vouch is error!");
		}
		// 将全部玩家的数据按照新用户和老用户进行区别(根据linkKeyID找出对应的全部playerID,最先创建的playerID则是新用户,之后创建的为老用户)
		// 并通过后台充值数据加以统计
		for(Entry<Integer, List<Player>> s : serverMap.entrySet()){
			int newPlayerMoney = 0;
			int newPlayerYuanBao = 0;
			int newPlayerRegNum = 0;
			int newPlayerPayNum = 0;
			
			int oldPlayerMoney = 0;
			int oldPlayerYuanBao = 0;
			int oldPlayerRegNum = 0;
			int oldPlayerPayNum = 0;
			
			List<Player> playerList = s.getValue();
			
			for(Player player : playerList){
				PlayerVouch playerVouch = PlayerVouch.get(player.getPlayerID());
				// 新用户数据统计
				if(newPlayerID.contains(player.getPlayerID())){
					if(playerVouch != null){
						newPlayerMoney += playerVouch.getMoney();
						newPlayerYuanBao += playerVouch.getYuanBao();
						newPlayerPayNum++;
					}
					newPlayerRegNum++;
				}
				// 老用户数据统计
				else{	
					if(playerVouch != null){
						oldPlayerMoney += playerVouch.getMoney();
						oldPlayerYuanBao += playerVouch.getYuanBao();
						oldPlayerPayNum++;
					}
					oldPlayerRegNum++;
				}
			}
			
			String msgFomat = "人民币\t元宝\t注册人数\t付费人数\n%d\t%d\t%d\t%d\t新用户\n%d\t%d\t%d\t%d\t滚服用户\n%d\t%d\t%d\t%d\t总和\n";
			System.out.println("ServerID : " + s.getKey() + ", ServerName : " + serverNodeMap.get(s.getKey()).getServerName() + "-------------------------------------------------");
			System.out.println(String.format(msgFomat, 
					newPlayerMoney, newPlayerYuanBao, newPlayerRegNum, newPlayerPayNum,
					oldPlayerMoney, oldPlayerYuanBao, oldPlayerRegNum, oldPlayerPayNum,
					newPlayerMoney+oldPlayerMoney,
					newPlayerYuanBao+oldPlayerYuanBao,
					newPlayerRegNum+oldPlayerRegNum,
					newPlayerPayNum+oldPlayerPayNum));
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println();
			System.out.println();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
