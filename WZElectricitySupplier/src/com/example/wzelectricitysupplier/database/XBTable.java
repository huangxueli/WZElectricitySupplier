package com.example.wzelectricitysupplier.database;

import java.util.ArrayList;
import java.util.List;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.Stmt;

import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.XBRecord;

public class XBTable extends GeometryTable {
	
	public String TAG = "XBTable";
	
	public final static String mTableName = "box_change";
	public final static String mGeometryType = "POINT";
	
	public XBTable(Database mDatabase) {
		this(mDatabase, mTableName, mGeometryType, new XBRecord().getFieldList());
	}
	public XBTable(Database mDatabase, String mTableName, String mGeometryType, ArrayList<Field> mFieldList) {
		super(mDatabase, mTableName, mGeometryType, mFieldList);
		CreateTable();
	}
	
	public List<XBRecord> getBoxChangeList(int circuitId) {
		String condition = XBRecord.ZD_CIRCUIT.NAME + "=" + circuitId;
		List<XBRecord> mRecordList = selectBoxChanges(condition);
		return mRecordList;
	}
	
	/**
	 * ����������ȡһ����¼
	 * @param primary
	 * @return
	 */
	public synchronized XBRecord selectBoxChange(int primary){
		XBRecord record = null;
		String condition = DBSetting.ZD_PRIMERY + " = " + primary;
		ArrayList<XBRecord> list = selectBoxChanges(condition);
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
	public synchronized ArrayList<XBRecord> selectBoxChanges(String condition){
		ArrayList<XBRecord> list = new ArrayList<XBRecord>();
		try{
			String mSQL = "SELECT AsGeoJSON(" + DBSetting.ZD_GEOMETRY + ")," + "* FROM " + mTableName + " WHERE " + condition ; //DBSetting.ZD_PRIMERY + " = " + primary;
			Stmt stmt = prepare(mSQL);
			XBRecord record = null;
			while (stmt.step()) {
				int size = new XBRecord().getFieldList().size() + 3; // ������������Ϣ��������ϢJSON��ʽ
				int index = 0;
				record = new XBRecord();
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
					record.mCapacity = valueList.get(2);
					record.mNum = valueList.get(3);
					record.mType = valueList.get(4);
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
	public boolean update(Point point, XBRecord record, int id){
		if(point!=null){
			String geo = GeometryTable.GeometryFromText(point);
			String mSQL = "UPDATE " + mTableName + " SET '" + XBRecord.ZD_NAME.NAME  + "'='"+ record.mName + "','" + 
					XBRecord.ZD_CAPACITY.NAME + "'='" + record.mCapacity + "','" + 
					XBRecord.ZD_NUM.NAME + "'='" + record.mNum + "','" + 
					XBRecord.ZD_TYPE.NAME + "'='" + record.mType + "','" + 
					XBRecord.ZD_PICTURE.NAME + "'='" + record.mPicture + "','" + 
					XBRecord.ZD_DEFECT_ID.NAME + "'='" + record.mDefectID + "','" + 
					XBRecord.ZD_ISEDIT.NAME + "'='" + record.mIsEdit + "','" + 
					XBRecord.ZD_REMARK.NAME + "'='" + record.mRemark + "'," + 
					DBSetting.ZD_GEOMETRY + "=" + geo + " WHERE " + DBSetting.ZD_PRIMERY + "=" + id;
			return execute(mSQL);
		}
		return false;
	} 
	
	/**
	 * �������ݣ�����Ӧ�����ݿ������µ����ݿ⣩
	 */
	public List<XBRecord> export(Database newDatabase, int circuitId){
		XBTable mXBTable = new XBTable(newDatabase);
		List<XBRecord> mList = getBoxChangeList(circuitId);
		List<XBRecord> mNewList = new ArrayList<XBRecord>();
		if(mList.size()>0){
			for(XBRecord record:mList){
				mXBTable.add(record, (Point)record.getGeometry());
			}
			mNewList = mXBTable.selectBoxChanges("1=1");
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
		XBTable mXBTable = new XBTable(sourceDatabase);
		DefectTable mDefectTable = new DefectTable(database);
		ArrayList<XBRecord> mSourceList = mXBTable.selectBoxChanges("1=1");
		// �����ݼ��е�������·ID��Ϊָ����ID
		for(XBRecord record :mSourceList){
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
			XBRecord newRecord = new XBRecord(record.mName, newCircuitId, record.mCapacity, record.mNum, record.mType, 
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
