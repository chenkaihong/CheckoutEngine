package com.bear.util;

import com.bear.connection.ServerNode;

public interface PackupSelfFactory<T> extends PackupSelf<T>{
	PackupSelfFactory<T> getModel(ServerNode serverNode);
}

