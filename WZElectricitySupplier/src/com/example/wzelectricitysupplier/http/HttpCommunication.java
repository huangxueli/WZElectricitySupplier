package com.example.wzelectricitysupplier.http;

import android.app.Activity;
import android.os.Handler;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;

public class HttpCommunication {
	
	public static final String TAG = "HttpCommunication";
	
	private AbHttpUtil mAbHttpUtil;
	private Handler mHandler;
	
	public HttpCommunication(Activity activity, Handler handler){
        mAbHttpUtil = AbHttpUtil.getInstance(activity);
        mAbHttpUtil.setDebug(true);
        mHandler = handler;
	}
	
	public void doLogin(String username){

		String url = createUrlbySql("select a.rvalue,t.* from ruleinfo a, userinfo t where a.ruleid=t.ruleid and t.cnname='" + username + "'");
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

			@Override // 获取数据成功会调用这里
			public void onSuccess(int statusCode, String content) {
				
			}

			@Override 
			public void onFailure(int statusCode, String content, Throwable error) {
				Util.Toast(error.getMessage());
			}

			@Override 
			public void onStart() {
				
			}

			@Override 
			public void onFinish() {
				
			};
		});
	}
	
	private String createUrlbySql(String sql){
		String result = Constants.URL_HTTP + "SqlBackJson.data?action=SelectFrom&Sql=";
		result = result + sql;
		result = result + "&JsonName=n";
		result = result.replace(" ", "%20");
		result = result.replace("'", "%27");
		return result;
	}
}
