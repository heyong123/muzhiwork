package mobi.zty.pay.sdk;

import java.util.HashMap;
import java.util.Map;

/**
 * 某个 游戏支付的 所有的数据，如果用户传入的gameID不存在 会走默认的 支付数据
 * @author Administrator
 *
 */
public class ShopInfo{
	public String AppId = "";//默认计费id（只有移动存储了）
	public String AppKey = "";//默认计费key（只有移动存储了）
	public Map<Integer, String>  payCodeMap = new HashMap<Integer, String>();//计费点数组的索引 key从0开始
	public Map<Integer, Integer> pricesMap = new HashMap<Integer, Integer>();//每个商品对应的money 单位分 key从0开始
	public Map<Integer, String> itemNameMap = new HashMap<Integer, String>();//对应商品的名称（移动的 没有存储名称列表）
	/**
	 * 计费点编号需要加上APPID构成完整的计费点
	 */
	private static String mpayItems[] = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15","16", "17", 
			"18", "19", "20", "21" };
	/**
	 * 在给商品列表 赋值价格的同时 也给 payCodeMap赋值了
	 * @param prices
	 */
	public void setPrice(int[] prices){
		if (prices != null) {
			pricesMap.clear();
			for (int i = 0; i < prices.length; i++) {
				pricesMap.put(i, prices[i]);
				payCodeMap.put(i, AppId+mpayItems[i]);
			}
		}
	}
	/**
	 * 只给商品列表 价格赋值
	 * @param prices
	 */
	public void setPriceAlone(int[] prices){
		if (prices != null) {
			pricesMap.clear();
			for (int i = 0; i < prices.length; i++) {
				pricesMap.put(i, prices[i]);
			}
		}
	}
	
	/**
	 *专门给 计费点 集合赋值
	 * @param codes
	 */
	public void setCodeMapAlone(String[] codes){
		if (codes!=null) {
			payCodeMap.clear();
			for (int i = 0; i < codes.length; i++) {
				payCodeMap.put(i, codes[i]);
			}
		}
	}
	
	/**
	 * 专门给商品列表 名称集合赋值
	 * @param names
	 */
	public void setItemNameMapAlone(String[] names){
		if (names!=null) {
			itemNameMap.clear();
			for (int i = 0; i < names.length; i++) {
				itemNameMap.put(i, names[i]);
			}
		}
	}
}
