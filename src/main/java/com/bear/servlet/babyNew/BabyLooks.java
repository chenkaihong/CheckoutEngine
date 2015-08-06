package com.bear.servlet.babyNew;

/**
 * Copyright (c) 2011-2013 by 广州游爱 Inc.
 * @Author Create by 李兴
 * @Date 2014年11月26日 下午3:14:23
 * @Description 
 */
public class BabyLooks {
	private int hair;	// 头发
	private int eye;	//眼睛
	private int decorate;	//装饰 
	
	public BabyLooks(){}
	public BabyLooks(BabyLooks babyLooks){
		this.hair = babyLooks.getHair();
		this.eye = babyLooks.getEye();
		this.decorate = babyLooks.getDecorate();
	}
	
	public int getHair() {
		return hair;
	}
	public void setHair(int hair) {
		this.hair = hair;
	}
	public int getEye() {
		return eye;
	}
	public void setEye(int eye) {
		this.eye = eye;
	}
	public int getDecorate() {
		return decorate;
	}
	public void setDecorate(int decorate) {
		this.decorate = decorate;
	}
}
