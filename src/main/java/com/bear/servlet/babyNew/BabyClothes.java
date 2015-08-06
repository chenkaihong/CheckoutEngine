package com.bear.servlet.babyNew;

/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * @Author Create by bear_ckh
 * @Date 2015年1月5日 下午3:08:13
 * @Description 
 */
public class BabyClothes {
	private int clothesID;			// 服饰ID
	private int rate;				// 合成进度
	private boolean isUseful;		// 是否可用

	public BabyClothes(int clothesID){
		this.clothesID = clothesID;
		this.rate = 0;
		this.isUseful = false;
	}
	public BabyClothes(int clothesID, int rate){
		this.clothesID = clothesID;
		this.rate = rate;
		this.isUseful = true;
	}
	
	public int getClothesID() {
		return clothesID;
	}
	public int getRate() {
		return rate;
	}
	public boolean isUseful() {
		return isUseful;
	}
	public void setClothesID(int clothesID) {
		this.clothesID = clothesID;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public void setUseful(boolean isUseful) {
		this.isUseful = isUseful;
	}
}
