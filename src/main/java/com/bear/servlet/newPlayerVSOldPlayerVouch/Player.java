package com.bear.servlet.newPlayerVSOldPlayerVouch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bear.util.DataModel;

public class Player extends DataModel<List<Player>>{
	private int playerID;
	private String linkID;
	private long create_time;
	
	public Player(){
		super("SELECT player_id,link_key,create_time FROM player_X", new String[]{});
	}
	public Player(int playerID,String linkID,long creatTime){
		this.playerID = playerID;
		this.linkID = linkID;
		this.create_time = creatTime;
	}
	
	@Override
	public List<Player> packup(ResultSet rs) throws SQLException {
		List<Player> playerList = new ArrayList<Player>();
		Player player = null;
		while(rs.next()){
			player = new Player(rs.getInt("player_id"), rs.getString("link_key"),rs.getLong("create_time"));
			playerList.add(player);
		}
		return playerList;
	}
	public int getPlayerID() {
		return playerID;
	}
	public String getLinkID() {
		return linkID;
	}
	public long getCreate_time() {
		return create_time;
	}
}
