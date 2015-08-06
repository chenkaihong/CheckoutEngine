package com.bear.servlet.loginCount;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.connection.ServerNode.Server;
import com.bear.connection.ServerNodeFactory;
import com.bear.util.CheckOutUtil;
import com.bear.util.excelUtil.Excel;
import com.bear.util.excelUtil.ExcelUtil;

public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp){
		try{
			Map<Integer, ServerNode> dataDB = ConnectionManager.getDataDB(ServerNodeFactory.onlineServerNode);
			Map<Integer, ServerNode> testDB = ConnectionManager.getTestDB(ServerNodeFactory.onlineServerNode);
			
			for(Entry<Integer, ServerNode> entry : dataDB.entrySet()){
				ServerNode serverNode = entry.getValue();
				Server server = serverNode.getServer();
				URL url = new URL(serverNode.getGame_addr());
				String host = url.getHost();
				server.setHost(host);
			}
			for(Entry<Integer, ServerNode> entry : testDB.entrySet()){
				ServerNode serverNode = entry.getValue();
				Server server = serverNode.getServer();
				URL url = new URL(serverNode.getGame_addr());
				String host = url.getHost();
				server.setHost(host);
			}
			
			List<String> yearAndMonthList = Arrays.asList("6,19",
														  "6,20",
														  "6,21",
														  "6,22",
														  "6,23",
														  "6,24");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Excel excel = ExcelUtil.getInstance();
			
			for(String yearAndMonth : yearAndMonthList){
				
				Calendar time = Calendar.getInstance();
				String[] info = yearAndMonth.split(",");
				time.set(2015, Integer.parseInt(info[0])-1, Integer.parseInt(info[1]), 0, 0, 0);
				time.set(Calendar.MILLISECOND, 0);
				String startTime = sf.format(time.getTime());
				time.add(Calendar.DAY_OF_MONTH, 1);
				String endTime = sf.format(time.getTime());
				
				Map<Integer, List<PlayerInfo>> playerList = CheckOutUtil.multipleCheckOut(new PlayerInfo("SELECT DISTINCT log_user as id,0 as NAME,0 as LEVEL,0 as voucherTotal FROM log_logout WHERE log_time BETWEEN ? AND ?", startTime, endTime), dataDB);
				StringBuilder sb = new StringBuilder();
				for(Entry<Integer, List<PlayerInfo>> entry : playerList.entrySet()){
					for(PlayerInfo s : entry.getValue()){
						sb.append(s.getId()).append(",");
					}
				}
				if(sb != null && sb.length() > 0){
					sb.deleteCharAt(sb.length()-1);
					String sql = String.format("SELECT id,NAME,LEVEL,voucherTotal FROM player WHERE id IN (%s)", sb.toString());
					Map<Integer, List<PlayerInfo>> playerLevelList = CheckOutUtil.multipleCheckOut(new PlayerInfo(sql), testDB);
					
					for(Entry<Integer, List<PlayerInfo>> playerInfoEntry : playerList.entrySet()){
						int serverID = playerInfoEntry.getKey();
						for(PlayerInfo playerInfo : playerInfoEntry.getValue()){
							List<PlayerInfo> temp = playerLevelList.get(serverID);
							if(temp != null){
								for(PlayerInfo s : temp){
									if(s.getId() == playerInfo.getId()){
										playerInfo.setName(s.getName());
										playerInfo.setLevel(s.getLevel());
										playerInfo.setVipLevel(s.getVipLevel());
										break;
									}
								}
							}
						}
					}
				}
				
				excel.getNewSheet(info[1] + "日");
				excel.insertAndMove("服务器ID").insertAndMove("服务器名称").insertAndMove("玩家ID").insertAndMove("玩家名称").insertAndMove("玩家等级").insertAndMove("玩家VIP等级").nextRow();
				for(Entry<Integer, List<PlayerInfo>> entry : playerList.entrySet()){
					int serverID = entry.getKey();
					for(PlayerInfo playerInfo : entry.getValue()){
						excel.insertAndMove(serverID).insertAndMove(testDB.get(serverID).getServerName()).insertAndMove(playerInfo.getId()).insertAndMove(playerInfo.getName()).insertAndMove(playerInfo.getLevel()).insertAndMove(playerInfo.getVipLevel()).nextRow();
					}
				}
			}
			
			File newFile = excel.saveExcel("voucher.xlsx");
			System.out.println("@@@ OutFile: " + newFile);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}