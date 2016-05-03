package com.example.wzelectricitysupplier.database;

import java.io.File;
import java.io.IOException;

import jsqlite.Constants;
import jsqlite.Database;
import android.content.Context;
import android.util.Log;

import com.example.wzelectricitysupplier.function.AssetHelper;
import com.example.wzelectricitysupplier.setting.Util;

public class DataBaseTool {

	private static final String TAG = "DatabaseTool";
	
	/**
	 * 创建并打开数据库（默认创建路径与应用数据库路径相同）
	 * @param context
	 * @param dbname
	 * @return
	 */
	public static Database OpenDatabase(Context context, String dbname) {
		return OpenDatabase(context, dbname, GetDatabaseDirPath());
	}
	public static Database OpenDatabase(Context context, String dbname, String dir) {
		Database mDatabase = null;
		try {
			CreateOriginalDatabase(context, "empty.sqlite", dbname, dir);
			String path = dir + "/" + dbname;
			Class.forName("jsqlite.JDBCDriver").newInstance();
			mDatabase = new Database();
			mDatabase.open(path, Constants.SQLITE_OPEN_READWRITE);
			if(mDatabase!=null){
				String dbversion = mDatabase.dbversion();
				Log.v(TAG, "数据库版本:" + dbversion);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return mDatabase;
	}
	
	/**
	 * 创造一个空的数据（从Assets文件夹中拷贝）
	 * @param context
	 * @param dbname
	 * @return
	 */
	public static boolean CreateOriginalDatabase(Context context, String copyname, String dbname){
		String dir = GetDatabaseDirPath();
		return CreateOriginalDatabase(context, copyname, dbname, dir);
	}
	
	/**
	 * 创造一个空的数据（从Assets文件夹中拷贝）
	 * @param context
	 * @param dbname
	 * @return
	 */
	public static boolean CreateOriginalDatabase(Context context, String copyname, String dbname, String dir){
		try {
			File db = new File(dir, dbname);
			if(!db.exists()){
				AssetHelper.CopyAsset(context, copyname, db.toString());
			}
			return true;
		} catch (IOException e) {
			Util.ShowDialog(context, "错误：拷贝数据库失败。");
		}
		return false;
	}
	
	/**
	 * 关闭数据库
	 * @param db
	 * @return
	 * @throws Exception
	 */
	public static boolean CloseDatabase(Database db){
		if (db != null) {
			try {
				db.close();
			} catch (jsqlite.Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	/**
	 * 获取应用数据库路径
	 * @return
	 */
	public static String GetDatabaseDirPath(){
		String mDBPath = Util.getAppRootPath() + "/" + DBSetting.DIR_DATABASE + "/";
		File file = new File(mDBPath);
		if (!file.exists()) {
			file.mkdir();
		}
		return mDBPath;
	}
	
}
