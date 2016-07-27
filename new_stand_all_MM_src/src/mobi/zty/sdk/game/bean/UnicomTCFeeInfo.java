package mobi.zty.sdk.game.bean;

import mobi.zty.sdk.util.Helper;

public class UnicomTCFeeInfo {
	/**
	 * 商户号（商户唯一标识）
	 */
	public String merid;
	/**
	 * 请求订单的Url（平台给我们分配的）
	 */
	public String pay_url;
	/**
	 * 消息回送地址(支付结果回调地址 我们给平台的)
	 */
	public String notifyurl;
	public String signKey;
	/**
	 * 签名算法类型
	 */
	public String signtype;
	
	/**
	 * 游戏名称
	 */
	public String gameName;
	
	/**
	 * 商品名称列表
	 */
	public String itemNames[] = null;
	/**
	 * 商品价格列表
	 */
	public String itemfees[] = null;
	
	/**
	 * 商品号列表
	 */
	public String goodsids[] = null;
	
	public boolean initSCC =false;
	
	public String getGoodsIds(int index){
		if (index<0||index>=goodsids.length) {
			return null;
		}
		String goodsId = goodsids[index];
		return goodsId;
	}
	public int getItemMoney(int index){
		if (index<0||index>=itemfees.length) {
			return 0;
		}
		String fee = itemfees[index];
		return Integer.parseInt(fee);
	}
	/**
	 * eg:{商户号（商户唯一标识）;请求订单的Url（平台给我们分配的）;消息回送地址(支付结果回调地址 我们给平台的);签名算法类型;密钥;
	 * 商品名称列表;商品价格列表;商品号列表;游戏名称)}
	 * @param data
	 */
	public void initData(String data){
		initSCC = false;
		if (data != null) {
			try {
				String[] str = data.split(";");
				merid = str[0];
				pay_url = str[1];
				notifyurl = str[2];
				signtype = str[3];
				signKey = str[4];
				itemNames = str[5].split(",");
				itemfees = str[6].split(",");
				goodsids = str[7].split(",");
				gameName = str[8];
				initSCC = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
