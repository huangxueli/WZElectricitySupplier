package com.example.wzelectricitysupplier.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.RouteAdapter;
import com.example.wzelectricitysupplier.bean.RouteRecord;
import com.example.wzelectricitysupplier.database.RouteTable;
import com.example.wzelectricitysupplier.dialog.RouteManageDialog;
import com.example.wzelectricitysupplier.function.route.RouteTakenManager;
import com.example.wzelectricitysupplier.setting.Util;

public class RouteManagerFragment extends Fragment implements OnItemClickListener{
	
	public static final String TAG = "RouteManagerFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private RouteTable mRouteTable;
	private RouteTakenManager mRouteTakenManager;
	
	private TextView mCurrRouteNum;
	private EditText mSearchEdit;
	private ImageButton mSearchBtn;
	private ImageButton mWipeBtn;
	private ListView mRouteListView;
	private RouteManageDialog mRouteManageDialog;
	private RouteAdapter mRouteAdapter;
	private String mCurrRouteID = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity) this.getActivity();
		mMainFragment = mMainActivity.getMainFragment();
		mRouteTable = mMainFragment.getRouteTable();
		mRouteTakenManager = mMainFragment.getRouteTakenManager();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = mMainActivity.getLayoutInflater().inflate(R.layout.fragment_route_manager, null);
		
		mCurrRouteNum = (TextView)view.findViewById(R.id.routeNum);
		mSearchEdit = (EditText) view.findViewById(R.id.SearchEdit);
		
		mRouteListView = (ListView) view.findViewById(R.id.RouteList);
		mRouteAdapter = new RouteAdapter(mMainActivity, mRouteTable);

		mRouteListView.setAdapter(mRouteAdapter);
		mRouteListView.setOnItemClickListener(this);
		
		mSearchBtn = (ImageButton) view.findViewById(R.id.SearchBtn);
		mSearchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSearchEdit.getText().toString();
				Util.HideKeyboard(mMainActivity);
			}
		});
		
		mWipeBtn = (ImageButton) view.findViewById(R.id.WipeBtn);
		mWipeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				wipe();
			}
		});
		
		return view;
	}
	
	public void wipe(){
		mCurrRouteID = "";
		mCurrRouteNum.setText(mCurrRouteID);
		mRouteAdapter.setCurrRouteID(mCurrRouteID);
		mRouteAdapter.notifyDataSetChanged();
		mRouteTakenManager.clearLayer();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

		mRouteManageDialog = new RouteManageDialog(mMainActivity);
		mRouteManageDialog.setOnLoadButtonListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(!mRouteTakenManager.isEnableRecordRoute()){
					RouteRecord record = (RouteRecord)mRouteAdapter.getItem(position);
					mRouteTakenManager.drawHistoryRoute(record.getId());
					mCurrRouteNum.setText(""+(position+1));
					mRouteManageDialog.Dismiss();
					mRouteAdapter.setCurrRouteID(String.valueOf(record.getId()));
					mRouteAdapter.notifyDataSetChanged();
				}else{
					Util.ShowDialog(mMainActivity, "请先关闭巡检路线记录！");
				}
				
			}
		});
		mRouteManageDialog.setOnDeleteButtonListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Util.ShowDialog(mMainActivity, "删除后数据不可恢复，确定要删除吗？", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						RouteRecord record = (RouteRecord)mRouteAdapter.getItem(position);
						mRouteTable.delete(record.getId());
						mRouteManageDialog.Dismiss();
						mRouteAdapter.reLoadData();
					}
				}, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mRouteManageDialog.Dismiss();
					}
				});
				
			}
		});
		mRouteManageDialog.ShowDialog();
	}

}
