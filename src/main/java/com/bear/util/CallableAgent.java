package com.bear.util;

import java.util.concurrent.Callable;

import com.bear.connection.ServerNode;

public class CallableAgent<V> implements Callable<V>{
	private final ServerNode serverNode;
	private final PackupSelf<V> packupSelf;
	public CallableAgent(PackupSelf<V> packupSelf, ServerNode serverNode){
		this.serverNode = serverNode;
		this.packupSelf = packupSelf;
	}
	@Override
	public V call() throws Exception {
		return CheckOutUtil.singleCheckOut(packupSelf, serverNode);
	}
}