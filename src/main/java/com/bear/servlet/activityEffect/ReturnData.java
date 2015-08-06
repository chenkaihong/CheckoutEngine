package com.bear.servlet.activityEffect;

import java.util.Map;

public class ReturnData {
	public final int activityPlayerNum;
	public final int voucherNum;
	public final int voucherPlayerNum;
	public final double arup;
	public final double arupLogin;
	public final Map<String,Integer> channelVoucher;
	public final Map<String,Integer> openVoucher;
	public final Map<Integer,Integer> vipVoucher;
	public final Map<String,Integer> levelVoucher;
	public final Map<String,Integer> serverVoucher;
	
	public ReturnData(int activityPlayerNum, int voucherNum, int voucherPlayerNum,
			Map<String, Integer> channelVoucher,
			Map<String, Integer> openVoucher, Map<Integer, Integer> vipVoucher,
			Map<String, Integer> levelVoucher,
			Map<String, Integer> serverVoucher) {
		
		this.activityPlayerNum = activityPlayerNum;
		this.voucherNum = voucherNum;
		this.voucherPlayerNum = voucherPlayerNum;
		this.channelVoucher = channelVoucher;
		this.openVoucher = openVoucher;
		this.vipVoucher = vipVoucher;
		this.levelVoucher = levelVoucher;
		this.serverVoucher = serverVoucher;
		
		if(voucherPlayerNum != 0){
			arup = (this.voucherNum*1d) / this.voucherPlayerNum;
		}else{
			arup = 0d;
		}
		if(activityPlayerNum != 0){
			arupLogin = (this.voucherNum*1d) / this.activityPlayerNum;
		}else{
			arupLogin = 0d;
		}
	}
}
