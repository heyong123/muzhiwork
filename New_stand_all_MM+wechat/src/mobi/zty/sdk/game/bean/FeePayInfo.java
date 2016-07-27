package mobi.zty.sdk.game.bean;

import java.util.ArrayList;
import java.util.List;

import mobi.zty.sdk.util.Util_G;
import android.util.Log;

/**
 * 该类用于管理 服务端激活响应 mkurl传过来的内容
 * @author Administrator twl
 *payUrl = strApkPublicKey+";"+strAppKey+";"+md5+";"+strProID+";"+strChannelID+";"
 *+app_id+";"+payList+";"+paydata.getPackageName()+";"+paydata.getMainActivity();
 */
public class FeePayInfo {
	public String strApkPublicKey = "";
	public String strAppKey = "";
	public String md5 = "";
	public String md5dex = "";
	public String strProID = "";
	public String strChannelID = "";
	public String app_id = "";
	public String[] payList ;
	public String packegename = "";
	public String mainActivity = "";
	public boolean initSuccess = false;
	public int getFeeConunt (int payIntdex,int money){
		if (payIntdex<0||payIntdex>=payList.length) {
			boolean exist = false;
			for (int i = 0; i < payList.length; i++) {
				if (payList[i].equals(money+"")) {
					payIntdex = i;
					exist = true;
					break;
				}
			}
			if (!exist) {
				payIntdex = 0;
			}
		}
		return Integer.parseInt(payList[payIntdex]);
	}
	public void initData(String data){
		try {
			String[] strings = data.split(";");
			this.strApkPublicKey = strings[0];
			this.strAppKey = strings[1];
			this.md5 = strings[2];
			this.md5dex = strings[3];
			this.strProID = strings[4];
			this.strChannelID = strings[5];
			this.app_id = strings[6];
			payList = strings[7].split(",");
			this.packegename = strings[8];
			this.mainActivity = strings[9];
			Log.e("FeePayInfo", "data==="+data);
			initSuccess = true;
		} catch (Exception e) {
			Util_G.debugE("FeePayInfo", e.getMessage());
		}
	}
}
