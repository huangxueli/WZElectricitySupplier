package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;
import android.util.Log;

import com.example.wzelectricitysupplier.bean.NormalRecord;

public class NormalTable {

	protected String TAG = "NormalTable";
	
	// ���ݿ�
	protected Database mDatabase;
	// ����
	protected String mTableName ;
	// �ֶμ���(���������ͼ�������)
	protected ArrayList<Field> mFieldList; 

	public NormalTable(Database mDatabase, String mTableName, ArrayList<Field> mFieldList){
		this.mDatabase = mDatabase;
		this.mTableName = mTableName;
		this.mFieldList = mFieldList;
	}
	
	/**
	 *  �������
	 * @param primery
	 * @return
	 * @throws Exception
	 */
	protected synchronized boolean CreateTable(Boolean primery) {
		boolean result = false;
		try{
			if (!isExist(mDatabase, mTableName)) {
				// ������ͨ�ֶ����
				String fieldString = "";
				Field field = null;
				for (int i = 0; i < mFieldList.size(); i++) {
					field = mFieldList.get(i);
					if(i==0)
						fieldString += (" '" + field.NAME + "' " + field.TYPE);
					else
						fieldString += (",'" + field.NAME + "' " + field.TYPE);
				}
				// ���������ֶ����
				String primeryStr = "";
				if(primery){
					primeryStr = "'" + DBSetting.ZD_PRIMERY + "' INTEGER " + "PRIMARY KEY AUTOINCREMENT,";
				}
				String sql = "CREATE TABLE '" + mTableName + "' (" + primeryStr + fieldString + ")";
				// ִ�����
				result = execute(sql); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * �������Ĭ��������
	 * @return
	 */
	protected synchronized boolean CreateTable() {
		return CreateTable(true);
	}
	
	/**
	 * ����һ������ -1��ʾ���벻�ɹ�
	 * @param record ��¼
	 * @return
	 */
	public int add(NormalRecord record) {
		int mLastRecordID = -1;
		String mSQL = "";
		String mFiledStr = record.getFieldStr();
		String mValueStr = record.getValueStr();
		mSQL = "INSERT INTO  " + mTableName + "(" + mFiledStr + ") VALUES (" + mValueStr + ")";
		if (execute(mSQL)) {
			mSQL = "SELECT " + DBSetting.ZD_PRIMERY + " FROM " + mTableName + " ORDER BY " + DBSetting.ZD_PRIMERY + " DESC";
			try {
				Stmt stmt = prepare(mSQL);
				if (stmt.step()) {
					mLastRecordID = stmt.column_int(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mLastRecordID;
	}
	
	/**
	 * ���¼�¼�����ֶ�
	 * @return
	 */
	public boolean update(String field, String value, String condition){
		String mSQL = "UPDATE " + mTableName + " SET " + field  + "='"+ value + "' " + condition;
		boolean b = execute(mSQL);
		return b;
	} 
	
	/**
	 * ���µ�����¼�����ֶ�
	 * @return
	 */
	public boolean update(String field, String value, int primaryid){
		return update(field, value, " WHERE " + DBSetting.ZD_PRIMERY + "=" + primaryid);
	} 
	
	/**
	 * ɾ����¼
	 * @param condition ɾ����������where�ؼ���
	 * @return
	 */
	public boolean delete(String condition){
		String sql = "DELETE FROM " + mTableName + " WHERE " + condition;
		return execute(sql);
	}
	public boolean delete(int primaryid){
		String condition = DBSetting.ZD_PRIMERY + "=" + primaryid;
		return delete(condition);
	}
	
	/**
	 * �жϼ�¼�Ƿ����
	 * @param lineId
	 * @return
	 * @throws Exception 
	 */
	public boolean isRecordExist(int id) throws Exception{
		boolean exist = false;
		String mSQL = "SELECT count(*) FROM "+ mTableName + " WHERE " + DBSetting.ZD_PRIMERY + " = " + id;
		Stmt stmt = prepare(mSQL);
		if (stmt.step()) {
			int count = stmt.column_int(0);
			if(count>0) exist = true;
		}
		return exist;
	}
	
	// �жϱ��Ƿ����
	protected boolean isExist(Database database, String table_name) throws Exception {
		String sql = "SELECT COUNT(*) FROM SQLITE_MASTER WHERE TYPE='table' AND NAME='" + table_name + "'";
		Stmt stmt = database.prepare(sql);
		if (stmt.step()) {
			if (Integer.valueOf(stmt.column_string(0)) > 0)
				return true;
		}
		return false;
	}
	
	//  ִ�����ݿ����,�����ݱ�ʶ��ӡ���
	protected boolean execute(String sql) {
		try {
			if (DBSetting.DEBUG){
				Log.i(TAG, sql);
			}
			mDatabase.exec(sql, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected Stmt prepare(String sql) throws Exception {
		if (DBSetting.DEBUG){
			Log.i(TAG, sql);
		}
		Stmt stmt = mDatabase.prepare(sql);
		return stmt;
	}
	
}
