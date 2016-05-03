package com.example.wzelectricitysupplier;

import java.util.HashMap;

import jsqlite.Database;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.view.slidingmenu.SlidingMenu;
import com.example.wzelectricitysupplier.AndBase.Util.AbDialogUtil;
import com.example.wzelectricitysupplier.database.DBSetting;
import com.example.wzelectricitysupplier.database.DataBaseTool;
import com.example.wzelectricitysupplier.dialog.BaseWindow.OnCameraListener;
import com.example.wzelectricitysupplier.fragment.CircuitManagerFragment;
import com.example.wzelectricitysupplier.fragment.DefectSearchFragment;
import com.example.wzelectricitysupplier.fragment.DeviceSearchFragment;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.fragment.MenuFragment;
import com.example.wzelectricitysupplier.function.Cache;
import com.example.wzelectricitysupplier.setting.Util;

public class MainActivity extends AbActivity {

	public static final String TAG = "MainActivity";
	public static final int REQUESTCODE = 2;

	private Intent mServiceIntent;
	private Database mDatabase;
	private Cache mCache;
	
	private Fragment mCurrentFragment; // 右边菜单当前视图
	private MenuFragment mMenuFragment;
	private long mExitTime;
	private int mBackStackIndex = -1;
	private OnCameraListener mOnCameraListener;
	
	public abstract class OnCameraCapturedListener {
		public abstract HashMap<String, Object> onCameraCaptured();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		Util.FullScreen(this);
		MyApplication.Context = getApplicationContext();
		MyApplication.Resources = getResources();
		MyApplication.Inflater = mInflater;

		View view = mInflater.inflate(this.getResources().getLayout(R.layout.activity_main), null);
		this.setAbContentView(view);

		mDatabase = DataBaseTool.OpenDatabase(this, DBSetting.DB_NAME_DLJ);
		
		SharedPreferences sp = getSharedPreferences(Cache.SharedPreferencesFile, 0);
		mCache = new Cache(sp);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.container, new MainFragment(), MainFragment.TAG);
		transaction.addToBackStack(null);
		mBackStackIndex = transaction.commit();

		Util.GetScreenArgument(this);// 获取屏幕尺寸、分辨率等相关参数
		mServiceIntent = new Intent(this, LocalService.class);
		startService(mServiceIntent);// 启动本地服务
		initView();
		
		Util.getImportDirRootPath();
		Util.getExportDirRootPath();
		Util.getPictureDirRootPath();
		Util.getLocalMapDirRootPath();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");

	}

	public void popBackStack() {
		if (mBackStackIndex >= 0) {
			getFragmentManager().popBackStack(mBackStackIndex, 0);
		}
	}

	private SlidingMenu mSlidingMenu;
	private Button mRightBtn;
	private void initView() {
		// 侧滑组件
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setShadowWidth(R.dimen.shadow_width);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT); // 添加到指定Activity

		// 侧滑菜单添加左边视图
		mSlidingMenu.setMenu(R.layout.sliding_menu);
		mMenuFragment = new MenuFragment();
		getFragmentManager().beginTransaction().replace(R.id.menu_frame, mMenuFragment, MenuFragment.TAG).commit();
		// 侧滑菜单添加右边视图
		mSlidingMenu.setSecondaryMenu(R.layout.sliding_menu_second);
		CircuitManagerFragment mFragment = new CircuitManagerFragment();
		getFragmentManager().beginTransaction().add(R.id.menu_frame_second, mFragment, CircuitManagerFragment.TAG).commit();
		mCurrentFragment = mFragment;

		// 上标题栏
		mAbTitleBar.setVisibility(View.VISIBLE);
		mAbTitleBar.setLogo(R.drawable.button_selector_titlebar_menu);
//		mAbTitleBar.addRightView(MyApplication.Inflater.inflate(R.layout.titlebar_mid_view, null));
		LinearLayout mTitleTextLayout = mAbTitleBar.getTitleTextLayout();
		View view = mInflater.inflate(R.layout.titlebar_mid_view, null);
		mTitleTextLayout.removeAllViews();
		mTitleTextLayout.addView(view);
//		mAbTitleBar.setTitleBarBackground(R.drawable.bg_titlebar_no_text);
//		mAbTitleBar.setTitleText(R.string.app_name);
//		mAbTitleBar.setLogoLine(R.drawable.line);
//		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		
		View rightview = mInflater.inflate(R.layout.titlebar_right_view, null);
		mAbTitleBar.addRightView(rightview);
		mRightBtn = (Button) rightview.findViewById(R.id.rightBtn);
		mRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSlidingMenu.showSecondaryMenu();
				
				switch(mCurrentFragment.getTag()){
				case DefectSearchFragment.TAG:
					DefectSearchFragment f1 = (DefectSearchFragment)MainActivity.this.getFragmentManager().findFragmentByTag(DefectSearchFragment.TAG);
					f1.fresh();
					break;
				case DeviceSearchFragment.TAG:
					DeviceSearchFragment f2 = (DeviceSearchFragment)MainActivity.this.getFragmentManager().findFragmentByTag(DeviceSearchFragment.TAG);
					f2.fresh();
				case CircuitManagerFragment.TAG:
					CircuitManagerFragment f3 = (CircuitManagerFragment)MainActivity.this.getFragmentManager().findFragmentByTag(CircuitManagerFragment.TAG);
					f3.fresh();
					break;
				}
			}
		});
		mAbTitleBar.getLogoView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mSlidingMenu.isMenuShowing()) {
					mSlidingMenu.showContent();
				} else {
					mSlidingMenu.showMenu();
				}
			}
		});
		// 下标题栏
		mAbBottomBar.setVisibility(View.GONE);
	}
	
	public void replaceRightBtn(int resid){
		mRightBtn.setBackground(MyApplication.Resources.getDrawable(resid));
	}
	
	/**
	 * 替换右边视图
	 */
	public void replaceSecondMenu(Fragment fragment, String Tag) {
		if (!mCurrentFragment.getTag().equals(Tag)) {
			Fragment pastfragment = getFragmentManager().findFragmentByTag(Tag);
			if (pastfragment == null) {
				pastfragment = fragment;
				getFragmentManager().beginTransaction().add(R.id.menu_frame_second, pastfragment, Tag).commit();
				MyApplication.FragmentMap.put(Tag, fragment);
			}else{
				getFragmentManager().beginTransaction().show(pastfragment).commit();
			}
			getFragmentManager().beginTransaction().hide(mCurrentFragment).commit();
			mCurrentFragment = pastfragment;
		}
		mSlidingMenu.showSecondaryMenu();
	}

	public void showContent() {
		mSlidingMenu.showContent();
	}

	public MainFragment getMainFragment() {
		return (MainFragment) getFragmentManager().findFragmentByTag(MainFragment.TAG);
	}

	public MenuFragment getMenuFragment() {
		return mMenuFragment;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK){
			return;
		} 
//		else if(requestCode == UploadActivity.REQUESTCODE){
//			Uri uri = data.getData();
//			String path = GetPathForDiffAPI.getPathForAPI19(mContext, uri);
//	        getPathAndNameAndShowView(path);
//		} 
		else if(requestCode == Util.REQUEST_TAKE_SYSTEM_CAMERA){
			if(mOnCameraListener!=null){
				mOnCameraListener.doSomethingAfterCamera();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		AbDialogUtil.removeDialog(this);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mSlidingMenu.isMenuShowing()
					|| mSlidingMenu.isSecondaryMenuShowing()) {
				mSlidingMenu.showContent();
				return false;
			} else if (!(getFragmentManager().findFragmentById(
					R.id.container) instanceof MainFragment)) {
				getFragmentManager().popBackStack();
				setTitleBarVisible(true);
			} else {
				if (System.currentTimeMillis() - mExitTime > 1000) {
					Util.Toast("再按一次退出程序");
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			}
		}
		return false;
	}

	public void setTitleBarVisible(boolean visible) {
		if (visible) {
			mAbTitleBar.setVisibility(View.VISIBLE);
		} else {
			mAbTitleBar.setVisibility(View.GONE);
		}
	}

	public void keepBehindoffsetHide(boolean hide) {
		if (hide) {
			mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset_zero);
		} else {
			mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
		DataBaseTool.CloseDatabase(mDatabase);
		stopService(mServiceIntent);
		System.exit(0);
	}

	public void setBackStackIndex(int index) {
		mBackStackIndex = index;
	}

	public Database getDatabase() {
		return mDatabase;
	}
	
	public Cache getCache() {
		return mCache;
	}

	public void setOnCameraListener(OnCameraListener mOnCameraListener) {
		this.mOnCameraListener = mOnCameraListener;
	}
	
}
