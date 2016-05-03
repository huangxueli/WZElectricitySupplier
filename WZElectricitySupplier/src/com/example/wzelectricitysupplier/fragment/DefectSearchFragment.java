package com.example.wzelectricitysupplier.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.DefectSearchAdapter;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.database.CircuitTable;
import com.example.wzelectricitysupplier.database.DefectTable;
import com.example.wzelectricitysupplier.dialog.DefectWindow;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.Util;

public class DefectSearchFragment extends Fragment implements OnItemClickListener{
	
	public static final String TAG = "DefectSearchFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private CircuitTable mCircuitTable;
	private DefectTable mDefectTable;
	
	private DefectWindow mDefectWindow;
	private EditText mSearchEdit;
	private ImageButton mSearchBtn;
	private ListView mDefectList;
	private DefectSearchAdapter mDefectSearchAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity) this.getActivity();
		mMainFragment = mMainActivity.getMainFragment();
		mCircuitTable = mMainFragment.getCircuitTable();
		mDefectTable = mMainFragment.getDefectTable();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = mMainActivity.getLayoutInflater().inflate(R.layout.fragment_defect_manager, null);
		
		mSearchEdit = (EditText) view.findViewById(R.id.SearchEdit);
		
		mDefectList = (ListView) view.findViewById(R.id.defectList);
		mDefectSearchAdapter = new DefectSearchAdapter(mMainActivity, mCircuitTable.getCircuitID());

		mDefectList.setAdapter(mDefectSearchAdapter);
		mDefectList.setOnItemClickListener(this);
		
		mSearchBtn = (ImageButton) view.findViewById(R.id.SearchBtn);
		mSearchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String mSearchStr = mSearchEdit.getText().toString();
				if(!mSearchStr.equals("")){
					String condition = "WHERE " + DefectRecord.ZD_NAME.NAME + " = '" + mSearchStr + "' or " + 
							DefectRecord.ZD_DESCRIBE.NAME + " LIKE '%" + mSearchStr + "%' or " + 
							DefectRecord.ZD_TYPE.NAME + " LIKE '%" + mSearchStr + "%' or " +
							DefectRecord.ZD_BELONG_DEVICE.NAME +  " LIKE '%" + mSearchStr + "%' ";
					ArrayList<DefectRecord> mSearchedDefectList = mDefectTable.SelectDefects(condition);
					ArrayList<DefectRecord> mFilterDefectList = new ArrayList<DefectRecord>();
					for(DefectRecord record : mSearchedDefectList){
						DeviceRecord deviceRecord = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, Integer.valueOf(record.mBelongId), record.mBelongDevice);
						if(deviceRecord==null){
							continue;
						}
						for(String branchname : mCircuitTable.getBranchName()){
							if(deviceRecord.mName.contains(branchname)){
								mFilterDefectList.add(record); 
							}
						}
					}
					// 实现按设备名称查询
					ArrayList<DefectRecord> mAllDefectList = mDefectTable.SelectDefects("");
					// 过滤不是当前线路的缺陷记录
					ArrayList<DefectRecord> mCircuitDefectList = new ArrayList<DefectRecord>();
					for(DefectRecord record : mAllDefectList){
						DeviceRecord deviceRecord = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, Integer.valueOf(record.mBelongId), record.mBelongDevice);
						for(String branchname : mCircuitTable.getBranchName()){
							if(deviceRecord.mName.contains(branchname)){
								mCircuitDefectList.add(record); 
							}
						}
					}
					// 确保不重复显示缺陷记录
					ArrayList<DefectRecord> mDeviceNameSearchedDefectList = new ArrayList<DefectRecord>(); // 按设备名搜索到的缺陷记录
					for(DefectRecord record : mCircuitDefectList){
						DeviceRecord deviceRecord = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, Integer.valueOf(record.mBelongId), record.mBelongDevice);
						String[] namepart = deviceRecord.mName.split(" ");
						if(namepart.length==2){
							if(namepart[1].contains(mSearchStr)){
								boolean exist = false; // 默认不存在
								for(DefectRecord defectRecord : mFilterDefectList){
									if(record.getId()==defectRecord.getId()){ // 若原先查询结果中已存在该缺陷记录 不添加
										exist = true;
										break;
									}
								}
								if(!exist){
									mDeviceNameSearchedDefectList.add(record);
								}
							}
						}
					}
					
					mFilterDefectList.addAll(mDeviceNameSearchedDefectList);
					mDefectSearchAdapter.reLoadData(mFilterDefectList);
				}else{
					mDefectSearchAdapter.reLoadData(mCircuitTable.getCircuitID());
				}
				Util.HideKeyboard(mMainActivity);
			}
		});
		
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		mDefectSearchAdapter.reLoadData(mCircuitTable.getCircuitID());
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		if(!hidden){
			mDefectSearchAdapter.reLoadData(mCircuitTable.getCircuitID());
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		DefectRecord record = (DefectRecord)mDefectSearchAdapter.getItem(position);
		mDefectWindow = new DefectWindow(mMainActivity, record, mDefectSearchAdapter);
		mDefectWindow.showWindow();
		mDefectWindow.toCheckView();
		mDefectWindow.hideBackBtn();
	}
	
	public void fresh(){
		mSearchEdit.setText("");
		mDefectSearchAdapter.reLoadData(mCircuitTable.getCircuitID());
	}
}
