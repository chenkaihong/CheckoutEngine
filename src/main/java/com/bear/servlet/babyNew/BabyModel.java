package com.bear.servlet.babyNew;

import java.io.File;
import java.io.IOException;
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
 * 用于全面统计孩子系统基础数据
 * @Author Create by ckh
 * @Date 2015-01-09 下午4:34:09
 * @Description
 */
public class BabyModel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, ServerNode> testserverNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
		
		Map<Integer, List<Baby>> babyMap = CheckOutUtil.multipleCheckOut(new Baby(), testserverNodeMap);
		Map<Integer, Integer> babyNumMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<Integer, Integer>> clothesMapMap = new HashMap<Integer, Map<Integer, Integer>>();
		Map<Integer, Map<Integer, Integer>> eyeMapMap = new HashMap<Integer, Map<Integer, Integer>>();
		Map<Integer, Map<Integer, Integer>> hairMapMap = new HashMap<Integer, Map<Integer, Integer>>();
		Map<Integer, Map<Integer, Integer>> decorateMapMap = new HashMap<Integer, Map<Integer, Integer>>();
		
		/* 服装的拥有情况 */
		/* 外观的拥有情况 */
		for(Entry<Integer, List<Baby>> babyEntrySet : babyMap.entrySet()){
			int serverID = babyEntrySet.getKey();
			List<Baby> babyList = babyEntrySet.getValue();
			babyNumMap.put(serverID, babyList.size());
			for(Baby baby : babyList){
				List<BabyClothes> clothesList = baby.getClothespress();
				// 服装统计
				Map<Integer, Integer> clothesMap = clothesMapMap.get(serverID);
				if(clothesMap == null){
					clothesMap = new HashMap<Integer, Integer>();
					clothesMapMap.put(serverID, clothesMap);
				}
				for(BabyClothes clothes : clothesList){
					if(!clothes.isUseful()){
						continue;
					}
					int clothesID = clothes.getClothesID();
					Integer clothesNum = clothesMap.get(clothesID);
					if(clothesNum == null){
						clothesMap.put(clothesID, 1);
					}else{
						clothesMap.put(clothesID, clothesNum+1);
					}
				}
				
				// 外观统计
				BabyLooks babyLooks = baby.getLooks();
				// 眼睛
				Map<Integer, Integer> eyeMap = eyeMapMap.get(serverID);
				if(eyeMap == null){
					eyeMap = new HashMap<Integer, Integer>();
					eyeMapMap.put(serverID, eyeMap);
				}
				int eyeID = babyLooks.getEye();
				Integer eyeNum = eyeMap.get(eyeID);
				if(eyeNum == null){
					eyeMap.put(eyeID, 1);
				}else{
					eyeMap.put(eyeID, eyeNum+1);
				}
				// 头发
				Map<Integer, Integer> hairMap = hairMapMap.get(serverID);
				if(hairMap == null){
					hairMap = new HashMap<Integer, Integer>();
					hairMapMap.put(serverID, hairMap);
				}
				int hairID = babyLooks.getHair();
				Integer hairNum = hairMap.get(hairID);
				if(hairNum == null){
					hairMap.put(hairID, 1);
				}else{
					hairMap.put(hairID, hairNum+1);
				}
				// 装饰
				Map<Integer, Integer> decorateMap = decorateMapMap.get(serverID);
				if(decorateMap == null){
					decorateMap = new HashMap<Integer, Integer>();
					decorateMapMap.put(serverID, decorateMap);
				}
				int decorateID = babyLooks.getDecorate();
				Integer decorateNum = decorateMap.get(decorateID);
				if(decorateNum == null){
					decorateMap.put(decorateID, 1);
				}else{
					decorateMap.put(decorateID, decorateNum+1);
				}
				
			}
		}
		
		// 先算总数,有需要再分服进行统计
		int totalBabyNum = 0;
		Map<Integer, Integer> totalClothesMapMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> totalEyeMapMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> totalHairMapMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> totalDecorateMapMap = new HashMap<Integer, Integer>();
		
		for(Entry<Integer, Integer> s : babyNumMap.entrySet()){
			totalBabyNum += s.getValue();
		}
		countTotal(clothesMapMap, totalClothesMapMap);
		countTotal(eyeMapMap, totalEyeMapMap);
		countTotal(hairMapMap, totalHairMapMap);
		countTotal(decorateMapMap, totalDecorateMapMap);
		
		Excel excel = ExcelUtil.getInstance();
		excel.getNewSheet("宝宝装饰统计");
		excel.insertAndMove("宝宝总数: ").insertAndMove(totalBabyNum).nextRow();
		excel.nextRow();
		excel.insertAndMove("服饰ID").insertAndMove("服饰数量").nextRow();
		for(Entry<Integer, Integer> s : totalClothesMapMap.entrySet()){
			excel.insertAndMove(s.getKey()).insertAndMove(s.getValue()).nextRow();
		}
		excel.nextRow();
		excel.insertAndMove("眼睛ID").insertAndMove("眼睛数量").nextRow();
		for(Entry<Integer, Integer> s : totalEyeMapMap.entrySet()){
			excel.insertAndMove(s.getKey()).insertAndMove(s.getValue()).nextRow();
		}
		excel.nextRow();
		excel.insertAndMove("头发ID").insertAndMove("头发数量").nextRow();
		for(Entry<Integer, Integer> s : totalHairMapMap.entrySet()){
			excel.insertAndMove(s.getKey()).insertAndMove(s.getValue()).nextRow();
		}
		excel.nextRow();
		excel.insertAndMove("装饰ID").insertAndMove("装饰数量").nextRow();
		for(Entry<Integer, Integer> s : totalDecorateMapMap.entrySet()){
			excel.insertAndMove(s.getKey()).insertAndMove(s.getValue()).nextRow();
		}
		
		File newFile = excel.saveExcel("baby.xlsx");
		PrintUtil.outputFileResponse(resp, newFile);
	}
	
	private Map<Integer, Integer> countTotal(Map<Integer, Map<Integer, Integer>> MapMap,Map<Integer, Integer> map){
		for(Entry<Integer, Map<Integer, Integer>> s : MapMap.entrySet()){
			Map<Integer, Integer> valueValue = s.getValue();
			for(Entry<Integer, Integer> p : valueValue.entrySet()){
				int key = p.getKey();
				int value = p.getValue();
				Integer eyeNum = map.get(key);
				if(eyeNum == null){
					map.put(key, value);
				}else{
					map.put(key, eyeNum+value);
				}
			}
		}
		return map;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}