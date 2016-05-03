package com.example.wzelectricitysupplier.function;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;

public class FTPCommand {

	private static final String TAG = "FTPCommand";
	
	public  CmdFactory mCmdFactory;
	
	private Handler mHandler;
	private FTPClient mFTPClient;
	
	public static final int MAX_DAMEON_TIME_WAIT = 10 * 1000;
	
	public static final int MSG_CMD_CONNECT_S = 1000 + 1;
	public static final int MSG_CMD_CONNECT_F = MSG_CMD_CONNECT_S + 1;
	public static final int MSG_CMD_LIST_S = MSG_CMD_CONNECT_F + 1;
	public static final int MSG_CMD_LIST_F = MSG_CMD_LIST_S + 1;
	public static final int MSG_CMD_CHANGEDIR_S = MSG_CMD_LIST_F + 1;
	public static final int MSG_CMD_CHANGEDIR_F = MSG_CMD_CHANGEDIR_S + 1;
	public static final int MSG_CMD_DELE_S = MSG_CMD_CHANGEDIR_F + 1;
	public static final int MSG_CMD_DELE_F = MSG_CMD_DELE_S + 1;
	public static final int MSG_CMD_RENAME_S = MSG_CMD_DELE_F + 1;
	public static final int MSG_CMD_RENAME_F = MSG_CMD_RENAME_S + 1;
	public static final int MSG_CMD_CURDIR_S = MSG_CMD_RENAME_F + 1;
	public static final int MSG_CMD_CURDIR_F = MSG_CMD_CURDIR_S + 1;
	public static final int MSG_CMD_UPLOAD_S = MSG_CMD_CURDIR_F + 1;
	public static final int MSG_CMD_UPLOAD_F = MSG_CMD_UPLOAD_S + 1;
	public static final int MSG_CMD_DOWNLOAD_S = MSG_CMD_UPLOAD_F + 1;
	public static final int MSG_CMD_DOWNLOAD_F = MSG_CMD_DOWNLOAD_S + 1;
	public static final int MSG_CMD_ISCONNECTED_Y = MSG_CMD_DOWNLOAD_F + 1;
	public static final int MSG_CMD_ISCONNECTED_N = MSG_CMD_ISCONNECTED_Y + 1;
	
	private boolean mDameonRunning = true;
	private Object mLock = new Object();
	
	private static String mFTPUser ;
	private static String mFTPPassword ;
	
	public FTPCommand(FTPClient client, Handler handler){
		mFTPClient = client;
		mHandler = handler;
		mFTPUser = Constants.FTPUser;
		mFTPPassword = Constants.FTPPassword;
		mCmdFactory = new CmdFactory();
	}
	
	public class CmdFactory {

		public Runnable createCmdIsConnected() {
			return new CmdIsConnected();
		}
		public Runnable createCmdConnect() {
			return new CmdConnect();
		}
		public Runnable createCmdDisConnect() {
			return new CmdDisConnect();
		}
		public Runnable createCmdPWD() {
			return new CmdGCD();
		}
		public Runnable createCmdLIST() {
			return new CmdLIST();
		}
		
		/**
		 * 改变路径并创建
		 */
		public Runnable createCmdCCD(String path) {
			return new CmdChangeCreateDirectory(path);
		}
		public Runnable createCmdDEL(String path, boolean isDirectory) {
			return new CmdDELE(path, isDirectory);
		}
		public Runnable createCmdRENAME(FTPFile file, String newPath) {
			return new CmdRENAME(file, newPath);
		}
	}

	public static class DameonFtpConnector implements Runnable {
		
		private FTPClient mFTPClient;
		private boolean mDameonRunning;
		private Handler mHandler;
		
		public DameonFtpConnector(Handler mHandler, FTPClient mFTPClient, boolean mDameonRunning){
			this.mHandler = mHandler;
			this.mFTPClient = mFTPClient;
			this.mDameonRunning = mDameonRunning;
		}
		@Override
		public void run() {
			while (mDameonRunning) {
				try {
					Thread.sleep(MAX_DAMEON_TIME_WAIT);
					Log.v(TAG, "DameonFtpConnector ### run " + MAX_DAMEON_TIME_WAIT + "s");
					String currentDir = "";
					if(mFTPClient != null){
						currentDir = mFTPClient.currentDirectory();
					}
					if(currentDir!=null && !currentDir.equals("")){
						
					}else{
						mHandler.sendEmptyMessage(MSG_CMD_ISCONNECTED_N); 
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (FTPIllegalReplyException e) {
					e.printStackTrace();
				} catch (FTPException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class CmdIsConnected implements Runnable {
		@Override
		public void run() {
			try {
				mFTPClient.currentDirectory();
				mHandler.sendEmptyMessage(MSG_CMD_ISCONNECTED_Y);
			} catch (IllegalStateException e) {
				mHandler.sendEmptyMessage(MSG_CMD_ISCONNECTED_N);
			} catch (IOException e) {
				
			} catch (FTPIllegalReplyException e) {
				
			} catch (FTPException e) {
				
			}
		}
	}
	
	public class CmdConnect implements Runnable {
		@Override
		public void run() {
			boolean errorAndRetry = false; // 根据不同的异常类型，是否重新捕获
			try {
				String[] welcome = mFTPClient.connect(Constants.FTPHost, Constants.FTPPort);
				if (welcome != null) {
					for (String value : welcome) {
						logv("connect " + value);
					}
				}
				mFTPClient.login(mFTPUser, mFTPPassword);
				mHandler.sendEmptyMessage(MSG_CMD_CONNECT_S);
			} catch (IllegalStateException illegalEx) {
				illegalEx.printStackTrace();
				errorAndRetry = true;
			} catch (IOException ex) {
				ex.printStackTrace();
				errorAndRetry = true;
			} catch (FTPIllegalReplyException e) {
				e.printStackTrace();
			} catch (FTPException e) {
				e.printStackTrace();
				errorAndRetry = true;
			}
			if (errorAndRetry && mDameonRunning) {
				mHandler.sendEmptyMessageDelayed(MSG_CMD_CONNECT_F, 2000);
			}
		}
	}

	public class CmdDisConnect implements Runnable {

		@Override
		public void run() {
			if (mFTPClient != null) {
				try {
					mFTPClient.disconnect(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public class CmdGCD implements Runnable {

		@Override
		public void run() {
			try {
				String dir = mFTPClient.currentDirectory();
				logv("CurrentDirectory --- > " + dir);
				mHandler.sendMessage(mHandler.obtainMessage(MSG_CMD_CURDIR_S, dir));
			} catch (Exception ex) {
				mHandler.sendMessage(mHandler.obtainMessage(MSG_CMD_CURDIR_F));
				ex.printStackTrace();
			}
		}
	}

	public class CmdLIST implements Runnable {
		public final static String KEY_CURRENTDIRECTORY = "CurrentDirectory";
		public final static String KEY_FTPFILELIST = "FTPFileList";
		@Override
		public void run() {
			try {
				String currentDir = mFTPClient.currentDirectory();
				FTPFile[] ftpFiles = mFTPClient.list();
				logv("Request Size  : " + ftpFiles.length);
				List<FTPFile> fileList = null;
				synchronized (mLock) {
					fileList = new ArrayList<FTPFile>();
					fileList.addAll(Arrays.asList(ftpFiles));
				}
				Message msg = mHandler.obtainMessage(MSG_CMD_LIST_S);
				HashMap<String, Object> map = new HashMap<>();
				map.put(KEY_CURRENTDIRECTORY, currentDir);
				map.put(KEY_FTPFILELIST, fileList);
				msg.obj = map;
				mHandler.sendMessage(msg);
			} catch (Exception ex) {
				mHandler.sendEmptyMessage(MSG_CMD_LIST_F);
				ex.printStackTrace();
			}
		}
	}

	public class CmdChangeCreateDirectory implements Runnable {

		String realivePath;

		public CmdChangeCreateDirectory(String path) {
			realivePath = path;
		}

		@Override
		public void run() {
			try {
				mFTPClient.changeDirectory(realivePath);
				mHandler.sendEmptyMessage(MSG_CMD_CHANGEDIR_S);
			} catch (Exception e) {
				try {
					if(realivePath.indexOf("/")==0){
						String name  = realivePath.substring(1);
						mFTPClient.createDirectory(name);
						mFTPClient.changeDirectory(realivePath);
					}
					mHandler.sendEmptyMessage(MSG_CMD_CHANGEDIR_S);
				} catch (Exception ex) {
					mHandler.sendEmptyMessage(MSG_CMD_CHANGEDIR_F);
					ex.printStackTrace();
				}
				
			}
		}
	}

	public class CmdDELE implements Runnable {

		String realivePath;
		boolean isDirectory;

		public CmdDELE(String path, boolean isDirectory) {
			realivePath = path;
			this.isDirectory = isDirectory;
		}

		@Override
		public void run() {
			try {
				if (isDirectory) {
					mFTPClient.deleteDirectory(realivePath);
				} else {
					mFTPClient.deleteFile(realivePath);
				}
				mHandler.sendEmptyMessage(MSG_CMD_DELE_S);
			} catch (Exception ex) {
				mHandler.sendEmptyMessage(MSG_CMD_DELE_F);
				ex.printStackTrace();
			}
		}
	}

	public class CmdRENAME implements Runnable {

		String newPath;
		FTPFile file;
		
		public CmdRENAME(FTPFile file, String newPath) {
			this.newPath = newPath;
			this.file = file;
		}

		@Override
		public void run() {
			try {
				mFTPClient.rename(file.getName(), newPath);
				mHandler.sendEmptyMessage(MSG_CMD_RENAME_S);
			} catch (Exception ex) {
				mHandler.sendEmptyMessage(MSG_CMD_RENAME_F);
				ex.printStackTrace();
			}
		}
	}
	
	public static class CmdDownLoad extends AsyncTask<Void, Integer, Boolean> {
		
		FTPClient mClient;
		Handler mHandler;
		
		String mFileName;
		long mFileSize;
		ProgressDialog mProgressDialog;
		
		public CmdDownLoad(Context context, Handler mHandler, FTPClient client ,String fileName, long fileSize, boolean showProgress) {
			this.mFileName = fileName;
			this.mFileSize = fileSize;
			this.mClient = client;
			this.mHandler = mHandler;
			if(showProgress){
				mProgressDialog = new ProgressDialog(context, ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setCancelable(true);
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.setMax(100);
				mProgressDialog.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						mProgressDialog.setProgress(0);
					}
				});
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean result = false;
			try {
				
				String dir = Util.getPictureDirRootPath();
				File file = new File(dir);
				if(!file.exists()){
					file.mkdirs();
				}
				String path = dir + mFileName;
				File cachefile = new File(path);
				mClient.download(mFileName, cachefile, new DownloadFTPDataTransferListener(mHandler, path, mFileSize, mProgressDialog));
				result = true;
			} catch (IllegalStateException e) {
				e.printStackTrace();
				result = false;
				Util.Toast("访问服务端文件失败");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				result = false;
				Util.Toast("未检测到内置存储卡");
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
				Util.Toast("访问服务端文件失败");
			} catch (FTPIllegalReplyException e) {
				e.printStackTrace();
				result = false;
				Util.Toast("访问服务端文件失败");
			} catch (FTPException e) {
				e.printStackTrace();
				result = false;
				Util.Toast("访问服务端文件失败");
			} catch (FTPDataTransferException e) {
				e.printStackTrace();
				result = false;
				Util.Toast("访问服务端文件失败");
			} catch (FTPAbortedException e) {
				e.printStackTrace();
				result = false;
				Util.Toast("访问服务端文件失败");
			}
			return result;
		}

		protected void onPostExecute(Boolean result) {
			if(mProgressDialog !=null){
				mProgressDialog.dismiss();
			}
			if(!result){
				mHandler.sendEmptyMessage(MSG_CMD_DOWNLOAD_F);
			}
		}
		
		protected void onProgressUpdate(Integer... progress) {
			if(mProgressDialog !=null){
				mProgressDialog.setProgress(progress[0]);
			}
		}
		
		private class DownloadFTPDataTransferListener implements FTPDataTransferListener {
			
			private Handler handler;
			private int totolTransferred = 0;
			private long fileSize = -1;
			private String path;
			private ProgressDialog progressDialog;

			public DownloadFTPDataTransferListener(Handler handler, String path, long fileSize, ProgressDialog progressDialog) {
				if (fileSize <= 0) {
					throw new RuntimeException("the size of file muset be larger than zero.");
				}
				this.handler = handler;
				this.fileSize = fileSize;
				this.path = path;
				this.progressDialog = progressDialog;
			}

			@Override
			public void aborted() {
				Log.v(TAG,"FTPDataTransferListener : aborted");
			}

			@Override
			public void completed() {
				Log.v(TAG,"FTPDataTransferListener : completed");
				if(progressDialog != null){
					publishProgress(progressDialog.getMax());
				}
				handler.sendMessage(handler.obtainMessage(MSG_CMD_DOWNLOAD_S, path) );
			}

			@Override
			public void failed() {
				Log.v(TAG,"FTPDataTransferListener : failed");
				handler.sendMessage(handler.obtainMessage(MSG_CMD_DOWNLOAD_F, path) );
			}

			@Override
			public void started() {
				Log.v(TAG,"FTPDataTransferListener : started");
				if(progressDialog != null){
					publishProgress(0);
				}
			}

			@Override
			public void transferred(int length) {
				if(progressDialog != null){
					totolTransferred += length;
					float percent = (float) totolTransferred / this.fileSize * 100.0f;
					DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.CHINA);
					format.applyPattern("##.00");
					Log.v(TAG,"FTPDataTransferListener : transferred # percent " + format.format(percent) + "%");
					publishProgress((int) percent);
				}
			}
		}
	}
	
	public static class CmdUpload extends AsyncTask<String, Integer, Boolean> {
		String path;
		ProgressDialog mProgressDialog;
		FTPClient mClient;
		Handler mHandler;
		
		public CmdUpload(Context context, Handler handler, FTPClient client) {
			this.mClient = client;
			this.mHandler = handler;
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);	
			mProgressDialog.setCancelable(true);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					mProgressDialog.setProgress(0);
				}
			});
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			for(String s : params){
				path = s;
				try {
					File file = new File(path);
					mClient.upload(file, new UpLoadFTPDataTransferListener(mHandler, path, file.length(), mProgressDialog));
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
			}
			if(path==null) 
				return false;
			else 
				return true;
		}

		protected void onPostExecute(Boolean result) {
			if(mProgressDialog!=null){
				mProgressDialog.dismiss();
			}
			if(!result){
				mHandler.sendEmptyMessage(MSG_CMD_UPLOAD_F);
			}
		}
		
		protected void onProgressUpdate(Integer... progress) {
			if(mProgressDialog!=null){
				mProgressDialog.setProgress(progress[0]);
			}
		}
		
		private class UpLoadFTPDataTransferListener implements FTPDataTransferListener {
			
			private Handler handler;
			private int totolTransferred = 0;
			private long fileSize = -1;
			private String path;
			private ProgressDialog mProgressDialog;

			public UpLoadFTPDataTransferListener(Handler handler, String path, long fileSize, ProgressDialog progressDialog) {
				if (fileSize <= 0) {
					throw new RuntimeException("the size of file muset be larger than zero.");
				}
				this.handler = handler;
				this.fileSize = fileSize;
				this.path = path;
				this.mProgressDialog = progressDialog;
			}

			@Override
			public void aborted() {
				Log.v(TAG,"FTPDataTransferListener : aborted");
			}

			@Override
			public void completed() {
				Log.v(TAG,"FTPDataTransferListener : completed");
				publishProgress(mProgressDialog.getMax());
				handler.sendMessage(handler.obtainMessage(MSG_CMD_UPLOAD_S, path) );
			}

			@Override
			public void failed() {
				Log.v(TAG,"FTPDataTransferListener : failed");
				handler.sendMessage(handler.obtainMessage(MSG_CMD_UPLOAD_F, path) );
			}

			@Override
			public void started() {
				Log.v(TAG,"FTPDataTransferListener : started");
				publishProgress(0);
			}

			@Override
			public void transferred(int length) {
				totolTransferred += length;
				float percent = (float) totolTransferred / this.fileSize * 100.0f;
				DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.CHINA);
				format.applyPattern("##.00");
				Log.v(TAG,"FTPDataTransferListener : transferred # percent " + format.format(percent) + "%");
				publishProgress((int) percent);
			}
		}
		
	}
	
	private void logv(String log) {
		Log.v(TAG, log);
	}
}
