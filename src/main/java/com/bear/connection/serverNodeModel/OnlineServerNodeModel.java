package com.bear.connection.serverNodeModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.bear.connection.ServerNode;
import com.bear.connection.ServerNode.Server;
import com.bear.util.DataModel;
import com.google.gson.Gson;

public class OnlineServerNodeModel extends DataModel<Map<Integer,ServerNode>>{
	private ServerNode serverNode;
	
	public OnlineServerNodeModel(ServerNode serverNode){
		super("SELECT id,NAME,serverCloseDate,create_time,login_addr,game_addr,log_db_config,masterServerId "
				+ "FROM servers "
				+ "WHERE serverCloseDate IS NULL AND id NOT IN (10,36,501,505,509)", 
				new String[]{});
		this.serverNode = serverNode;
	}
	@Override
	public Map<Integer, ServerNode> packup(ResultSet rs) throws SQLException {
		Map<Integer,ServerNode> serverNodeMap = new HashMap<Integer,ServerNode>();
		ServerNode serverNode = null;
		while(rs.next()){
			int serverID = rs.getInt("id");
			String serverName = rs.getString("NAME");
			String serverCloseDate = rs.getString("serverCloseDate");
			String createTime = rs.getString("create_time");
			String area = this.serverNode.getArea();
			String login_addr = rs.getString("login_addr");
			String game_addr = rs.getString("game_addr");
			String masterServerID = rs.getString("masterServerId");
			serverNode = new ServerNode(serverID, serverName, serverCloseDate, createTime, area, login_addr, game_addr, masterServerID);
			Server server = new Gson().fromJson(rs.getString("log_db_config"),Server.class);
			server.setPort("3306");
			serverNode.setServer(server);
			serverNodeMap.put(serverNode.getServerId(), serverNode);
		}
		return serverNodeMap;
	}
}
