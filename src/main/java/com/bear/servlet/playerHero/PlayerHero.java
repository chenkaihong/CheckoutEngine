package com.bear.servlet.playerHero;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.connection.ServerNodeFactory;
import com.bear.util.CheckOutUtil;
import com.bear.util.PrintUtil;
import com.bear.util.excelUtil.Excel;
import com.bear.util.excelUtil.ExcelUtil;

/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * 用于统计在线玩家的上阵卡牌情况
 * @Author Create by ckh
 * @Date 2014-11-12 下午4:34:09
 * @Description
 */
public class PlayerHero extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static final Logger logger = Logger.getLogger(NewPlayerVSOldPlayerVouch.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, ServerNode> serverNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
		final int AttackStatue = 1;
		final int DefenseStatue = 2;
		
		// 获取符合规则的玩家的阵容信息
		Map<Integer, Map<Integer, List<HeroInfo>>> playerHeroServerMap = CheckOutUtil.multipleCheckOut(new HeroInfo(), serverNodeMap);
		Map<Integer, List<HeroInfo>> heroClassify = null;
		
		Excel excel = ExcelUtil.getInstance();
		for(Entry<Integer, Map<Integer, List<HeroInfo>>> playerHeroServerMapEntry : playerHeroServerMap.entrySet()){
			int serverID = playerHeroServerMapEntry.getKey();
			Map<Integer, List<HeroInfo>> playerHeroMap = playerHeroServerMapEntry.getValue();
			excel.getNewSheet("serverID-" + serverID);
			for(Entry<Integer, List<HeroInfo>> playerHeroMapEntry : playerHeroMap.entrySet()){
				int playerID = playerHeroMapEntry.getKey();
				List<HeroInfo> heroInfoList = playerHeroMapEntry.getValue();
				heroClassify = new HashMap<Integer, List<HeroInfo>>();
				heroClassify.put(1, new ArrayList<HeroInfo>());
				heroClassify.put(2, new ArrayList<HeroInfo>());
				for(HeroInfo heroInfo : heroInfoList){
					if(heroInfo.getFormationType() == AttackStatue){
						heroClassify.get(AttackStatue).add(heroInfo);
					}else if(heroInfo.getFormationType() == DefenseStatue){
						heroClassify.get(DefenseStatue).add(heroInfo);
					}
				}
				
				excel.insertAndMove("playerID").insertAndMove(playerID).nextRow();
				excel.insertAndMove("Attack Hero: ").nextRow();
				excel.insertAndMove("CardRefID").insertAndMove("JieShu").insertAndMove("Level").nextRow();
				List<HeroInfo> attackHeroList = heroClassify.get(AttackStatue);
				for(HeroInfo attackHero : attackHeroList){
					excel.insertAndMove(attackHero.getCardRefID()).insertAndMove(attackHero.getJieshu()).insertAndMove(attackHero.getLevel()).nextRow();
				}
				excel.insertAndMove("Defense Hero: ").nextRow();
				excel.insertAndMove("CardRefID").insertAndMove("JieShu").insertAndMove("Level").nextRow();
				List<HeroInfo> defenseHeroList = heroClassify.get(DefenseStatue);
				for(HeroInfo defenseHero : defenseHeroList){
					excel.insertAndMove(defenseHero.getCardRefID()).insertAndMove(defenseHero.getJieshu()).insertAndMove(defenseHero.getLevel()).nextRow();
				}
			}
		}
		File newFile = excel.saveExcel("test.xlsx");
		PrintUtil.outputFileResponse(resp, newFile);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}