package com.example.wzelectricitysupplier;

import java.util.HashMap;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;

public class MyApplication extends Application {
	
	public static Boolean Debug;
	public static Context Context;
	public static LayoutInflater Inflater;
	public static Resources Resources;
	
	public static HashMap<String, Fragment> FragmentMap = new HashMap<String, Fragment>();
	
	
}
