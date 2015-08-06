package com.bear.util;

import java.util.HashMap;

public class MapWithoutNull<K,V> extends HashMap<K,V>{
	private static final long serialVersionUID = 1L;
	private V defaultValue;
			
	public MapWithoutNull(Class<V> valueClass){
		try {
			this.defaultValue = valueClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	@Override
	public V get(Object key) {
		V result = super.get(key);
		return  result == null?defaultValue:result;
	}
}
