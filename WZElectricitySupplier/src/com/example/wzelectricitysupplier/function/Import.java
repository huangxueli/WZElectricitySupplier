package com.example.wzelectricitysupplier.function;

import java.io.File;
import java.util.List;

import jsqlite.Database;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.bean.CircuitRecord;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.database.BDSTable;
import com.example.wzelectricitysupplier.database.CircuitTable;
import com.example.wzelectricitysupplier.database.DXTable;
import com.example.wzelectricitysupplier.database.DataBaseTool;
import com.example.wzelectricitysupplier.database.DefectTable;
import com.example.wzelectricitysupplier.database.GBTable;
import com.example.wzelectricitysupplier.database.GLTable;
import com.example.wzelectricitysupplier.database.GTTable;
import com.example.wzelectricitysupplier.database.HWTable;
import com.example.wzelectricitysupplier.database.KBTable;
import com.example.wzelectricitysupplier.database.LKTable;
import com.example.wzelectricitysupplier.database.PDTable;
import com.example.wzelectricitysupplier.database.SwitchTable;
import com.example.wzelectricitysupplier.database.XBTable;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;

public class Import {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void ImportCircuits(MainActivity mMainActivity){
		Database db =  mMainActivity.getDatabase();
		MainFragment mMainFragment = mMainActivity.getMainFragment();
		CircuitTable mCircuitTable = mMainFragment.getCircuitTable();
		DefectTable mDefectTable = mMainFragment.getDefectTable();
		BDSTable mBDSTable = mMainFragment.getBDSTable();
		SwitchTable mSwitchTable = mMainFragment.getSwitchTable();
		GTTable mGTTable = mMainFragment.getGTTable();
		GLTable mGLTable = mMainFragment.getGLTable();
		LKTable mLKTable = mMainFragment.getLKTable();
		PDTable mPDTable = mMainFragment.getPDTable();
		KBTable mKBTable = mMainFragment.getKBTable();
		HWTable mHWTable = mMainFragment.getHWTable();
		GBTable mGBTable = mMainFragment.getGBTable();
		XBTable mXBTable = mMainFragment.getXBTable();
		DXTable mDXTable = mMainFragment.getDXTable();
		
		File dir = new File(Util.getImportDirRootPath());
		File[] files = dir.listFiles();
		for(File file:files){
			if(file.isDirectory()){
				boolean importResult = true;
				String mCircuitName = file.getName();
				String mImportCircuitPath = Util.getImportDirRootPath() + mCircuitName;
				Database mSourceDatabase = DataBaseTool.OpenDatabase(mMainActivity, mCircuitName + ".sqlite", mImportCircuitPath);
				CircuitTable mSourceCircuitTable = new CircuitTable(mSourceDatabase);
				CircuitRecord mCircuitRecord = mSourceCircuitTable.SelectCircuits("").get(0);
				String mCircuitID = String.valueOf(mCircuitTable.add(mCircuitRecord));
				List<DefectRecord> mAddedDefects = mDefectTable.inport(mSourceDatabase);
				importResult = importResult && mBDSTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mGTTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mGLTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mLKTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mPDTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mKBTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mHWTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mGBTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mXBTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mSwitchTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				importResult = importResult && mDXTable.inport(db, mSourceDatabase, mCircuitID, mAddedDefects);
				
				// 线路导入成功后删除文件夹
				if(importResult) {
					// 导入缺陷图片
					ImportDefectPicture(mSourceDatabase, mAddedDefects, mCircuitName);
					// 导入设备图片
					int circuitId = Integer.valueOf(mCircuitID);
					List mBDSList = mBDSTable.getSubstationList(circuitId);
					ImportDevicePicture(mMainFragment, mBDSList, mCircuitName);
					
					List mSwitchList = mSwitchTable.getSwitchList(circuitId);
					ImportDevicePicture(mMainFragment, mSwitchList, mCircuitName);
					
					List mGTList = mGTTable.getTowerList(circuitId);
					ImportDevicePicture(mMainFragment, mGTList, mCircuitName);
					
					List mGLList = mGLTable.getDisconnectoreList(circuitId);
					ImportDevicePicture(mMainFragment, mGLList, mCircuitName);
					
					List mLKList = mLKTable.getLineConnectorList(circuitId);
					ImportDevicePicture(mMainFragment, mLKList, mCircuitName);
					
					List mPDList = mPDTable.getSwitchingRoomList(circuitId);
					ImportDevicePicture(mMainFragment, mPDList, mCircuitName);
					
					List mKBList = mKBTable.getSwitchingStationList(circuitId);
					ImportDevicePicture(mMainFragment, mKBList, mCircuitName);
					
					List mHWList = mHWTable.getRingMainUnitList(circuitId);
					ImportDevicePicture(mMainFragment, mHWList, mCircuitName);
					
					List mGBList = mGBTable.getBarTypeVariableList(circuitId);
					ImportDevicePicture(mMainFragment, mGBList, mCircuitName);
					
					List mXBList = mXBTable.getBoxChangeList(circuitId);
					ImportDevicePicture(mMainFragment, mXBList, mCircuitName);
					
					List mDXList = mDXTable.getElectricWireList(circuitId);
					ImportDevicePicture(mMainFragment, mDXList, mCircuitName);
					
					// 关闭数据库
					DataBaseTool.CloseDatabase(mSourceDatabase);
					// 删除导入文件夹及文件夹中的所有文件
					Util.delFolder(mImportCircuitPath);
				}
					
			}
		}
		
		
	}
	
	/**
	 * 导入缺陷照片
	 */
	public static void ImportDefectPicture(Database mSourceDatabase, List<DefectRecord> mAddedDefects, String mCircuitName){
		// 导出缺陷照片
		for(DefectRecord defect:mAddedDefects){
			DeviceRecord device = AppUtil.getDeviceRecordByDefectRecord(mSourceDatabase, Integer.valueOf(defect.mBelongId), defect.mBelongDevice);
			String mFromDir = Util.getImportDirRootPath() + mCircuitName + "/" + Constants.EXPROT_DIR_DEFECTNAME + "/"
					+ device.getDeviceType() + "/" + device.mName + "/" + defect.mName;
			String mToDir = Util.getPictureDirRootPath();
			List<String> mNameList = defect.getPictureList();
			for(String name :mNameList){
				Util.CopyFile(new File(mFromDir, name), new File(mToDir, name), false);
			}
		}
	}
	/**
	 * 导入缺陷照片
	 */
	public static void ImportDevicePicture(MainFragment mMainFragment, List<DeviceRecord> mDevices, String mCircuitName){
		// 导入设备照片
		for(DeviceRecord device:mDevices){
			String mFromDir = Util.getImportDirRootPath() + mCircuitName + "/" + Constants.EXPROT_DIR_NORMALNAME + "/"
					+ device.getDeviceType() + "/" + device.mName;
			String mToDir = Util.getPictureDirRootPath();
			List<String> mNameList = device.getPictureList();
			for(String name :mNameList){
				Util.CopyFile(new File(mFromDir, name), new File(mToDir, name), false);
			}
		}
	}
}
