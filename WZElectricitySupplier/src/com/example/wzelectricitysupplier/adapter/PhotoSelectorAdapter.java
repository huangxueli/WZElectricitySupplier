package com.example.wzelectricitysupplier.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MyApplication;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.function.photoselector.PhotoModel;
import com.example.wzelectricitysupplier.setting.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoSelectorAdapter extends RecyclerView.Adapter<PhotoSelectorAdapter.ViewHolder> {
	
	private int mItemWidth;
	private int mHorizentalNum = 3;
	private LayoutParams mItemLayoutParams;
	private ArrayList<PhotoModel> mDataset;
	private OnItemClickListener mOnItemClickListener;
	private OnItemLongClickListener mOnItemLongClickListener;
	private OnDeleteClickListener mDeleteClickListener;
	private boolean mIsPhotoItemEdit;
	
	public PhotoSelectorAdapter(ArrayList<PhotoModel> dataset, int mScreenWidth){
		super();
		if(dataset ==null)
			dataset = new ArrayList<PhotoModel>();
		mDataset = dataset;
		setItemWidth(mScreenWidth);
	}
	
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = View.inflate(viewGroup.getContext(), R.layout.photoselector_layout_photoitem, null);
		view.setLayoutParams(mItemLayoutParams);
		ViewHolder holder = new ViewHolder(view, mOnItemClickListener, mOnItemLongClickListener);
		return holder;
	}
	
	public void onBindViewHolder(ViewHolder viewHolder, final int i) {
		fillThumbnail(viewHolder.mPhotoPreview, mDataset.get(i));
		if(mDeleteClickListener!=null)
			viewHolder.mDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDeleteClickListener.doDelete(mDataset.get(i).getName());
				}
			});
		if(mIsPhotoItemEdit){
			viewHolder.mDelete.setVisibility(View.VISIBLE);
		}else{
			viewHolder.mDelete.setVisibility(View.GONE);
		}
	}
	
	// 设置每一个Item的宽高
	public void setItemWidth(int mScreenWidth) {
		int mHorizentalSpace = MyApplication.Resources.getDimensionPixelSize(R.dimen.sticky_item_horizontalSpacing);
		mItemWidth = (mScreenWidth - (mHorizentalSpace * (mHorizentalNum - 1))) / mHorizentalNum;
		mItemLayoutParams = new LayoutParams(mItemWidth, mItemWidth);
	}
	public void setPhotoItemEdit(boolean mIsPhotoItemEdit) {
		this.mIsPhotoItemEdit = mIsPhotoItemEdit;
	}
	// 更新数据
	public void update1(List<PhotoModel> photos) {
		if (photos == null) return;
		mDataset.clear();
		for (PhotoModel model : photos) {
			mDataset.add(model);
		}
		notifyDataSetChanged();
	}
	// 更新数据
	public void update2(List<String> photoname) {
		if (photoname == null) return;
		mDataset.clear();
		for (String name : photoname) {
			String originalPath = Util.getPictureDirRootPath() + name;
			mDataset.add(new PhotoModel(name, originalPath));
		}
		notifyDataSetChanged();
	}
	// 更新数据
	public void update(boolean mIsPhotoItemEdit) {
		this.mIsPhotoItemEdit = mIsPhotoItemEdit;
		notifyDataSetChanged();
	}
	public PhotoModel getItem(int position){
		PhotoModel pm  = null;
		if(position<mDataset.size()){
			pm =  mDataset.get(position);
		}
		return pm;
	}
	public ArrayList<PhotoModel> getDataset(){
		return mDataset;
	}
	public void setOnItemClickListener(OnItemClickListener listener){
		this.mOnItemClickListener = listener;
	}
	
	public void setOnItemLongClickListener(OnItemLongClickListener listener){
		this.mOnItemLongClickListener = listener;
	}
	public void setDeleteClickListener(OnDeleteClickListener listener){
		this.mDeleteClickListener = listener;
	}
	
	// 填充路径下的图片对应的缩略图
	private void fillThumbnail(final ImageView mImageView, final PhotoModel photo) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ImageLoader.getInstance().displayImage("file://" + photo.getOriginalPath(), mImageView);
			}
		}, new Random().nextInt(10));
	}
	
	
	
	public int getItemCount() {
		return mDataset.size();
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener,OnLongClickListener{

		public ImageView mPhotoPreview;
		public TextView mDelete;
		public CheckBox mPhotoCheck;
		
		private OnItemClickListener mListener;
		private OnItemLongClickListener mLongClickListener;

		public ViewHolder(View view, OnItemClickListener mOnItemClickListener, OnItemLongClickListener mOnItemLongClickListener) {
			super(view);
			mPhotoPreview = (ImageView)view.findViewById(R.id.photo_preview);
			mPhotoCheck = (CheckBox)view.findViewById(R.id.photo_check);
			mDelete = (TextView)view.findViewById(R.id.delete);
			mListener = mOnItemClickListener;
			mLongClickListener = mOnItemLongClickListener;
			view.setOnClickListener(this);
			view.setOnLongClickListener(this);
		}
		
		public void onClick(View v) {
			if (mListener != null) {
				mListener.onItemClick(v, getPosition());
			}
		}

		public boolean onLongClick(View v) {
			if (mLongClickListener != null) {
				mLongClickListener.onItemLongClick(v, getPosition());
			}
			return true;
		}
	}
	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}
	public interface OnItemLongClickListener {
		public void onItemLongClick(View view, int position);
	}
	public interface OnDeleteClickListener {
		public void doDelete(String name);
	}
	
}
