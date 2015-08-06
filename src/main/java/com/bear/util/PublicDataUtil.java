package com.bear.util;

import java.sql.SQLException;
import java.util.Map;

import com.bear.connection.ServerNode;

public class PublicDataUtil {
	public static <T> T cacheServerPublicData(int serverID, Map<Integer,T> map, PackupSelf<T> dataDemo, ServerNode serverNode) throws SQLException{
		if(serverNode == null){
			throw new RuntimeException("@@@@ no find the serverNode ----> serverID: " + serverID);
		}
		T publicData = map.get(serverID);
		if(publicData == null){
			synchronized (serverNode) {
				publicData = map.get(serverID);
				if(publicData == null){
					publicData = CheckOutUtil.singleCheckOut(dataDemo, serverNode);
					map.put(serverNode.getServerId(), publicData);
				}
			}
		}
		return publicData;
	}
	public static <T> Map<Integer,T> cachePbulicData(Map<Integer,T> map, PackupSelf<Map<Integer,T>> dataDemo, ServerNode serverNode) throws SQLException{
		if(serverNode == null){
			throw new RuntimeException("@@@@ no find the serverNode when cachePbulicData!");
		}
		if(map == null){
			map = CheckOutUtil.singleCheckOut(dataDemo, serverNode);
		}
		return map;
	}
}
