package mobi.zty.pay.sdk;

import android.content.Context;
import android.util.Log;

public abstract class PaymentInterf {

	public abstract void init(Context context,Object...parameters);

	public abstract void pay(Context context,Object...parameters);
	
	public void notifyPay(int type){//这个方法 只有破解的时候才有用
		
	}

}
