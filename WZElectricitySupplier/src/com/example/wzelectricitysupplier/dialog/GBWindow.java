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
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.adapter.PhotoSelectorAdapter;
import com.example.wzelectricitysupplier.bean.GBRecord;
import com.example.wzelectricitysupplier.customwidget.DividerItemDecoration;
import com.example.wzelectricitysupplier.database.GBTable;
import com.example.wzelectricitysupplier.function.ArcgisTool;
import com.example.wzelectricitysupplier.function.GraphicsPointMove;
import com.example.wzelectricitysupplier.function.photoselector.PhotoModel;
import com.example.wzelectricitysupplier.listener.MyOnSingleTapListener.AddMode;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;
import com.example.wzelectricitysupplier.setting.Util;

public class GBWindow extends BaseWindow {
	
	public final static String TAG = "GBWindow";
	
	private GBTable mGBTable;
	private Integer mPrimaryID;
	private Integer mGraphicID;
	private GBRecord mGBRecord;
	private Point mPoint;
	private DefectWindow mDefectWindow;
	private boolean isNewAdd;
	
	private TextView mCapacitytext;
	private EditText mCapacity;
	private ViewGroup mCheckButtonGroup;
	private ViewGroup mEditButtongroup;
	private ImageButton mMoveBtn;
	private EditText mEditableSpinnerEdit;
	private CheckBox mEditableSpinnerBtn;
	private ListView mEditablespinnerDropdown;
	// �½�
	public GBWindow(MainActivity mMainActivity, Point mPoint, int mGraphicID) {
		super(mMainActivity);
		this.mPoint = mPoint;
		this.mGraphicID = mGraphicID;
		mGBTable = mMainFragment.getGBTable();
		mCircuitTable = mMainFragment.getCircuitTable();
		isNewAdd = true;
	}
	// �Ѵ���
	public GBWindow(MainActivity mMainActivity, Integer mPrimaryID, Point mPoint, int mGraphicID) {
		super(mMainActivity);
		this.mPoint = mPoint;
		this.mPrimaryID = mPrimaryID;
		this.mGraphicID = mGraphicID;
		mGBTable = mMainFragment.getGBTable();
		mCircuitTable = mMainFragment.getCircuitTable();
		isNewAdd = false;
	}
	
	public void showWindow(final float screenx, final float screeny, final GraphicsLayer graphicsLayer){
		mView = mMainActivity.getLayoutInflater().inflate(R.layout.window_gb, null);
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
		mCapacitytext = (TextView)mView.findViewById(R.id.capacitytext);
		mCapacity = (EditText)mView.findViewById(R.id.capacity);
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
			mGBRecord = mGBTable.selectBarTypeVariable(mPrimaryID);
		}
		if(mGBRecord!=null){
			String[] mNameParts = separateName(mGBRecord.mName);
			mEditableSpinnerEdit.setText(mNameParts[0]);
			mEditableSpinnerEdit.setSelection(mNameParts[0].length());
			mName.setText(mNameParts[1]);
			mName.setSelection(mNameParts[1].length());
			mRemark.setText(mGBRecord.mRemark);
			mCapacity.setText(mGBRecord.mCapacity);
			
			String pictureStr = mGBRecord.mPicture;
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
			
			String defectIDStr = mGBRecord.mDefectID;
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
				String capacity = mCapacity.getText().toString();
				
				String picture = "";
				String defectIds = "";
				// �ύʱ��֤�� �����û��༭ʱ�޸��û����������໥�����͸�����ʷ��Ƭ����
				ArrayList<String> tempNameList = new ArrayList<String>(mNormalNameList);
				for(int i=0; i<tempNameList.size(); i++){ 
					String picname = tempNameList.get(i);
					File pictureFile = new File(Util.getPictureDirRootPath(), picname);
					
					String oldChar = picname.split("_")[1];
					picname = picname.replace(oldChar, devicename);
					mNormalNameList.set(i, picname);
					
					if(pictureFile.exists()) {
						// ��ʷ��Ƭ������
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
					mDefectIDList = AppUtil.turnStringToList(mGBTable.selectBarTypeVariable(mPrimaryID).mDefectID);
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
						Util.ShowDialog(mMainActivity, "����д���������ƣ���·�� + �˱����ƣ�");
						HideKeyboard();
					}else{
						GBRecord record = new GBRecord(name, circuitid, capacity, picture, defectIds, remark, "��");
						record = new GBRecord(name, circuitid, capacity, picture, defectIds, remark, getEditState(mGBRecord, record));
						if(mPrimaryID == null){ 
							// ����
							mPrimaryID = mGBTable.add(record, mPoint);
							ArcgisTool.updateGraphic(mGraphicID, graphicsLayer, mPrimaryID, AddMode.DeviceGB);
							ArcgisTool.addTextGraphic(mPoint, record.mName, mMap, mMainFragment.getDeviceTextLayer());
						}else{
							// ����
							boolean b = mGBTable.update(mPoint, record, mPrimaryID);
							if(b && !mGBRecord.mName.equals(name)){ // �����Ʊ����� ͬ�������յ��ֶ�
								AppUtil.updateDXField(mMainFragment, mCircuitTable.getCircuitID(), name, mGBRecord.mName);
							}
							mMainFragment.doLoadCircuit(circuitid);
						}
						mTemporaryNameList.clear();
						mGBRecord = mGBTable.selectBarTypeVariable(mPrimaryID);
						mPopwindow.dismiss();
					}
				}else{
					Log.e(TAG, "�ύ���ݿ�ʧ�ܣ���·IDΪ��");
					if(MyApplication.Debug){
						Util.Toast("�ύ���ݿ�ʧ�ܣ���·IDΪ��");
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
					mDefectWindow = new DefectWindow(mMainActivity, mPrimaryID, DeviceType.DeviceGB);
					mDefectWindow.showWindow();
				}else{
					Util.Toast("�豸������");
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
							result = result && mGBTable.delete(mPrimaryID);
							if(result) {
								deleteRelatePicture();
								mMainFragment.doLoadCircuit(mCircuitTable.getCircuitID());
							}
						}else{
							Util.Toast("��δ����");
						}
					}
				};
				DialogInterface.OnClickListener mNegativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				Util.ShowDialog(mMainActivity, "ɾ�������ݲ��ɻָ���ȷ��Ҫɾ����", mPositiveListener, mNegativeListener);
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
	
	public void toEditView(){
		super.toEditView();
		mEditButtongroup.setVisibility(View.VISIBLE);
		mCheckButtonGroup.setVisibility(View.GONE);
		mCapacitytext.setTextColor(MyApplication.Resources.getColor(R.color.black));
		mCapacity.setTextColor(MyApplication.Resources.getColor(R.color.popwindow_add_graphic_gray));
		mCapacity.setEnabled(true);
//		mEditableSpinnerEdit.setEnabled(true);
		mEditableSpinnerEdit.setTextColor(MyApplication.Resources.getColor(R.color.gray));
		mEditableSpinnerBtn.setClickable(true);
	}
	public void toCheckView(){
		super.toCheckView();
		mEditButtongroup.setVisibility(View.GONE);
		mCheckButtonGroup.setVisibility(View.VISIBLE);
		mCapacitytext.setTextColor(MyApplication.Resources.getColor(R.color.popwindow_add_graphic_blue));
		mCapacity.setTextColor(MyApplication.Resources.getColor(R.color.black));
		mCapacity.setEnabled(false);
		mEditableSpinnerEdit.setEnabled(false);
		mEditableSpinnerEdit.setTextColor(MyApplication.Resources.getColor(R.color.black));
		mEditableSpinnerBtn.setClickable(false);
		mEditableSpinnerBtn.setChecked(false);
	}
	
	private String getEditState(GBRecord oldone, GBRecord newone){
		boolean isChanged = false;
		String state = "";
		if(oldone!=null){
			if(oldone.mIsEdit.equals("����")){
				return "����";
			}
			if(oldone.mIsEdit.equals("��")){
				return "��";
			}
			isChanged = isChanged || oldone.mName.equals(newone.mName)?false:true;
			isChanged = isChanged || oldone.mDefectID.equals(newone.mDefectID)?false:true;
			isChanged = isChanged || oldone.mPicture.equals(newone.mPicture)?false:true;
			isChanged = isChanged || oldone.mRemark.equals(newone.mRemark)?false:true;
			isChanged = isChanged || oldone.mCapacity.equals(newone.mCapacity)?false:true;
			if(isChanged){
				state = "��";
			}else{
				state = "��";
			}
		}else{
			state = "����";
		}
		return state;
	}
	
	@Override
	protected void HideKeyboard() {
		super.HideKeyboard();
		Util.HideKeyboard(mMainActivity, mCapacity);
		Util.HideKeyboard(mMainActivity, mEditableSpinnerEdit);
	}
}