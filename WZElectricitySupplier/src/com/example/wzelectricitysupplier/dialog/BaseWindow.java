package com.example.wzelectricitysupplier.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.PhotoSelectorAdapter;
import com.example.wzelectricitysupplier.adapter.PhotoSelectorAdapter.OnDeleteClickListener;
import com.example.wzelectricitysupplier.adapter.PhotoSelectorAdapter.OnItemClickListener;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.database.CircuitTable;
import com.example.wzelectricitysupplier.database.DBSetting;
import com.example.wzelectricitysupplier.database.DXTable;
import com.example.wzelectricitysupplier.database.DefectTable;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.function.GraphicsPointMove.OnAfterMoveListener;
import com.example.wzelectricitysupplier.function.OpenFileFunction;
import com.example.wzelectricitysupplier.function.photoselector.PhotoModel;
import com.example.wzelectricitysupplier.function.photoselector.PhotoPreviewActivity;
import com.example.wzelectricitysupplier.function.photoselector.PhotoSelectorDomain;
import com.example.wzelectricitysupplier.function.photoselector.PhotoSelectorDomain.OnLocalReccentListener;
import com.example.wzelectricitysupplier.setting.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BaseWindow {
	
	protected MainActivity mMainActivity;
	protected MainFragment mMainFragment;
	protected MapView mMap;
	protected PhotoSelectorDomain mPhotoSelectorDomain;
	protected CircuitTable mCircuitTable;
	protected DXTable mDXTable;
	protected DefectTable mDefectTable;
	protected List<String> mDefectIDList = new ArrayList<>();
	protected ArrayList<String> mNormalNameList = new ArrayList<>();
	protected ArrayList<String> mDeleteNameList = new ArrayList<>();
	protected ArrayList<String> mTemporaryNameList = new ArrayList<>();
	
	protected OnCameraListener mOnCameraListener; //  拍照监听器
	protected OnAfterMoveListener mOnAfterMoveListener; // 移位监听器
	
	protected View mView;
	protected ScrollView mScrollView1;
	protected ViewGroup mWindowView;
	protected ViewGroup mContainer;
	protected TextView mNametext;
	protected TextView mRemarktext;
	protected EditText mName;
	protected EditText mRemark;
	protected TextView mCameratext;
	protected ImageView mDevideline;
	protected RecyclerView mPhotos;
	protected PhotoSelectorAdapter mPhotoSelectorAdapter;
	
	protected ImageButton mEditBtn;
	protected ImageButton mCameraBtn;
	protected ImageButton mOKBtn;
	protected ImageButton mDeleteBtn;
	protected ImageButton mCloseBtn;
	protected ImageButton mDefectManageBtn;
	
	public abstract class OnCameraListener{
		public abstract void doSomethingAfterCamera();
	}
	
	public BaseWindow(MainActivity mMainActivity){
		this.mMainActivity = mMainActivity;
		this.mMainFragment = mMainActivity.getMainFragment();
		this.mMap = mMainFragment.getMap();
		this.mPhotoSelectorDomain = new PhotoSelectorDomain(mMainActivity);
		this.mCircuitTable = mMainFragment.getCircuitTable();
		this.mDefectTable = mMainFragment.getDefectTable();
		this.mDXTable = mMainFragment.getDXTable();
		
		DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder() 
			.considerExifParams(true) // 调整图片方向
			.resetViewBeforeLoading(true) // 载入之前重置ImageView
			.showImageOnLoading(R.drawable.photoselector_ic_picture_loading) // 载入时图片设置为黑色
			.showImageOnFail(R.drawable.photoselector_ic_picture_loadfailed) // 加载失败时显示的图片
			.delayBeforeLoading(0) // 载入之前的延迟时间
			.build(); 
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MyApplication.Context)
			.defaultDisplayImageOptions(defaultDisplayImageOptions).memoryCacheExtraOptions(480, 800)
			.threadPoolSize(5).build();
		ImageLoader.getInstance().init(config);
	}
	
	public void showWindow(final float screenx, final float screeny, final GraphicsLayer layer){};
	public void showWindow(){};
	
	public OnAfterMoveListener getOnAfterMoveListener() {
		return mOnAfterMoveListener;
	}
	
	protected int xh;
	protected TextView xh_tv;
	protected TextView delete;
	protected TextView preview;
	protected void addEditPictureRow(final ViewGroup parent, final String name){
		xh++;
		View view = MyApplication.Inflater.inflate(R.layout.row_picture_edit, null);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, Util.Dip2Px(mMainActivity, 0), 0, 0);
		view.setLayoutParams(params);
		xh_tv = (TextView)view.findViewById(R.id.xh);
		xh_tv.setText(""+xh);
		
		delete = (TextView)view.findViewById(R.id.delete);
		delete.setText(Html.fromHtml("<u>" + "删除" + "</u>"));
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDeleteNameList.add(name);
				if(mNormalNameList.contains(name)) 
					mNormalNameList.remove(name);
				refesh(parent, true);
			}
		});
		
		preview = (TextView)view.findViewById(R.id.preview);
		preview.setText(Html.fromHtml(name));
		preview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String path = Util.getPictureDirRootPath() + name;
				if(new File(path).exists()){
					OpenFileFunction.OpenFile(mMainActivity, new File(path));
				}else{
					Util.Toast("文件不存在");
				}
			}
		});
		parent.addView(view);
	}
	
	protected void addCheckPictureRow(final ViewGroup parent, final String name){
		xh++;
		View view = MyApplication.Inflater.inflate(R.layout.row_picture_check, null);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, Util.Dip2Px(mMainActivity, 0), 0, 0);
		view.setLayoutParams(params);
		xh_tv = (TextView)view.findViewById(R.id.xh);
		xh_tv.setText(""+xh);
		
		preview = (TextView)view.findViewById(R.id.preview);
		preview.setText(Html.fromHtml(name));
		preview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String path = Util.getPictureDirRootPath() + name;
				if(new File(path).exists()){
					OpenFileFunction.OpenFile(mMainActivity, new File(path));
				}else{
					Util.Toast("文件不存在");
				}
			}
		});
		parent.addView(view);
	}
	
	protected void refesh(ViewGroup parent, boolean editAble){
		xh = 0;
		parent.removeAllViews();
		ArrayList<String> mNameList = new ArrayList<>();
		mNameList.addAll(mNormalNameList);
		if(editAble){
			for(String name : mNameList){
				addEditPictureRow(parent, name);
			}
		}else{
			for(String name : mNameList){
				addCheckPictureRow(parent, name);
			}
		}
		
	}
	
	protected class OnCameraClickListener implements OnClickListener{
		private String deviceName = "";
		@Override
		public void onClick(View v) {
			if(!mName.getText().toString().equals("")){
				deviceName = mName.getText().toString();
				String dir = Util.getPictureDirRootPath() ;
				File dirfile = new File(dir);
				if(!dirfile.exists()){
					dirfile.mkdirs();
				}
				final String name = "普通_" + deviceName+ "_" + DateFormat.format("yyyyMMddHHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
				String mImagePath = dir + name;
				Util.DoTakePhoto(mMainActivity, mImagePath);
				mMainActivity.setOnCameraListener(new OnCameraListener() {
					@Override
					public void doSomethingAfterCamera() {
						mNormalNameList.add(name);
						mTemporaryNameList.add(name);
//						mPhotoSelectorDomain.getAlbum(deviceName, mReccentListener); // 更新照片预览
						mPhotoSelectorAdapter.update2(mNormalNameList);
					}
				});
			}else{
				Util.ShowDialog(mMainActivity, "请先填写名称！");
			}
		}
	}
	public void toCheckView(){
//		refesh(mContainer, false);
		mPhotoSelectorAdapter.update(false);
		mWindowView.setBackground(MyApplication.Resources.getDrawable(R.drawable.bg_window_addgraphic_blue));
		mNametext.setTextColor(MyApplication.Resources.getColor(R.color.popwindow_add_graphic_blue));
		mName.setTextColor(MyApplication.Resources.getColor(R.color.black));
		mName.setEnabled(false);
		mDevideline.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_blue));
		mRemarktext.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_blue));
		mRemark.setTextColor(MyApplication.Resources.getColor(R.color.black));
		mRemark.setEnabled(false);
		mCameratext.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_blue));
		mOKBtn.setVisibility(View.GONE);
		mEditBtn.setVisibility(View.VISIBLE);
		mCameraBtn.setVisibility(View.GONE);
		mScrollView1.smoothScrollTo(0, 0);
		HideKeyboard();
	}
	
	public void toEditView(){
//		refesh(mContainer, true);
		mPhotoSelectorAdapter.update(true);
		mWindowView.setBackground(MyApplication.Resources.getDrawable(R.drawable.bg_window_addgraphic_red));
		mDevideline.setBackground(MyApplication.Resources.getDrawable(R.color.gray));
		mRemarktext.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_dark_gray));
		mRemark.setTextColor(MyApplication.Resources.getColor(R.color.gray));
		mRemark.setEnabled(true);
		mNametext.setTextColor(MyApplication.Resources.getColor(R.color.black));
		mName.setTextColor(MyApplication.Resources.getColor(R.color.gray));
		mName.setEnabled(true);
		mCameratext.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_dark_gray));
		mOKBtn.setVisibility(View.VISIBLE);
		mEditBtn.setVisibility(View.GONE);
		mCameraBtn.setVisibility(View.VISIBLE);
		mScrollView1.smoothScrollTo(0, 0);
		HideKeyboard();
	}
	protected String[] separateName(String name){
		String[] nameParts = new String[]{"", ""};
		if(name.contains(" ")){
			nameParts = name.split(" ");
		}
		return nameParts;
	}
	
	protected String combineName(String circuitname, String devicename){
		String name = null;
		if(circuitname!=null && devicename!=null && !"".equals(circuitname) && !"".equals(devicename)){
			name = circuitname + " " + devicename;
		}
		return name;
	}
	
	protected List<String> getCombinationNameByBranch(List<String> mBranchName){
		List<String> mCombinationName = new ArrayList<String>();
		mCombinationName.addAll(mBranchName);
		for(int i=0; i<mBranchName.size(); i++){
			String name1 = mBranchName.get(i);
			for(int j=i+1; j<mBranchName.size(); j++){
				String name2 = mBranchName.get(j);
				if(name1.lastIndexOf("线") == name1.length()-1) 
					name1 = name1.substring(0, name1.length()-1);
				if(name2.lastIndexOf("线") == name2.length()-1) 
					name2 = name2.substring(0, name2.length()-1);
				String combination = name1 + name2 + "线";
				mCombinationName.add(combination);
			}
		}
		return mCombinationName;
	}
	protected void HideKeyboard(){
		Util.HideKeyboard(mMainActivity, mName);
		Util.HideKeyboard(mMainActivity, mRemark);
	}
	protected OnLocalReccentListener mReccentListener = new OnLocalReccentListener() {

		public void onPhotoLoaded(List<PhotoModel> photos) {
			mPhotoSelectorAdapter.update1(photos);
			mPhotos.smoothScrollToPosition(0); // 滚动到顶端
		}
	};
	protected OnDeleteClickListener mOnDeleteClickListener = new OnDeleteClickListener() {

		public void doDelete(String name) {
			mDeleteNameList.add(name);
			if(mNormalNameList.contains(name)) 
				mNormalNameList.remove(name);
			mPhotoSelectorAdapter.setPhotoItemEdit(true);
			mPhotoSelectorAdapter.update2(mNormalNameList);
		}
	};
	protected OnItemClickListener mOnItemClickListener = new PhotoSelectorAdapter.OnItemClickListener() {
		
		public void onItemClick(View view, int position) {
//			String name = mPhotoSelectorAdapter.getItem(position).getName();
//			String path = Util.getPictureDirRootPath() + name;
//			if(new File(path).exists()){
//				OpenFileFunction.OpenFile(mMainActivity, new File(path));
//			}else{
//				Util.Toast("文件不存在");
//			}
			ArrayList<PhotoModel> mDataset = mPhotoSelectorAdapter.getDataset();
			Bundle bundle = new Bundle();
			bundle.putSerializable("photos", mDataset);
			bundle.putInt("position", position);
			Intent intent = new Intent(mMainActivity, PhotoPreviewActivity.class);
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			mMainActivity.startActivity(intent);
		}
	};
	
	protected boolean deleteRelateDefect(){
		boolean result = true;
		String condition = "" ;
		ArrayList<String> mDefectPictureName = new ArrayList<>();
		for(int i=0; i<mDefectIDList.size(); i++){
			if(i==0) condition = DBSetting.ZD_PRIMERY + " in (" + mDefectIDList.get(i);
			if(i>0 && i<mDefectIDList.size()) condition += "," + mDefectIDList.get(i);
			if(i==mDefectIDList.size()-1) condition += ")";
			
			DefectRecord defectrecord = mDefectTable.SelectDefect(Integer.valueOf(mDefectIDList.get(i)));
			if(!"".equals(defectrecord.mPicture)){
				if(defectrecord.mPicture.contains("&")){
					String[] arr = defectrecord.mPicture.split("&");
					for(String name:arr){
						mDefectPictureName.add(name);
					}
				}else{
					mDefectPictureName.add(defectrecord.mPicture);
				};
			}
		}
		if(!condition.equals("")){
			result = mDefectTable.delete(condition);
			if(result){
				// 删除执行过删除操作的图片
				for(String name : mDefectPictureName){
					boolean b = new File(Util.getPictureDirRootPath(), name).delete();
					result = result && b;
				}
			}
		}
		return result;
				
	}
	
	protected boolean deleteRelatePicture(){
		// 删除执行过删除操作的图片
		boolean result = true;
		for(String deletename : mDeleteNameList){
			boolean b = new File(Util.getPictureDirRootPath(), deletename).delete();
			result = result && b;
		}
		// 删除新拍的图片
		for(String name : mTemporaryNameList){
			boolean b = new File(Util.getPictureDirRootPath(), name).delete();
			result = result && b;
		}
		// 删除原先存在的图片
		for(String name : mNormalNameList){
			boolean b = new File(Util.getPictureDirRootPath(), name).delete();
			result = result && b;
		}
		return result;
	}
	
	protected void pictureSelectorRefresh(){
		mPhotoSelectorAdapter.update2(mNormalNameList);
	}
}
