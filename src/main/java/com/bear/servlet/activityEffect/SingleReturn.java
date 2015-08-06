package com.bear.servlet.activityEffect;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.bear.connection.ServerNode;
import com.bear.util.TimeUtil;
import com.bear.util.VipUtil;

public class SingleReturn {
	
	// 根据信息封装返回内容
	public static ReturnData getSingleReturn(Map<Integer, Integer> voucherMap, 
								 Map<Integer, Map<Integer, PlayerInfo>> playerVip, 
								 Map<Integer, ServerNode> testserverNodeMap,
								 Set<Integer> zgsy,
								 Set<Integer> yibu,
								 Set<Integer> mingtong) throws ParseException{
		// 日活跃人数
		int activityPlayerNum = 0;
		// 充值总额
		int voucherNum = 0;
		// 活跃充值人数
		int voucherPlayerNum = 0;
		
		// 数据统计
			// 渠道累计充值
		Map<String,Integer> channelVoucher = new LinkedHashMap<String,Integer>();
		channelVoucher.put("中手游", 0);
		channelVoucher.put("明通", 0);
		channelVoucher.put("亿部", 0);
		channelVoucher.put("安卓混服", 0);
			// 开服时间充值比较
		long month1 = 3600 * 24 * 30 * 1 * 1000L;
		long month2 = 3600 * 24 * 30 * 2 * 1000L;
		long month4 = 3600 * 24 * 30 * 4 * 1000L;
		long month6 = 3600 * 24 * 30 * 6 * 1000L;
		long month12 = 3600 * 24 * 30 * 12 * 1000L;
		
		Map<String,Integer> openVoucher = new LinkedHashMap<String,Integer>();
		openVoucher.put("1个月内", 0);
		openVoucher.put("1-2个月内", 0);
		openVoucher.put("2-4个月内", 0);
		openVoucher.put("4-6个月内", 0);
		openVoucher.put("6-12个月内", 0);
		openVoucher.put("大于12个月", 0);
		
			// Vip充值比较
		Map<Integer,Integer> vipVoucher = new LinkedHashMap<Integer,Integer>();
		vipVoucher.put(0, 0);
		vipVoucher.put(1, 0);
		vipVoucher.put(2, 0);
		vipVoucher.put(3, 0);
		vipVoucher.put(4, 0);
		vipVoucher.put(5, 0);
		vipVoucher.put(6, 0);
		vipVoucher.put(7, 0);
		vipVoucher.put(8, 0);
		vipVoucher.put(9, 0);
		vipVoucher.put(10, 0);
		vipVoucher.put(11, 0);
		vipVoucher.put(12, 0);
		vipVoucher.put(13, 0);
		vipVoucher.put(14, 0);
		
			// 等级充值比较
		Map<String,Integer> levelVoucher = new LinkedHashMap<String,Integer>();
		levelVoucher.put("700以上", 0);
		levelVoucher.put("600-699", 0);
		levelVoucher.put("500-599", 0);
		levelVoucher.put("400-499", 0);
		levelVoucher.put("300-399", 0);
		levelVoucher.put("200-299", 0);
		levelVoucher.put("100-199", 0);
		levelVoucher.put("0-99", 0);
		
			// 服务器充值比较
		Map<String,Integer> serverVoucher = new HashMap<String,Integer>();
		
		for(Entry<Integer, Map<Integer, PlayerInfo>> s : playerVip.entrySet()){
			int serverID = s.getKey();
			ServerNode serverNode = testserverNodeMap.get(serverID);
			activityPlayerNum += s.getValue().size();
			for(Entry<Integer, PlayerInfo> t : s.getValue().entrySet()){
				int playerID = t.getKey();
				Integer voucher = voucherMap.get(playerID);
				PlayerInfo player = t.getValue();
				if(voucher != null){
					voucherPlayerNum += 1;
					voucherNum += voucher;
					
					// 渠道累计充值判断
					if(zgsy.contains(serverID)){
						channelVoucher.put("中手游", channelVoucher.get("中手游")+voucher);
					}else if(mingtong.contains(serverID)){
						channelVoucher.put("明通", channelVoucher.get("明通")+voucher);
					}else if(yibu.contains(serverID)){
						channelVoucher.put("亿部", channelVoucher.get("亿部")+voucher);
					}else{
						channelVoucher.put("安卓混服", channelVoucher.get("安卓混服")+voucher);
					}
					
					// 开服时间充值比较
					if(openRange(month1, serverNode.getCreateTime())){
						openVoucher.put("1个月内", openVoucher.get("1个月内")+voucher);
					}else if(openRange(month2, serverNode.getCreateTime())){
						openVoucher.put("1-2个月内", openVoucher.get("1-2个月内")+voucher);
					}else if(openRange(month4, serverNode.getCreateTime())){
						openVoucher.put("2-4个月内", openVoucher.get("2-4个月内")+voucher);
					}else if(openRange(month6, serverNode.getCreateTime())){
						openVoucher.put("4-6个月内", openVoucher.get("4-6个月内")+voucher);
					}else if(openRange(month12, serverNode.getCreateTime())){
						openVoucher.put("6-12个月内", openVoucher.get("6-12个月内")+voucher);
					}else{
						openVoucher.put("大于12个月", openVoucher.get("大于12个月")+voucher);
					}
					
					// vip充值比较
					int vipLevel = VipUtil.getVipLevel(player.voucherTotal);
					vipVoucher.put(vipLevel, vipVoucher.get(vipLevel)+voucher);
					
					// 等级充值比较
					int level = player.level;
					if(level >= 700){
						levelVoucher.put("700以上", levelVoucher.get("700以上") + voucher);
					}else if(level >= 600){
						levelVoucher.put("600-699", levelVoucher.get("600-699") + voucher);
					}else if(level >= 500){
						levelVoucher.put("500-599", levelVoucher.get("500-599") + voucher);
					}else if(level >= 400){
						levelVoucher.put("400-499", levelVoucher.get("400-499") + voucher);
					}else if(level >= 300){
						levelVoucher.put("300-399", levelVoucher.get("300-399") + voucher);
					}else if(level >= 200){
						levelVoucher.put("200-299", levelVoucher.get("200-299") + voucher);
					}else if(level >= 100){
						levelVoucher.put("100-199", levelVoucher.get("100-199") + voucher);
					}else{
						levelVoucher.put("0-99", levelVoucher.get("0-99") + voucher);
					}
					
					// 服务器充值比较
					Integer voucherTemp = serverVoucher.get(serverNode.getServerName());
					if(voucherTemp != null){
						serverVoucher.put(serverNode.getServerName(), voucherTemp+voucher);
					}else{
						serverVoucher.put(serverNode.getServerName(), voucher);
					}
				}
			}
			// 判断服务器充值比较,当前服是否没有充值记录,如果没有充值记录,则放入0
			Integer voucherTemp = serverVoucher.get(serverNode.getServerName());
			if(voucherTemp == null){
				serverVoucher.put(serverNode.getServerName(), 0);
			}
		}
		
		return new ReturnData(activityPlayerNum, voucherNum, voucherPlayerNum, channelVoucher, openVoucher, vipVoucher, levelVoucher, serverVoucher);
	}
	// 判断开服时间是否在开服分段范围内
	private static boolean openRange(long range, String openTime) throws ParseException{
		return range > (System.currentTimeMillis() - TimeUtil.fromStringToLong(openTime));
	}
}
