package com.example.wzelectricitysupplier.function;

import java.io.File;
import java.util.ArrayList;
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
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;
import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;

public class Export {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void ExportCircuit(MainActivity mMainActivity, int circuitId){
		
		MainFragment mMainFragment = mMainActivity.getMainFragment();
		CircuitTable mCircuitTable = mMainFragment.getCircuitTable();
		CircuitRecord mCircuitRecord = mCircuitTable.SelectCircuitRecord(circuitId);
		String mCircuitName = mCircuitRecord.mName;
		String mCircuitRootPath = Util.getExportDirRootPath() + mCircuitName;
		File rootdir = new File(mCircuitRootPath);
		// 删除原先
		if(rootdir.exists()){
			Util.delFolder(mCircuitRootPath);
		}
		rootdir.mkdir();
		
		// 导出数据库
		Database mDatabase = DataBaseTool.OpenDatabase(mMainActivity, mCircuitName + ".sqlite", mCircuitRootPath);
		mCircuitTable.export(mDatabase, circuitId);
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
		
		// 导出图片
		List mBDSList = mBDSTable.getSubstationList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DeviceBDS, mBDSList, circuitId, mCircuitRootPath);
		
		List mSwitchList = mSwitchTable.getSwitchList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DeviceSwitch, mSwitchList, circuitId, mCircuitRootPath);
		
		List mGTList = mGTTable.getTowerList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DeviceGT, mGTList, circuitId, mCircuitRootPath);
		
		List mGLList = mGLTable.getDisconnectoreList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DeviceGL, mGLList, circuitId, mCircuitRootPath);
		
		List mLKList = mLKTable.getLineConnectorList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DeviceLK, mLKList, circuitId, mCircuitRootPath);
		
		List mPDList = mPDTable.getSwitchingRoomList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DevicePD, mPDList, circuitId, mCircuitRootPath);
		
		List mKBList = mKBTable.getSwitchingStationList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DeviceKG, mKBList, circuitId, mCircuitRootPath);
		
		List mHWList = mHWTable.getRingMainUnitList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DeviceHW, mHWList, circuitId, mCircuitRootPath);
		
		List mGBList = mGBTable.getBarTypeVariableList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DeviceGB, mGBList, circuitId, mCircuitRootPath);
		
		List mXBList = mXBTable.getBoxChangeList(circuitId);
		ExportPicture(mDefectTable, DeviceType.DeviceXB, mXBList, circuitId, mCircuitRootPath);
		
		List mDXList = mDXTable.getElectricWireList(circuitId);
		ExportPicture(mDefectTable, DeviceType.Line, mDXList, circuitId, mCircuitRootPath);
		
		List mNewBDSList = mBDSTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewBDSList, mBDSList);
		
		List mNewSwitchList = mSwitchTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewSwitchList, mSwitchList);
		List mNewGTList = mGTTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewGTList, mGTList);
		List mNewGLList = mGLTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewGLList, mGLList);
		List mNewLKList = mLKTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewLKList, mLKList);
		List mNewPDList = mPDTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewPDList, mPDList);
		List mNewKBList = mKBTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewKBList, mKBList);
		List mNewHWList = mHWTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewHWList, mHWList);
		List mNewGBList = mGBTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewGBList, mGBList);
		List mNewXBList = mXBTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewXBList, mXBList);
		List mNewDXList = mDXTable.export(mDatabase, circuitId);
		mDefectTable.export(mDatabase, mNewDXList, mDXList);
		
		ExportTableToExcelUtil mExportTableToExcelUtil = new ExportTableToExcelUtil(mDatabase, mMainFragment);
		mExportTableToExcelUtil.exportExlById(circuitId);
		
	}
	
	/**
	 * 导出对应设备的缺陷照片
	 * @param id
	 */
	public static void ExportPicture(DefectTable mDefectTable, String mBelongDevice, List<DeviceRecord> deviceList, int circuitId, String circuitPath){
		// 导出缺陷照片
		for(DeviceRecord deviceRecord:deviceList){
			ArrayList<DefectRecord> defectList = mDefectTable.SelectDefects(deviceRecord.getId(), mBelongDevice);
			for(DefectRecord defectRecord:defectList){
				List<String> mPictureList = defectRecord.getPictureList();
				String dir = Util.getPictureDirRootPath();
				String seconddir = circuitPath + "/" + Constants.EXPROT_DIR_DEFECTNAME + "/" + mBelongDevice ;
				String todir = seconddir + "/" + deviceRecord.mName + "/" + defectRecord.mName;
				File devicedir = new File(seconddir);
				if(!devicedir.exists()){
					devicedir.mkdirs();
				}
				for(String name : mPictureList){
					Util.CopyFile(new File(dir, name), new File(todir, name), false);
				}
			}
		}
		// 导出普通照片
		for(DeviceRecord deviceRecord:deviceList){
			String mPictureStr = deviceRecord.mPicture;
			String dir = Util.getPictureDirRootPath();
			String seconddir = circuitPath + "/" + Constants.EXPROT_DIR_NORMALNAME + "/" + mBelongDevice;
			File devicedir = new File(seconddir);
			if(!devicedir.exists()){
				devicedir.mkdirs();
			}
			if(!"".equals(mPictureStr)){
				String todir = seconddir + "/" + deviceRecord.mName;
				String[] mPictureArr = mPictureStr.split("&");
				for(String name:mPictureArr){
					Util.CopyFile(new File(dir, name), new File(todir, name), false);
				}
			}
			
		}
		
	}
	
}
