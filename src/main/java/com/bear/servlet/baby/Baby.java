package com.bear.servlet.baby;

import java.io.File;
import java.io.IOException;
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
 * 用于统计孩子系统上线后对全线的影响
 * @Author Create by ckh
 * @Date 2015-01-09 下午4:34:09
 * @Description
 */
public class Baby extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, ServerNode> testserverNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
		Map<Integer, ServerNode> dataserverNodeMap = ConnectionManager.getDataDB(ServerNodeFactory.onlineServerNode);
		
		/*已怀孕的数量(宝宝未出生)*/
		/*所有宝宝数量(宝宝已经出生)*/
		/*宝宝升级的元宝总消耗*/
		/*道具天书、成长丹、护级丹的元宝消耗数量*/
		Map<Integer, Integer> unbirthBabyMap = CheckOutUtil.multipleCheckOut(new UnbirthBaby(), testserverNodeMap);
		Map<Integer, Integer> birthBabyMap = CheckOutUtil.multipleCheckOut(new BirthBaby(), testserverNodeMap);
		Map<Integer, List<Upgrade>> upgradeMap = CheckOutUtil.multipleCheckOut(new Upgrade(), dataserverNodeMap);
		Map<Integer, List<ItemUse>> itemUseMap = CheckOutUtil.multipleCheckOut(new ItemUse(), dataserverNodeMap);
		
		Excel excel = ExcelUtil.getInstance();
		excel.getNewSheet("unbirth");
		excel.insertAndMove("服务器名称").insertAndMove("未出生的孩子数量").nextRow();
		for(Entry<Integer, Integer> unbirthBaby : unbirthBabyMap.entrySet()){
			excel.insertAndMove(testserverNodeMap.get(unbirthBaby.getKey()).getServerName()).insertAndMove(unbirthBaby.getValue()).nextRow();
		}
		excel.getNewSheet("birth");
		excel.insertAndMove("服务器名称").insertAndMove("已经出生的孩子数量").nextRow();
		for(Entry<Integer, Integer> birthBaby : birthBabyMap.entrySet()){
			excel.insertAndMove(testserverNodeMap.get(birthBaby.getKey()).getServerName()).insertAndMove(birthBaby.getValue()).nextRow();
		}
		excel.getNewSheet("Upgrade");
		excel.insertAndMove("服务器名称").insertAndMove("玩家ID").insertAndMove("使用元宝数量").nextRow();
		for(Entry<Integer, List<Upgrade>> upgradeEntry : upgradeMap.entrySet()){
			int serverID = upgradeEntry.getKey();
			List<Upgrade> upgradeList = upgradeEntry.getValue();
			for(Upgrade upgrade : upgradeList){
				excel.insertAndMove(testserverNodeMap.get(serverID).getServerName()).insertAndMove(upgrade.playerID).insertAndMove(upgrade.useYuanBao).nextRow();
			}
		}
		excel.getNewSheet("itemUse");
		excel.insertAndMove("服务器ID").insertAndMove("玩家ID").insertAndMove("使用元宝数量").nextRow();
		for(Entry<Integer, List<ItemUse>> itemUseEntry : itemUseMap.entrySet()){
			int serverID = itemUseEntry.getKey();
			List<ItemUse> itemUseList = itemUseEntry.getValue();
			for(ItemUse itemUse : itemUseList){
				excel.insertAndMove(testserverNodeMap.get(serverID).getServerName()).insertAndMove(itemUse.playerID).insertAndMove(itemUse.useYuanBao).nextRow();
			}
		}
		File newFile = excel.saveExcel("baby.xlsx");
		PrintUtil.outputFileResponse(resp, newFile);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}