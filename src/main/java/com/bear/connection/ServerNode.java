package com.bear.connection;


public class ServerNode{
	
	private String serverName;
	private int serverId;
	private String login_addr;
	private String game_addr;
	private Server server;
	private String createTime;
	private String serverCloseDate;
	private String masterServerID;
	private String area;
	
	public ServerNode(){
		
	}
	
	public ServerNode(int serverID,String serverName,String serverCloseDate,String createTime,String area,String login_addr,String game_addr,String masterServerID){
		this.serverId = serverID;
		this.serverName = serverName;
		this.serverCloseDate = serverCloseDate;
		this.createTime = createTime;
		this.area = area;
		this.login_addr = login_addr;
		this.game_addr = game_addr;
		this.masterServerID = masterServerID;
	}

	public ServerNode(String serverName, int serverID,String host,String userName,String password,String db,String area) {
		this.serverName = serverName;
		this.serverId = serverID;
		this.server = new Server(host,userName,password,db);
		this.setArea(area);
	}
	
	public ServerNode(ServerNode serverNode){
		this.serverId = serverNode.getServerId();
		this.serverName = serverNode.getServerName();
		this.serverCloseDate = serverNode.getServerCloseDate();
		this.createTime = serverNode.getCreateTime();
		this.area = serverNode.getArea();
		this.login_addr = serverNode.getLogin_addr();
		this.game_addr = serverNode.getGame_addr();
		this.masterServerID = serverNode.getMasterServerID();
		this.server = new Server(serverNode.getServer());
	}
	
	public class Server {
		private String host;
		private String user;
		private String password;
		private String db;
		private String port;
		
		private Server(){}
		
		private Server(String host, String user, String password, String db) {
			super();
			this.host = host;
			this.user = user;
			this.password = password;
			this.db = db;
			this.port = "3306";
		}
		
		private Server(Server server){
			this.host = server.getHost();
			this.user = server.getUser();
			this.password = server.getPassword();
			this.db = server.getDb();
			this.port = "3306";
		}
		
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getDb() {
			return db;
		}
		public void setDb(String db) {
			this.db = db;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
	}

	public String getServerName() {
		return serverName;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public String getLogin_addr() {
		return login_addr;
	}
	public void setLogin_addr(String login_addr) {
		this.login_addr = login_addr;
	}
	public String getServerCloseDate() {
		return serverCloseDate;
	}
	public void setServerCloseDate(String serverCloseDate) {
		this.serverCloseDate = serverCloseDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMasterServerID() {
		return masterServerID;
	}
	public void setMasterServerID(String masterServerID) {
		this.masterServerID = masterServerID;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getGame_addr() {
		return game_addr;
	}
	public void setGame_addr(String game_addr) {
		this.game_addr = game_addr;
	}
}
