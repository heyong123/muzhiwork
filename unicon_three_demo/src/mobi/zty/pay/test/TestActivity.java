package mobi.zty.pay.test;

import mobi.zty.pay.sdk.PayResultInfo;
import mobi.zty.sdk.game.GameSDK;
import mobi.zty.sdk.game.GameSDKInitListener;
import mobi.zty.sdk.game.GameSDKPaymentListener;
import mobi.zty.sdk.util.Helper;
import mobi.zty.sdk.util.Util_G;
import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.pansen.zumalocal.BuildConfig;
import com.pansen.zumalocal.R;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.test_pay_layout);
		
		final EditText edt = (EditText) findViewById(R.id.index_in);
		findViewById(R.id.testPay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int feeIndex = 0;
				try {
					feeIndex = Integer.parseInt(edt.getText().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				//金额（单位分）、payIndex（支付索引计费文件里面有）、道具名称
				GameSDK.getInstance().startPay(200,feeIndex, "22钻石", new GameSDKPaymentListener() {
					
					@Override
					public void onPayFinished(int mount) {
						float v = mount;
						v = v / 100;
						String str = String.format("支付%2f元成功", v);
//						Util_G.debugE("AllPay->>", "支付成功");
						GameSDK.getInstance().makeToast(str);
					}
					@Override
					public void onPayFail(PayResultInfo info) {
						GameSDK.getInstance().makeToast(info.retMsg);
//						Util_G.debugE("AllPay->>", "支付失败："+info.retMsg);
					}
					@Override
					public void onPayCancelled() {
						GameSDK.getInstance().makeToast("支付取消");
//						Util_G.debugE("AllPay->>", "支付取消");
					}
				});
			}
		});
		
		//"300009194701" 是appid 计费文件里面有
		GameSDK.initSDK(this,"201602175282", new GameSDKInitListener() {
			
			@Override
			public void initOver(boolean arg0, int arg1, boolean arg2) {
				
			}

			@Override
			public void onOpenDark(int arg0, int arg1, int arg2, int arg3,
					int arg4) {
				// TODO Auto-generated method stub
				
			}
		},"","",Helper.getVersionCode(this)+"");//初始化sdk
		
		if (BuildConfig.DEBUG) {
			GameSDK.setDebug(true);
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
}
