package com.example.wzelectricitysupplier.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wzelectricitysupplier.R;

public class PunishCaseFragment extends Fragment implements OnItemSelectedListener{

	public static final String TAG = "PunishCaseFragment";
	
	private String mLiushuiID;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	Spinner punnish_type;
	Spinner big_classes1;
	Spinner xh1;
	Spinner weifa_action;
	Spinner big_classes2;
	Spinner xh2;
	TextView placename;
	TextView starttime;
	TextView place_address;
	TextView placearea;
	TextView company_name;
	TextView linkman;
	TextView linkmantel;
	TextView legalman;
	TextView legalmantel;
	TextView company_address;
	TextView company_remark;
	TextView badness_act;
	TextView egal_basis;
	TextView judging_standards;
	TextView judging_remark;
	TextView remark;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_punish_case, null);
		placename = (TextView)view.findViewById(R.id.placename);
		starttime = (TextView)view.findViewById(R.id.starttime);
		place_address = (TextView)view.findViewById(R.id.place_address);
		placearea = (TextView)view.findViewById(R.id.placearea);
		company_name = (TextView)view.findViewById(R.id.company_name);
		linkman = (TextView)view.findViewById(R.id.linkman);
		linkmantel = (TextView)view.findViewById(R.id.linkmantel);
		legalman = (TextView)view.findViewById(R.id.legalman);
		legalmantel = (TextView)view.findViewById(R.id.legalmantel);
		company_address = (TextView)view.findViewById(R.id.company_address);
		company_remark = (TextView)view.findViewById(R.id.company_remark);
		punnish_type = (Spinner)view.findViewById(R.id.punnish_type);
		ArrayAdapter<CharSequence> punnish_typeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		punnish_typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		punnish_type.setAdapter(punnish_typeAdapter);
		punnish_type.setOnItemSelectedListener(this);
		big_classes1 = (Spinner)view.findViewById(R.id.big_classes1);
		ArrayAdapter<CharSequence> big_classes1Adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		big_classes1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		big_classes1.setAdapter(big_classes1Adapter);
		big_classes1.setOnItemSelectedListener(this);
		xh1 = (Spinner)view.findViewById(R.id.xh1);
		ArrayAdapter<CharSequence> xh1Adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		xh1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		xh1.setAdapter(xh1Adapter);
		xh1.setOnItemSelectedListener(this);
		weifa_action = (Spinner)view.findViewById(R.id.weifa_action);
		ArrayAdapter<CharSequence> weifa_actionAdapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		weifa_actionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weifa_action.setAdapter(weifa_actionAdapter);
		weifa_action.setOnItemSelectedListener(this);
		big_classes2 = (Spinner)view.findViewById(R.id.big_classes2);
		ArrayAdapter<CharSequence> big_classes2Adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		big_classes2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		big_classes2.setAdapter(big_classes2Adapter);
		big_classes2.setOnItemSelectedListener(this);
		xh2 = (Spinner)view.findViewById(R.id.xh2);
		ArrayAdapter<CharSequence> xh2Adapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		xh2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		xh2.setAdapter(xh2Adapter);
		xh2.setOnItemSelectedListener(this);
		badness_act = (TextView)view.findViewById(R.id.badness_act);
		egal_basis = (TextView)view.findViewById(R.id.egal_basis);
		judging_standards = (TextView)view.findViewById(R.id.judging_standards);
		judging_remark = (TextView)view.findViewById(R.id.judging_remark);
		remark = (TextView)view.findViewById(R.id.remark);
		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	public void setLiushuiID(String liushuiID) {
		this.mLiushuiID = liushuiID;
	}
}
