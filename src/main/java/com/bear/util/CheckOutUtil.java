package com.bear.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.bear.connection.ServerNode;
import com.bear.connection.ServerNode.Server;

public class CheckOutUtil {
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("@@@@ Jdbc Driver loading is error!");
		}
	}
	
	/**
	 * 查询单个任务的工具类,其返回类型必须与PackupSelf接口的packup方法返回值一致
	 * @param packupSelf
	 * @return
	 * @throws SQLException
	 */
	public static <T>T singleCheckOut(PackupSelf<T> packupSelf, ServerNode serverNode) throws SQLException{
		T result = null;
		// must use connection pool!
		Server server = serverNode.getServer();
		// 暂时不使用连接池
		// Connection ct = ConnectionPool.getInstance().getConnection(serverNode.getServerId(), serverNode.getServer());
		String databaseUrl="jdbc:mysql://%s:%s/%s?characterEncoding=UTF-8&user=%s&password=%s";
		String url = String.format(databaseUrl, server.getHost(), server.getPort(), server.getDb(), server.getUser(), server.getPassword());
		Connection ct = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ct = DriverManager.getConnection(url);
			if(ct == null){
				throw new RuntimeException("@@@@ Connction is null! serverID: " + serverNode.getServerId() + ", serverName: " + serverNode.getServerName());
			}
			// 如果遇到player_X的表,则需要将所有Data表合服的数据表进行全部查出,并且将参数产生多次赋值
			String usefulSql = getUsefulSql(packupSelf.getSql().trim(), serverNode);
			ps = ct.prepareStatement(usefulSql);
			String[] parmArray = packupSelf.getParm();
			if(parmArray != null && parmArray.length > 0){
				for(int i = 0;i < parmArray.length;i++){
					ps.setString(i+1, parmArray[i]);
				}
			}
			rs = ps.executeQuery();
			result = packupSelf.packup(rs);
		} catch(SQLException e){
			throw new RuntimeException("@@@@ Connction is error! serverID: " + serverNode.getServerId() + ", serverName: " + serverNode.getServerName() + "\n", e);
		} finally{
			ClosedUtil.closedResultSet(rs);
			ClosedUtil.closedPreparedStatement(ps);
			ClosedUtil.closedConnection(ct);
		}
		return result;
	}
	/**
	 * 多线程查询多个任务,并返回统一的数据结构,并限制一个服务器只能建立一条Connection进行连接查询(简单判断: 通过serverID不重复来保证对一个服务器只能产生一条连接)
	 * @param packupSelf
	 * @return
	 * @throws SQLException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	public static <T>Map<Integer,T> multipleCheckOut(PackupSelf<T> packupSelf, List<ServerNode> serverNodeList){
		// 判断每个服务器信息都唯一存在
		List<Integer> serverIDList = new ArrayList<Integer>();
		for(ServerNode serverNode : serverNodeList){
			serverIDList.add(serverNode.getServerId());
		}
		Set<Integer> serverIDSet = new HashSet<Integer>();
		serverIDSet.addAll(serverIDList);
		if(serverIDList.size() != serverIDSet.size()){
			throw new RuntimeException("@@@@ Server Repeat error!");
		}
		
		Map<Integer,T> resultList = new HashMap<Integer,T>();
		// 将一个任务分派到多个Connection中
		// 这里使用CallableAgent对serverNode和packupSelf进行分离
		List<Callable<T>> callableAgentList = new ArrayList<Callable<T>>();
		for(ServerNode serverNode : serverNodeList){
			callableAgentList.add(new CallableAgent<T>(packupSelf, serverNode));
		}
		List<Future<T>> futureList = TreadUtil.getFutureList(callableAgentList);
		int count = 0;
		for(Future<?> future : futureList){
			ServerNode serverNode = serverNodeList.get(count);
			try {
				resultList.put(serverNode.getServerId(), (T) future.get(60, TimeUnit.SECONDS));
				System.out.println("serverID : " + serverNode.getServerId() + " ----> is done!");
			} catch (InterruptedException e) {
				System.out.println("serverID : " + serverNode.getServerId() + " ----> errorMsg : \n");
				e.printStackTrace();
			} catch (ExecutionException e) {
				System.out.println("serverID : " + serverNode.getServerId() + " ----> errorMsg : \n");
				e.printStackTrace();
			} catch (TimeoutException e) {
				System.out.println("serverID : " + serverNode.getServerId() + " ----> errorMsg : \n");
				e.printStackTrace();
			}
			count++;
		}
		return resultList;
	}
	/**
	 * 多线程查询多个任务,并返回统一的数据结构,并限制一个服务器只能建立一条Connection进行连接查询(简单判断: 通过serverID不重复来保证对一个服务器只能产生一条连接)
	 * @param packupSelf
	 * @return
	 * @throws SQLException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	public static <T>Map<Integer,T> multipleCheckOutByFactory(PackupSelfFactory<T> packupSelf, List<ServerNode> serverNodeList){
		// 判断每个服务器信息都唯一存在
		List<Integer> serverIDList = new ArrayList<Integer>();
		for(ServerNode serverNode : serverNodeList){
			serverIDList.add(serverNode.getServerId());
		}
		Set<Integer> serverIDSet = new HashSet<Integer>();
		serverIDSet.addAll(serverIDList);
		if(serverIDList.size() != serverIDSet.size()){
			throw new RuntimeException("@@@@ Server Repeat error!");
		}
		
		Map<Integer,T> resultList = new HashMap<Integer,T>();
		// 将一个任务分派到多个Connection中
		// 这里使用CallableAgent对serverNode和packupSelf进行分离
		List<Callable<T>> callableAgentList = new ArrayList<Callable<T>>();
		for(ServerNode serverNode : serverNodeList){
			callableAgentList.add(new CallableAgent<T>(packupSelf.getModel(serverNode), serverNode));
		}
		List<Future<T>> futureList = TreadUtil.getFutureList(callableAgentList);
		int count = 0;
		for(Future<?> future : futureList){
			ServerNode serverNode = serverNodeList.get(count);
			try {
				resultList.put(serverNode.getServerId(), (T) future.get(60, TimeUnit.SECONDS));
				System.out.println("serverID : " + serverNode.getServerId() + " ----> is done!");
			} catch (InterruptedException e) {
				System.out.println("serverID : " + serverNode.getServerId() + " ----> errorMsg : \n");
				e.printStackTrace();
			} catch (ExecutionException e) {
				System.out.println("serverID : " + serverNode.getServerId() + " ----> errorMsg : \n");
				e.printStackTrace();
			} catch (TimeoutException e) {
				System.out.println("serverID : " + serverNode.getServerId() + " ----> errorMsg : \n");
				e.printStackTrace();
			}
			count++;
		}
		return resultList;
	}
	public static <T>Map<Integer,T> multipleCheckOut(PackupSelf<T> packupSelf, Map<Integer, ServerNode> serverNodeMap){
		List<ServerNode> serverNodeList = new ArrayList<ServerNode>();
		for(Entry<Integer, ServerNode> serverNode : serverNodeMap.entrySet()){
			serverNodeList.add(serverNode.getValue());
		}
		return multipleCheckOut(packupSelf, serverNodeList);
	}
	public static <T>Map<Integer,T> multipleCheckOutByFactory(PackupSelfFactory<T> packupSelf, Map<Integer, ServerNode> serverNodeMap){
		List<ServerNode> serverNodeList = new ArrayList<ServerNode>();
		for(Entry<Integer, ServerNode> serverNode : serverNodeMap.entrySet()){
			serverNodeList.add(serverNode.getValue());
		}
		return multipleCheckOutByFactory(packupSelf, serverNodeList);
	}
	
	// 如果遇到player_X的表,则需要将所有Data表合服的数据表进行全部查出,并且将参数产生多次赋值
	private static String getUsefulSql(String sql,ServerNode serverNode) throws SQLException{
		String usefulSql = null;
		if(sql.contains("player_X")){
			List<DBTable> tableList = CheckOutUtil.singleCheckOut(new DBTable(), serverNode);
			int count = 0;
			for(DBTable table : tableList){
				if(table.getTableName().matches("player_[0-9]+")){
					if(count == 0){
						usefulSql = sql.replace("player_X", table.getTableName());
						count++;
					}else{
						usefulSql += " UNION " + sql.replace("player_X", table.getTableName());
					}
				}
			}
			if(usefulSql == null){
				usefulSql = sql;
			}
		}else{
			usefulSql = sql;
		}
		return usefulSql;
	}
}
// 查询各服务器Data库tableName的方法
class DBTable extends DataModel<List<DBTable>>{
	String tableName;
	
	public DBTable(){
		super("SHOW TABLES", new String[]{});
	}
	public DBTable(String tableName){
		this.tableName = tableName;
	}

	@Override
	public List<DBTable> packup(ResultSet rs) throws SQLException {
		List<DBTable> dbTable = new ArrayList<DBTable>();
		while(rs.next()){
			dbTable.add(new DBTable(rs.getString(1)));
		}
		return dbTable;
	}
	
	public String getTableName() {
		return tableName;
	}
}
