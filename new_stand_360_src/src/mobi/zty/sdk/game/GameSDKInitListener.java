package mobi.zty.sdk.game;

public interface GameSDKInitListener {
	/**
	 * @param openDark 1是打开支付模糊描述 0正常状态不变
	 * @param openAlert 1:主动支付弹框打开 0主动弹框关闭
	 * @param openButton 0：按钮购买按钮 1：确认按钮 2：领取按钮
	 * @param openOurAlert 0：关闭二次确认框 1：打开二次确认框
	 * @param cootype 0:线上弹框逻辑 1线下
	 */
	public void onOpenDark(int openDark,int openAlert,int openButton, int openOurAlert,int cootype);
	/**
	 * 获取支付配置成功后 的回调
	 * @param isOver 只代表获取到了计费数据
	 * @param payWay 支付类型
	 * @param openVoice 有些sdk会要求控制音效 如mm基地
	 */
	public void initOver(boolean isOver,int payWay,boolean openVoice);
}
