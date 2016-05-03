package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.HWRecord;

public class HWTable extends GeometryTable {
	
	public String TAG = "HWTable";
	
	public final static String mTableName = "ring_main_unit";
	public final static String mGeometryType = "POINT";
	
	public HWTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new HWRecord().getFieldList());
	}
	public HWTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<HWRecord> getRingMainUnitList(int circuitId) {
		String condition = HWRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<HWRecord> mRecordList = selectRingMainUnits(condition);
		return mRecordList;
	}
	
	/**
	 * ����������ȡһ����¼
	 * @param primary
	 * @return
	 */
	public synchronized HWRecord selectRingMainUnit(int primary){
		HWRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<HWRecord> list = selectRingMainUnits(condition);
		if(!list.isEmpty()){
			record = list.get(0);
		}
		return record;
	}
	/**
	 * ���ݲ�ѯ������ȡ������¼��
	 * @param condition
	 * @return
	 */
	public synchronized ArrayList<HWRecord> selectRingMainUnits(String condition){
		ArrayList<HWRecord> list = new ArrayList<HWRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ; //DBSetting.ZD_PRIMERY + " = " + primary;
			Stmt stmt = prepare(mSQL);
			HWRecord record = null;
			while (stmt.step()) {
				int size = new HWRecord().getFieldList().size() + 3; // ������������Ϣ��������ϢJSON��ʽ
				int index = 0;
				record = new HWRecord();
				if(index<size){ // ������ϢJSON��ʽ
					record.setGeometry(stmt.column_string(index));
					index++;
				}
				if(index<size){ // ����
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
	 * ���¼�¼
	 * @return
	 */
	public boolean update(Point point, HWRecord record, int id){
		if(point!=null){
			String geo = GeometryTable.GeometryFromText(point);
			String mSQL = "UPDATE " + mTableName + " SET '" + HWRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					HWRecord.ZD_BACKUP_INTERVAL_NUM.NAME + "'='" + record.mBackupIntervalNum + "','" + 
					HWRecord.ZD_OUT_INTERVAL_NUM.NAME + "'='" + record.mOutIntervalNum + "','" + 
					HWRecord.ZD_IN_INTERVAL_NUM.NAME + "'='" + record.mInIntervalNum + "','" + 
					HWRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					HWRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					HWRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					HWRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * �������ݣ�����Ӧ�����ݿ������µ����ݿ⣩
	 */
	public List<HWRecord> export(Database newDatabase, int circuitId){
		HWTable mHWTable = new HWTable(newDatabase);
		List<HWRecord> mList = getRingMainUnitList(circuitId);
		List<HWRecord> mNewList = new ArrayList<HWRecord>();
		if(mList.size()>0){
			for(HWRecord record:mList){
				mHWTable.add(record, (Point)record.getGeometry());
			}
			mNewList = mHWTable.selectRingMainUnits("1=1");
		}
		return mNewList;
	}
	
	/**
	 * ��������
	 * @param sourceDatabase �������ݿ�
	 * @param newCircuitId �������ݵ�����·ID
	 */
	public boolean inport(Database database, Database sourceDatabase, String newCircuitId, List<DefectRecord> mAddedDefects){// ����·���������豸��ȱ�� û���ض�˳��
		boolean result = true;
		HWTable mHWTable = new HWTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<HWRecord> mSourceList = mHWTable.selectRingMainUnits("1=1");
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(HWRecord record :mSourceList){
			String mDefectIDStr = "";
			String[] defectIDs = null;
			// �����豸����е�ȱ�ݱ���ֶ�
			if(!"".equals(record.mDefectID)){
				if(record.mDefectID.contains("&")){
					defectIDs = record.mDefectID.split("&");
					for(String id : defectIDs){
						int index = Integer.valueOf(id)-1;
						if(index>=0 && index<mAddedDefects.size()){
							if(mDefectIDStr.equals("")) // ����mSourceDefectList �ҵ���Ӧ��index ����index ��mAddedDefects���ҵ�id
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
			HWRecord newRecord = new HWRecord(record.mName, newCircuitId, record.mBackupIntervalNum, 
					record.mOutIntervalNum, record.mInIntervalNum, record.mPicture, mDefectIDStr, record.mRemark, "��");
			int deviceId = add(newRecord, (Point)record.getGeometry());
			if(deviceId==-1) result = false;
			// ����ȱ�ݱ��е������豸�ֶ�
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
