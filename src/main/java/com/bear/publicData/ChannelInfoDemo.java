package com.bear.publicData;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.bear.util.DataModel;

public class ChannelInfoDemo extends DataModel<Map<Integer,ChannelInfoDemo>>{
	private int channelID;
	private String name;
	
	public ChannelInfoDemo(){
		super("", new String[]{});
		this.channelID = 1;
	}
	public ChannelInfoDemo(int playerID,String name){
		this.channelID = playerID;
		this.name = name;
	}
	
	@Override
	public Map<Integer,ChannelInfoDemo> packup(ResultSet rs) {
		Map<Integer,ChannelInfoDemo> map = new HashMap<Integer,ChannelInfoDemo>();
		map.put(1, new ChannelInfoDemo(1,"app1"));
		map.put(2, new ChannelInfoDemo(2,"app2"));
		map.put(3, new ChannelInfoDemo(3,"app3"));
		return map;
	}
	
	public int getChannel() {
		return channelID;
	}
	public String getName() {
		return name;
	}	
}
