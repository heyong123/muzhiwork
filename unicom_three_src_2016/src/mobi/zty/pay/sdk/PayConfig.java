package mobi.zty.pay.sdk;

import java.util.HashMap;
import java.util.Map;

import android.media.audiofx.Equalizer;
import mobi.zty.sdk.util.StringUtil;


public class PayConfig {
	/**
	 * 支付通知总接口 消息号
	 */
	public static final int NOTIFY_PAYRESULT = 0;
	
	/**
	 * 移动手机(移动MM白包支付也用的这个变量)
	 */
	public static final int CMCC_MOBLE = 1;
	/**
	 * 移动本地破解计费
	 */
	public static final int CMCC_NETIVE_FEE = 2;
	
	/**
	 * 不支持的运营商
	 */
	public static final int NO_THIS_PAY = 15;

	/**
	 * 移动MM安安破解
	 */
	public static final int ANAN_FEE = 4;
	
	/**
	 * 移动MM乐游破解
	 */
	public static final int LEYOU_FEE =5;
	
	/**
	 * 联通手机
	 */
	public static final int UNICOM_MOBLE = 5;
	
	/**
	 * 十分科技 联通wo+破解
	 */
	public static final int UNICOM_FEE =6;
	/**
	 * 电信手机
	 */
	public static final int TIANYI_MOBLE = 10;
	
	/**
	 * * 接入天翼空间的破解支付
	 */
	public static final int TC_FEE = 7;
	
	
	/**
	 * 电信全网支付
	 */
	public static final int TC_ALL_FEE = 9;
	
	/**页游
	 */
	public static final int YEYOU_FEE = 11;
	
	/**
	 * 乐动支付(线下暂不接入)
	 */
	public static final int LEDONG_PAY = 12;
	
	/**
	 * 饭盒支付
	 */
	public static final int FANHE_PAY = 13;
	/**
	 * 微信支付类型
	 */
	public static final int WECHAT_PAY = 16;
	/**
	 * 东风 支付类型（支付 后台必须给  需要拦截验证码中的内容）
	 */
	public static final int DONGFENG_PAY = 18;
	/**
	 * 易迅支付类型
	 */
	public static final int YIXUN_PAY = 19;
	/**
	 * 动漫包月
	 */
	public static final int DONGMA_PAY = 20;
	
	
	/**
	 * 服务端支撑的 可动态更新的所有支付 对应的class key
	 */
	public static final int GENERA_KEY = 35;
	/**
	 * 掌龙支付
	 */
	public static final int ZHANGLONG_PAY = 39;
	
	/**
	 * 支付成功
	 */
	public static final int BIIL_SUCC = 1010;
	
	/**
	 * 支付超时后 用来判断结果    
	 * 
	 */
	public static final int NATIVE_FEE_FAIL = 4000;
	
	/**
	 * 短信发送失败
	 */
	public static final int SEND_MSG_FAIL = 4001;
	
	/**
	 * 直接发货
	 */
	public static final int GIVE_ITEM_BEFIN = 4003;
	
	/**
	 * 重复点击支付 取消
	 */
	public static final int PAY_REPEAT_CANER= 4005;
	
	/**
	 * 客户端代码支付报错
	 */
	public static final int PAY_EXPTION_FAIL = 4200;
	
	/**
	 *代码提供方 sdk执行支付逻辑时 抛异常
	 */
	public static final int SP_PAY_EXPTION = 4201;
	
	/**
	 * 代码提供方 sdk初始化失败(可能是后台参数配置错误导致)
	 */
	public static final int SP_INIT_FAIL = 4205;
	
	/**
	 * 代码提供方 sdk支付回调 告知失败
	 */
	public static final int SP_PAY_FAIL = 4210;
	
	/**
	 * 本地sdk初始化 未成功
	 */
	public static final int SDK_INIT_FAIL = 5010;
	
	/**
	 *含有回调的计费方式，超时视为成功 实际没有成功
	 */
	public static final int YIMEROUT_SCC_PAY = 5030; 
	/**
	 * 微信支付失败
	 */
	public static final int WECHAT_PAY_FAIL = 6015;
	
	public  final static int WECAHT_SUCC = 6020;
	public  final static int WECAHT_CANCER = 6021;
	public  final static int WECAHT_FAIL = 6025;
	
	/**
	 * 后台未返回 任何可用计费
	 */
	public  final static int NO_FEE_FAIL = 10005;
	
	/**
	 * 后台返回的 计费组合 某个计费点 为空
	 */
	public  final static int FEE_ISNULL = 10010;
	
	/**
	 * 后台返回计费响应，整体解析失败不能往下执行支付
	 */
	public  final static int FEE_RESPONSE_FAIL= 10015;
	
	/**
	 *解析后台 响应回来的计费信息 报异常
	 */
	public  final static int FEE_RESPONSE_EXCEPTION= 10018;
	
	/**
	 * 服务端告知 前端执行发送短信，可未给对应的短信信息
	 */
	public final static int SMS_INFO_NULL = 10020;
	
	/**
	 *后台未给 本地支付所需的  计费信息
	 */
	public final static int SDK_INFO_NULL = 10025;
	
	/**
	 *后台给的计费信息 前端解析 出现问题 最后未拿到可用计费
	 */
	public final static int FEE_PARSE_FAIL = 10030;
	
	/**
	 * 移动mm基地白包支付类型
	 */
	public static final int JD_PAY = 35;

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
	 * 移动基地初始化失败
	 */
	public static final int JD_INIT_FAIL = 6000;
	/**
	 * 移动基地支付失败
	 */
	public static final int JD_PAY_FAIL = 6005;
	
	public static String[] appIds = {
		"300009123542","300009153610","300009025587","300009149638",
		"300009183910","300009184063","300009183915","300009184069",
		"300009184052","300009184065","300009183591","300009189599",
		"300009190020","300009189673","300009189685","300009194038",
		"300009194036","300009194701","300008962376","300008991832",
		"300009209411","300009117621","300009125717","300009123576",
		"300009115525","300009117620","300009117485","300009183555",
		"300009183903","300009006190","300009006182","300008991748",
		"300008991842","300009007541","300008923915","300009017533",
		"300008989464","300008992882","300009115502","300009137563",
		"300009206108","300008991820","300008991832","300008989467",
		"300009296278","300008991718","300008991839","300008945728",
		"300008991738","300008963684","300008979492","300008963695",
		"300008859866","300008859935","300008860058","300008860040",
		"90211415556" ,"90211415557" ,"201606025272","201606205304"};//锤妖记的是联通的，90211415557：暂时没有appid,雷电
	//暂时把羊羊去哪15元这套计费的 gameid写成5256
	//暂时把羊羊去哪含有29元和15混合这套计费的 gameid写成5234
	//暂时把羊羊去哪只有29元这套计费的 gameid写成5233
	public static String[] gameIds = {
		"5242","5242","5242","5256",
		"5269","5269","5270","5270",
		"5271","5271","5271","5270",
		"5256","5272","5272","5271",
		"5271","5274","5234","5233",
		"5272","5256","5256","5256",
		"5256","5256","5256","5256",
		"5256","5234","5234","5233",
		"5233","5234","5234","5233",
		"5233","5233","5242","5242",
		"5256","5233","5233","5233",
		"5272","5233","5233","5159",
		"5233","5159","5233","5159",
		"5286","5286","5286","5286",
		"5275","5279","5272","5304"};

	public static Map<String, String> appidMap = new HashMap<String, String>();// 按理来说所有的appid都应该在这里配置

	static {
		try {
			for (int i = 0; i < gameIds.length; i++) {
				appidMap.put(appIds[i], gameIds[i]);
			}
			appidMap.put("300009183552", "5256");
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
		if (StringUtil.isEmpty(appId)&&!appidMap.containsKey(appId)) return null;
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
				} else if (appId.equals("300009006190") || appId.equals("300008962376")) {
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "54A8215239F9968665EF090054BAB175";// 直接用MM的appkey
					int[] prices = { 15 * 100, 9 * 100, 6 * 100, 4 * 100, 15 * 100,
							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 15 * 100, 1 };
//					String codes[] = { "016", "015", "003", "004", "018", "006",
//							"007", "008", "009", "010", "011", "012", "013", "017" };
					String codes[] = { "001", "002", "003", "004", "005", "006",
							"007", "008", "009", "010", "011", "012", "013", "014" };
					String names[] = { "290钻石", "140钻石", "80钻石", "40钻石", "60000金币",
							"27000金币", "8000金币", "三倍金币", "财神礼包", "复活", "体力",
							"一键升满", "助力礼包", "组合礼包" };
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009025587")) {
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "54A8215239F9968665EF090054BAB175";// 直接用MM的appkey
					int[] prices = { 15 * 100, 10 * 100, 10 * 100, 6 * 100, 4 * 100,
							8 * 100, 6 * 100, 10 * 100};
					String codes[] = { "009", "010", "011", "012", "013", "014",
							"015", "016"};
					String names[] = { "登陆礼包", "畅玩礼包", "幸运大奖", "生命加3", "生命加1",
							"暂停道具", "重列道具", "助力礼包" };
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009296278")) {//老虎机特殊处理的一个联通包
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
				}else if (appId.equals("90211415556")) {//锤妖记
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "9021119276220150821165515012300";// 直接用MM的appkey
					int[] prices = { 15*100, 20*100, 10*100, 8*100, 2*100, 4*100, 6*100};
					String codes[] = { "001", "002" , "003", "004", "005", "006",
							"007"};
					String names[] = { "豪华大礼包", "超级大礼包", "锤子大礼包", "护盾大礼包", "垃圾场关卡解锁", 
					        "糖果铺关卡解锁", "无线模式解锁"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appidMap.get(appId).trim().equals("5279")) {//雷电
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "9021119276220150821165515012300";// 直接用MM的appkey
					int[] prices = { 15*100, 20*100, 10*100, 8*100, 2*100, 4*100, 6*100};
					String codes[] = { "001", "002" , "003", "004", "005", "006",
							"007"};
					String names[] = { "豪华大礼包", "超级大礼包", "锤子大礼包", "护盾大礼包", "垃圾场关卡解锁", 
					        "糖果铺关卡解锁", "无线模式解锁"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009189673")||appId.equals("300009189685")) {//萌跑三国
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "9021119276220151015144443221600";// 直接用MM的appkey
					int[] prices = { 20 * 100,18 * 100, 18 * 100, 6 * 100,10 * 100, 10 * 100,  4 * 100,2 * 100,6 * 100,25 * 100,
							20*100,6*100,25*100};
					String codes[] = { "011", "008", "005", "012", "003",
							"002", "006", "001", "009","013"};
					String names[] = { "猛将锦囊", "财神锦囊", "粮草锦囊", "超值战斗锦囊", "战斗锦囊",
							"道具锦囊", "体力锦囊", "复活", "60钻石" , "275钻石"};
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
				}else if(appId.equals("201606025272")){//三国酷跑天团
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 2 * 100, 10* 100,10 * 100, 20 * 100, 18 * 100,
							4 * 100,6 * 100,18 * 100,6 * 100, 25 * 100, 20 * 100, 6*100, 25 * 100};
					String codes[] = { "001", "002", "003", "004", "005",
							"006", "007", "008", "009", "010", "011", "012", "013"};
					String names[] = { "复活", "道具精囊","战斗精囊", "红颜精囊","粮草精囊",
							"体力精囊","助力精囊","财神精囊", "60钻石", "288钻石", "猛将精囊", "超值战斗精囊", "275钻石"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if(appId.equals("201606205304")){//电玩捕鱼达人
					info = new ShopInfo();
					info.AppId = appId;// 没有MM的appID
//					info.AppKey = "9021122219520160216175541592400";
					int[] prices = { 2 * 100, 15* 100,4 * 100, 6 * 100, 8 * 100,
							20 * 100,10 * 100};
					String codes[] = { "001", "002", "003", "004", "005",
							"006", "007"};
					String names[] = { "新手礼包", "超值礼包","小堆金币", "中堆金币","大堆金币",
							"特大堆金币","技能补充礼包"};
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
