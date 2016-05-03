package com.example.wzelectricitysupplier.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.database.BDSTable;
import com.example.wzelectricitysupplier.database.DXTable;
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
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;

public class DefectSearchAdapter extends BaseAdapter {
	
	private String mCircuitID;
	private DefectTable mDefectTable;
	private ArrayList<DefectRecord> mDefectList = new ArrayList<DefectRecord>();
	private MainFragment mMainFragment;
	
	public DefectSearchAdapter(MainActivity mMainActivity, String mCircuitID) {
		this.mCircuitID = mCircuitID;
		this.mMainFragment = mMainActivity.getMainFragment();
		this.mDefectTable = mMainFragment.getDefectTable();
		if(!mCircuitID.equals("")){
			this.mDefectList = getAllDefectByCircuit(mCircuitID);
		}
	}
	// 获取所有设备（线缆）的缺陷
	public ArrayList<DefectRecord> getAllDefectByCircuit(String mCircuitID){
		ArrayList<DeviceRecord> mDeviceList = new ArrayList<>();
		ArrayList<? extends DeviceRecord> mList = null;
		BDSTable mBDSTable = (BDSTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceBDS);
		mList = mBDSTable.selectSubstations(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		GBTable mGBTable = (GBTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceGB);
		mList = mGBTable.selectBarTypeVariables(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		GLTable mGLTable = (GLTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceGL);
		mList = mGLTable.selectDisconnectores(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		GTTable mGTTable = (GTTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceGT);
		mList = mGTTable.selectTowers(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		HWTable mHWTable = (HWTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceHW);
		mList = mHWTable.selectRingMainUnits(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		KBTable mKBTable = (KBTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceKG);
		mList = mKBTable.selectSwitchingStations(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		LKTable mLKTable = (LKTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceLK);
		mList = mLKTable.selectLineConnectors(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		PDTable mPDTable = (PDTable)AppUtil.getTableByType(mMainFragment, DeviceType.DevicePD);
		mList = mPDTable.selectSwitchingRooms(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		SwitchTable mSwitchTable = (SwitchTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceSwitch);
		mList = mSwitchTable.selectSwitchs(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		XBTable mXBTable = (XBTable)AppUtil.getTableByType(mMainFragment, DeviceType.DeviceXB);
		mList = mXBTable.selectBoxChanges(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		DXTable mDXTable = (DXTable)AppUtil.getTableByType(mMainFragment, DeviceType.Line);
		mList = mDXTable.selectElectricWires(DeviceRecord.ZD_CIRCUIT.NAME + " = " + mCircuitID);
		mDeviceList.addAll(mDeviceList.size(), mList);
		
		ArrayList<String> mDefectIDList = new ArrayList<String>();
		for(DeviceRecord record:mDeviceList){
			String mDefectIDStr = record.mDefectID;
			if(!"".equals(mDefectIDStr)){
				if(mDefectIDStr.contains("&")){
					String[] arr = mDefectIDStr.split("&");
					for(String id:arr){
						mDefectIDList.add(id);
					}
				}else{
					mDefectIDList.add(mDefectIDStr);
				}
			}
		}
		ArrayList<DefectRecord> mDefectRecordList = new ArrayList<DefectRecord>();
		for(String primary:mDefectIDList){
			mDefectRecordList.add(mDefectTable.SelectDefect(Integer.valueOf(primary)));
		}
		return mDefectRecordList;
	}
	
	public void reLoadData() {
		reLoadData(mCircuitID);
	}
	
	public void reLoadData(String mCircuitID) {
		if(mCircuitID.equals("")){
			mDefectList.clear();
		}else{
			mDefectList = getAllDefectByCircuit(mCircuitID);
		}
		notifyDataSetChanged();
	}
	
	public void reLoadData(ArrayList<DefectRecord> mDefectList) {
		this.mDefectList = mDefectList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mDefectList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDefectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public final class ViewHolder {
		TextView Name;
		TextView Type;
		TextView Describe;
		ViewGroup Item;
		DefectRecord record;
		DeviceRecord devicerecord;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = MyApplication.Inflater.inflate(R.layout.listitem_defect_manager, parent, false);
			holder = new ViewHolder();
			holder.Item = (ViewGroup) convertView.findViewById(R.id.item);
			holder.Name = (TextView) convertView.findViewById(R.id.name);
			holder.Type = (TextView) convertView.findViewById(R.id.type);
			holder.Describe = (TextView) convertView.findViewById(R.id.remark);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.record = mDefectList.get(position);
		holder.devicerecord = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, Integer.valueOf(holder.record.mBelongId),
				holder.record.mBelongDevice);

		holder.Name.setText(holder.devicerecord.mName + "（缺陷" + holder.record.mName + "）");
		holder.Type.setText(holder.record.mType);
		holder.Describe.setText(holder.record.mDescribe);
		return convertView;
	}

}
