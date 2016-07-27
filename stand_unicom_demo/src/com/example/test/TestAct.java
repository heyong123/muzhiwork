package com.example.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mzyw.sgkptt.R;
import com.unicom.dcLoader.Utils;
import com.unicom.dcLoader.Utils.UnipayPayResultListener;

public class TestAct extends ListActivity {
	private static Handler mHandler = new Handler(Looper.getMainLooper());
	private static Activity mAcitivity;
	private static String cpProductName;
	private static String cpOrderID;
	
	private static UnipayPayResultListener offLineListener = new UnipayPayResultListener() {
		
		@Override
		public void PayResult(String arg0, int arg1, int arg2, String arg3) {
			switch (arg1) {
			case 1://success
				//此处放置支付请求已提交的相关处理代码
				showPayResultOffLine(cpProductName + " " + "支付成功");
				break;

			case 2://fail
				//此处放置支付请求失败的相关处理代码
				showPayResultOffLine(cpProductName + " " + "支付失败");
				break;
				
			case 3://cancel
				//此处放置支付请求被取消的相关处理代码
				showPayResultOffLine(cpProductName + " " + "支付取消");
				break;
				
			default:
				showPayResultOffLine(cpProductName + " " + "支付结果未知");
				break;
			}
		}
	};
	
	private static UnipayPayResultListener OnLineListener = new UnipayPayResultListener() {
		
		@Override
		public void PayResult(String arg0, int arg1, int arg2, String arg3) {
			switch (arg1) {
			case 1://success
				//此处放置支付请求已提交的相关处理代码
				showPayResultOnLine(cpProductName + " " + "支付请求已提交");
				break;

			case 2://fail
				//此处放置支付请求失败的相关处理代码
				showPayResultOnLine(cpProductName + " " + "支付失败");
				break;
				
			case 3://cancel
				//此处放置支付请求被取消的相关处理代码
				showPayResultOnLine(cpProductName + " " + "支付取消");
				break;
				
			default:
				showPayResultOnLine(cpProductName + " " + "支付结果未知");
				break;
			}
		}
	};
		
	String test[] = new String[] {
			"单机道具0.1元", 
			"单机道具30元", 
			"单机道具40元", 
			"联网道具0.1元", 
			"联网道具30元", 
			"联网道具40元", 
			"联网道具500元", 
			"联网道具600元", 
			"按次代缴订购", 
			"按次代缴退订",  
			"单机关卡0.1元", 
			
			"更多游戏", 
			"检查sim卡" 
		};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAcitivity = this;

		getListView().setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, test));

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				//记录当前购买物品名称
				cpProductName = test[position];
				
				switch (position) {

				case 0:
					payOffLine("001");
					break;

				case 1:
					payOffLine("020");
					break;

				case 2:
					payOffLine("021");
					break;
				
				case 3:
					payOnLine("001");
					break;

				case 4:
					payOnLine("020");
					break;

				case 5:
					payOnLine("021");
					break;

				case 6:
					payOnLine("022");
					break;

				case 7:
					payOnLine("023");
					break;

				case 8:
					anCiDaiJiaoSubscribe("041");
					break;

				case 9:
					anCiDaiJiaoUnsubscribe("041");
					break;

				case 10:
					payOffLine("046");
					break;


				case 11:
					Utils.getInstances().MoreGame(TestAct.this);
					break;
					
				case 12:
					String text = "" + Utils.getInstances().getSimType(TestAct.this);
					Toast.makeText(TestAct.this, text, Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		Utils.getInstances().onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Utils.getInstances().onPause(this);
	}
	
	/**
	 * 单机类支付接口封装， 用handler主要是为了保证pay接口在UI线程上进行调用。
	 * 建议在Cocos2dx 中，调用java代码时，这样处理一下，保证UI线程。
	 * @param code 计费点编码， 三位， 具体参考开发者帮助文档。
	 */
	public static void payOffLine(final String code){
		mHandler.post(new Runnable() {
			public void run() {
				Utils.getInstances().pay(mAcitivity, code, offLineListener);
			}
		});
	}
	
	/**
	 * 联网支付接口封装， 用handler主要是为了保证pay接口在UI线程上进行调用。
	 * @param code 计费点编码， 三位， 具体参考开发者帮助文档。
	 */
	public static void payOnLine(final String code){
		mHandler.post(new Runnable() {
			public void run() {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				Utils.getInstances().payOnline(mAcitivity, code, "0", dateFormat.format(new Date()) + "2222222222", OnLineListener);
			}
		});
	}
	
	/**
	 * 按次代缴订购
	 * @param code 计费点编码， 三位， 具体参考开发者帮助文档。
	 */
	public static void anCiDaiJiaoSubscribe(final String code){
		mHandler.post(new Runnable() {
			public void run() {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				Utils.getInstances().payOnline(mAcitivity, code, "1", dateFormat.format(new Date()) + "2222222222", OnLineListener);
			}
		});
	}
	
	/**
	 * 按次代缴退订
	 * @param code 计费点编码， 三位， 具体参考开发者帮助文档。
	 */
	public static void anCiDaiJiaoUnsubscribe(final String code){
		mHandler.post(new Runnable() {
			public void run() {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				Utils.getInstances().payOnline(mAcitivity, code, "2", dateFormat.format(new Date()) + "2222222222", OnLineListener);
			}
		});
	}

	protected static void showPayResultOffLine(final String result) {
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new Builder(mAcitivity);
				builder.setMessage(result);
				builder.setTitle("单机支付结果");
				builder.setIcon(R.drawable.ic_launcher);

				builder.setPositiveButton("ok",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.dismiss();
							}
						});

				builder.setCancelable(false);
				builder.create().show();
			}
		});
	}
	
	protected static void showPayResultOnLine(final String result) {
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new Builder(mAcitivity);
				builder.setMessage(result);
				builder.setTitle("联网支付结果");
				builder.setIcon(R.drawable.ic_launcher);

				builder.setPositiveButton("ok",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.dismiss();
							}
						});

				builder.setCancelable(false);
				builder.create().show();
			}
		});
	}
	
	protected void showAlertDialog(final String result) {
		AlertDialog.Builder builder = new Builder(mAcitivity);
		builder.setMessage(result);
		builder.setTitle("警告");
		builder.setIcon(R.drawable.ic_launcher);

		builder.setPositiveButton("ok",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});

		builder.setCancelable(false);
		builder.create().show();
	}
	
	class GetOrderIdAsyncTask extends AsyncTask<String, String, String> {

		private String url = "http://210.22.123.94:18081/cpapp/order/register";
		private String serviceId = "";
		private ProgressDialog dialog;
		private Context context;
		private String payMethod;
		
		HttpService httpService = new HttpService();
		
		public GetOrderIdAsyncTask(Context context, String productId, String payMethod) {
			this.context = context;
			this.serviceId = productId;
			this.payMethod = payMethod;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(context, "提示", "正在申请订单...");
			dialog.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... args) {
			
			JSONObject object = new JSONObject();
			
			try {
				object.put("payAcct", getImsi());
				object.put("macAddress", getDeviceMac());
				object.put("imei", getImei());
				object.put("productid", serviceId);
				object.put("appversion", getVersionName());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return httpService.httpQueryPost(url, object.toString());
		}

		@Override
		protected void onPostExecute(String message) {
			dialog.dismiss();
			
			JSONObject object;
			try {
				object = new JSONObject(message);
				
				cpOrderID = object.optString("orderid");
				
				if(!TextUtils.isEmpty(cpOrderID)){
//					if(payMethod.equals("payOnline")){
//						Utils.getInstances().payOnline(context, serviceId, "0", cpOrderID, new OnlinePaylistener());
//					}else if(payMethod.equals("payOnlineWithWostre")){
//						Utils.getInstances().payOnlineWithWostre(context, serviceId, "0", cpOrderID, new OnlinePaylistener());
//					}else if(payMethod.equals("payOnline1")){//按此代缴订购
//						Utils.getInstances().payOnlineWithWostre(context, serviceId, "1", cpOrderID, new OnlinePaylistener());
//					}else if(payMethod.equals("payOnline2")){//按此代缴订购
//						Utils.getInstances().payOnlineWithWostre(context, serviceId, "2", cpOrderID, new OnlinePaylistener());
//					}
				}else{
					showAlertDialog("订单申请失败");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				showAlertDialog("服务器响应异常");
			}
		}
	}

	class OrderResultAsyncTask extends AsyncTask<String, String, String> {

		private String url = "http://210.22.123.94:18081/cpapp/order";
		private Context context;
		private ProgressDialog dialog;
		
		HttpService httpService = new HttpService();
		
		public OrderResultAsyncTask(Context context) {
			this.context = context;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(context, "提示", "正在查询支付结果...");
			dialog.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... args) {
			
			JSONObject object = new JSONObject();
			try {
				object.put("orderid", cpOrderID);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return httpService.httpQueryPost(url, object.toString());
		}

		@Override
		protected void onPostExecute(String message) {
			dialog.dismiss();
			
			JSONObject object;
			try {
				object = new JSONObject(message);
				
				int state = object.optInt("state", -2);
				
				switch(state){
				case -2:
					showAlertDialog("订单不存在或者过期");
					break;
				case -1:
					showAlertDialog("支付中");
					break;
				case 0:
					showAlertDialog("支付成功");
					break;
				case 1:
					showAlertDialog("支付失败");
					break;
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				showAlertDialog("服务器响应异常");
			}
		}
	}
	
	private String getImsi() {	
		TelephonyManager mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = mTelephonyMgr.getSubscriberId();
		if(imsi == null){
			return "";
		}
		Log.d("unipaysdk", "IMSI "+imsi);
		return imsi;
	}
	
	private String getImei() {
		
		TelephonyManager mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = mTelephonyMgr.getDeviceId();
		if(imsi == null){
			return "";
		}
		Log.d("unipaysdk", "IMEI "+imsi);
		return imsi;
	}
	
	private String getVersionName(){
		try {  
            PackageInfo pi=getPackageManager().getPackageInfo(getPackageName(), 0);  
            return pi.versionName;  
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
            return "0";  
        }  
	}
	
	private String getDeviceMac() {
		try {
			WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			String wifiString = wifiInfo.getMacAddress();
			return wifiString == null ? "" : wifiString;
		} catch (Exception e) {
			return "";
		}
	}	
}
