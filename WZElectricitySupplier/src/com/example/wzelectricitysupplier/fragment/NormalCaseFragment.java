package com.example.wzelectricitysupplier.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;

public class NormalCaseFragment extends Fragment implements OnItemSelectedListener{

	public static final String TAG = "NormalCaseFragment";
	public MainActivity mMainActivity;
	
	private String mLiushuiID;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainActivity = (MainActivity)getActivity();
	}
	
	Spinner name;
	Spinner bigclass_name;
	Spinner smallclass_code;
	Spinner limiteddays;
	
	EditText time;
	EditText place_address;
	EditText place_area;
	EditText companyname;
	EditText linkman;
	EditText linkmantel;
	EditText company_remark;
	EditText checkman;
	EditText checktime;
	
	TextView legalman;
	TextView legalmantel;
	TextView company_address;
	TextView smallclass_name;
	TextView liandept;
	TextView jieancondittion;
	TextView limittime;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_normal_case, null);
		name = (Spinner)view.findViewById(R.id.name);
		ArrayAdapter<CharSequence> nameAdapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		name.setAdapter(nameAdapter);
		name.setOnItemSelectedListener(this);
		time = (EditText)view.findViewById(R.id.time);
		place_address = (EditText)view.findViewById(R.id.place_address);
		place_area = (EditText)view.findViewById(R.id.place_area);
		companyname = (EditText)view.findViewById(R.id.companyname);
		linkman = (EditText)view.findViewById(R.id.linkman);
		linkmantel = (EditText)view.findViewById(R.id.linkmantel);
		legalman = (TextView)view.findViewById(R.id.legalman);
		legalmantel = (TextView)view.findViewById(R.id.legalmantel);
		company_address = (TextView)view.findViewById(R.id.company_address);
		company_remark = (EditText)view.findViewById(R.id.company_remark);
		checkman = (EditText)view.findViewById(R.id.checkman);
		checktime = (EditText)view.findViewById(R.id.checktime);
		bigclass_name = (Spinner)view.findViewById(R.id.bigclass_name);
		ArrayAdapter<CharSequence> bigclass_nameAdapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		bigclass_nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bigclass_name.setAdapter(bigclass_nameAdapter);
		bigclass_name.setOnItemSelectedListener(this);
		smallclass_code = (Spinner)view.findViewById(R.id.smallclass_code);
		ArrayAdapter<CharSequence> smallclass_codeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		smallclass_codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		smallclass_code.setAdapter(smallclass_codeAdapter);
		smallclass_code.setOnItemSelectedListener(this);
		smallclass_name = (TextView)view.findViewById(R.id.smallclass_name);
		liandept = (TextView)view.findViewById(R.id.liandept);
		jieancondittion = (TextView)view.findViewById(R.id.jieancondittion);
		limittime = (TextView)view.findViewById(R.id.limittime);
		limiteddays = (Spinner)view.findViewById(R.id.limiteddays);
		ArrayAdapter<CharSequence> limiteddaysAdapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.small_class_1, android.R.layout.simple_spinner_item);
		smallclass_codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		limiteddays.setAdapter(limiteddaysAdapter);
		limiteddays.setOnItemSelectedListener(this);
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
