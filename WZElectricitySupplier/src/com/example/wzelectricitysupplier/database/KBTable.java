package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.KBRecord;

public class KBTable extends GeometryTable {
	
	public String TAG = "KBTable";
	
	public final static String mTableName = "switching_station";
	public final static String mGeometryType = "POINT";
	
	public KBTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new KBRecord().getFieldList());
	}
	public KBTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<KBRecord> getSwitchingStationList(int circuitId) {
		String condition = KBRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<KBRecord> mRecordList = selectSwitchingStations(condition);
		return mRecordList;
	}
	
	/**
	 * 根据主键获取一条记录
	 * @param primary
	 * @return
	 */
	public synchronized KBRecord selectSwitchingStation(int primary){
		KBRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<KBRecord> list = selectSwitchingStations(condition);
		if(!list.isEmpty()){
			record = list.get(0);
		}
		return record;
	}
	/**
	 * 根据查询条件获取多条记录集
	 * @param condition
	 * @return
	 */
	public synchronized ArrayList<KBRecord> selectSwitchingStations(String condition){
		ArrayList<KBRecord> list = new ArrayList<KBRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ; //DBSetting.ZD_PRIMERY + " = " + primary;
			Stmt stmt = prepare(mSQL);
			KBRecord record = null;
			while (stmt.step()) {
				int size = new KBRecord().getFieldList().size() + 3; // 主键，几何信息，几何信息JSON形式
				int index = 0;
				record = new KBRecord();
				if(index<size){ // 几何信息JSON形式
					record.setGeometry(stmt.column_string(index));
					index++;
				}
				if(index<size){ // 主键
					record.setId(stmt.column_int(index));
					index++;
				}
				ArrayList<String> valueList = record.getValueList();
				while(index<size-1){
					valueList.add(stmt.column_string(index));
					index++;
				}
				index++;
				if(index == size){
					record.mName = valueList.get(0);
					record.mCircuit = valueList.get(1);
					record.mBackupIntervalNum = valueList.get(2);
					record.mOutIntervalNum = valueList.get(3);
					record.mInIntervalNum = valueList.get(4);
					record.mPicture = valueList.get(5);
					record.mDefectID = valueList.get(6);
					record.mRemark = valueList.get(7);
					record.mIsEdit = valueList.get(8);
					list.add(record);
				};
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 更新记录
	 * @return
	 */
	public boolean update(Point point, KBRecord record, int id){
		if(point!=null){
			String geo = GeometryTable.GeometryFromText(point);
			String mSQL = "UPDATE " + mTableName + " SET '" + KBRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					KBRecord.ZD_BACKUP_INTERVAL_NUM.NAME + "'='" + record.mBackupIntervalNum + "','" + 
					KBRecord.ZD_OUT_INTERVAL_NUM.NAME + "'='" + record.mOutIntervalNum + "','" + 
					KBRecord.ZD_IN_INTERVAL_NUM.NAME + "'='" + record.mInIntervalNum + "','" + 
					KBRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					KBRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					KBRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					KBRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * 导出数据（将对应的数据拷贝到新的数据库）
	 */
	public List<KBRecord> export(Database newDatabase, int circuitId){
		KBTable mKBTable = new KBTable(newDatabase);
		List<KBRecord> mList = getSwitchingStationList(circuitId);
		List<KBRecord> mNewList = new ArrayList<KBRecord>();
		if(mList.size()>0){
			for(KBRecord record:mList){
				mKBTable.add(record, (Point)record.getGeometry());
			}
			mNewList = mKBTable.selectSwitchingStations("1=1");
		}
		return mNewList;
	}
	/**
	 * 导入数据
	 * @param sourceDatabase 导入数据库
	 * @param newCircuitId 导入数据的新线路ID
	 */
	public boolean inport(Database database, Database sourceDatabase, String newCircuitId, List<DefectRecord> mAddedDefects){// 单条路线上所有设备的缺陷 没有特定顺序
		boolean result = true;
		KBTable mKBTable = new KBTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<KBRecord> mSourceList = mKBTable.selectSwitchingStations("1=1");
		// 将数据集中的所属线路ID改为指定的ID
		for(KBRecord record :mSourceList){
			String mDefectIDStr = "";
			String[] defectIDs = null;
			// 更新设备表的中的缺陷编号字段
			if(!"".equals(record.mDefectID)){
				if(record.mDefectID.contains("&")){
					defectIDs = record.mDefectID.split("&");
					for(String id : defectIDs){
						int index = Integer.valueOf(id)-1;
						if(index>=0 && index<mAddedDefects.size()){
							if(mDefectIDStr.equals("")) // 根据mSourceDefectList 找到对应的index 根据index 在mAddedDefects中找到id
								mDefectIDStr = String.valueOf(mAddedDefects.get(index).getId());
							else
								mDefectIDStr += "&" + mAddedDefects.get(index).getId();
						}
					}
				}else{
					defectIDs = new String[]{record.mDefectID};
					int index = Integer.valueOf(defectIDs[0])-1;
					if(index>=0 && index<mAddedDefects.size()){
						mDefectIDStr = String.valueOf(mAddedDefects.get(index).getId());
					}
					
				}
			}
			KBRecord newRecord = new KBRecord(record.mName, newCircuitId, record.mBackupIntervalNum, 
					record.mOutIntervalNum, record.mInIntervalNum, record.mPicture, mDefectIDStr, record.mRemark, "否");
			int deviceId = add(newRecord, (Point)record.getGeometry());
			if(deviceId==-1) result = false;
			// 更新缺陷表中的所属设备字段
			if(defectIDs!=null){
				for(String oldDefectId : defectIDs){
					int index = Integer.valueOf(oldDefectId)-1;
					if(index>=0 && index<mAddedDefects.size()){
						String condition = "WHERE " + DBSetting.ZD_PRIMERY + " = " + mAddedDefects.get(index).getId();
						result = result && mDefectTable.update(DefectRecord.ZD_BELONG_ID.NAME, String.valueOf(deviceId), condition);
					}
				}
			}
		}
		return result;
	}
}
