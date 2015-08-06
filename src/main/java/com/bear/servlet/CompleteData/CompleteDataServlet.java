package com.bear.servlet.CompleteData;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.connection.ServerNodeFactory;
import com.bear.servlet.newPlayerVSOldPlayerVouch.Player;
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
public class CompleteDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static final Logger logger = Logger.getLogger(NewPlayerVSOldPlayerVouch.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServerNode backgroudServerNodeMap = ConnectionManager.getBackgroud();
		Map<Integer, ServerNode> testServerNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
		Map<Integer, ServerNode> dataServerNodeMap = ConnectionManager.getDataDB(ServerNodeFactory.onlineServerNode);
		
		// 获取在时间段内充值玩家的充值金额统计
//		Map<Integer, PlayerVouche> playerMap = CheckOutUtil.singleCheckOut(new PlayerVouche(), backgroudServerNodeMap);
		
		// 通过多线程获取全服player信息
//		Map<Integer, List<PlayerVouche>> playerMap = CheckOutUtil.multipleCheckOut(new PlayerVouche(), backgroudServerNodeMap);
		
		Excel excel = ExcelUtil.getInstance();
		
		
		
		File newFile = excel.saveExcel("test.xlsx");
		PrintUtil.outputFileResponse(resp, newFile);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}