package com.bear.publicData;

import java.sql.ResultSet;

import com.bear.util.DataModel;

public class PlayerInfoDemo extends DataModel<PlayerInfoDemo>{
	private int playerID;
	
	public PlayerInfoDemo(){
		super("", new String[]{});
		this.playerID = 1;
	}
	public PlayerInfoDemo(int playerID){
		this.playerID = playerID;
	}
	
	@Override
	public PlayerInfoDemo packup(ResultSet rs) {
		return new PlayerInfoDemo(playerID);
	}
	
	public int getPlayerID() {
		return playerID;
	}
}
