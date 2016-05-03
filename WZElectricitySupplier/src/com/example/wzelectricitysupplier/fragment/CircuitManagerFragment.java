package com.example.wzelectricitysupplier.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.CircuitAdapter;
import com.example.wzelectricitysupplier.bean.CircuitRecord;
import com.example.wzelectricitysupplier.database.CircuitTable;
import com.example.wzelectricitysupplier.dialog.CircuitDialog;
import com.example.wzelectricitysupplier.dialog.CircurtManageDialog;
import com.example.wzelectricitysupplier.function.Cache;
import com.example.wzelectricitysupplier.function.Export;
import com.example.wzelectricitysupplier.setting.Util;

public class CircuitManagerFragment extends Fragment implements OnItemClickListener{
	
	public static final String TAG = "CircuitManagerFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private CircuitTable mCircuitTable;
	private ListView mCircuitListView;
	private TextView mCurrCircuitName;
	private EditText mSearchEdit;
	private ImageButton mSearchBtn;
	private ImageButton mAddBtn;
	private CircuitDialog mCircuitDialog;
	private CircurtManageDialog mCircurtManageDialog;
	private CircuitAdapter mCircuitAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity) this.getActivity();
		mMainFragment = mMainActivity.getMainFragment();
		mCircuitTable = mMainFragment.getCircuitTable();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = mMainActivity.getLayoutInflater().inflate(R.layout.fragment_circuit_manager, null);
		
		mCurrCircuitName = (TextView)view.findViewById(R.id.currcircuitname);
		String mCircuitID = mCircuitTable.getCircuitID();
		if(!"".equals(mCircuitID)){
			CircuitRecord record = mCircuitTable.SelectCircuitRecord(Integer.valueOf(mCircuitID));
			if(record!=null)
				mCurrCircuitName.setText(record.mName);
		}
		mSearchEdit = (EditText) view.findViewById(R.id.SearchEdit);
		
		mCircuitListView = (ListView) view.findViewById(R.id.CircuitList);
		mCircuitAdapter = new CircuitAdapter(mMainActivity, mCircuitTable);

		mCircuitListView.setAdapter(mCircuitAdapter);
		mCircuitListView.setOnItemClickListener(this);
		
		mSearchBtn = (ImageButton) view.findViewById(R.id.SearchBtn);
		mSearchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String mSearchStr = mSearchEdit.getText().toString();
				if(!mSearchStr.equals("")){
					String condition = "WHERE " + CircuitRecord.ZD_NAME.NAME + " LIKE '%" + mSearchStr + "%' or " + 
							CircuitRecord.ZD_REMARK.NAME + 	" LIKE '%" + mSearchStr + "%'";
					ArrayList<CircuitRecord> mCircuitList = mCircuitTable.SelectCircuits(condition);
					mCircuitAdapter.reLoadData(mCircuitList);
				}else{
					mCircuitAdapter.reLoadData();
				}
				Util.HideKeyboard(mMainActivity);
			}
		});
		
		mAddBtn = (ImageButton) view.findViewById(R.id.AddBtn);
		mAddBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCircuitDialog = new CircuitDialog(mMainActivity);
				mCircuitDialog.setOnSaveButtonListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						if(mCircuitDialog.isRemarkStandard()){
							String name =  mCircuitDialog.getName();
							String remark = mCircuitDialog.getRemark();
							int id = mCircuitTable.add(new CircuitRecord(name, remark));
							Log.i(TAG, "新增线路ID：" + id);
							mCircuitDialog.Dismiss();
							mCircuitAdapter.reLoadData();
						}else{
							Util.ShowDialog(mMainActivity, "分支线描述不能为空");
						}
						
					}
				});
				mCircuitDialog.ShowDialog();
			}
		});
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

		mCircurtManageDialog = new CircurtManageDialog(mMainActivity);
		mCircurtManageDialog.setOnExportButtonListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCircurtManageDialog.Dismiss();
				mMainActivity.showProgressDialog("正在导出数据...");
				new Thread(){
					public void run() {
						CircuitRecord record = (CircuitRecord)mCircuitAdapter.getItem(position);
						Export.ExportCircuit(mMainActivity, record.getId());
						mMainActivity.removeProgressDialog();
						mMainFragment.getHandler().post(new Runnable() {
							@Override
							public void run() {
								Util.ShowDialog(mMainActivity, "数据导出完成！");
							}
						});
					};
				}.start();
			}
		});
		mCircurtManageDialog.setOnDeleteButtonListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Util.ShowDialog(mMainActivity, "删除后数据不可恢复，确定要删除吗？", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CircuitRecord record = (CircuitRecord)mCircuitAdapter.getItem(position);
						String idStr = String.valueOf(record.getId());
						mCircuitTable.delete(record.getId());
						if(mCircuitTable.getCircuitID().equals(idStr)){
							mCircuitTable.setCircuitID("");
							mMainActivity.getCache().write(Cache.KEY_CODE_CIRCUITID, -1);
							mMainFragment.doLoadNoCircuit();
						}
						
						mCircurtManageDialog.Dismiss();
						mCircuitAdapter.reLoadData();
					}
				}, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mCircurtManageDialog.Dismiss();
					}
				});
			}
		});
		mCircurtManageDialog.setOnEditButtonListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CircuitRecord record = (CircuitRecord)mCircuitAdapter.getItem(position);
				mCircuitDialog = new CircuitDialog(mMainActivity);
				mCircuitDialog.setName(record.mName);
				mCircuitDialog.setRemark(record.mRemark);
				mCircuitDialog.setOnSaveButtonListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						record.mName = mCircuitDialog.getName();
						record.mRemark = mCircuitDialog.getRemark();
						
						mCircuitTable.update(record);
						mCircuitDialog.Dismiss();
						mCircuitAdapter.reLoadData();
					}
				});
				mCircurtManageDialog.Dismiss();
				mCircuitDialog.ShowDialog();
			}
			
		});
		mCircurtManageDialog.setOnLoadButtonListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// 加载该路线相关的设备和线缆
				
				mCircurtManageDialog.Dismiss();
				mMainActivity.showProgressDialog("正在加载线路...");
				new Thread(){
					public void run() {
						final CircuitRecord record = (CircuitRecord)mCircuitAdapter.getItem(position);
						mMainActivity.getMainFragment().doLoadCircuit(String.valueOf(record.getId()));
						mMainActivity.removeProgressDialog();
						mMainFragment.getHandler().post(new Runnable() {
							@Override
							public void run() {
								mCurrCircuitName.setText(record.mName);
								mCircuitAdapter.notifyDataSetChanged();
								mMainFragment.refreshTuli();
								Util.Toast("加载完成！");
							}
						});
					};
				}.start();
			}
		});
		mCircurtManageDialog.ShowDialog();
	}
	
	public void fresh(){
		mCircuitAdapter.reLoadData();
	}
}
