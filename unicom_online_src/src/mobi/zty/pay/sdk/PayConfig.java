package mobi.zty.pay.sdk;

import java.util.HashMap;
import java.util.Map;

import mobi.zty.sdk.util.StringUtil;

public class PayConfig {

	/**
	 *支付通知
	 */
	public static final int NOTIFY_PAYRESULT = 0;
	/**
	 * 正在支付中
	 */
	public static boolean IS_PAYING = false;
	/**
	 * 联通手机(也代表白包支付类型)
	 */
	public static final int UNICOM_MOBLE = 5;

	// public static final String defultAppIddefultAppIddefultAppIddefultAppIddefultAppIddefultAppIddefultAppIddefultAppIddefultAppId = "300008138647";// 默认计费id
	// public static final String defultAppKey = "349C972728AEEBFF";// 默认计费key
	public static final int defultMPayPrice[] = { 2, 3, 5, 10, 15, 20, 25, 30,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };// 默认支付计费 价格
	/**
	 * mm初始化失败
	 */
	public static final int MM_INIT_FAIL = 1000;
	/**
	 * mm初始化返回结果码
	 */
	public static final int MM_INIT_CODE = 1001;
	/**
	 * 天翼空间 未集成进去 初始化报错
	 */
	public static final int TC_INIT_CODE = 1003;
	/**
	 * mm计费点不存在
	 */
	public static final int MM_CODE_ERRO = 1005;
	/**
	 * mm支付成功
	 */
	public static final int BIIL_SUCC = 1010;
	/**
	 * mm支付取消
	 */
	public static final int BIIL_CANCER = 1015;
	/**
	 * mm支付失败
	 */
	public static final int MM_BIIL_FAIL = 1020;

	/**
	 * 支付失败转支付宝
	 */
	public static final int FIAL_TO_OTHER_PAY = 1030;

	/**
	 * 电信初始化失败
	 */
	public static final int TC_INIT_FAIL = 2000;

	/**
	 * 电信计费点不存在
	 */
	public static final int TC_CODE_ERRO = 2010;

	/**
	 * 电信支付失败
	 */
	public static final int TC_PAY_FAIL = 2040;

	/**
	 * 联通初始化失败
	 */
	public static final int UNICON_INIT_FAIL = 3000;

	/**
	 * 支付索引不对
	 */
	public static final int UNICON_CODE_ERRO = 3010;

	/**
	 * 联通支付失败
	 */
	public static final int UNICON_PAY_FAIL = 3030;

	/**
	 * 破解发送短信失败
	 */
	public static final int NATIVE_FEE_FAIL = 4000;

	/**
	 * 联通wo+破解发送短信失败
	 */
	public static final int UNICON_FEE_FAIL = 4005;

	/**
	 * 天翼空间破解发送短信失败
	 */
	public static final int TC_FEE_FAIL = 4007;

	/**
	 * 破解支付额度不够支付失败
	 */
	public static final int LIMIT_FEE_FAIL = 4010;

	/**
	 * anan初始化失败(走这个消息说明程序有问题了)
	 */
	public static final int ANAN_INIT_FEE = 4500;
	/**
	 * leyou初始化失败(走这个消息说明程序有问题了)
	 */
	public static final int LEYOU_INIT_FEE = 4700;

	/**
	 * 饭盒支付失败
	 */
	public static final int FANHE_PAY_FAIL = 4725;
	/**
	 * 没有此支付类型的sdk（比如发给用户的sdk无电信的，在电信手机上会报此错）
	 */
	public static final int NO_THIS_PAYTYPE = 5000;

	/**
	 * 破解失败 转正常支付
	 */
	public static final int FEE_FAIL_TO_OTHER = 5500;

	/**
	 * 移动基地初始化失败
	 */
	public static final int JD_INIT_FAIL = 6000;
	/**
	 * 移动基地支付失败
	 */
	public static final int JD_PAY_FAIL = 6005;
	

	// public static int[] gameIds =
	// {5234,5158,5183,5230,5233,5235,5236,5237,5238,5239,5240,5241,5242,5243,5244,5246,5247,5248,
	// 5250,5251,5252,5253,5254,5255,5256,5257,5258,5259,5260,5261,5262,5264,5265,5266,5267};
	// public static String[] appIds =
	// {"300009006190","300008962376","300008983707","300008992882","300009006182","300009007513",
	// "300009007534","300009007541","300009007533","300009017538","300009017533","300009017518","300009025587",
	// "300009023862","300008588847","300009103653","300009103640","300009107963","300009115493","300009115497","300009115500",
	// "300009115502","300009115505","300009115509","300009115525","300009117485","300009123534","300009123542","300009117620"
	// ,"300009117621","300009125717","300009123576","300009137507","300009137550","300009137563"};
	public static String[] appIds = { "300009125717", "300009117621","300009006190","300009137563","300009123542",
		"300009153610","300009025587","300009149638","300009183903","300009183910","300009184063",
		"300009183915","300009184069","300009184052","300009184065","300009183591","300009007541","300009189602","300009189599",
		"300009183555","300009190020","300009189673","300009189685","300009194038","300009194036","300009194701","201602175282",
		"902114155562","300009189686","300009189687","300009189688","201603215278","201603235292","201603235283","201603235293",
		"201603285279","201603285287","201603295279","300009006191","201606125305"};//300009189686：拇指游玩自己的联通计费
	public static String[] gameIds = { "5256", "5256", "5234","5259","5259","5259","5242", "5234","5256","5269","5269",
		"5270","5270","5271","5271","5271", "5234","5269","5270","5234","5234","5272","5272","5271","5271","5274","5282",
		"5279","5272","5290","5291","5278","5292","5283","5293",
		"5279","5287","5279","5234","5305"};

	public static Map<String, String> appidMap = new HashMap<String, String>();// 按理来说
																					// 所有的appid
						  															// 都应该在这里配置
	static {
		try {
			for (int i = 0; i < gameIds.length; i++) {
				appidMap.put(appIds[i], gameIds[i]);
			}
			appidMap.put("999999999", "5273");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取特定gameId 特定支付类型的 所有商品信息
	 * 
	 * @param gameId
	 * @param payType
	 * @return 返回商品详情
	 */
	public static ShopInfo getShopInfo(String appId, int payType) {
		if (StringUtil.isEmpty(appId)) return null;
		appId = appId.trim();
		ShopInfo info = null;
		switch (payType) {// 根据支付类型返回 shopInfo
			case UNICOM_MOBLE: {// 联通
				if (appId.equals("300008992882")) {
					int[] prices = { 29 * 100, 9 * 100, 6 * 100, 4 * 100, 29 * 100,
							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 29 * 100 };
					String codes[] = { "001", "015", "003", "004", "005", "006",
							"014", "007", "008", "009", "010", "011", "012", "013" };
					String names[] = { "580钻石", "140钻石", "80钻石", "40钻石",
							"120000金币", "27000金币", "8000金币", "三倍金币", "财神礼包", "复活",
							"体力", "一键升满", "助力礼包", "组合礼包" };
					info = new ShopInfo();
					info.AppId = "300008992882"; // 森林疾跑 少了一个首次复活计费点
					info.AppKey = "C3A04525EA8C8A9E8CF0A04CF3A85657";
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				} else if (appId.equals("300009006190")) {//羊羊去哪儿联通审核三网包
//					info = new ShopInfo();
//					info.AppId = appId;// 直接用 MM的appId
//					info.AppKey = "54A8215239F9968665EF090054BAB175";// 直接用MM的appkey
//					int[] prices = { 15 * 100, 9 * 100, 6 * 100, 4 * 100, 15 * 100,
//							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
//							15 * 100, 10 * 100, 15 * 100, 1 };
//					String codes[] = { "016", "015", "003", "004", "018", "006",
//							"007", "008", "009", "010", "011", "012", "013", "017" };
//					String names[] = { "290钻石", "140钻石", "80钻石", "40钻石", "60000金币",
//							"27000金币", "8000金币", "三倍金币", "财神礼包", "复活", "体力",
//							"一键升满", "助力礼包", "组合礼包" };
//					info.setPriceAlone(prices);
//					info.setCodeMapAlone(codes);
//					info.setItemNameMapAlone(names);
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "54A8215239F9968665EF090054BAB175";// 直接用MM的appkey
					int[] prices = { 15 * 100, 9 * 100, 6 * 100, 4 * 100, 15 * 100,
							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 15 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006",
							"007", "008", "009", "010", "011", "012", "013", "014" };
					String names[] = { "290钻石", "140钻石", "80钻石", "40钻石", "60000金币",
							"27000金币", "8000金币", "三倍金币", "财神礼包", "复活", "体力",
							"一键升满", "助力礼包", "组合礼包" };
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("300009006191")){//羊羊去哪儿含有不用计费点
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "54A8215239F9968665EF090054BAB175";// 直接用MM的appkey
					int[] prices = { 29 * 100, 14 * 100, 6 * 100, 4 * 100, 29 * 100,
							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 29 * 100,9 * 100,15*100,15*100,15*100,15*100};
					String codes[] = { "001", "002", "003", "004", "005", "006",
							"007", "008", "009", "010", "011", "012", "013", "014" ,"015",
							"016","017","018","019"};
					String names[] = { "290钻石", "140钻石", "80钻石", "40钻石", "120000金币",
							"27000金币", "8000金币", "三倍金币", "财神礼包", "复活", "体力",
							"一键升满", "助力礼包", "组合礼包" ,"140钻石","290钻石","组合礼包","6000金币","豪华礼包"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009025587")) {
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "54A8215239F9968665EF090054BAB175";// 直接用MM的appkey
//					int[] prices = { 15 * 100, 10 * 100, 10 * 100, 6 * 100, 4 * 100,
//							8 * 100, 6 * 100, 10 * 100};
//					String codes[] = { "009", "010", "011", "012", "013", "014",
//							"015", "016"};
//					String names[] = { "登陆礼包", "畅玩礼包", "幸运大奖", "生命加3", "生命加1",
//							"暂停道具", "重列道具", "助力礼包" };
					int[] prices = { 15 * 100, 10 * 100, 6 * 100, 15 * 100, 6 * 100,6 * 100,
							8 * 100, 10 * 100, 4 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006",
							"007", "008","009"};
					String names[] = { "登陆礼包", "畅玩礼包", "3体力", "3锤子", "3重列",
							"3暂停", "幸运礼包", "重玩本关" };
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009189673")||appId.equals("300009189685")) {//萌跑三国
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "9021119276220150821165515012300";// 直接用MM的appkey
					int[] prices = { 2000, 1800, 1800, 600, 1000, 1000, 400, 200, 600, 2500, 
					        2000, 600, 2500};
					String codes[] = { "001", "002" , "003", "011", "005", "006",
							"007", "008", "009", "010"};
					String names[] = { "猛将锦囊", "财神锦囊", "粮草锦囊", "超值战斗锦囊", "战斗锦囊", 
					        "道具锦囊", "体力锦囊", "复活", "60钻石", "275钻石" };
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009189686")) {//拇指游玩酷跑天团
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "9021119276220150821165515012300";// 直接用MM的appkey
					int[] prices = { 2000, 1800, 1800, 600, 1000, 1000, 400, 200, 600, 2500, 
					        2000, 600, 2500};
					String codes[] = { "011", "008" , "005", "012", "003", "002",
							"006", "001", "009", "013"};
					String names[] = { "猛将锦囊", "财神锦囊", "粮草锦囊", "超值战斗锦囊", "战斗锦囊", 
					        "道具锦囊", "体力锦囊", "复活", "60钻石", "275钻石" };
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009194701")) {//倒霉熊太空历险记
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "9021119276220150827121643032000";
					int[] prices = { 2 * 100,12* 100,2 * 100, 6 * 100, 18 * 100,
							30 * 100,12 * 100,6 * 100,6 * 100,8 * 100,
							1 * 100,8 * 100,12 * 100,29 * 100,5 * 100};
					String codes[] = { "001", "003", "006", "007", "008", "009", "019", "020",
							"021", "022", "023", "024", "025", "026", "027"};
					String names[] = { "20钻石", "180钻石","复活", "80钻石","280钻石","500钻石","一键满级","一键进阶",
							"库拉卡","公公", "新手礼包","限时优惠","官方推荐","多多的祝福","体力大礼包"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("902114155562")) {//雷电
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "9021119276220150827121643032000";
					int[] prices = { 4 * 100,4* 100, 2 * 100, 2 * 100,
							2 * 100,8 * 100,15 * 100,20 * 100,1 * 100,
							10 * 100,6 * 100, 2*100};
					String codes[] = { "001", "002", "003", "004", "005", "007", "008",
							"009", "010", "011","012"};
					String names[] = { "新战机", "魔装机神","获得必杀", "获得护盾","复活","15元礼包","20元礼包",
							"1元礼包","80000宝石", "体力","宝石礼包"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
			}else if(appId.equals("201603295279")){//雷电创趣（有6号计费点，，但是不用）
				info = new ShopInfo();
				info.AppId = appId;// 直接用 MM的appId
				info.AppKey = "9021119276220150827121643032000";
				int[] prices = { 4 * 100,4* 100, 2 * 100, 2 * 100,
						2 * 100,8 * 100,15 * 100,20 * 100,1 * 100,
						10 * 100,6 * 100, 2*100};
				String codes[] = { "001", "002", "003", "004", "005", "006","007", "008",
						"009", "010", "011","012"};
				String names[] = { "新战机", "魔装机神","获得必杀", "获得护盾","复活","宝石1000","15元礼包","20元礼包",
						"1元礼包","80000宝石", "体力","宝石礼包"};
				info.setPriceAlone(prices);
				info.setCodeMapAlone(codes);
				info.setItemNameMapAlone(names);
			}else if(appId.equals("201603285279")){//雷电特殊处理的一个包(北青)
				info = new ShopInfo();
				info.AppId = appId;// 直接用 MM的appId
				info.AppKey = "9021119276220150827121643032000";
				int[] prices = { 4 * 100,4* 100, 2 * 100, 2 * 100,
						2 * 100,15 * 100,20 * 100,1 * 100,
						10 * 100,6 * 100, 2*100};
				String codes[] = { "001", "002", "003", "004", "005", "006", "007",
						"008", "009", "010","011"};
				String names[] = { "新战机", "魔装机神","获得必杀", "获得护盾","复活","15元礼包","20元礼包",
						"1元礼包","80000宝石", "体力","宝石礼包"};
				info.setPriceAlone(prices);
				info.setCodeMapAlone(codes);
				info.setItemNameMapAlone(names);
			}else if (appId.equals("201602175282")) {
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 18 * 100,15* 100,8 * 100, 15 * 100, 12 * 100,
							12 * 100,10 * 100,15 * 100,15 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008","009"};
					String names[] = { "豪华大礼包", "尊贵大礼包","新手礼包", "体力礼包","爆破道具","刷新道具","魔棒道具","加时间",
							"一键过关"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("300009189687")){
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 20 * 100,18* 100,12 * 100, 10 * 100, 10 * 100,
							10 * 100,8 * 100,4 * 100,2 * 100,6 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008","009","010"};
					String names[] = { "一键升满", "金币礼包","钢球礼包", "推浪礼包","复活","关卡解锁","补充体力","钢球*1",
							"推浪*2","新手礼包"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("300009189688")){
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 15 * 100,15* 100,15 * 100, 15 * 100, 12 * 100,
							10 * 100,10 * 100,10 * 100,12 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008","009"};
					String names[] = { "豪华礼包", "粮草礼包","招财进宝", "角色解锁","关卡解锁","炸弹道具","力量药水","时间道具",
							"复活"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("201603215278")){//萌宠
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 15 * 100,10* 100,6 * 100, 15 * 100, 6 * 100,
							6 * 100,8 * 100,10 * 100,4 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008","009"};
					String names[] = { "登录大礼包", "畅玩大礼包","体力", "助力大礼包","锤子","重列","暂停","幸运礼包",
							"重玩本关"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("201603235292")){//捕鱼
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 20 * 100,15* 100,12 * 100, 10 * 100, 8 * 100,
							6 * 100,4 * 100,2 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008"};
					String names[] = { "大礼包", "金币礼包","畅玩礼包", "技能礼包","道具礼包","炮倍解锁","大份钻石","小份钻石"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("201603235283")){//宅男的春天
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 2 * 100, 4* 100,6 * 100, 8 * 100, 10 * 100,
							12 * 100,15 * 100,20 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008"};
					String names[] = { "2元购买金币", "4元购买金币","6元购买金币", "8元购买金币","10元购买金币","12元购买金币","15元购买金币","20元购买金币"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("201603235293")){//家园
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 2 * 100, 4* 100,6 * 100, 8 * 100, 10 * 100,
							12 * 100,15 * 100,20 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008"};
					String names[] = { "一小堆金币", "一小袋金币","一麻袋金币", "一小箱金币","一大箱金币","一房间金币","一仓库金币","满满的金币"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("")){//厨娘爱消除
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 2 * 100, 4* 100,6 * 100, 8 * 100, 10 * 100,
							12 * 100,15 * 100,20 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008"};
					String names[] = { "一小堆金币", "一小袋金币","一麻袋金币", "一小箱金币","一大箱金币","一房间金币","一仓库金币","满满的金币"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("201603285287")){
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 18 * 100, 15* 100,8 * 100, 15 * 100, 12 * 100,
							12 * 100,10 *100,15 * 100,15 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008","009"};
					String names[] = { "豪华大礼包", "尊贵大礼包","新手礼包", "体力礼包","爆破道具","魔棒道具","刷新道具","加时间","一键过关"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("201606125305")){
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 2 * 100, 4* 100,6 * 100, 8 * 100, 10 * 100,
							12 * 100,15 * 100,20 * 100};
					String codes[] = { "001", "002", "003", "004", "005", "006", "007", "008"};
					String names[] = { "一小堆金币", "一小袋金币","一麻袋金币", "一小箱金币","一大箱金币","一房间金币","一仓库金币","满满的金币"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}
				break;
			}
		}
		return info;
	}
}
