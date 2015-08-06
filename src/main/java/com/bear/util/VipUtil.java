package com.bear.util;

public class VipUtil {
	public static int getVipLevel(int voucherTotal){
		
		int level = 0;
		
		if(voucherTotal < 100){
			level = 0;
		}else if(voucherTotal < 500){
			level = 1;
		}else if(voucherTotal < 1000){
			level = 2;
		}else if(voucherTotal < 2000){
			level = 3;
		}else if(voucherTotal < 5000){
			level = 4;
		}else if(voucherTotal < 10000){
			level = 5;
		}else if(voucherTotal < 50000){
			level = 6;
		}else if(voucherTotal < 100000){
			level = 7;
		}else if(voucherTotal < 200000){
			level = 8;
		}else if(voucherTotal < 500000){
			level = 9;
		}else if(voucherTotal < 1000000){
			level = 10;
		}else if(voucherTotal < 1500000){
			level = 11;
		}else if(voucherTotal < 3500000){
			level = 12;
		}else if(voucherTotal < 6500000){
			level = 13;
		}else{
			level = 14;
		}
		return level;
	}
}
