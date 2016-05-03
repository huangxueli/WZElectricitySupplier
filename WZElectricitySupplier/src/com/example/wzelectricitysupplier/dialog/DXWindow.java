package com.example.wzelectricitysupplier.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Polyline;
import com.esri.core.symbol.Symbol;
import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.PhotoSelectorAdapter;
import com.example.wzelectricitysupplier.bean.DXRecord;
import com.example.wzelectricitysupplier.customwidget.DividerItemDecoration;
import com.example.wzelectricitysupplier.customwidget.InnerListView;
import com.example.wzelectricitysupplier.database.DXTable;
import com.example.wzelectricitysupplier.function.ArcgisTool;
import com.example.wzelectricitysupplier.function.photoselector.PhotoModel;
import com.example.wzelectricitysupplier.listener.MyOnSingleTapListener.AddMode;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.Util;

public class DXWindow extends BaseWindow implements OnCheckedChangeListener, OnItemClickListener{
	
	public final static String TAG = "DXWindow";
	
	private DXTable mDXTable;
	private Integer mPrimaryID;
	private Integer mGraphicID;
	private String mLineName;
	private String mDeviceName1;
	private String mDeviceName2;
	private String mLineNum;
	private DXRecord mDXRecord;
	private Polyline mPolyline;
	private boolean isNewAdd;
	
	private TextView mHeightText;
	private EditText mHeight;
	private RadioGroup mOption1group;
	private RadioButton mType1;
	private RadioButton mType2;
	private RadioButton mType3;
	private TextView mTypeText;
	private TextView mType;
	private TextView mModelText;
	private TextView mModel;
	private RadioGroup mOption2group;
	private InnerListView mDLModelListView;
	private InnerListView mLDModelListView;
	private InnerListView mJYModelListView;
	private MyAdapter mDLAdapter;
	private MyAdapter mLDAdapter;
	private MyAdapter mJYAdapter;
	private ViewGroup mCheckButtonGroup;
	private ViewGroup mEditButtongroup;
	private DefectWindow mDefectWindow;
	
	private String[] mDLArray;
	private String[] mLDArray;
	private String[] mJYArray;
	private String mModelStr = "";
	private int mJYPostion = 0;
	private int mDLPostion = 0;
	private int mLDPostion = 0;
	// 新建
	public DXWindow(MainActivity mMainActivity, Polyline mPolyline, int mGraphicID, String mDeviceName1, String mDeviceName2, String mLineNum) {
		super(mMainActivity);
		this.mPolyline = mPolyline;
		this.mGraphicID = mGraphicID;
		this.mLineNum = mLineNum;
		this.mDeviceName1 = mDeviceName1;
		this.mDeviceName2 = mDeviceName2;
		mDXTable = mMainFragment.getDXTable();
		mCircuitTable = mMainFragment.getCircuitTable();
		isNewAdd = true;
	}
	// 已存在
	public DXWindow(MainActivity mMainActivity, Integer mPrimaryID, Polyline mPolyline, int mGraphicID, String mLineNum) {
		super(mMainActivity);
		this.mPolyline = mPolyline;
		this.mPrimaryID = mPrimaryID;
		this.mGraphicID = mGraphicID;
		this.mLineNum = mLineNum;
		mDXTable = mMainFragment.getDXTable();
		mCircuitTable = mMainFragment.getCircuitTable();
		isNewAdd = false;
	}
	public void showWindow(final float screenx, final float screeny, final GraphicsLayer graphicsLayer){
		mView = mMainActivity.getLayoutInflater().inflate(R.layout.window_dx, null);
		mWindowView = (ViewGroup) mView.findViewById(R.id.windowView);
		mContainer = (ViewGroup) mView.findViewById(R.id.Container);
		final PopupWindow mPopwindow = new PopupWindow(mView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopwindow.setFocusable(true);
		mPopwindow.setOutsideTouchable(false);
		int arg[] = Util.GetScreenArgument(mMainActivity);
		mPopwindow.setWidth(arg[0]); 
		mPopwindow.setHeight(arg[1]); 
		mPopwindow.showAtLocation(mMainFragment.getMarkView(), Gravity.CENTER, 0, 0); 
		
		mCheckButtonGroup = (ViewGroup)mView.findViewById(R.id.check_buttongroup); 
		mEditButtongroup = (ViewGroup)mView.findViewById(R.id.edit_buttongroup); 
		mNametext = (TextView)mView.findViewById(R.id.nametext);
		mName = (EditText)mView.findViewById(R.id.name);
		mDevideline = (ImageView)mView.findViewById(R.id.devideline);
		mRemarktext = (TextView)mView.findViewById(R.id.remarktext);
		mRemark = (EditText)mView.findViewById(R.id.remark);
		mCameratext = (TextView)mView.findViewById(R.id.cameratext);
		mHeightText = (TextView)mView.findViewById(R.id.heighttext);
		mHeight = (EditText)mView.findViewById(R.id.height);
		mOption1group = (RadioGroup)mView.findViewById(R.id.option1group);
		mOption2group = (RadioGroup)mView.findViewById(R.id.option2group);
		mType1 = (RadioButton)mView.findViewById(R.id.type1);
		mType1.setOnCheckedChangeListener(this);
		mType2 = (RadioButton)mView.findViewById(R.id.type2);
		mType2.setOnCheckedChangeListener(this);
		mType3 = (RadioButton)mView.findViewById(R.id.type3);
		mType3.setOnCheckedChangeListener(this);
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
		
		if(mPrimaryID !=null){
			mDXRecord = mDXTable.selectElectricWire(mPrimaryID);
			mModelStr = mDXRecord.mModel;
		}
		
		mModelText = (TextView)mView.findViewById(R.id.ModelText);
		mModel = (TextView)mView.findViewById(R.id.Model);
		mDLModelListView = (InnerListView)mView.findViewById(R.id.DLModelListView);
		mDLArray = MyApplication.Resources.getStringArray(R.array.dl_model);
		mDLAdapter = new MyAdapter(mDLArray);
		mDLModelListView.setAdapter(mDLAdapter);
		mDLModelListView.setMaxHeight(Util.Dip2Px(mMainActivity, 140));
		mDLModelListView.setParentScrollView(mScrollView1);
		mDLModelListView.setOnItemClickListener(this);
		mLDModelListView = (InnerListView)mView.findViewById(R.id.LDModelListView);
		mLDArray = MyApplication.Resources.getStringArray(R.array.ld_model);
		mLDAdapter = new MyAdapter(mLDArray);
		mLDModelListView.setAdapter(mLDAdapter);
		mLDModelListView.setMaxHeight(Util.Dip2Px(mMainActivity, 140));
		mLDModelListView.setParentScrollView(mScrollView1);
		mLDModelListView.setOnItemClickListener(this);
		mJYModelListView = (InnerListView)mView.findViewById(R.id.JYModelListView);
		mJYArray = MyApplication.Resources.getStringArray(R.array.jy_model);
		mJYAdapter= new MyAdapter(mJYArray);
		mJYModelListView.setAdapter(mJYAdapter);
		mJYModelListView.setMaxHeight(Util.Dip2Px(mMainActivity, 140));
		mJYModelListView.setParentScrollView(mScrollView1);
		mJYModelListView.setOnItemClickListener(this);
		
		if(mDXRecord!=null){
			mName.setText(mDXRecord.mName);
			mName.setSelection(mDXRecord.mName.length());
			mRemark.setText(mDXRecord.mRemark);
			mHeight.setText(mDXRecord.mHeight);
			mType.setText(mDXRecord.mType);
			decideWhichCheckedWithmType(mDXRecord.mType);
			mModel.setText(mDXRecord.mModel);
			mDLAdapter.notifyDataSetChanged();
			mLDAdapter.notifyDataSetChanged();
			mJYAdapter.notifyDataSetChanged();
			
			String pictureStr = mDXRecord.mPicture;
			if(!"".equals(pictureStr)){
				if(pictureStr.contains("&")){
					String[] arr = pictureStr.split("&");
					for(String name:arr){
						mNormalNameList.add(name);
					}
				}else{
					mNormalNameList.add(pictureStr);
				}
				mPhotoSelectorAdapter.setPhotoItemEdit(false);
				mPhotoSelectorAdapter.update2(mNormalNameList);
			}
			String defectIDStr = mDXRecord.mDefectID;
			if(!"".equals(defectIDStr)){
				if(defectIDStr.contains("&")){
					String[] arr = defectIDStr.split("&");
					for(String name:arr){
						mDefectIDList.add(name);
					}
				}else{
					mDefectIDList.add(defectIDStr);
				}
			}
		}else{
			mLineName = "";
			if(mDeviceName1.contains(" ")){
				mLineName = mDeviceName1.split(" ")[0].trim();
			}else if(mDeviceName2.contains(" ")){
				mLineName = mDeviceName2.split(" ")[0].trim();
			}
			String name1 = mDeviceName1;
			if(mDeviceName1.contains(" "))
				name1 = mDeviceName1.split(" ")[1].trim();
			String name2 = mDeviceName2;
			if(mDeviceName2.contains(" "))
				name2 = mDeviceName2.split(" ")[1].trim();
			String name = mLineName +" [" + name1 + "]" + "-" + "[" + name2 + "]";
			mName.setText(name);
			mName.setSelection(name.length());
		}
		
		mEditBtn = (ImageButton)mView.findViewById(R.id.edit);
		mEditBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toEditView();
			}
		});
		mCameraBtn = (ImageButton)mView.findViewById(R.id.camera);
		mCameraBtn.setOnClickListener(new OnCameraClickListener());
		
		mOKBtn = (ImageButton)mView.findViewById(R.id.ok);
		mOKBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String circuitid = mCircuitTable.getCircuitID();
				String name = mName.getText().toString();
				String remark = mRemark.getText().toString();
				String height = mHeight.getText().toString();
				String type = mType.getText().toString();
				String model = mModel.getText().toString();
				String picture = "";
				String defectIds = "";
				for(int i=0; i<mNormalNameList.size(); i++){
					if(i==mNormalNameList.size()-1){
						picture += mNormalNameList.get(i);
					}else{
						picture += mNormalNameList.get(i) + "&";
					}
				}
				if(mPrimaryID!=null){
					mDefectIDList = AppUtil.turnStringToList(mDXTable.selectElectricWire(mPrimaryID).mDefectID);
					for(int i=0; i<mDefectIDList.size(); i++){
						if(i == mDefectIDList.size()-1){
							defectIds += mDefectIDList.get(i);
						}else{
							defectIds += mDefectIDList.get(i) + "&";
						}
					}
				}
				
				for(String deletename : mDeleteNameList){
					new File(Util.getPictureDirRootPath(), deletename).delete();
				}
				if(circuitid!=null){
					if(name.equals("") || name.indexOf("线")==0 || name.indexOf("线")==-1 || !name.contains(" ")){
						Util.ShowDialog(mMainActivity, "命名不规范，请参照底部提示信息命名！");
						mMainFragment.SetMessageAreaText("\"XXX线\" + \"空格\" + \"[起点设备名称]-[终点设备名称]\" 注：设备名不包含线名称 	例如：屏山9301线 [22#]-[1#环网柜]");
						HideKeyboard();
					}else{
						if(mPrimaryID == null){ 
							// 新增
							DXRecord record = new DXRecord(name, circuitid, height, type, model, mLineNum, mDeviceName1, mDeviceName2, picture, defectIds, remark, "新增");
							mPrimaryID = mDXTable.add(record, mPolyline);
						}else{
							// 更新
							DXRecord record = new DXRecord(name, circuitid, height, type, model, mLineNum, mDXRecord.mDeviceName1, mDXRecord.mDeviceName2, picture, defectIds, remark, "否");
							record = new DXRecord(name, circuitid, height, type, model, mLineNum, mDXRecord.mDeviceName1, mDXRecord.mDeviceName2, picture, defectIds, remark, getEditState(mDXRecord, record));
							mDXTable.update(mPolyline, record, mPrimaryID);
						}
						AddMode mode = null;
						Symbol symbol = null;
						switch(mLineNum){
						case "1":
							mode = AddMode.LineOne;
							List<String> mBranchNames = mCircuitTable.getBranchName();
							symbol = ArcgisTool.getLineOneSymbol(type, mBranchNames.indexOf(mLineName));
							break;
						case "2":
							mode = AddMode.LineTwo;
							symbol = ArcgisTool.getLineTwoSymbol(type);
							break;
						case "4":
							mode = AddMode.LineFour;
							symbol = ArcgisTool.getLineFourSymbol(type);
							break;
						}
						ArcgisTool.removeGraphic(mGraphicID, graphicsLayer);
						mGraphicID  = ArcgisTool.addLineGraphic(mPolyline, symbol, graphicsLayer);
						ArcgisTool.updateGraphic(mGraphicID, graphicsLayer, mPrimaryID, mode);
						mMainFragment.doLoadCircuit(circuitid);
						mTemporaryNameList.clear();
						mPopwindow.dismiss();
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
				if(mPrimaryID == null){
					graphicsLayer.removeGraphic(mGraphicID);
				}
				for(String name : mTemporaryNameList){
					new File(Util.getPictureDirRootPath(), name).delete();
				}
				if(mDefectWindow!=null){
					mDefectWindow.dismissWindow();
				}
				HideKeyboard();
				mPopwindow.dismiss();
				graphicsLayer.clearSelection();
			}
		});
		
		mDeleteBtn = (ImageButton)mView.findViewById(R.id.deleteBtn);
		mDeleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogInterface.OnClickListener mPositiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mPopwindow.dismiss();
						graphicsLayer.clearSelection();
						if(mPrimaryID!=null){
							boolean result = deleteRelateDefect();
							result = result && mDXTable.delete(mPrimaryID);
							if(result) {
								deleteRelatePicture();
								mMainFragment.doLoadCircuit(mCircuitTable.getCircuitID());
							}
						}else{
							Util.Toast("尚未添加");
						}
					}
				};
				DialogInterface.OnClickListener mNegativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				Util.ShowDialog(mMainActivity, "删除后数据不可恢复，确定要删除吗？", mPositiveListener, mNegativeListener);
			}
		});
		
		mDefectManageBtn = (ImageButton)mView.findViewById(R.id.defectmanageBtn);
		mDefectManageBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPrimaryID!=null){
					mDefectWindow = new DefectWindow(mMainActivity, mPrimaryID, DeviceType.Line);
					mDefectWindow.showWindow();
				}else{
					Util.Toast("设备不存在");
				}
			}
		});
		
		if(isNewAdd){
			mDeleteBtn.setVisibility(View.INVISIBLE);
		}else{
			mDeleteBtn.setVisibility(View.VISIBLE);
		}
		
	}
	private void decideWhichCheckedWithmType(String model){
		switch(model){
		case "绝缘导线":
			mType1.setChecked(true);
			break;
		case "电缆":
			mType2.setChecked(true);
			break;
		case "裸导线":
			mType3.setChecked(true);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			switch(buttonView.getId()){
			case R.id.type1:
				for(int i=0; i<mJYArray.length;i++){
					if(mModelStr.equals(mJYArray[i])){
						mJYPostion = i;
					}
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mJYModelListView.setSelection(mJYPostion);
					}
				}, 100);
				mType.setText("绝缘导线");
				mDLModelListView.setVisibility(View.GONE);
				mLDModelListView.setVisibility(View.GONE);
				mJYModelListView.setVisibility(View.VISIBLE);
				mModelStr = (String)mJYModelListView.getItemAtPosition(mJYPostion);
				mJYAdapter.notifyDataSetChanged();
				break;
			case R.id.type2:
				for(int i=0; i<mDLArray.length;i++){
					if(mModelStr.equals(mDLArray[i])){
						mDLPostion = i;
					}
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mDLModelListView.setSelection(mDLPostion);
					}
				}, 100);
				mType.setText("电缆");
				mDLModelListView.setVisibility(View.VISIBLE);
				mLDModelListView.setVisibility(View.GONE);
				mJYModelListView.setVisibility(View.GONE);
				mModelStr = (String)mDLModelListView.getItemAtPosition(mDLPostion);
				mDLAdapter.notifyDataSetChanged();
				break;
			case R.id.type3:
				for(int i=0; i<mLDArray.length;i++){
					if(mModelStr.equals(mLDArray[i])){
						mLDPostion = i;
					}
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mLDModelListView.setSelection(mLDPostion);
					}
				}, 100);
				mType.setText("裸导线");
				mDLModelListView.setVisibility(View.GONE);
				mLDModelListView.setVisibility(View.VISIBLE);
				mJYModelListView.setVisibility(View.GONE);
				mModelStr = (String)mLDModelListView.getItemAtPosition(mLDPostion);
				mLDAdapter.notifyDataSetChanged();
				break;
			}
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch(parent.getId()){
		case R.id.DLModelListView:
			mModelStr = (String)mDLModelListView.getItemAtPosition(position);
			mModel.setText(mModelStr);
			mDLAdapter.notifyDataSetChanged();
			break;
		case R.id.LDModelListView:
			mModelStr = (String)mLDModelListView.getItemAtPosition(position);
			mModel.setText(mModelStr);
			mLDAdapter.notifyDataSetChanged();
			break;
		case R.id.JYModelListView:
			mModelStr = (String)mJYModelListView.getItemAtPosition(position);
			mModel.setText(mModelStr);
			mJYAdapter.notifyDataSetChanged();
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
			if (mModelStr.equals(getItem(position))) { // 如果当前的行就是ListView中选中的一行，就更改显示样式 
				convertView.setBackgroundColor(MyApplication.Resources.getColor(R.color.custom_red));// 更改整行的背景色
			}else{
				convertView.setBackgroundColor(MyApplication.Resources.getColor(R.color.transparent));// 更改整行的背景色 
			}
			return convertView;
		}
	}
	
	public void toEditView(){
		super.toEditView();
		mName.setEnabled(false);
		mEditButtongroup.setVisibility(View.VISIBLE);
		mCheckButtonGroup.setVisibility(View.GONE);
		mHeightText.setTextColor(MyApplication.Resources.getColor(R.color.black));
		mHeight.setTextColor(MyApplication.Resources.getColor(R.color.popwindow_add_graphic_gray));
		mHeight.setEnabled(true);
		mOption1group.setVisibility(View.VISIBLE);
		mOption2group.setVisibility(View.VISIBLE);
		mTypeText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_dark_gray));
		mModelText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_dark_gray));
		
	}
	public void toCheckView(){
		super.toCheckView();
		mEditButtongroup.setVisibility(View.GONE);
		mCheckButtonGroup.setVisibility(View.VISIBLE);
		mHeightText.setTextColor(MyApplication.Resources.getColor(R.color.popwindow_add_graphic_blue));
		mHeight.setTextColor(MyApplication.Resources.getColor(R.color.black));
		mHeight.setEnabled(false);
		mOption1group.setVisibility(View.GONE);
		mOption2group.setVisibility(View.GONE);
		mTypeText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_blue));
		mModelText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_blue));
	}
	
	private String getEditState(DXRecord oldone, DXRecord newone){
		boolean isChanged = false;
		String state = "";
		if(oldone!=null){
			if(oldone.mIsEdit.equals("新增")){
				return "新增";
			}
			if(oldone.mIsEdit.equals("是")){
				return "是";
			}
			isChanged = isChanged || oldone.mName.equals(newone.mName)?false:true;
			isChanged = isChanged || oldone.mDefectID.equals(newone.mDefectID)?false:true;
			isChanged = isChanged || oldone.mHeight.equals(newone.mHeight)?false:true;
			isChanged = isChanged || oldone.mModel.equals(newone.mModel)?false:true;
			isChanged = isChanged || oldone.mPicture.equals(newone.mPicture)?false:true;
			isChanged = isChanged || oldone.mRemark.equals(newone.mRemark)?false:true;
			isChanged = isChanged || oldone.mType.equals(newone.mModel)?false:true;
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
	
	@Override
	protected void HideKeyboard() {
		super.HideKeyboard();
		Util.HideKeyboard(mMainActivity, mHeight);
	}
}
