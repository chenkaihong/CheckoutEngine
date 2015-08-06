package com.bear.publicData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNodeFactory;
import com.bear.util.PublicDataUtil;

public class PublicDataManager {
	private static Map<Integer,PlayerInfoDemo> playerInfoDemoMap;
	private static Map<Integer,ChannelInfoDemo> channelInfoDemoMap;
	
	static{
		playerInfoDemoMap = new HashMap<Integer, PlayerInfoDemo>();
	}
	
	public static PlayerInfoDemo getPlayerInfo(int serverID) throws SQLException {
		return PublicDataUtil.cacheServerPublicData(serverID, playerInfoDemoMap, new PlayerInfoDemo(), ConnectionManager.getDataDB(ServerNodeFactory.onlineServerNode).get(serverID));
	}
	public static ChannelInfoDemo getChannelInfo(int channelID) throws SQLException{
		if(channelInfoDemoMap == null){
			synchronized (ChannelInfoDemo.class) {
				channelInfoDemoMap = PublicDataUtil.cachePbulicData(channelInfoDemoMap, new ChannelInfoDemo(), ConnectionManager.getBackgroud());
			}
		}
		return channelInfoDemoMap.get(channelID);
	}
	
	public static void main(String[] args) throws SQLException {
		for(int i = 0;i < 100;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("ChannelName: " + PublicDataManager.getChannelInfo(1).getName());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		for(int i = 0;i < 100;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("ChannelName: " + PublicDataManager.getChannelInfo(2).getName());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}