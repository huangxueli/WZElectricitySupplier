package com.example.wzelectricitysupplier.AndBase.Util;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.wzelectricitysupplier.AndBase.Util.AbAlertDialogFragment.AbDialogOnClickListener;
import com.example.wzelectricitysupplier.AndBase.Util.AbDialogFragment.AbDialogOnLoadListener;


public class AbDialogUtil {

	/** dialog tag */
	public static final String TAG = "AbDialogUtil";

	/**
	 * 全屏显示�?��Fragment
	 * 
	 * @param view
	 * @return
	 */
	public static AbSampleDialogFragment showFragment(View view) {

		removeDialog(view);

		FragmentActivity activity = (FragmentActivity) view.getContext();
		// Create and show the dialog.
		AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Holo_Light);
		newFragment.setContentView(view);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		// 作为全屏显示,使用“content”作为fragment容器的基本视�?这始终是Activity的基本视�?
		ft.add(android.R.id.content, newFragment, TAG)
				.addToBackStack(null).commit();

		return newFragment;
	}

	/**
	 * 显示�?��自定义的对话�?有背景层)
	 * 
	 * @param view
	 */
	public static AbSampleDialogFragment showDialog(View view) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);

		// Create and show the dialog.
		AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setContentView(view);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?��自定义的对话�?有背景层)
	 * 
	 * @param view
	 * @param animEnter
	 * @param animExit
	 * @param animPopEnter
	 * @param animPopExit
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view, int animEnter,
			int animExit, int animPopEnter, int animPopExit) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);

		// Create and show the dialog.
		AbSampleDialogFragment newFragment = AbSampleDialogFragment
				.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setContentView(view);

		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?��自定义的对话�?有背景层)
	 * 
	 * @param view
	 * @param onCancelListener
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view,
			DialogInterface.OnCancelListener onCancelListener) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);

		// Create and show the dialog.
		AbSampleDialogFragment newFragment = AbSampleDialogFragment
				.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setContentView(view);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.setOnCancelListener(onCancelListener);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?��自定义的对话�?有背景层)
	 * 
	 * @param view
	 * @param animEnter
	 * @param animExit
	 * @param animPopEnter
	 * @param animPopExit
	 * @param onCancelListener
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view, int animEnter,
			int animExit, int animPopEnter, int animPopExit,
			DialogInterface.OnCancelListener onCancelListener) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);

		// Create and show the dialog.
		AbSampleDialogFragment newFragment = AbSampleDialogFragment
				.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setContentView(view);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.setOnCancelListener(onCancelListener);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?��自定义的对话�?无背景层)
	 * 
	 * @param view
	 */
	public static AbSampleDialogFragment showPanel(View view) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);

		// Create and show the dialog.
		AbSampleDialogFragment newFragment = AbSampleDialogFragment
				.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Light_Panel);
		newFragment.setContentView(view);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?��自定义的对话�?无背景层)
	 * 
	 * @param view
	 * @param onCancelListener
	 * @return
	 */
	public static AbSampleDialogFragment showPanel(View view,
			DialogInterface.OnCancelListener onCancelListener) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);

		// Create and show the dialog.
		AbSampleDialogFragment newFragment = AbSampleDialogFragment
				.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Light_Panel);
		newFragment.setContentView(view);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.setOnCancelListener(onCancelListener);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：对话框dialog （图标，标题，String内容�?
	 * 
	 * @param context
	 * @param icon
	 * @param title
	 *            对话框标题内�?
	 * @param view
	 *            对话框提示内�?
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,
			int icon, String title, String message) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				icon, title, message, null, null);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?���?��的对话框（图标，标题，string内容，确认，取消�?
	 * 
	 * @param context
	 * @param icon
	 * @param title
	 *            对话框标题内�?
	 * @param message
	 *            对话框提示内�?
	 * @param onClickListener
	 *            点击确认按钮的事件监�?
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,
			int icon, String title, String message,
			AbDialogOnClickListener onClickListener) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				icon, title, message, null, onClickListener);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?���?��的对话框（标题，String内容，确认，取消�?
	 * 
	 * @param context
	 * @param title
	 *            对话框标题内�?
	 * @param message
	 *            对话框提示内�?
	 * @param onClickListener
	 *            点击确认按钮的事件监�?
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,
			String title, String message,
			AbDialogOnClickListener onClickListener) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				0, title, message, null, onClickListener);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?���?��的对话框（View内容�?
	 * 
	 * @param view
	 *            对话框标题内�?
	 */
	public static AbAlertDialogFragment showAlertDialog(View view) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				0, null, null, view, null);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?���?��的对话框（String内容�?
	 * 
	 * @param context
	 * @param title
	 *            对话框标题内�?
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,
			String message) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				0, null, message, null, null);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：对话框dialog （图标，标题，View内容�?
	 * 
	 * @param icon
	 * @param title
	 *            对话框标题内�?
	 * @param view
	 *            对话框提示内�?
	 */
	public static AbAlertDialogFragment showAlertDialog(int icon, String title,
			View view) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				icon, title, null, view, null);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?���?��的对话框（图标，标题，View内容，确认，取消�?
	 * 
	 * @param icon
	 * @param title
	 *            对话框标题内�?
	 * @param view
	 *            对话框提示内�?
	 * @param onClickListener
	 *            点击确认按钮的事件监�?
	 */
	public static AbAlertDialogFragment showAlertDialog(int icon, String title,
			View view, AbDialogOnClickListener onClickListener) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				icon, title, null, view, onClickListener);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：对话框dialog （标题，View内容�?
	 * 
	 * @param title
	 *            对话框标题内�?
	 * @param view
	 *            对话框提示内�?
	 */
	public static AbAlertDialogFragment showAlertDialog(String title, View view) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				0, title, null, view, null);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 显示�?���?��的对话框（标题，View内容，确认，取消�?
	 * 
	 * @param title
	 *            对话框标题内�?
	 * @param view
	 *            对话框提示内�?
	 * @param onClickListener
	 *            点击确认按钮的事件监�?
	 */
	public static AbAlertDialogFragment showAlertDialog(String title,
			View view, AbDialogOnClickListener onClickListener) {
		FragmentActivity activity = (FragmentActivity) view.getContext();
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				0, title, null, view, onClickListener);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：对话框dialog （标题，String内容�?
	 * 
	 * @param context
	 * @param title
	 *            对话框标题内�?
	 * @param view
	 *            对话框提示内�?
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,
			String title, String message) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(
				0, title, message, null, null);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：显示进度框.
	 * 
	 * @param context
	 *            the context
	 * @param indeterminateDrawable
	 *            用默认请�?
	 * @param message
	 *            the message
	 */
	public static AbProgressDialogFragment showProgressDialog(Context context,
			int indeterminateDrawable, String message) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbProgressDialogFragment newFragment = AbProgressDialogFragment
				.newInstance(indeterminateDrawable, message);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：显示加载框.
	 * 
	 * @param context
	 *            the context
	 * @param indeterminateDrawable
	 * @param message
	 *            the message
	 */
	public static AbLoadDialogFragment showLoadDialog(Context context,
			int indeterminateDrawable, String message) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(
				DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：显示加载框.
	 * 
	 * @param context
	 *            the context
	 * @param indeterminateDrawable
	 * @param message
	 *            the message
	 */
	public static AbLoadDialogFragment showLoadDialog(Context context,
			int indeterminateDrawable, String message,
			AbDialogOnLoadListener abDialogOnLoadListener) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(
				DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：显示加载框.
	 * 
	 * @param context
	 *            the context
	 * @param indeterminateDrawable
	 * @param message
	 *            the message
	 */
	public static AbLoadDialogFragment showLoadPanel(Context context,
			int indeterminateDrawable, String message) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(
				DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Light_Panel);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：显示加载框.
	 * 
	 * @param context
	 *            the context
	 * @param indeterminateDrawable
	 * @param message
	 *            the message
	 * @param abDialogOnRefreshListener
	 */
	public static AbLoadDialogFragment showLoadPanel(Context context,
			int indeterminateDrawable, String message,
			AbDialogOnLoadListener abDialogOnLoadListener) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(
				DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Light_Panel);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：显示刷新框.
	 * 
	 * @param context
	 *            the context
	 * @param indeterminateDrawable
	 * @param message
	 *            the message
	 * @param abDialogOnRefreshListener
	 */
	public static AbRefreshDialogFragment showRefreshDialog(Context context,
			int indeterminateDrawable, String message) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbRefreshDialogFragment newFragment = AbRefreshDialogFragment
				.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(null);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：显示刷新框.
	 * 
	 * @param context
	 * @param indeterminateDrawable
	 * @param message
	 * @param abDialogOnRefreshListener
	 * @return
	 */
	public static AbRefreshDialogFragment showRefreshDialog(Context context,
			int indeterminateDrawable, String message,
			AbDialogOnLoadListener abDialogOnLoadListener) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbRefreshDialogFragment newFragment = AbRefreshDialogFragment
				.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：显示刷新框.
	 * 
	 * @param context
	 *            the context
	 * @param indeterminateDrawable
	 * @param message
	 *            the message
	 */
	public static AbRefreshDialogFragment showRefreshPanel(Context context,
			int indeterminateDrawable, String message) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbRefreshDialogFragment newFragment = AbRefreshDialogFragment
				.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Light_Panel);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(null);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：显示刷新框.
	 * 
	 * @param context
	 * @param indeterminateDrawable
	 * @param message
	 * @param abDialogOnRefreshListener
	 * @return
	 */
	public static AbRefreshDialogFragment showRefreshPanel(Context context,
			int indeterminateDrawable, String message,
			AbDialogOnLoadListener abDialogOnLoadListener) {
		FragmentActivity activity = (FragmentActivity) context;
		removeDialog(activity);
		AbRefreshDialogFragment newFragment = AbRefreshDialogFragment
				.newInstance(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_Light_Panel);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();
		// 指定�?��过渡动画
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		newFragment.show(ft, TAG);
		return newFragment;
	}

	/**
	 * 描述：移除Fragment.
	 * 
	 * @param context
	 *            the context
	 */
	public static void removeDialog(Context context) {
		try {
			FragmentActivity activity = (FragmentActivity) context;
			FragmentTransaction ft = activity.getFragmentManager()
					.beginTransaction();
			// 指定�?��过渡动画
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			Fragment prev = activity.getFragmentManager().findFragmentByTag(TAG);
			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);
			ft.commit();
		} catch (Exception e) {
			// 可能有Activity已经被销毁的异常
			e.printStackTrace();
		}
	}

	/**
	 * 描述：移除Fragment和View
	 * 
	 * @param view
	 */
	public static void removeDialog(View view) {
		removeDialog(view.getContext());
		ViewParent parent = view.getParent();
		if (parent != null) {
			if (parent instanceof ViewGroup) {
				((ViewGroup) parent).removeView(view);
			}
		}
	}

}
