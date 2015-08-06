package com.bear.servlet.babyNew;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.bear.util.DataModel;
import com.bear.util.JsonManager;
import com.google.gson.reflect.TypeToken;

public class Baby extends DataModel<List<Baby>>{
	
	private int babyId;
	private int fatherId;                    // 父亲Id
	private int motherId;                    // 母亲Id
	private String name;                     // 名字
	private boolean gender;                  // 性别 true: 男 false: 女
	private BabyLooks looks;   				 // 容貌明细
	private long birthTime;                  // 出生时间
	private long adoptTime;					 // 过继时间(使用520招亲榜后)
	private int mood;                        // 心情
	private long lastMoodTime;               // 上次更新心情时间-用于当前心情计算
	private boolean readCartoonOfFatherId;   // 父亲是否阅读了出生动画
	private boolean readCartoonOfMotherId;   // 母亲是否阅读了出生动画
	private long lastBirthRewardOfFatherId;  // 父亲是否领取了生日礼物
	private long lastBirthRewardOfMotherId;  // 母亲是否领取了生日礼物
	private int renameTimes;                 // 改名次数
	private int fatherInteractiveTodayTimes; // 父亲今天互动次数
	private long fatherInteractiveLastTime;  // 父亲最后互动时间
	private int motherInteractiveTodayTimes; // 母亲今天互动次数
	private long motherInteractiveLastTime;  // 母亲最后互动时间
	private int exp;                         // 当前经验值
	private int level;                       // 当前等级
	private int jieshu;                      // 当前阶数
	private int fatherRemainQualification;   // 父亲剩余可分配资质
	private int motherRemainQualification;   // 母亲剩余可分配资质
	private int minAttackQualification;      // 小攻资质
	private int maxAttackQualification;      // 大攻资质
	private int minDefendQualification;      // 小防资质
	private int maxDefendQualification;      // 大防资质
	private int savvy;                       // 悟性
	private int grow;                        // 成长值
	private int growHistoryMax;              // 历史最高成长
	private List<List<String>> growpRecord;	 // 成长洗练记录(以Json进行保存) List<一条洗练信息> ----> 洗练信息 List<String>: 玩家姓名,当前成长值,最高成长值,与客户端约定的文字描述
	private int jieshuItem;					 // 转生投入的道具
	private int savvyItem;					 // 悟性投入的道具
	private List<BabyClothes> clothespress;  // 宝宝的衣柜
	private int usingClothesID;				 // 宝宝现在身上穿的衣服
	
	public Baby(){
		super("SELECT * FROM baby");
	}

	@Override
	public List<Baby> packup(ResultSet rs) throws SQLException {
		List<Baby> babyList = new ArrayList<Baby>();
		while(rs.next()){
			Baby baby = new Baby();
			
			baby.setBabyId(rs.getInt("babyId"));
			baby.setFatherId(rs.getInt("fatherId"));
			baby.setMotherId(rs.getInt("motherId"));
			baby.setName(rs.getString("name"));
			baby.setGender(rs.getBoolean("gender"));
	        Type type = new com.google.gson.reflect.TypeToken<BabyLooks> () {}.getType();
	        BabyLooks looks = JsonManager.getGson().fromJson(rs.getString("looks"), type);
	        baby.setLooks(looks);
	        baby.setBirthTime(parseTimestampToLong(rs.getTimestamp("birthTime")));
	        baby.setAdoptTime(parseTimestampToLong(rs.getTimestamp("adoptTime")));
	        baby.setMood(rs.getInt("mood"));
	        baby.setLastMoodTime(rs.getLong("lastMoodTime"));
	        baby.setReadCartoonOfFatherId(rs.getBoolean("readCartoonOfFatherId"));
	        baby.setReadCartoonOfMotherId(rs.getBoolean("readCartoonOfMotherId"));
	        baby.setLastBirthRewardOfFatherId(rs.getLong("lastBirthRewardOfFatherId"));
	        baby.setLastBirthRewardOfMotherId(rs.getLong("lastBirthRewardOfMotherId"));
	        baby.setRenameTimes(rs.getInt("renameTimes"));
	        baby.setFatherInteractiveTodayTimes(rs.getInt("fatherInteractiveTodayTimes"));
	        baby.setFatherInteractiveLastTime(rs.getLong("fatherInteractiveLastTime"));
	        baby.setMotherInteractiveTodayTimes(rs.getInt("motherInteractiveTodayTimes"));
	        baby.setMotherInteractiveLastTime(rs.getLong("motherInteractiveLastTime"));
	        baby.setExp(rs.getInt("exp"));
	        baby.setLevel(rs.getInt("level"));
	        baby.setJieshu(rs.getInt("jieshu"));
	        baby.setFatherRemainQualification(rs.getInt("fatherRemainQualification"));
	        baby.setMotherRemainQualification(rs.getInt("motherRemainQualification"));
	        baby.setMinAttackQualification(rs.getInt("minAttackQualification"));
	        baby.setMaxAttackQualification(rs.getInt("maxAttackQualification"));
	        baby.setMinDefendQualification(rs.getInt("minDefendQualification"));
	        baby.setMaxDefendQualification(rs.getInt("maxDefendQualification"));
	        baby.setSavvy(rs.getInt("savvy"));
	        baby.setGrow(rs.getInt("grow"));
	        baby.setGrowHistoryMax(rs.getInt("growHistoryMax"));
	        List<List<String>> growpRecord = JsonManager.getGson().fromJson(rs.getString("growpRecord"),new TypeToken<List<List<String>>>() {}.getType());
	        baby.setGrowpRecord(growpRecord);
	        
	        List<BabyClothes> babyClothespress = JsonManager.getGson().fromJson(rs.getString("babyClothespress"),new TypeToken<List<BabyClothes>>() {}.getType());
	        if(babyClothespress == null){
	        	babyClothespress = new ArrayList<BabyClothes>();
	        }
	        baby.setClothespress(babyClothespress);
	        baby.setUsingClothesID(rs.getInt("usingClothesID"));
	        baby.setJieshuItem(rs.getInt("jieshuItem"));
	        baby.setSavvyItem(rs.getInt("savvyItem"));
	        
	        babyList.add(baby);
		}
		return babyList;
	}
	
	private long parseTimestampToLong(Timestamp timestamp){
		return timestamp.getTime();
	}
	
	public int getBabyId() {
		return babyId;
	}

	public void setBabyId(int babyId) {
		this.babyId = babyId;
	}

	public int getFatherId() {
		return fatherId;
	}

	public void setFatherId(int fatherId) {
		this.fatherId = fatherId;
	}

	public int getMotherId() {
		return motherId;
	}

	public void setMotherId(int motherId) {
		this.motherId = motherId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public long getBirthTime() {
		return birthTime;
	}

	public void setBirthTime(long birthTime) {
		this.birthTime = birthTime;
	}

	public int getMood() {
		return mood;
	}

	public void setMood(int mood) {
		this.mood = mood;
	}

	public long getLastMoodTime() {
		return lastMoodTime;
	}

	public void setLastMoodTime(long lastMoodTime) {
		this.lastMoodTime = lastMoodTime;
	}

	public boolean isReadCartoonOfFatherId() {
		return readCartoonOfFatherId;
	}

	public void setReadCartoonOfFatherId(boolean readCartoonOfFatherId) {
		this.readCartoonOfFatherId = readCartoonOfFatherId;
	}

	public boolean isReadCartoonOfMotherId() {
		return readCartoonOfMotherId;
	}

	public void setReadCartoonOfMotherId(boolean readCartoonOfMotherId) {
		this.readCartoonOfMotherId = readCartoonOfMotherId;
	}

	public long getLastBirthRewardOfFatherId() {
		return lastBirthRewardOfFatherId;
	}

	public void setLastBirthRewardOfFatherId(long lastBirthRewardOfFatherId) {
		this.lastBirthRewardOfFatherId = lastBirthRewardOfFatherId;
	}

	public long getLastBirthRewardOfMotherId() {
		return lastBirthRewardOfMotherId;
	}

	public void setLastBirthRewardOfMotherId(long lastBirthRewardOfMotherId) {
		this.lastBirthRewardOfMotherId = lastBirthRewardOfMotherId;
	}

	public int getRenameTimes() {
		return renameTimes;
	}

	public void setRenameTimes(int renameTimes) {
		this.renameTimes = renameTimes;
	}

	public int getFatherInteractiveTodayTimes() {
		return fatherInteractiveTodayTimes;
	}

	public void setFatherInteractiveTodayTimes(int fatherInteractiveTodayTimes) {
		this.fatherInteractiveTodayTimes = fatherInteractiveTodayTimes;
	}

	public long getFatherInteractiveLastTime() {
		return fatherInteractiveLastTime;
	}

	public void setFatherInteractiveLastTime(long fatherInteractiveLastTime) {
		this.fatherInteractiveLastTime = fatherInteractiveLastTime;
	}

	public int getMotherInteractiveTodayTimes() {
		return motherInteractiveTodayTimes;
	}

	public void setMotherInteractiveTodayTimes(int motherInteractiveTodayTimes) {
		this.motherInteractiveTodayTimes = motherInteractiveTodayTimes;
	}

	public long getMotherInteractiveLastTime() {
		return motherInteractiveLastTime;
	}

	public void setMotherInteractiveLastTime(long motherInteractiveLastTime) {
		this.motherInteractiveLastTime = motherInteractiveLastTime;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getJieshu() {
		return jieshu;
	}

	public void setJieshu(int jieshu) {
		this.jieshu = jieshu;
	}

	public int getFatherRemainQualification() {
		return fatherRemainQualification;
	}

	public void setFatherRemainQualification(int fatherRemainQualification) {
		this.fatherRemainQualification = fatherRemainQualification;
	}

	public int getMotherRemainQualification() {
		return motherRemainQualification;
	}

	public void setMotherRemainQualification(int motherRemainQualification) {
		this.motherRemainQualification = motherRemainQualification;
	}

	public int getMinAttackQualification() {
		return minAttackQualification;
	}

	public void setMinAttackQualification(int minAttackQualification) {
		this.minAttackQualification = minAttackQualification;
	}

	public int getMaxAttackQualification() {
		return maxAttackQualification;
	}

	public void setMaxAttackQualification(int maxAttackQualification) {
		this.maxAttackQualification = maxAttackQualification;
	}

	public int getMinDefendQualification() {
		return minDefendQualification;
	}

	public void setMinDefendQualification(int minDefendQualification) {
		this.minDefendQualification = minDefendQualification;
	}

	public int getMaxDefendQualification() {
		return maxDefendQualification;
	}

	public void setMaxDefendQualification(int maxDefendQualification) {
		this.maxDefendQualification = maxDefendQualification;
	}

	public int getSavvy() {
		return savvy;
	}

	public void setSavvy(int savvy) {
		this.savvy = savvy;
	}

	public int getGrow() {
		return grow;
	}

	public void setGrow(int grow) {
		this.grow = grow;
	}

	public int getGrowHistoryMax() {
		return growHistoryMax;
	}

	public void setGrowHistoryMax(int growHistoryMax) {
		this.growHistoryMax = growHistoryMax;
	}

	public long getAdoptTime() {
		return adoptTime;
	}

	public void setAdoptTime(long adoptTime) {
		this.adoptTime = adoptTime;
	}

	public BabyLooks getLooks() {
		return looks;
	}

	public void setLooks(BabyLooks looks) {
		this.looks = looks;
	}

	public List<List<String>> getGrowpRecord() {
		return growpRecord;
	}

	public void setGrowpRecord(List<List<String>> growpRecord) {
		this.growpRecord = growpRecord;
	}

	public int getJieshuItem() {
		return jieshuItem;
	}

	public void setJieshuItem(int jieshuItem) {
		this.jieshuItem = jieshuItem;
	}

	public int getSavvyItem() {
		return savvyItem;
	}

	public void setSavvyItem(int savvyItem) {
		this.savvyItem = savvyItem;
	}

	public List<BabyClothes> getClothespress() {
		return clothespress;
	}

	public void setClothespress(List<BabyClothes> clothespress) {
		this.clothespress = clothespress;
	}

	public int getUsingClothesID() {
		return usingClothesID;
	}

	public void setUsingClothesID(int usingClothesID) {
		this.usingClothesID = usingClothesID;
	}
}
