package com.example.wzelectricitysupplier.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.MenuAdapter;
import com.example.wzelectricitysupplier.bean.CircuitRecord;
import com.example.wzelectricitysupplier.fragment.MenuItem.ButtonType;
import com.example.wzelectricitysupplier.function.Cache;
import com.example.wzelectricitysupplier.function.Export;
import com.example.wzelectricitysupplier.function.Import;
import com.example.wzelectricitysupplier.function.ArcGISTiledLayerManager;
import com.example.wzelectricitysupplier.function.route.RouteTakenManager;
import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;

public class MenuFragment extends Fragment{

	public final static String TAG = "MenuFragment";
	
	private MainActivity mMainActivity;
	private MainFragment mMainFragment;
	private Cache mCache;
	private ArcGISTiledLayerManager mTiledLayerManager;
	private RouteTakenManager mRouteTakenManager;
	
	private ArrayList<MenuItem> mGroups;
	private ArrayList<ArrayList<MenuItem>> mChilds;
	private MenuAdapter mAdapter;
	
	private ImageButton mPhotoBtn;
	private TextView mUsername;
	private TextView mLonTextView;
	private TextView mLatTextView;
	private ExpandableListView mMenuListView;
	
	private boolean mUseLocalMap; 
	private boolean mIconExplain; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity) getActivity();
		mMainFragment = mMainActivity.getMainFragment();
		mCache = mMainActivity.getCache();
		mTiledLayerManager = mMainFragment.getTiledLayerManager();
		mRouteTakenManager = mMainFragment.getRouteTakenManager();
		mUseLocalMap = (boolean)mCache.read(Cache.KEY_CODE_LOCALMAP);
		mIconExplain = (boolean)mCache.read(Cache.KEY_CODE_ICONEXPLAIN);
		if(mIconExplain){
			mMainFragment.hideTuli(false);
		}else{
			mMainFragment.hideTuli(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, null);

		mUsername = (TextView) view.findViewById(R.id.user_name);
		mUsername.setText(Constants.UserName);
		mPhotoBtn = (ImageButton) view.findViewById(R.id.user_photo);
		mPhotoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		mLonTextView = (TextView) view.findViewById(R.id.longitude);
		mLatTextView = (TextView) view.findViewById(R.id.latitude);

		mMenuListView = (ExpandableListView) view.findViewById(R.id.menu_list);

		mGroups = new ArrayList<MenuItem>();
		mChilds = new ArrayList<ArrayList<MenuItem>>();
		ArrayList<MenuItem> mChild1 = new ArrayList<MenuItem>();
		ArrayList<MenuItem> mChild2 = new ArrayList<MenuItem>();
		ArrayList<MenuItem> mChild3 = new ArrayList<MenuItem>();
		mChilds.add(mChild1);
		mChilds.add(mChild2);
		mChilds.add(mChild3);

		mAdapter = new MenuAdapter(getActivity(), mGroups, mChilds);
		mMenuListView.setAdapter(mAdapter);
		mMenuListView.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		mMenuListView.setOnChildClickListener(new OnChildClickListener() {

			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				switch(groupPosition){
				case 0:
					switch(childPosition){
					case 0: // 线路管理
						mMainActivity.replaceSecondMenu(new CircuitManagerFragment(), CircuitManagerFragment.TAG);
						mMainActivity.replaceRightBtn(R.drawable.button_selector_titlebar_circuit);
						break;
					case 1: // 电力设备搜索
						mMainActivity.replaceSecondMenu(new DeviceSearchFragment(), DeviceSearchFragment.TAG);
						mMainActivity.replaceRightBtn(R.drawable.button_selector_titlebar_device_search);
						break;
					case 2: // 缺陷记录管理
						mMainActivity.replaceSecondMenu(new DefectSearchFragment(), DefectSearchFragment.TAG);
						mMainActivity.replaceRightBtn(R.drawable.button_selector_titlebar_defect_search);
						break;
					case 3: // 巡检路线管理
						mMainActivity.replaceSecondMenu(new RouteManagerFragment(), RouteManagerFragment.TAG);
						mMainActivity.replaceRightBtn(R.drawable.button_selector_titlebar_route);
						break;
					case 5: // 导入数据
						Util.ShowDialog(mMainActivity, "确定要导入数据吗？", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mMainActivity.showProgressDialog("正在导入数据...");
								new Thread(){
									public void run() {
										Import.ImportCircuits(mMainActivity);
										mMainActivity.removeProgressDialog();
									};
								}.start();
							}
						}, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						});
						break;
					case 6: // 导出数据
						Util.ShowDialog(mMainActivity, "确定要导出数据吗？", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mMainActivity.showProgressDialog("正在导出数据...");
								new Thread(){
									public void run() {
										ArrayList<CircuitRecord> mCircuitList = mMainFragment.getCircuitTable().SelectCircuits("");
										for(CircuitRecord record : mCircuitList){
											Export.ExportCircuit(mMainActivity, record.getId());
										}
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
						}, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						});
						
						break;
					}
					break;
				case 1:
					switch(childPosition){
					case 2:
						mMainActivity.replaceRightBtn(R.drawable.button_selector_titlebar_setting);
						break;
					}
					break;
				}
				return true;
			}
		});

		initMenuItem(mChild1, mChild2, mChild3); // 初始化菜单列表数据

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		disconnect();
	}
	
	public void initMenuItem(ArrayList<MenuItem> mChild1, ArrayList<MenuItem> mChild2, 
			ArrayList<MenuItem> mChild3) {
		mGroups.clear();
		mChild1.clear();
		mChild2.clear();
		
		mGroups.add(new MenuItem("电力设置选项", R.drawable.feature_lightning));
		mGroups.add(new MenuItem("地图设置选项", R.drawable.feature_map));
		MenuItem item = null;
		
		item = new MenuItem("线路管理", R.drawable.feature_circuit);
		mChild1.add(item);
		item = new MenuItem("电力设备管理", R.drawable.feature_device_search);
		mChild1.add(item);
		item = new MenuItem("缺陷记录管理", R.drawable.feature_defect_search);
		mChild1.add(item);
		item = new MenuItem("巡检路线管理", R.drawable.feature_routemanage);
		mChild1.add(item);
		item = new MenuItem("开启巡检路线记录", R.drawable.feature_routeopen, ButtonType.SliderButton, false, new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if(MyApplication.FragmentMap.containsKey(RouteManagerFragment.TAG)){
						RouteManagerFragment mRouteManagerFragment = (RouteManagerFragment)MyApplication.FragmentMap.get(RouteManagerFragment.TAG);
						mRouteManagerFragment.wipe();
					}
					mRouteTakenManager.startRecordRoute();
				}else{
					mRouteTakenManager.stopRecordRoute();
				}
			}
		});
		mChild1.add(item);
		item = new MenuItem("导入数据", R.drawable.feature_import);
		mChild1.add(item);
		item = new MenuItem("导出数据", R.drawable.feature_export);
		mChild1.add(item);
		
		
		item = new MenuItem("使用离线地图", R.drawable.feature_localmap, ButtonType.SliderButton, mUseLocalMap, new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mUseLocalMap = isChecked;
				mTiledLayerManager.switchMap(mTiledLayerManager.getMode());
				mCache.write(Cache.KEY_CODE_LOCALMAP, mUseLocalMap);
			}
		});
		mChild2.add(item);
		item = new MenuItem("显示图例", R.drawable.feature_explain, ButtonType.SliderButton, mIconExplain, new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mIconExplain = isChecked;
				if(isChecked){
					mMainFragment.hideTuli(false);
				}else{
					mMainFragment.hideTuli(true);
				}
				mCache.write(Cache.KEY_CODE_ICONEXPLAIN, mIconExplain);
			}
		});
		mChild2.add(item);
		item = new MenuItem("系统设置", R.drawable.feature_setting);
		mChild2.add(item);
		
		mAdapter.notifyDataSetChanged();
		for (int i = 0; i < mGroups.size(); i++) {
			mMenuListView.expandGroup(i);
		}
	}
	// 刷新当前经纬度
	public void setLongitudeLatitude(double x, double y){
		String lon = String.valueOf(x);
		String lat = String.valueOf(y);
		mLonTextView.setText(lon);
		mLatTextView.setText(lat);
	}
	
}
