package com.bear.servlet.showClomun;

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
import com.bear.util.TimeUtil;
import com.bear.util.excelUtil.Excel;
import com.bear.util.excelUtil.ExcelUtil;

/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * 用于统计在线玩家的上阵卡牌情况
 * @Author Create by ckh
 * @Date 2014-11-12 下午4:34:09
 * @Description
 */
public class ShowModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final int testDB = 1;
	private final int dataDB = 2;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String sql = null;
		String[] columName = null;
		int dbType = 1;			// 默认使用Test库
		try{
			sql = req.getParameter("sql").trim();
			columName = req.getParameter("columName").trim().split(",");
			dbType = Integer.parseInt(req.getParameter("dbType").trim());
		}catch(Exception e){
			throw new RuntimeException("@@@ Input is err!");
		}
		
		if(sql.isEmpty() || columName.length <= 0){
			throw new RuntimeException("@@@ Input must have some value!");
		}
		
		Map<Integer, ServerNode> serverNodeMap = null;
		switch(dbType){
		case testDB:
			serverNodeMap = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
			break;
		case dataDB:
			serverNodeMap = ConnectionManager.getDataDB(ServerNodeFactory.onlineServerNode);
			break;
		default:
			throw new RuntimeException("@@@ dbType is err!");
		}
		
		Map<Integer, List<RowMsg>> show = CheckOutUtil.multipleCheckOut(new RowMsg(sql), serverNodeMap);
		
		Excel excel = ExcelUtil.getInstance();
		excel.getNewSheet("result");
		System.out.println("开始整合excel....");
		excel.insertAndMove("服务器ID");
		excel.insertAndMove("服务器名称");
		for(String name : columName){
			excel.insertAndMove(name);
		}
		excel.nextRow();
		for(Entry<Integer, List<RowMsg>> showEntry : show.entrySet()){
			int serverID = showEntry.getKey();
			List<RowMsg> rowMsgList = showEntry.getValue();
			ServerNode serverNode = serverNodeMap.get(serverID);
			System.out.println(String.format("%s - %d",serverNode.getServerName(),rowMsgList.size()));
			excel.insertAndMove(serverNode == null?"?":serverNode.getServerId()+"");
			excel.insertAndMove(serverNode == null?"?":serverNode.getServerName());
			for(RowMsg rowMsg : rowMsgList){
				for(Object obj : rowMsg.contents){
					// 可以做类型判断,然后分开显示
					if(obj == null){
						excel.insertAndMove("");
						continue;
					}
					excel.insertAndMove(obj.toString());
				}
				excel.nextRow();
			}
		}
		System.out.println("结束整合excel....");
		File newFile = excel.saveExcel(String.format("%s.xlsx",TimeUtil.nowTimeString()));
		PrintUtil.outputFileResponse(resp, newFile);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}