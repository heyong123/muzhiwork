package mobi.zty.pay.sdk;

import java.util.HashMap;
import java.util.Map;

import mobi.zty.sdk.util.StringUtil;

public class PayConfig {

	/**
	 * 正在支付中
	 */
	public static boolean IS_PAYING = false;
	/**
	 * 移动计费
	 */
	public static final int NOTIFY_PAYRESULT = 0;
	/**
	 * 移动手机(移动MM白包支付也用的这个变量)
	 */
	public static final int CMCC_MOBLE = 1;

	/**
	 * 联通手机(也代表白包支付类型)
	 */
	public static final int UNICOM_MOBLE = 5;
	
	/**
	 * 移动本地破解计费
	 */
	public static final int CMCC_NETIVE_FEE = 2;

	/**
	 * 电信手机(也代表白包支付类型)
	 */
	public static final int TIANYI_MOBLE = 10;

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
	 * 微信支付
	 */
	public static final int WECHAT_PAY = 16;
	/**
	 * 微信支付失败
	 */
	public static final int WECHAT_PAY_FAIL = 1101;
	/**
	 * 微信支付取消
	 */
	public static final int WECAHT_PAY_CANCER = 1102;
	
	/**
	 * 微信支付成功
	 */
	public static final int WECHAT_PAY_SUCCESS = 1103;
	/**
	 * 没有此支付类型的sdk（比如发给用户的sdk无电信的，在电信手机上会报此错）
	 */
	public static final int NO_THIS_PAYTYPE = 5000;
	
	public  final static int WECAHT_SUCC = 6020;
	public  final static int WECAHT_FAIL = 6025;
	public  final static int ALIPAY_RQF_PAY = 6030;

	public static String[] appIds = {
		"300009123542","300009153610","300009025587","300009149638",
		"300009183910","300009184063","300009183915","300009184069",
		"300009184052","300009184065","300009183591","300009189599",
		"300009190020","300009189673","300009189685","300009194038",
		"300009194036","300009370275","300008962376","300008991832",
		"300009209411","300009117621","300009125717","300009123576",
		"300009115525","300009117620","300009117485","300009183555",
		"300009183903","300009006190","300009006182","300008991748",
		"300008991842","300009007541","300008923915","300009017533",
		"300008989464","300008992882","300009115502","300009137563",
		"300009206108","300008991820","300008991832","300008989467",
		"300009296278","300008991718","300008991839","300008945728",
		"300008991738","300008963684","300008979492","300008963695",
		"300008859866","300008859935","300008860058","300008860040",
		"90211415556","300009470256","300009470246","300009470254",
		"300009007533","300008991838","300009648257","300009648258",
		"300008262076","300009189686","300009568216"};//锤妖记的是联通的，
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
		"5275","5279","5278","5282",
		"5233","5233","5280","5280",
		"5280","5272","5272"};
	public static String[] appkeys = { 
		"1DA839BCB30C45C584DB274D0015E162","03B24935332BA767220AA2A39C301078","F3DD5BAAEFF2A28BDBCB615711CA9790","839AEAC27EF7885E5C5DAA7E939E5606",
		"22C5476E89B9C9BCF338EDA0FD25BF51","6787EEBD79B44EC679FE1D69789E6DF3","ED92BA193D3B8FFE5F73CF08C26F01ED","02E9F10E952E717D04888FE26A02EB90",
		"AD4777FD81C11B7B713F1176C09DD73B","A761A7FDEE863FDB31397FDC5BD43296","F4A5494B68EAEB96489A552B651C0940","72B3EF27D8F3194D639B1339E52E985B",
		"66496EFDE1ECD26E4CE43B0E0D51DA4C","1B560D886A7CD9D5D6BC18D6800A306B","DD85CDACE687702C3C85E1638006B530","39A936F3E0580B046F17B2BA8CF65C02",
		"C30FB0246C7FC460F350B999F6F1A7DE","E73A75686C89E16786E84E2E6D213A66","9E7EAD646C6145F4AF0CEFB7B0A4E018","A9A6E8D5A39C995BF64837E600D18595",
		"B215B33D64DDBF04591DACECF3FBF6EB","42AC7297387431C47E943CD36F2507A8","F533E72FBF91CCB7F939A44EF7A21B2A","C13D42FB0BA466FA410B9BA209A5A33C",
		"4F1F37103D1BCF647F2D09648008B07F","604D2DB3F243EF437256A21406EE6B2D","EA2D07CA264818619380D1CD89E9129B","FF809675E979268061BC538360C75C50",
		"1A17E50F778446B373299C887171C655","B09A5C0D48F7C92629FE53F909C475F8","938282D940E2E0BEBA1FEB45CC4B7DB6","102BDEEDC9E6992DA5035BB1270B9CC7",
		"B4F5BD8EA9300C3648B1F8BE33A428AF","CEB13144E40FAAF83A5053D0B42B8A51","AA40F0303576E422FF2FC2006BD7DDE9","76A61D04A83C198914BB4A285010053D",
		"FF102E655BD91C944251AE98DEA6B86D","C3A04525EA8C8A9E8CF0A04CF3A85657","8222C4ED16ADBDC3E08BD2248B040C6C","25E8210926E8D0AC88E72A34DAD9ACDC",
		"4434E1B76B475A36C99E5BA900731AAE","1CA75AD32E179CAB1F931C77615F6A12","A9A6E8D5A39C995BF64837E600D18595","A5B71528346E71017FBE9241EE1E420C",
		"CE5D3E5BF19E58C42894DC8402F37CE4","6862347A7B61C40179BED9456DD26F63","239F8BD7FC3F50E6D1DAA9062A39213B","C9A8D0AA2470090752732D356F60B251",
		"6F860F1030DE9B0C97D9E08B81E81171","D93A3912B4B67A01E79BDD88A2B9894E","C564BC378587C76F7486AFAA915168D5","5CA86D43D894E2F88D7EB7976D0B6A3A",
		"4A6EA52E8C9F11CF3D207DB36CDA54B3","63E151D117C7ED3E365949A3BEF52785","C5D8DB3E78ED34DC861F9664651A81EE","9A64FA3FE7F31DF8AC158B3F055E4A1D",
		"e4a8e6624de894b44238dfdfgfghghgh","1F78244B4BBA3AB17DF0335A6AF01842","61EB0DE06295BF58D2E32C31DC932997","FC8A551B6CDFDAC5315FEB00947A5B8D",
		"5D364BA2A331822A0B1AEA6A5AF78D93","CEB13144E40FAAF83A5053D0B42B8A51","26693EFC97A1FC4C3F3F6950D91BEF72","E19A10E4D1078B885CB8279BFA705552",
		"90446A601AB6F5D1"                ,"DD85CDACE687702C3C85E1638006B530","34611F8EA2A4F5081F16D57C2E78A9CE"};//

	public static Map<String, String> appidMap = new HashMap<String, String>();// 按理来说所有的appid都应该在这里配置
	public static Map<String, String> appidKeyMap = new HashMap<String, String>();// 按理来说所有的appid都应该在这里配置
	static {
		try {
			for (int i = 0; i < gameIds.length; i++) {
				appidMap.put(appIds[i], gameIds[i]);
			}
			for (int i = 0; i < appIds.length; i++) {
				appidKeyMap.put(appIds[i], appkeys[i]);
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
			case CMCC_MOBLE:
			case CMCC_NETIVE_FEE: {// 移动MM的计费
				
				if(appId.equals("300009183552")){//开心跳跳跳 gameID = 5269
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "BD92410F09A3ADB8AD7F29E688D173FD";
					int[] prices = { 100 };
					String codes[] = { "30000918355212" };
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
				}else if(appId.equals("300009183910")){//开心跳跳跳 gameID = 5269
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "22C5476E89B9C9BCF338EDA0FD25BF51";
					int[] prices = { 10 * 100 };
					info.setPrice(prices);
				}else if(appId.equals("300009184063")){
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "6787EEBD79B44EC679FE1D69789E6DF3";
					int[] prices = { 10 * 100 };
					info.setPrice(prices);
				}else if(appId.equals("300009189599")){//蜜蜂侠（畅玩版）gameID = 5270
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "72B3EF27D8F3194D639B1339E52E985B";
					int[] prices = { 10 * 100 ,19 * 100,15 * 100,10 * 100,10 * 100};
					info.setPrice(prices);
				}else if(appId.equals("300009183915")){//蜜蜂侠gameID = 5270
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "ED92BA193D3B8FFE5F73CF08C26F01ED";
					int[] prices = { 10 * 100 ,19 * 100,15 * 100,10 * 100,10 * 100};
					info.setPrice(prices);
				}else if(appId.equals("300009184069")){
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "02E9F10E952E717D04888FE26A02EB90";
					int[] prices = { 10 * 100 ,19 * 100,15 * 100,10 * 100,10 * 100};
					info.setPrice(prices);
				}else if(appId.equals("300009184052")){
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "AD4777FD81C11B7B713F1176C09DD73B";
					int[] prices = { 15 * 100 ,6 * 100,6 * 100,6 * 100,10 * 100};
					info.setPrice(prices);
				}else if(appId.equals("300009184065")){
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "A761A7FDEE863FDB31397FDC5BD43296";
					int[] prices = { 15 * 100 ,6 * 100,6 * 100,6 * 100,10 * 100};
					info.setPrice(prices);
				}else if(appId.equals("300009194038")){//星之恋（萌宠版）
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "39A936F3E0580B046F17B2BA8CF65C02";
					int[] prices = { 15 * 100 ,6 * 100,6 * 100,6 * 100,10 * 100};
					info.setPrice(prices);
				}else if(appId.equals("300009194036")){//星之恋（爱游版）
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "C30FB0246C7FC460F350B999F6F1A7DE";
					int[] prices = { 15 * 100 ,6 * 100,6 * 100,6 * 100,10 * 100};
					info.setPrice(prices);
				}else if(appId.equals("300009183591")){
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "F4A5494B68EAEB96489A552B651C0940";
					int[] prices = { 15 * 100 ,6 * 100,6 * 100,6 * 100,10 * 100};
					info.setPrice(prices);
				}else if (appId.equals("300008962376")||appId.equals("300008923915")) {//gameId5234 特殊处理部分
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 15 * 100, 9 * 100, 6 * 100, 4 * 100, 15 * 100,
							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 15 * 100, 1 };
					String codes[] = { appId+"17", appId+"02", appId+"03", appId+"04", appId+"18", appId+"06",
							appId+"07", appId+"08", appId+"16", appId+"10", appId+"11", appId+"12", 
							appId+"13",appId+"19",appId+"15" };
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
				}else if (appId.equals("300009296278")) {//三国老虎机移动特殊处理的一个包
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 20 * 100, 18 * 100, 18 * 100, 6 * 100, 10 * 100,
							10 * 100, 4 * 100, 2 * 100, 6 * 100, 25 * 100};
					String codes[] = { appId+"04", appId+"08", appId+"05", appId+"07", appId+"03", appId+"02",
							appId+"06", appId+"01", appId+"09", appId+"10"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
				}else if (appId.equals("300009189685")) {//三国移动特殊处理的一个包
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 2 * 100, 10 * 100, 10 * 100, 20 * 100, 18 * 100,
							4 * 100, 6 * 100, 18 * 100, 6 * 100, 25 * 100};
					String codes[] = { appId+"11", appId+"12", appId+"13", appId+"14", appId+"15", appId+"16",
							appId+"17", appId+"18", appId+"19", appId+"20"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
				}else if (appidMap.get(appId).trim().equals("5272")&&appidKeyMap.get(appId).trim().length()>0) {//三国统一用 5272
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 2 * 100, 10 * 100, 10 * 100, 20 * 100, 18 * 100,
							4 * 100, 6 * 100, 18 * 100, 6 * 100, 25 * 100};
					info.setPrice(prices);
				}else if (appId.equals("300009370275")) {//倒霉熊太空历险记
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "E73A75686C89E16786E84E2E6D213A66";
					int[] prices = { 2 * 100, 12 * 100,2 * 100, 6 * 100, 18 * 100,
							30 * 100,12 * 100,6 * 100,6 * 100,8 * 100,
							1 * 100,8 * 100,12 * 100,29 * 100,5 * 100};
					String codes[] = { appId+"01", appId+"02", appId+"03", appId+"04", appId+"05", 
							appId+"06",appId+"07", appId+"08", appId+"09", appId+"10", appId+"11", appId+"12", 
							appId+"13",appId+"14",appId+"15" };
					info.setPrice(prices);
					info.setCodeMapAlone(codes);
				}else if (appId.equals("300009470246")) {//宠物大暴走
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "61EB0DE06295BF58D2E32C31DC932997";
					int[] prices = { 15 * 100, 10 * 100,2 * 100, 15 * 100, 6 * 100,
							6 * 100,8 * 100,10*100,4 * 100};
					String codes[] = { appId+"01", appId+"02", appId+"03", appId+"04", appId+"05", 
							appId+"06",appId+"07", appId+"08", appId+"09" };
					info.setPrice(prices);
					info.setCodeMapAlone(codes);
				}else if (appId.equals("300009470254")) {//厨娘爱消除
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "FC8A551B6CDFDAC5315FEB00947A5B8D";
					int[] prices = { 18 * 100, 15 * 100,8 * 100, 15 * 100, 12 * 100,
							12 * 100,10 * 100,15*100,15 * 100};
					String codes[] = { appId+"01", appId+"02", appId+"03", appId+"04", appId+"05", 
							appId+"06",appId+"07", appId+"08", appId+"09" };
					info.setPrice(prices);
					info.setCodeMapAlone(codes);
				}else if(appId.equals("300009470256")){ //雷电
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "1F78244B4BBA3AB17DF0335A6AF01842";
					int[] prices = { 4 * 100, 4 * 100,2 * 100, 2 * 100, 8 * 100,
							2 * 100,15 * 100,20*100};
					String codes[] = { appId+"01", appId+"02", appId+"03", appId+"04", appId+"05", 
							appId+"06",appId+"07", appId+"08", appId+"09" };
					info.setPrice(prices);
					info.setCodeMapAlone(codes);
				}else if (appidMap.get(appId).trim().equals("5233")&&appidKeyMap.get(appId).trim().length()>0) {//跑酷纯 29元计费用 5233
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 29 * 100, 9 * 100, 6 * 100, 4 * 100, 29 * 100,
							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 29 * 100, 1 };
					info.setPrice(prices);
				}else if (appidMap.get(appId).trim().equals("5159")&&appidKeyMap.get(appId).trim().length()>0) {//跑酷纯 29元计费用 5159（含有首冲礼包19元）
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 29 * 100, 9 * 100, 6 * 100, 4 * 100, 29 * 100,
							9 * 100, 4 * 100, 6 * 100, 19 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 29 * 100, 1 };
					info.setPrice(prices);
				}else if (appidMap.get(appId).trim().equals("5256")&&appidKeyMap.get(appId).trim().length()>0) {// 跑酷纯15元计费用 5256
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 15 * 100, 9 * 100, 6 * 100, 4 * 100, 15 * 100,
							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 15 * 100, 1 };
					String codes[] = { appId+"01", appId+"02", appId+"03", appId+"04", appId+"05", appId+"06",
							appId+"07", appId+"08", appId+"09", appId+"10", appId+"11", appId+"12", 
							appId+"13",appId+"14",appId+"15" };
					info.setPrice(prices);
					info.setCodeMapAlone(codes);
				}else if (appidMap.get(appId).trim().equals("5234")&&appidKeyMap.get(appId).trim().length()>0) {// 跑酷 29和15混合计费筛选 用5234
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 15 * 100, 9 * 100, 6 * 100, 4 * 100, 15 * 100,
							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 15 * 100, 1 };
					String codes[] = { appId+"16", appId+"02", appId+"03", appId+"04", appId+"17", appId+"06",
							appId+"07", appId+"08", appId+"09", appId+"10", appId+"11", appId+"12", 
							appId+"13",appId+"18",appId+"15" };
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
				}else if (appidMap.get(appId).trim().equals("5242")&&appidKeyMap.get(appId).trim().length()>0) {//天降萌宠 统一用5242
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 15 * 100, 10 * 100, 10 * 100, 6 * 100, 4 * 100,
							8 * 100, 6 * 100, 10 * 100, 10, 1};
					String codes[] = { appId+"01", appId+"02", appId+"10", appId+"11", appId+"12", appId+"13",
							appId+"14", appId+"16", appId+"17", appId+"18"};
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
				}else if (appidMap.get(appId).trim().equals("5286")&&appidKeyMap.get(appId).trim().length()>0) {//一触即消 统一用5286
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 18 * 100, 10 * 100, 8 * 100, 6 * 100, 4 * 100,
							3 * 100, 5 * 100, 2 * 100, 1 * 100, 4 * 100, 1 ,1 ,10};
					info.setPrice(prices);
				}else if(appidMap.get(appId).trim().equals("5280")&&appidKeyMap.get(appId).trim().length()>0){//猫咪联盟
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 2 * 100, 4 * 100, 6 * 100, 8 * 100, 10 * 100,
							12 * 100, 15 * 100, 20 * 100 };
					info.setPrice(prices);
				}else if(appId.equals("300008262076")){
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = appidKeyMap.get(appId).trim();
					int[] prices = { 2 * 100, 2 * 100,2 * 100, 4 * 100, 6 * 100, 20 * 100,1};
					info.setPrice(prices);
				}else if(appId.equals("300009568216")){
					info = new ShopInfo();
					info.AppId = appId; 
					info.AppKey = "34611F8EA2A4F5081F16D57C2E78A9CE";
					int[] prices = { 20 * 100, 18 * 100,18 * 100, 6 * 100, 10 * 100,
							10 * 100,4 * 100,2 * 100,6 * 100,25 * 100};
					String codes[] = { appId+"01", appId+"02", appId+"03", appId+"04", appId+"05", 
							appId+"06",appId+"07", appId+"08", appId+"09", appId+"10"};
					info.setPrice(prices);
					info.setCodeMapAlone(codes);
				}
				break;
			}
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
				}else if (appidMap.get(appId).trim().equals("5279")&&appidKeyMap.get(appId).trim().length()>0) {//雷电
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
				}else if (appId.equals("300009370275")) {//倒霉熊太空历险记
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
				}
				break;
			}
			case TIANYI_MOBLE: {// 电信
				if (appId.equals("300008963682")) {
					int prices[] = { 2 * 100, 2 * 100, 8 * 100, 19 * 100, 6 * 100,
							8 * 100 };
					String codes[] = { "5067927", "5083311", "5083312", "5083313",
							"5122407", "5122408" };
					String names[] = { "星座礼包", "22钻石", "125钻石", "380钻石", "金币大礼包",
							"欢乐礼包" };
					info = new ShopInfo();
					info.AppId = "300008963682";
					info.AppKey = "74b0f029affbd539a5aa223936340715";
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				} else if (appId.equals("300009006190") || appId.equals("300008962376")) {//羊羊去哪儿，，
					String codes[] = { "TOOL1", "TOOL2", "TOOL3", "TOOL4", "TOOL5",
							"TOOL6", "TOOL7", "TOOL8", "TOOL9", "TOOL10", "TOOL11",
							"TOOL12", "TOOL13", "TOOL14" };
					int[] prices = { 15 * 100, 9 * 100, 6 * 100, 4 * 100, 15 * 100,
							9 * 100, 4 * 100, 6 * 100, 6 * 100, 2 * 100, 2 * 100,
							15 * 100, 10 * 100, 15 * 100 };
					String names[] = { "290钻石", "140钻石", "80钻石", "40钻石", "60000金币",
							"27000金币", "8000金币", "三倍金币", "首充礼包", "复活", "体力",
							"一键升满", "助力礼包", "组合礼包" };
					info = new ShopInfo();
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009025587")) {
					String codes[] = { "TOOL1", "TOOL2", "TOOL3", "TOOL4", "TOOL5",
							"TOOL9", "TOOL7", "TOOL8" };
					int[] prices = { 15 * 100, 10 * 100, 10 * 100, 6 * 100, 4 * 100,
							8 * 100, 6 * 100, 10 * 100 };
					String names[] = { "登陆礼包", "畅玩礼包", "幸运大奖", "生命加3", "生命加1",
							"暂停道具", "重列道具", "助力礼包"};
					info = new ShopInfo();
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009370275")) {
					int[] prices = { 2 * 100,12* 100,2 * 100, 6 * 100, 18 * 100,
							30 * 100,12 * 100,6 * 100,6 * 100,8 * 100,
							1 * 100,8 * 100,12 * 100,29 * 100,5 * 100};
					String codes[] = { "TOOL1", "TOOL3", "TOOL5", "TOOL6", "TOOL7", "TOOL16", "TOOL17", "TOOL18",
							"TOOL19", "TOOL20", "TOOL21", "TOOL22", "TOOL23", "TOOL24", "TOOL25"};
					String names[] = { "20钻石", "180钻石","复活", "80钻石","280钻石","500钻石","一键满级","一键进阶",
							"库拉卡","公公", "新手礼包","限时优惠","官方推荐","多多的祝福","体力大礼包"};
					info = new ShopInfo();
					info.setPriceAlone(prices);
					info.setCodeMapAlone(codes);
					info.setItemNameMapAlone(names);
				}else if (appId.equals("300009189673")||appId.equals("300009189685")||appId.equals("300009296278")) {//萌跑三国
					info = new ShopInfo();
					info.AppId = appId;// 直接用 MM的appId
					info.AppKey = "9021119276220150821165515012300";// 直接用MM的appkey
					int[] prices = { 20 * 100,18 * 100, 18 * 100, 6 * 100,10 * 100, 10 * 100,  4 * 100,2 * 100,6 * 100};
					String codes[] = { "TOOL10", "TOOL11", "TOOL12", "TOOL13", "TOOL14", "TOOL15"
							, "TOOL16", "TOOL17", "TOOL18"};
					String names[] = { "猛将锦囊", "财神锦囊", "粮草锦囊", "超值战斗锦囊", "战斗锦囊",
							"道具锦囊", "体力锦囊", "复活", "60钻石"};
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
