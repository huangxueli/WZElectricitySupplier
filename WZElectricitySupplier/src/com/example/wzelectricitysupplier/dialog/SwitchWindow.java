package com.example.wzelectricitysupplier.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.PhotoSelectorAdapter;
import com.example.wzelectricitysupplier.bean.SwitchRecord;
import com.example.wzelectricitysupplier.customwidget.DividerItemDecoration;
import com.example.wzelectricitysupplier.database.SwitchTable;
import com.example.wzelectricitysupplier.function.ArcgisTool;
import com.example.wzelectricitysupplier.function.GraphicsPointMove;
import com.example.wzelectricitysupplier.function.photoselector.PhotoModel;
import com.example.wzelectricitysupplier.listener.MyOnSingleTapListener.AddMode;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;
import com.example.wzelectricitysupplier.setting.Util;

public class SwitchWindow extends BaseWindow implements OnCheckedChangeListener{
	
	public final static String TAG = "SwitchWindow";
	
	private SwitchTable mSwitchTable;
	private Integer mPrimaryID;
	private Integer mGraphicID;
	private SwitchRecord mSwitchRecord;
	private Point mPoint;
	private DefectWindow mDefectWindow;
	private boolean isNewAdd;
	
	private RadioGroup mOption1group;
	private RadioButton mOption1_1;
	private RadioButton mOption1_2;
	private TextView mHavedzText;
	private TextView mHavedz;
	private RadioGroup mOption2group;
	private RadioButton mOption2_1;
	private RadioButton mOption2_2;
	private TextView mStateText;
	private TextView mState;
	private ViewGroup mCheckButtonGroup;
	private ViewGroup mEditButtongroup;
	private ImageButton mMoveBtn;
	private EditText mEditableSpinnerEdit;
	private CheckBox mEditableSpinnerBtn;
	private ListView mEditablespinnerDropdown;
	// 新建
	public SwitchWindow(MainActivity mMainActivity, Point mPoint, int mGraphicID) {
		super(mMainActivity);
		this.mPoint = mPoint;
		this.mGraphicID = mGraphicID;
		mSwitchTable = mMainFragment.getSwitchTable();
		mCircuitTable = mMainFragment.getCircuitTable();
		isNewAdd = true;
	}
	// 已存在
	public SwitchWindow(MainActivity mMainActivity, Integer mPrimaryID, Point mPoint, int mGraphicID) {
		super(mMainActivity);
		this.mPoint = mPoint;
		this.mPrimaryID = mPrimaryID;
		this.mGraphicID = mGraphicID;
		mSwitchTable = mMainFragment.getSwitchTable();
		mCircuitTable = mMainFragment.getCircuitTable();
		isNewAdd = false;
	}
	
	public void showWindow(final float screenx, final float screeny, final GraphicsLayer graphicsLayer){
		mView = mMainActivity.getLayoutInflater().inflate(R.layout.window_switch, null);
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
		mEditableSpinnerEdit = (EditText)mView.findViewById(R.id.editableSpinner_edit);
		mEditablespinnerDropdown = (ListView)mView.findViewById(R.id.editablespinner_dropdowm);
		List<String> mCombinationNames = getCombinationNameByBranch(mCircuitTable.getBranchName());
		final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(mMainActivity, 
				R.layout.listitem_simple_dropdown_item_1line, (String[])mCombinationNames.toArray(new String[mCombinationNames.size()]));
		mEditablespinnerDropdown.setAdapter(mAdapter);
		mEditablespinnerDropdown.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String circuitname = (String)mAdapter.getItem(position);
				mEditableSpinnerEdit.setText(circuitname);
				mEditableSpinnerEdit.setSelection(circuitname.length());
				mEditableSpinnerBtn.setChecked(false);
			}
		});
		mScrollView1 = (ScrollView)mView.findViewById(R.id.ScrollView1);
		mDevideline = (ImageView)mView.findViewById(R.id.devideline);
		mRemarktext = (TextView)mView.findViewById(R.id.remarktext);
		mRemark = (EditText)mView.findViewById(R.id.remark);
		mCameratext = (TextView)mView.findViewById(R.id.cameratext);
		mOption1group = (RadioGroup)mView.findViewById(R.id.option1group);
		mOption1_1 = (RadioButton)mView.findViewById(R.id.option1_1);
		mOption1_1.setOnCheckedChangeListener(this);
		mOption2group = (RadioGroup)mView.findViewById(R.id.option2group);
		mOption1_2 = (RadioButton)mView.findViewById(R.id.option1_2);
		mOption1_2.setOnCheckedChangeListener(this);
		mHavedzText = (TextView)mView.findViewById(R.id.HavedzText);
		mHavedz = (TextView)mView.findViewById(R.id.Havedz);
		mOption2_1 = (RadioButton)mView.findViewById(R.id.option2_1);
		mOption2_1.setOnCheckedChangeListener(this);
		mOption2_2 = (RadioButton)mView.findViewById(R.id.option2_2);
		mOption2_2.setOnCheckedChangeListener(this);
		mStateText = (TextView)mView.findViewById(R.id.StateText);
		mState = (TextView)mView.findViewById(R.id.State);
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
			mSwitchRecord = mSwitchTable.selectSwitch(mPrimaryID);
		}
		if(mSwitchRecord!=null){
			String[] mNameParts = separateName(mSwitchRecord.mName);
			mEditableSpinnerEdit.setText(mNameParts[0]);
			mEditableSpinnerEdit.setSelection(mNameParts[0].length());
			mName.setText(mNameParts[1]);
			mName.setSelection(mNameParts[1].length());
			mRemark.setText(mSwitchRecord.mRemark);
			mHavedz.setText(mSwitchRecord.mHavedz);
			decideWhichCheckedWithHavedz(mSwitchRecord.mHavedz);
			mState.setText(mSwitchRecord.mState);
			decideWhichCheckedWithState(mSwitchRecord.mState);
			
			String pictureStr = mSwitchRecord.mPicture;
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
			
			String defectIDStr = mSwitchRecord.mDefectID;
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
		}
		mEditableSpinnerBtn = (CheckBox)mView.findViewById(R.id.editableSpinner_btn);
		mEditableSpinnerBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mEditablespinnerDropdown.setVisibility(View.VISIBLE);
				}else{
					mEditablespinnerDropdown.setVisibility(View.GONE);
				}
			}
		});
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
				String circuitname = mEditableSpinnerEdit.getText().toString();
				String devicename = mName.getText().toString();
				String name = combineName(circuitname, devicename);
				String remark = mRemark.getText().toString();
				String havedz = mHavedz.getText().toString();
				String state = mState.getText().toString();
				String picture = "";
				String defectIds = "";
				// 提交时认证， 避免用户编辑时修改用户名和拍照相互操作和更新历史照片名称
				ArrayList<String> tempNameList = new ArrayList<String>(mNormalNameList);
				for(int i=0; i<tempNameList.size(); i++){ 
					String picname = tempNameList.get(i);
					File pictureFile = new File(Util.getPictureDirRootPath(), picname);
					
					String oldChar = picname.split("_")[1];
					picname = picname.replace(oldChar, devicename);
					mNormalNameList.set(i, picname);
					
					if(pictureFile.exists()) {
						// 历史照片重命名
						pictureFile.renameTo(new File(Util.getPictureDirRootPath() + picname));
						pictureSelectorRefresh();
					}
				}
				for(int i=0; i<mNormalNameList.size(); i++){
					if(i==mNormalNameList.size()-1){
						picture += mNormalNameList.get(i);
					}else{
						picture += mNormalNameList.get(i) + "&";
					}
				}
				if(mPrimaryID!=null){
					mDefectIDList = AppUtil.turnStringToList(mSwitchTable.selectSwitch(mPrimaryID).mDefectID);
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
					if(name==null){
						Util.ShowDialog(mMainActivity, "请填写完整的名称：线路名 + 开关名称！");
						HideKeyboard();
					}else{
						SwitchRecord record = new SwitchRecord(name, circuitid, havedz, state, picture, defectIds, remark, "否");
						record = new SwitchRecord(name, circuitid, havedz, state, picture, defectIds, remark, getEditState(mSwitchRecord, record));
						if(mPrimaryID == null){ 
							// 新增
							mPrimaryID = mSwitchTable.add(record, mPoint);
							ArcgisTool.updateGraphic(mGraphicID, graphicsLayer, mPrimaryID, AddMode.DeviceSwitch);
							ArcgisTool.addTextGraphic(mPoint, record.mName, mMap, mMainFragment.getDeviceTextLayer());
						}else{
							// 更新
							boolean b = mSwitchTable.update(mPoint, record, mPrimaryID);
							if(b && !mSwitchRecord.mName.equals(name)){ // 若名称被更改 同步起点或终点字段
								AppUtil.updateDXField(mMainFragment, mCircuitTable.getCircuitID(), name, mSwitchRecord.mName);
								AppUtil.updateDefectField(mMainFragment, name, mSwitchRecord.mName, mSwitchRecord.mDefectID);
							}
							mMainFragment.doLoadCircuit(circuitid);
						}
						mTemporaryNameList.clear();
						mSwitchRecord = mSwitchTable.selectSwitch(mPrimaryID);
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
					ArcgisTool.removeGraphic(screenx, screeny, mMap, graphicsLayer);
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
		
		mMoveBtn = (ImageButton)mView.findViewById(R.id.moveBtn);
		mMoveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!graphicsLayer.getGraphic(mGraphicID).getAttributes().isEmpty()){
					mOnAfterMoveListener = GraphicsPointMove.Prepare(mMainFragment, graphicsLayer, mGraphicID);
					mPopwindow.dismiss();
				}
			}
		});
		mDefectManageBtn = (ImageButton)mView.findViewById(R.id.defectmanageBtn);
		mDefectManageBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPrimaryID!=null){
					mDefectWindow = new DefectWindow(mMainActivity, mPrimaryID, DeviceType.DeviceSwitch);
					mDefectWindow.showWindow();
				}else{
					Util.Toast("设备不存在");
				}
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
							result = result && mSwitchTable.delete(mPrimaryID);
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
		if(isNewAdd){
			mMoveBtn.setVisibility(View.INVISIBLE);
			mDeleteBtn.setVisibility(View.INVISIBLE);
		}else{
			mMoveBtn.setVisibility(View.VISIBLE);
			mDeleteBtn.setVisibility(View.VISIBLE);
		}
		
	}
	private void decideWhichCheckedWithHavedz(String type){
		switch(type){
		case "是":
			mOption1_1.setChecked(true);
			break;
		case "否":
			mOption1_2.setChecked(true);
			break;
		}
	}
	private void decideWhichCheckedWithState(String type){
		switch(type){
		case "分":
			mOption2_1.setChecked(true);
			break;
		case "合":
			mOption2_2.setChecked(true);
			break;
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			switch(buttonView.getId()){
			case R.id.option1_1:
				mHavedz.setText("是");
				break;
			case R.id.option1_2:
				mHavedz.setText("否");
				break;
			case R.id.option2_1:
				mState.setText("分");
				break;
			case R.id.option2_2:
				mState.setText("合");
				break;
			}
		}
	}
	
	public void toEditView(){
		super.toEditView();
		mEditButtongroup.setVisibility(View.VISIBLE);
		mCheckButtonGroup.setVisibility(View.GONE);
		mHavedzText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_dark_gray));
		mStateText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_dark_gray));
		mOption1group.setVisibility(View.VISIBLE);
		mOption2group.setVisibility(View.VISIBLE);
//		mEditableSpinnerEdit.setEnabled(true);
		mEditableSpinnerEdit.setTextColor(MyApplication.Resources.getColor(R.color.gray));
		mEditableSpinnerBtn.setClickable(true);
	}
	public void toCheckView(){
		super.toCheckView();
		mEditButtongroup.setVisibility(View.GONE);
		mCheckButtonGroup.setVisibility(View.VISIBLE);
		mHavedzText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_blue));
		mStateText.setBackground(MyApplication.Resources.getDrawable(R.color.popwindow_add_graphic_blue));
		mOption1group.setVisibility(View.GONE);
		mOption2group.setVisibility(View.GONE);
		mEditableSpinnerEdit.setEnabled(false);
		mEditableSpinnerEdit.setTextColor(MyApplication.Resources.getColor(R.color.black));
		mEditableSpinnerBtn.setClickable(false);
		mEditableSpinnerBtn.setChecked(false);
	}
	
	private String getEditState(SwitchRecord oldone, SwitchRecord newone){
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
			isChanged = isChanged || oldone.mPicture.equals(newone.mPicture)?false:true;
			isChanged = isChanged || oldone.mRemark.equals(newone.mRemark)?false:true;
			isChanged = isChanged || oldone.mRemark.equals(newone.mRemark)?false:true;
			isChanged = isChanged || oldone.mHavedz.equals(newone.mHavedz)?false:true;
			isChanged = isChanged || oldone.mState.equals(newone.mState)?false:true;
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
		Util.HideKeyboard(mMainActivity, mEditableSpinnerEdit);
	}
}
