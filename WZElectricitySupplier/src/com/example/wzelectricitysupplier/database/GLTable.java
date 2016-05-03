package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.GLRecord;

public class GLTable extends GeometryTable {
	
	public String TAG = "GLTable";
	
	public final static String mTableName = "disconnectore";
	public final static String mGeometryType = "POINT";
	
	public GLTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new GLRecord().getFieldList());
	}
	public GLTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<GLRecord> getDisconnectoreList(int circuitId) {
		String condition = GLRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<GLRecord> mRecordList = selectDisconnectores(condition);
		return mRecordList;
	}
	
	/**
	 * ����������ȡһ����¼
	 * @param primary
	 * @return
	 */
	public synchronized GLRecord selectDisconnectore(int primary){
		GLRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<GLRecord> list = selectDisconnectores(condition);
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
	public synchronized ArrayList<GLRecord> selectDisconnectores(String condition){
		ArrayList<GLRecord> list = new ArrayList<GLRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ; //DBSetting.ZD_PRIMERY + " = " + primary;
			Stmt stmt = prepare(mSQL);
			GLRecord record = null;
			while (stmt.step()) {
				int size = new GLRecord().getFieldList().size() + 3; // ������������Ϣ��������ϢJSON��ʽ
				int index = 0;
				record = new GLRecord();
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
					record.mState = valueList.get(2);
					record.mPicture = valueList.get(3);
					record.mDefectID = valueList.get(4);
					record.mRemark = valueList.get(5);
					record.mIsEdit = valueList.get(6);
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
	public boolean update(Point point, GLRecord record, int id){
		if(point!=null){
			String geo = GeometryTable.GeometryFromText(point);
			String mSQL = "UPDATE " + mTableName + " SET '" + GLRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					GLRecord.ZD_STATE.NAME + "'='" + record.mState + "','" + 
					GLRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					GLRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					GLRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					GLRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * �������ݣ�����Ӧ�����ݿ������µ����ݿ⣩
	 */
	public List<GLRecord> export(Database newDatabase, int circuitId){
		GLTable mGLTable = new GLTable(newDatabase);
		List<GLRecord> mList = getDisconnectoreList(circuitId);
		List<GLRecord> mNewList = new ArrayList<GLRecord>();
		if(mList.size()>0){
			for(GLRecord record:mList){
				mGLTable.add(record, (Point)record.getGeometry());
			}
			mNewList = mGLTable.selectDisconnectores("1=1");
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
		GLTable mGLTable = new GLTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<GLRecord> mSourceList = mGLTable.selectDisconnectores("1=1");
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(GLRecord record :mSourceList){
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
			GLRecord newRecord = new GLRecord(record.mName, newCircuitId, record.mState, record.mPicture, mDefectIDStr, record.mRemark, "��");
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
