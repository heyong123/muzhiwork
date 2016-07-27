package mobi.zty.sdk.game.bean;
/**
 * 支付所需要的下行短信的信息
 * @author Administrator
 *
 */
public class DownSmsInfo {


	/**
	 * 回传下行短信的url
	 */
	public String down_url = "";
	/**
	 * 下行短信的下行端口号
	 */
	public String down_num = "";
	/**
	 * 下行短信的关键字
	 */
	public String[] down_content ;
}
