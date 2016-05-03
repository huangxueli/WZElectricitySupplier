package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.SwitchRecord;

public class SwitchTable extends GeometryTable {
	
	public String TAG = "SwitchTable";
	
	public final static String mTableName = "switch";
	public final static String mGeometryType = "POINT";
	
	public SwitchTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new SwitchRecord().getFieldList());
	}
	public SwitchTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<SwitchRecord> getSwitchList(int circuitId) {
		String condition = SwitchRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<SwitchRecord> mRecordList = selectSwitchs(condition);
		return mRecordList;
	}
	
	/**
	 * ����������ȡһ����¼
	 * @param primary
	 * @return
	 */
	public synchronized SwitchRecord selectSwitch(int primary){
		SwitchRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<SwitchRecord> list = selectSwitchs(condition);
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
	public synchronized ArrayList<SwitchRecord> selectSwitchs(String condition){
		ArrayList<SwitchRecord> list = new ArrayList<SwitchRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ; //DBSetting.ZD_PRIMERY + " = " + primary;
			Stmt stmt = prepare(mSQL);
			SwitchRecord record = null;
			while (stmt.step()) {
				int size = new SwitchRecord().getFieldList().size() + 3; // ������������Ϣ��������ϢJSON��ʽ
				int index = 0;
				record = new SwitchRecord();
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
					record.mHavedz = valueList.get(2);
					record.mState = valueList.get(3);
					record.mPicture = valueList.get(4);
					record.mDefectID = valueList.get(5);
					record.mRemark = valueList.get(6);
					record.mIsEdit = valueList.get(7);
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
	public boolean update(Point point, SwitchRecord record, int id){
		if(point!=null){
			String geo = GeometryTable.GeometryFromText(point);
			String mSQL = "USwitchATE " + mTableName + " SET '" + SwitchRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					SwitchRecord.ZD_HAVEDZ.NAME + "'='" + record.mHavedz + "','" + 
					SwitchRecord.ZD_STATE.NAME + "'='" + record.mState + "','" + 
					SwitchRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					SwitchRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					SwitchRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					SwitchRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * �������ݣ�����Ӧ�����ݿ������µ����ݿ⣩
	 */
	public List<SwitchRecord> export(Database newDatabase, int circuitId){
		SwitchTable mSwitchTable = new SwitchTable(newDatabase);
		List<SwitchRecord> mList = getSwitchList(circuitId);
		List<SwitchRecord> mNewList = new ArrayList<SwitchRecord>();
		if(mList.size()>0){
			for(SwitchRecord record:mList){
				mSwitchTable.add(record, (Point)record.getGeometry());
			}
			mNewList = mSwitchTable.selectSwitchs("1=1");
		}
		return mNewList;
	}
	/**
	 * ��������
	 * @param sourceDatabase �������ݿ�
	 * @param newCircuitId �������ݵ�����·ID
	 */
	public ArrayList<SwitchRecord> inport(Database sourceDatabase, String newCircuitId){
		SwitchTable mSwitchTable = new SwitchTable(sourceDatabase);
		ArrayList<SwitchRecord> mSourceList = mSwitchTable.selectSwitchs("1=1");
		ArrayList<SwitchRecord> mAddedList = new ArrayList<>();
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(SwitchRecord record :mSourceList){
			SwitchRecord newRecord = new SwitchRecord(record.mName, newCircuitId, record.mHavedz, record.mState, 
					record.mPicture, record.mDefectID, record.mRemark, "��");
			int deviceId = add(newRecord, (Point)record.getGeometry());
			newRecord.setId(deviceId);
			mAddedList.add(newRecord);
		}
		return mAddedList;
	}
	/**
	 * ��������
	 * @param sourceDatabase �������ݿ�
	 * @param newCircuitId �������ݵ�����·ID
	 */
	public boolean inport(Database database, Database sourceDatabase, String newCircuitId, List<DefectRecord> mAddedDefects){// ����·���������豸��ȱ�� û���ض�˳��
		boolean result = true;
		SwitchTable mSwitchTable = new SwitchTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<SwitchRecord> mSourceList = mSwitchTable.selectSwitchs("1=1");
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(SwitchRecord record :mSourceList){
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
			SwitchRecord newRecord = new SwitchRecord(record.mName, newCircuitId, record.mHavedz, record.mState, 
					record.mPicture, mDefectIDStr, record.mRemark, "��");
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
