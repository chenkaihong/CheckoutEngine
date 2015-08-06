package com.bear.servlet.babyNew;


/**
 * Copyright (c) 2011-2013 by 广州游爱 Inc.
 * @Author Create by 李兴
 * @Date 2014年11月28日 下午2:10:24
 * @Description 
 */
public class BabyLooksRef  implements Comparable<BabyLooksRef> {
	private int id;				// 索引
	private int part;			// 部位
	private String style;		// 类型
	private String image1;		// 图片1
	private String image2;		// 图片2
	private int cost;			// 花费
	private String sameId;		// 区分是否为同种样式

	@Override
	public int compareTo(BabyLooksRef o) {
		return o.getId() - id;
	}

	public int getId() {
		return id;
	}
	public int getPart() {
		return part;
	}
	public String getStyle() {
		return style;
	}
	public String getImage1() {
		return image1;
	}
	public String getImage2() {
		return image2;
	}
	public int getCost() {
		return cost;
	}
	public String getSameId() {
		return sameId;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPart(int part) {
		this.part = part;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public void setSameId(String sameId) {
		this.sameId = sameId;
	}	
}
