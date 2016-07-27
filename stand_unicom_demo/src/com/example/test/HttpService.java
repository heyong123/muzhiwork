package com.example.test;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import android.util.Log;

public class HttpService {

	public String httpQueryGet(String url) {
		HttpGet httpGet = null;
		String returnMess = "";

		BasicHttpParams httpParams = new BasicHttpParams();  
		ConnManagerParams.setTimeout(httpParams, 10000);
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);  
        HttpConnectionParams.setSoTimeout(httpParams, 10000);
        
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		
		try {
			
			httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Log.e("UnipayDemo", "Yangzc http status code " + httpResponse.getStatusLine().getStatusCode());
				returnMess = "";
			}else{
				HttpEntity httpEntity = httpResponse.getEntity();
				returnMess = EntityUtils.toString(httpEntity);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpGet != null){
				httpGet.abort();
			}
			httpClient.getConnectionManager().shutdown();
		}
		
		return returnMess;
	}
	
	
	public String httpQueryPost(String url, String clearBody) {
		HttpPost httpPost = null;
		String returnMess = "";

		BasicHttpParams httpParams = new BasicHttpParams();  
		ConnManagerParams.setTimeout(httpParams, 10000);
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);  
        HttpConnectionParams.setSoTimeout(httpParams, 10000);
        
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		
		try {
			
			httpPost = new HttpPost(url);

			Log.d("UnipayDemo", "Yangzc Post " + url);
			Log.d("UnipayDemo", "Yangzc Post " + clearBody);
			
			httpPost.setHeader("Content-Type", "text/plain; charset=UTF-8");
			httpPost.setEntity(new StringEntity(clearBody, "utf-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Log.e("UnipayDemo", "Yangzc http status code " + httpResponse.getStatusLine().getStatusCode());
				returnMess = "";
			}else{
				HttpEntity httpEntity = httpResponse.getEntity();
				returnMess = EntityUtils.toString(httpEntity);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpPost != null){
				httpPost.abort();
			}
			httpClient.getConnectionManager().shutdown();
		}
		
		Log.d("UnipayDemo", "response:"+ returnMess);
		return returnMess;
	}
}
