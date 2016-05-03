package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.example.wzelectricitysupplier.bean.CircuitRecord;

public class CircuitTable extends NormalTable{
	
	public final String TAG = "CircuitTable";
	
	private String mCircuitID = "";
	private List<String> mBranchName = new ArrayList<String>();
	
	// 表名
	private static String mTableName = "circuit";;
	
	public CircuitTable(Database mDatabase) {
		super(mDatabase, mTableName, new CircuitRecord().getFieldList());
		CreateTable();
	}

	public String getCircuitID() {
		return mCircuitID;
	}
	public List<String> getBranchName() {
		return mBranchName;
	}

	public void setCircuitID(String mCircuitID) {
		this.mCircuitID = mCircuitID;
		if(mCircuitID!=null && !mCircuitID.equals("")){
			mBranchName.clear();
			CircuitRecord record = SelectCircuitRecord(Integer.valueOf(mCircuitID));
			if(record!=null){
				if(record.mRemark.contains("、")){
					String[] arr = record.mRemark.split("、");
					for(String branch : arr){
						mBranchName.add(branch);
					}
				}else{
					mBranchName.add(record.mRemark);
				}
			}
		}
	}
	
	public synchronized ArrayList<CircuitRecord> SelectCircuits(String condition){
		ArrayList<CircuitRecord> list = new ArrayList<>();
		try{
			String mSQL = "SELECT * FROM "+ mTableName + " " + condition ; 
			Stmt stmt = prepare(mSQL);
			CircuitRecord record = null;
			while (stmt.step()) {
				int size = new CircuitRecord().getFieldList().size() + 1; // 主键
				int index = 0;
				record = new CircuitRecord();
				if(index<size){
					record.setId(stmt.column_int(index));
					index++;
				}
				ArrayList<String> valueList = record.getValueList();
				while(index<size){
					valueList.add(stmt.column_string(index));
					index++;
				}
				if(index == size){
					record.mName = valueList.get(0);
					record.mRemark = valueList.get(1);
					list.add(record);
				};
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public synchronized CircuitRecord SelectCircuitRecord(int primary){
		CircuitRecord record = null;
		String condition = "WHERE " + DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<CircuitRecord> list = SelectCircuits(condition);
		if(!list.isEmpty()){
			record = list.get(0);
		}
		return record;
	}
	
	public boolean update(CircuitRecord record){
		String mSQL = "UPDATE " + mTableName + " SET '" + 
				CircuitRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
				CircuitRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'" +
				" WHERE " + DBSetting.ZD_PRIMERY + "=" + record.getId();;
		return execute(mSQL);
	} 
	/**
	 * 导出数据（将对应的数据拷贝到新的数据库）
	 * @param newDatabase 导出数据库
	 * @param circuitId 线路ID
	 */
	public void export(Database newDatabase, int circuitId){
		CircuitTable mCircuitTable = new CircuitTable(newDatabase);
		CircuitRecord mCircuitRecord = SelectCircuitRecord(circuitId);
		mCircuitTable.add(mCircuitRecord);
	}
}
