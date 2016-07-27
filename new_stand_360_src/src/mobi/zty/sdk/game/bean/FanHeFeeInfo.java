package mobi.zty.sdk.game.bean;

import mobi.zty.sdk.util.Util_G;

/**
 * 联通饭盒计费信息对象
 * @author Administrator
 *
 */
public class FanHeFeeInfo {
	public boolean initSCC = false;
	public long limitMoney = 20000;
	public String[] payCodes = null;
	public String[] itemMoneys = null;
	public String[] itemNames = null;
	public String appPayID = "";
	public String getPayCode(int index){
		String payCode = "";
		if (payCodes!=null&&payCodes.length>index) {
			payCode = payCodes[index];
		}
		return payCode;
	}
	public String getItemName(int index){
		String name = "";
		if (itemNames!=null&&itemNames.length>index) {
			name = itemNames[index];
		}
		return name;
		
	}
	public void initData(String data){
		Util_G.debugE("OthreFeeInfo","OthreFeeInfo=="+data);
		if (data != null) {
			try {
				String[] str = data.split(";");
				payCodes = str[0].split(",");
				itemMoneys = str[1].split(",");
				itemNames = str[2].split(",");
				initSCC = true;
				Util_G.debugE("FanHeFeeInfo","FanHeFeeInfo initSuccs");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getItemMoney(int index){
		if (itemMoneys!=null&&index<itemMoneys.length) {
			Util_G.debugE("OthreFeeInfo","支付索引=="+index+"  itemMoneysSize=="+itemMoneys.length);
			return Integer.parseInt(itemMoneys[index]);
		}
		return 30000;//如果cp金额传错 就基本不让走 黑包
	}
}
