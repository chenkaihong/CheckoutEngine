package com.bear.connection;

import java.util.Map;

import com.bear.connection.serverNodeModel.OnlineServerNodeModel;
import com.bear.util.PackupSelf;

public class ServerNodeFactory {
	/** 在线库serverNode */
	public final static int onlineServerNode = 1;

	public static PackupSelf<Map<Integer,ServerNode>> getServerNodeModel(int whichType, ServerNode backgroud){
		switch(whichType){
		case onlineServerNode:
			return new OnlineServerNodeModel(backgroud);
		default:
			return null;
		}
	}
}
