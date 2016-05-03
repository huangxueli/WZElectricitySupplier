package com.example.wzelectricitysupplier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LocalService extends Service {
	
	private ExecutorService mThreadPool;
	private int MAX_THREAD_NUMBER = 5;// 线程池数量
	private IBinder mBinder = new LocalBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mThreadPool = Executors.newFixedThreadPool(MAX_THREAD_NUMBER); // 创建线程池
	}
	
	
	
	public void executeInThreadPool(Runnable command){
		if(mThreadPool!=null){
			mThreadPool.execute(command);
		}
	}
	
	public class LocalBinder extends Binder{
		public LocalService getLocalService(){
			return LocalService.this;
		}
	}
}
