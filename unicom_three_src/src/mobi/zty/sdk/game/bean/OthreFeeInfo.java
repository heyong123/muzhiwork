package mobi.zty.sdk.game.bean;

/**
 * 第三方 破解支付通用 计费信息对象
 * @author Administrator
 *
 */
public class OthreFeeInfo {
	public String appID = "";
	public String appKey = "";
	public String ChannelID = "";
	public String[] limitMoneys = null;
	public boolean initSCC = false;
	public String getPayCode(int index){
		String payCode = String.format("%s%02d", appID,index+1);
		return payCode;
	}
	public void initData(String data){
		if (data != null) {
			try {
				String[] str = data.split(";");
				appID = str[0];
				appKey = str[1];
				ChannelID = str[2];
				limitMoneys = str[3].split(",");
				initSCC = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getMoney(int index){
		if (limitMoneys!=null&&index<limitMoneys.length) {
			return Integer.parseInt(limitMoneys[index]);
		}
		return 30000;//如果cp金额传错 就基本不让走 黑包
	}
}
