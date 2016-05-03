package com.example.wzelectricitysupplier.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.DefectAdapter;
import com.example.wzelectricitysupplier.adapter.DefectSearchAdapter;
import com.example.wzelectricitysupplier.adapter.PhotoSelectorAdapter;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.customwidget.DividerItemDecoration;
import com.example.wzelectricitysupplier.customwidget.InnerListView;
import com.example.wzelectricitysupplier.database.GeometryTable;
import com.example.wzelectricitysupplier.function.photoselector.PhotoModel;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;
import com.example.wzelectricitysupplier.setting.Util;

public class DefectWindow extends BaseWindow implements OnItemClickListener{
	
	public final static String TAG = "DefectWindow";
	
	private DefectRecord mDefectRecord;
	private Integer mPrimaryID;
	private Integer mBelongID;
	private String mBelongDevice;
	private PopupWindow mPopwindow;
	private DefectSearchAdapter mDefectSearchAdapter;
	
	private ViewGroup mListContent;
	private ListView mDefectListView;
	private DefectAdapter mDefectAdapter;
	private ViewGroup mDetailContent;
	private TextView mIndex;
	private TextView mTypeText;
	private TextView mType;
	private RadioGroup mOption1group;
	private InnerListView mType1ListView;
	private InnerListView mType2ListView;
	private InnerListView mType3ListView;
	private MyAdapter mType1Adapter;
	private MyAdapter mType2Adapter;
	private MyAdapter mType3Adapter;
	private ViewGroup mListButtonGroup;
	private ViewGroup mCheckButtonGroup;
	private ViewGroup mEditButtonGroup;
	private ImageButton mBackBtn;
	private ImageButton mDeleteBtn;
	private ImageButton mAddBtn;
	
	private String[] mType1Array;
	private String[] mType2Array;
	private String[] mType3Array;
	private String mTypeStr = "";
	// 新建
	public DefectWindow(MainActivity mMainActivity, int mBelongID, String mBelongDevice) {
		super(mMainActivity);
		this.mBelongID = mBelongID;
		this.mBelongDevice = mBelongDevice;
		mDefectTable = mMainFragment.getDefectTable();
		mCircuitTable = mMainFragment.getCircuitTable();
	}
	 
	public DefectWindow(MainActivity mMainActivity, DefectRecord mDefectRecord, DefectSearchAdapter mDefectSearchAdapter) {
		super(mMainActivity);
		this.mDefectRecord = mDefectRecord;
		this.mDefectSearchAdapter = mDefectSearchAdapter;
		mDefectTable = mMainFragment.getDefectTable();
		mCircuitTable = mMainFragment.getCircuitTable();
	}
	
	public void showWindow(){
		mView = mMainActivity.getLayoutInflater().inflate(R.layout.window_defect, null);
		mWindowView = (ViewGroup) mView.findViewById(R.id.windowView);
		mContainer = (ViewGroup) mView.findViewById(R.id.Container);
		mPopwindow = new PopupWindow(mView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopwindow.setFocusable(true);
		mPopwindow.setOutsideTouchable(true);
		int arg[] = Util.GetScreenArgument(mMainActivity);
		mPopwindow.setWidth(arg[0]); 
		mPopwindow.setHeight(arg[1]); 
		mPopwindow.setBackgroundDrawable(MyApplication.Resources.getDrawable(R.drawable.bg_fullscreen_gray)); 
		mPopwindow.showAtLocation(mMainFragment.getMarkView(), Gravity.RIGHT, 0, 0); 
		
		mListContent = (ViewGroup)mView.findViewById(R.id.listContent); 
		mListButtonGroup = (ViewGroup)mView.findViewById(R.id.list_buttongroup); 
		mDetailContent = (ViewGroup)mView.findViewById(R.id.detailContent); 
		mCheckButtonGroup = (ViewGroup)mView.findViewById(R.id.check_buttongroup); 
		mEditButtonGroup = (ViewGroup)mView.findViewById(R.id.edit_buttongroup); 
		mNametext = (TextView)mView.findViewById(R.id.nametext);
		mName = (EditText)mView.findViewById(R.id.name);
		mIndex = (TextView)mView.findViewById(R.id.index);
		mDevideline = (ImageView)mView.findViewById(R.id.devideline);
		mRemarktext = (TextView)mView.findViewById(R.id.remarktext);
		mRemark = (EditText)mView.findViewById(R.id.remark);
		mCameratext = (TextView)mView.findViewById(R.id.cameratext);
		mOption1group = (RadioGroup)mView.findViewById(R.id.option1group);
		mTypeText = (TextView)mView.findViewById(R.id.TypeText);
		mType = (TextView)mView.findViewById(R.id.Type);
		mScrollView1 = (ScrollView)mView.findViewById(R.id.ScrollView1);
		mPhotos = (RecyclerView) mView.findViewById(R.id.photos);
		LinearLayoutManager layout = new LinearLayoutManager(mMainActivity);
		layout.setOrientation(LinearLayoutManager.HORIZONTAL);
		mPhotos.setLayoutManager(layout);
		int dimension = MyApplication.Resources.getDimensionPixelSize(R.dimen.recyclerview_width);
		mPhotoSelectorAdapter = new PhotoSelectorAdapter(new ArrayList<PhotoModel>(), dimension);
		mPhotoSelectorAdapter.setDeleteClickListener(mOnDeleteClickListener);
		mPhotoSelectorAdapter.setOnItemClickListener(mOnItemClickListener);
		mPhotos.setAdapter(mPhotoSelectorAdapter);
		DividerItemDecoration mItemDecoration = new DividerItemDecoration(mMainActivity, DividerItemDecoration.HORIZONTAL_LIST);
		mPhotos.addItemDecoration(mItemDecoration);
		
		if(mDefectRecord!=null){
			mIndex.setText(mDefectRecord.mName);
			mBelongID = Integer.valueOf(mDefectRecord.mBelongId);
			mBelongDevice = mDefectRecord.mBelongDevice;
			mPrimaryID = mDefectRecord.getId();
			mType.setText(mDefectRecord.mType);
			mRemark.setText(mDefectRecord.mDescribe);
			
			String pictureStr = mDefectRecord.mPicture;
			if(!"".equals(pictureStr)){
				if(pictureStr.contains("&")){
					String[] arr = pictureStr.split("&");
					for(String name:arr){
						mNormalNameList.add(name);
					}
				}else{
					mNormalNameList.add(pictureStr);
				}
//				refesh(mContainer, false);
				mPhotoSelectorAdapter.setPhotoItemEdit(false);
				mPhotoSelectorAdapter.update2(mNormalNameList);
				
			}
		}
		
		mDefectListView = (ListView)mView.findViewById(R.id.defectList);
		mDefectAdapter = new DefectAdapter(mDefectTable, mBelongID, mBelongDevice);
		mDefectListView.setAdapter(mDefectAdapter);
		mDefectListView.setOnItemClickListener(this);
		mType1ListView = (InnerListView)mView.findViewById(R.id.type1);
		mType1Array = MyApplication.Resources.getStringArray(R.array.defect_type_pdxb);
		mType1Adapter = new MyAdapter(mType1Array);
		mType1ListView.setAdapter(mType1Adapter);
		mType1ListView.setMaxHeight(Util.Dip2Px(mMainActivity, 140));
		mType1ListView.setParentScrollView(mScrollView1);
		mType1ListView.setOnItemClickListener(this);
		mType2ListView = (InnerListView)mView.findViewById(R.id.type2);
		mType2Array = MyApplication.Resources.getStringArray(R.array.defect_type_kg);
		mType2Adapter = new MyAdapter(mType2Array);
		mType2ListView.setAdapter(mType2Adapter);
		mType2ListView.setMaxHeight(Util.Dip2Px(mMainActivity, 140));
		mType2ListView.setParentScrollView(mScrollView1);
		mType2ListView.setOnItemClickListener(this);
		mType3ListView = (InnerListView)mView.findViewById(R.id.type3);
		mType3Array = MyApplication.Resources.getStringArray(R.array.defect_type_other);
		mType3Adapter = new MyAdapter(mType3Array);
		mType3ListView.setAdapter(mType3Adapter);
		mType3ListView.setMaxHeight(Util.Dip2Px(mMainActivity, 140));
		mType3ListView.setParentScrollView(mScrollView1);
		mType3ListView.setOnItemClickListener(this);
		
		chooseDefectTypeListView(mBelongDevice);
		mEditBtn = (ImageButton)mView.findViewById(R.id.edit);
		mEditBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toEditView();
			}
		});
		mCameraBtn = (ImageButton)mView.findViewById(R.id.camera);
		DeviceRecord record = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, mBelongID, mBelongDevice);
		mCameraBtn.setOnClickListener(new OnDefectCameraClickListener(record.mName));
		
		mOKBtn = (ImageButton)mView.findViewById(R.id.ok);
		mOKBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String circuitid = mCircuitTable.getCircuitID();
				String index = mIndex.getText().toString();
				String remark = mRemark.getText().toString();
				String type = mType.getText().toString();
				String picture = "";
				for(int i=0; i<mNormalNameList.size(); i++){
					if(i==mNormalNameList.size()-1){
						picture += mNormalNameList.get(i);
					}else{
						picture += mNormalNameList.get(i) + "&";
					}
				}
				for(String deletename : mDeleteNameList){
					File file = new File(Util.getPictureDirRootPath(), deletename);
					if(file.exists())
						file.delete();
				}
				if(circuitid!=null){
					DefectRecord record = new DefectRecord(index, String.valueOf(mBelongID), mBelongDevice, picture, type, remark, "否");
					record = new DefectRecord(index, String.valueOf(mBelongID), mBelongDevice, picture, type, remark, getEditState(mDefectRecord, record));
					if(mPrimaryID == null){ 
						// 新增
						mPrimaryID = mDefectTable.add(record);
						DeviceRecord mDeviceRecord = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, mBelongID, mBelongDevice);
						String mDefectIDStr  = 	mDeviceRecord.mDefectID;
						if("".equals(mDefectIDStr)){
							mDefectIDStr = String.valueOf(mPrimaryID);
						}else{
							mDefectIDStr += "&"+mPrimaryID;
						}
						AppUtil.getTableByType(mMainFragment, mBelongDevice).update(DeviceRecord.ZD_DEFECT_ID.NAME, mDefectIDStr, mBelongID);
					}else{
						// 更新
						mDefectTable.update(record, mPrimaryID);
					}
					mTemporaryNameList.clear();
					toCheckView();
					if(mDefectSearchAdapter!=null){
						mDefectSearchAdapter.reLoadData(circuitid);
					}
				}else{
					Log.e(TAG, "提交数据库失败，线路ID为空");
					if(MyApplication.Debug){
						Util.Toast("提交数据库失败，线路ID为空");
					}
				}
				
			}
		});
		
		mCloseBtn = (ImageButton)mView.findViewById(R.id.close);
		mCloseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(String name : mTemporaryNameList){
					new File(Util.getPictureDirRootPath(), name).delete();
				}
				HideKeyboard();
				mPopwindow.dismiss();
			}
		});
		
		mBackBtn = (ImageButton)mView.findViewById(R.id.backBtn);
		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toListView();
			}
		});
		
		mAddBtn = (ImageButton)mView.findViewById(R.id.addBtn);
		mAddBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toEditView();
				int size = mDefectAdapter.getCount();
				if(size==0)
					mIndex.setText("1");
				else{
					int lastIndex = size-1;
					DefectRecord defectrecord = (DefectRecord)mDefectAdapter.getItem(lastIndex);
					String defectnum = String.valueOf(Integer.valueOf(defectrecord.mName) + 1);
					mIndex.setText(defectnum);
				}
			}
		});
		
		mDeleteBtn = (ImageButton)mView.findViewById(R.id.deleteBtn);
		mDeleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPrimaryID!=null){
					DialogInterface.OnClickListener mPositiveListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							DefectRecord defectrecord = mDefectTable.SelectDefect(mPrimaryID);
							DeviceRecord devicerecord = AppUtil.getDeviceRecordByDefectRecord(mMainFragment, 
									Integer.valueOf(defectrecord.mBelongId), defectrecord.mBelongDevice);
							String mDefectID = devicerecord.mDefectID;
							if(mDefectTable.delete(mPrimaryID)){
								String primaryIdStr = String.valueOf(mPrimaryID);
								if(mDefectID.contains(primaryIdStr)){
									int index = mDefectID.indexOf(primaryIdStr);
									if(index==0){ // 位于开头的情况
										int index2 = index + primaryIdStr.length();
										if(index2<mDefectID.length()){
											mDefectID = mDefectID.substring(index2+1); // +1把&也去掉
										}else {
											mDefectID = "";
										}
									}else if(index>0){ // 位于中间或者末尾情况
										String[] arr = mDefectID.split(("&"+primaryIdStr));
										mDefectID = "";
										for(int i=0;i<arr.length;i++){
											mDefectID += arr[i];
										} 
									}
								}
								GeometryTable table = AppUtil.getTableByType(mMainFragment, defectrecord.mBelongDevice);
								table.update(DeviceRecord.ZD_DEFECT_ID.NAME, mDefectID, Integer.valueOf(defectrecord.mBelongId)); // 更新对应设备表中的缺陷编号
								deleteRelatePicture();
								toListView();
							}else{
								Util.Toast("删除失败");
							}
						}
					};
					DialogInterface.OnClickListener mNegativeListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					};
					Util.ShowDialog(mMainActivity, "确定要删除吗？", mPositiveListener, mNegativeListener);
				}else{
					toListView();
				}
				
			}
		});
	}
	
	public void dismissWindow(){
		if(mPopwindow!=null){
			mPopwindow.dismiss();
		}
	}
	
	public void hideBackBtn(){
		mBackBtn.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch(parent.getId()){
		case R.id.type1:
			mTypeStr = (String)mType1ListView.getItemAtPosition(position);
			mType.setText(mTypeStr);
			mType1Adapter.notifyDataSetChanged();
			break;
		case R.id.type2:
			mTypeStr = (String)mType2ListView.getItemAtPosition(position);
			mType.setText(mTypeStr);
			mType2Adapter.notifyDataSetChanged();
			break;
		case R.id.type3:
			mTypeStr = (String)mType3ListView.getItemAtPosition(position);
			mType.setText(mTypeStr);
			mType3Adapter.notifyDataSetChanged();
			break;
		case R.id.defectList:
			DefectRecord mDefectRecord = (DefectRecord)mDefectListView.getItemAtPosition(position);
			mPrimaryID = mDefectRecord.getId();
			toCheckView();
			mType.setText(mDefectRecord.mType);
			mRemark.setText(mDefectRecord.mDescribe);
			DefectRecord defectrecord = (DefectRecord)mDefectAdapter.getItem(position);
			mIndex.setText(defectrecord.mName);
			String pictureStr = mDefectRecord.mPicture;
			if(!"".equals(pictureStr)){
				if(pictureStr.contains("&")){
					String[] arr = pictureStr.split("&");
					for(String name:arr){
						mNormalNameList.add(name);
					}
				}else{
					mNormalNameList.add(pictureStr);
				}
//				refesh(mContainer, false);
				mPhotoSelectorAdapter.setPhotoItemEdit(false);
				mPhotoSelectorAdapter.update2(mNormalNameList);
			}
			break;
		}
	}
	
	private class MyAdapter extends BaseAdapter{
		
		private String[] models;
		
		public MyAdapter(String[] models){
			this.models = models;
		}
		
		@Override
		public int getCount() {
			int count = 0;
			if(models!=null){
				count = models.length;
			}
			return count;
		}

		@Override
		public String getItem(int position) {
			String item = "";
			if(models!=null){
				item = models[position];
			}
			return item;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public final class ViewHolder {
			public TextView text;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = MyApplication.Inflater.inflate(R.layout.listitem_model, null);
				
				holder = new ViewHolder();
				holder.text = (TextView)convertView.findViewById(R.id.modeltext);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(getItem(position));
			if (mTypeStr.equals(getItem(position))) { // 如果当前的行就是ListView中选中的一行，就更改显示样式 
				convertView.setBackgroundColor(MyApplication.Resources.getColor(R.color.custom_red));// 更改整行的背景色
			}else{
				convertView.setBackgroundColor(MyApplication.Resources.getColor(R.color.transparent));// 更改整行的背景色 
			}
			return convertView;
		}
	}
	
	public void toListView(){
		mWindowView.setBackground(MyApplication.Resources.getDrawable(R.drawable.bg_window_adddefect_blue));
		mEditBtn.setVisibility(View.GONE);
		mListContent.setVisibility(View.VISIBLE);
		mDetailContent.setVisibility(View.GONE);
		mListButtonGroup.setVisibility(View.VISIBLE);
		mCheckButtonGroup.setVisibility(View.GONE);
		mEditButtonGroup.setVisibility(View.GONE);
		mOKBtn.setVisibility(View.GONE);
		mPrimaryID = null;
		mType.setText("");
		mRemark.setText("");
		mNormalNameList.clear();
		mDefectAdapter.reLoadData();
		mPhotoSelectorAdapter.update1(new ArrayList<PhotoModel>());
	}
	
	public void toEditView(){
		super.toEditView();
		mView.setBackground(MyApplication.Resources.getDrawable(R.drawable.bg_fullscreen_gray));
		mWindowView.setBackground(MyApplication.Resources.getDrawable(R.drawable.bg_window_adddefect_red));
		mListContent.setVisibility(View.GONE);
		mDetailContent.setVisibility(View.VISIBLE);
		mListButtonGroup.setVisibility(View.GONE);
		mCheckButtonGroup.setVisibility(View.GONE);
		mEditButtonGroup.setVisibility(View.VISIBLE);
		mTypeText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_dark_gray));
		mOption1group.setVisibility(View.VISIBLE);
		mScrollView1.smoothScrollTo(0, 0);
	}
	
	public void toCheckView(){
		super.toCheckView();
		mView.setBackground(MyApplication.Resources.getDrawable(R.drawable.bg_fullscreen_gray));
		mWindowView.setBackground(MyApplication.Resources.getDrawable(R.drawable.bg_window_adddefect_blue));
		mListContent.setVisibility(View.GONE);
		mDetailContent.setVisibility(View.VISIBLE);
		mListButtonGroup.setVisibility(View.GONE);
		mCheckButtonGroup.setVisibility(View.VISIBLE);
		mEditButtonGroup.setVisibility(View.GONE);
		mTypeText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_blue));
		mOption1group.setVisibility(View.GONE);
		mScrollView1.smoothScrollTo(0, 0);
	}
	
	private void chooseDefectTypeListView(String type){
		switch(type){
		case DeviceType.DeviceBDS:
			mType1ListView.setVisibility(View.GONE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.VISIBLE);
			break;
		case DeviceType.DeviceGB:
			mType1ListView.setVisibility(View.GONE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.VISIBLE);
			break;
		case DeviceType.DeviceGL:
			mType1ListView.setVisibility(View.GONE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.VISIBLE);
			break;
		case DeviceType.DeviceGT:
			mType1ListView.setVisibility(View.GONE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.VISIBLE);
			break;
		case DeviceType.DeviceHW:
			mType1ListView.setVisibility(View.GONE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.VISIBLE);
			break;
		case DeviceType.DeviceKG:
			mType1ListView.setVisibility(View.GONE);
			mType2ListView.setVisibility(View.VISIBLE);
			mType3ListView.setVisibility(View.GONE);
			break;
		case DeviceType.DeviceLK:
			mType1ListView.setVisibility(View.GONE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.VISIBLE);
			break;
		case DeviceType.DevicePD:
			mType1ListView.setVisibility(View.VISIBLE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.GONE);
			break;
		case DeviceType.DeviceSwitch:
			mType1ListView.setVisibility(View.GONE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.VISIBLE);
			break;
		case DeviceType.DeviceXB:
			mType1ListView.setVisibility(View.VISIBLE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.GONE);
			break;
		case DeviceType.Line:
			mType1ListView.setVisibility(View.GONE);
			mType2ListView.setVisibility(View.GONE);
			mType3ListView.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	private String getEditState(DefectRecord oldone, DefectRecord newone){
		boolean isChanged = false;
		String state = "";
		if(oldone!=null){
			if(oldone.mIsEdit.equals("新增")){
				return "新增";
			}
			if(oldone.mIsEdit.equals("是")){
				return "是";
			}
			isChanged = isChanged || oldone.mBelongDevice.equals(newone.mBelongDevice)?false:true;
			isChanged = isChanged || oldone.mBelongId.equals(newone.mBelongId)?false:true;
			isChanged = isChanged || oldone.mPicture.equals(newone.mPicture)?false:true;
			isChanged = isChanged || oldone.mDescribe.equals(newone.mDescribe)?false:true;
			isChanged = isChanged || oldone.mType.equals(newone.mType)?false:true;
			if(isChanged){
				state = "是";
			}else{
				state = "否";
			}
		}else{
			state = "新增";
		}
		
		return state;
	}
	
	protected class OnDefectCameraClickListener implements OnClickListener{
		private String devicename = "";
		
		public OnDefectCameraClickListener(String devicename){
			this.devicename = devicename;
		}
		@Override
		public void onClick(View v) {
			if(!devicename.equals("")){
				String dir = Util.getPictureDirRootPath();
				String index = mIndex.getText().toString();
				final String name = "缺陷_" + devicename + "_" + index + "_" + DateFormat.format("MMddss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
				String mImagePath = dir + name;
				Util.DoTakePhoto(mMainActivity, mImagePath);
				mMainActivity.setOnCameraListener(new OnCameraListener() {
					@Override
					public void doSomethingAfterCamera() {
						mNormalNameList.add(name);
						mTemporaryNameList.add(name);
						mPhotoSelectorAdapter.update2(mNormalNameList);
					}
				});
			}else{
				Util.ShowDialog(mMainActivity, "请先填写名称！");
			}
		}
	}
	
}
