package com.bear.connection;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.bear.connection.ServerNode.Server;
import com.bear.publicData.ChannelInfoDemo;
import com.bear.util.PackupSelf;
import com.bear.util.PropertiesUtil;
import com.bear.util.PublicDataUtil;

public class ConnectionManager {
	private static ServerNode background;
	private static Map<Integer,ServerNode> testDB;
	private static Map<Integer,ServerNode> dataDB;
	
	public synchronized static ServerNode getBackgroud(){
		if(background == null){
			background = PropertiesUtil.initBackgroud(PropertiesUtil.getProperty("local"));
		}
		return background;
	}
	public static Map<Integer,ServerNode> getTestDB(int serverNodeFactory){
		if(testDB == null){
			synchronized (ChannelInfoDemo.class) {
				dataDB = getDataDB(serverNodeFactory);
				testDB = new HashMap<Integer,ServerNode>();
				for(Entry<Integer, ServerNode> serverNodeEntry : dataDB.entrySet()){
					ServerNode serverNode = new ServerNode(serverNodeEntry.getValue());
					Server server = serverNode.getServer();
					server.setDb(server.getDb().replace("data", "test"));
					testDB.put(serverNode.getServerId(), serverNode);
				}
			}
		}
		return testDB;
	}
	public static Map<Integer,ServerNode> getDataDB(int serverNodeFactory){
		if(dataDB == null){
			synchronized (ChannelInfoDemo.class) {
				try {
					ServerNode backgroud = getBackgroud();
					PackupSelf<Map<Integer,ServerNode>> serverNodeModel = ServerNodeFactory.getServerNodeModel(serverNodeFactory, backgroud);
					if(serverNodeModel == null){
						throw new RuntimeException("@@@@ serverNodeModel is not defind!");
					}
					dataDB = PublicDataUtil.cachePbulicData(dataDB, serverNodeModel, backgroud);
					
					//test begin
//					Map<Integer,ServerNode> newDB = new HashMap<Integer,ServerNode>();
//					newDB.put(12, dataDB.get(12));
//					dataDB = newDB;
					//test end
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException("DataDb connection error!May be the backgroup is wrong! msg : " + e.getMessage());
				}
			}
		}
		return dataDB;
	}
}
