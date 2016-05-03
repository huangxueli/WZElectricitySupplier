package com.example.wzelectricitysupplier.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.DeviceSearchAdapter;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.database.CircuitTable;
import com.example.wzelectricitysupplier.function.ArcgisTool;
import com.example.wzelectricitysupplier.setting.Util;

public class DeviceSearchFragment extends Fragment implements OnItemClickListener{
	
	public static final String TAG = "DeviceSearchFragment";

	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private CircuitTable mCircuitTable;
	
	private EditText mSearchEdit;
	private ImageButton mSearchBtn;
	private ListView mDeviceList;
	private DeviceSearchAdapter mDeviceSearchAdapter;
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
		
		View view = mMainActivity.getLayoutInflater().inflate(R.layout.fragment_device_manager, null);
		
		mSearchEdit = (EditText) view.findViewById(R.id.SearchEdit);
		
		mDeviceList = (ListView) view.findViewById(R.id.deviceList);
		mDeviceSearchAdapter = new DeviceSearchAdapter(mMainActivity, mCircuitTable.getCircuitID());

		mDeviceList.setAdapter(mDeviceSearchAdapter);
		mDeviceList.setOnItemClickListener(this);
		
		mSearchBtn = (ImageButton) view.findViewById(R.id.SearchBtn);
		mSearchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String mSearchStr = mSearchEdit.getText().toString();
				if(!mSearchStr.equals("")){
					mDeviceSearchAdapter.reLoadData(mCircuitTable.getCircuitID(), mSearchStr);
				}else{
					mDeviceSearchAdapter.reLoadData(mCircuitTable.getCircuitID());
				}
				Util.HideKeyboard(mMainActivity);
			}
		});
		
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		mDeviceSearchAdapter.reLoadData(mCircuitTable.getCircuitID());
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			mDeviceSearchAdapter.reLoadData(mCircuitTable.getCircuitID());
		}
	}
	
	private long mDelayMillis = 1000L;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		DeviceRecord record = (DeviceRecord)mDeviceSearchAdapter.getItem(position);
		final Point mMapPoint = (Point)record.getGeometry();
		final MapView mMapView = mMainFragment.getMap();
		mMapView.centerAt(mMapPoint, false);
		mMainActivity.showContent();
		mMapView.zoomToScale(mMapPoint, 4513.997733376551);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Point mScreenPoint = mMapView.toScreenPoint(mMapPoint);
				int graphicId = ArcgisTool.getGraphicId(mMapView, (float)mScreenPoint.getX(), (float)mScreenPoint.getY(), mMainFragment.getInterestPointLayer());
				mMainFragment.getInterestPointLayer().clearSelection();
				mMainFragment.getInterestPointLayer().setSelectedGraphics(new int[]{graphicId}, true);
			}
		}, mDelayMillis);
		mDelayMillis = 0;
	}
	
	public void fresh(){
		mSearchEdit.setText("");
		mDeviceSearchAdapter.reLoadData(mCircuitTable.getCircuitID());
	}
}
