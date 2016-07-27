package mobi.zty.sdk.game;

public class Constants {
	//网游
	//1.31.1,增加财付通支付，修改公告第一次不能显示问题
	//1.32.1,增加游戏退出广告界面
	//1.33.1,增加增加银联支付
	//1.34.1,升级MM支付3。0
    //public static final String GAME_SDK_VERSION = "1.34.1";//1.20.1加跑马灯效果
    
    //单机
	//1.01.2
	//1.02.2去掉读程序ID功能 
	//1.03.2修改破解参数 
	//1.04.2修改渠道号规则、修改透传参数、计费点切换功能、在线时长统计
	//1.05.2重构后的新版本
	//1.06.2加入三网后 升级版本号
    public static final String GAME_SDK_VERSION = "1.06.2";
    public static final String STAND_ALONE_MK = "2";

    //public static final int DEBUG = 1;
    //private static final String PACKETID="1";
   // public static final String PACKETID="001001";
    //public static final String GAMEID="002";
    public static String SERVER_URL = "http://sa.91muzhi.com:8080/sdk/%s";//单机服务器
   
//    public static String SERVER_URL = "http://gm.91muzhi.com:8080/sdk/%s";//广州
//    public static String SERVER_URL = "http://sa.91muzhi.com:8090/sdk/%s";//深圳,测试
    //public static int STAND_ALONE = 0;//单机版
    //public static String  SERVER_URL = "http://192.168.0.213:8080/sdk/%s";
//    public static String  OSERVER_URL = SERVER_URL;
    
    public static final String COMMON_MM = "0";
    public static final String NATIVE_FEE = "1";
    public static final String NATIVE2_FEE = "2";
    public static final String NET_FEE = "3";
    public static final String ANAN_FEE = "4";
    public static final String LEYOU_FEE = "5";
    public static final String UNICOM_FEE = "6";
    public static final String TC_FEE = "7";
    public static final String MM_JD_PAY = "8";
    public static final String TC_ALL_FEE = "9";
    
    public static final String ALI_PAY = "15";
    public static final String WECHAT_PAY = "16";
    
    /**
     * 饭盒支付
     */
    public static final String FANHE_PAY = "13";
    public static final String TAG = "GameSDK";
    public static final String DEVICE_ID = "device_id";
    public static final String URL = "url";
    public static final String DisCon = "discon";
    public static final String DisCon2 = "discon2";
    public static final String DisUrl = "disurl";//登录公告，
    public static final String MKUrl = "MKUrl";
    public static final String LMoney = "LMoney";
    
    public static final String LOGIN_ACCOUNT = "login_account";
    public static final String PASSWORD = "password";
    
    static String dataPath = "/sdcard/mzyw.data";
    public static final String AMOUNT = "amount";
    public static final String MAX_AMOUNT = "max_amount";
    
    public static int ERROR_CODE_NET = -2;
    public static int ERROR_CODE_SYS = -1;

}
