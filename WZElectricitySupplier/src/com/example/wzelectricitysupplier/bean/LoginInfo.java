package com.example.wzelectricitysupplier.bean;

public class LoginInfo {
	
	public String TestPlugin;
	public String UID;
	public String ENNAME;
	public String PASSWORD;
	public String DPID;
	public String CNNAME;
	public String SEX; 
	public String PHONE;
	public String EMAIL;
	public String FOXNUM;
	public String ADDRESS;
	public String POSTNUM;
	public String DESTEXT;
	public String RULEID;
	public String WEIXINID;
	public String ROWID;
		
	public LoginInfo(String TestPlugin, String UID, String ENNAME, String PASSWORD, String DPID, String CNNAME, String SEX, 
			String PHONE, String EMAIL, String FOXNUM, String ADDRESS, String POSTNUM, String DESTEXT, 
			String RULEID,String WEIXINID){
	    this.TestPlugin = TestPlugin;
	    this.UID = UID;
	    this.ENNAME = ENNAME;
	    this.PASSWORD = PASSWORD;
	    this.DPID = DPID;
	    this.CNNAME = CNNAME;
	    this.SEX = SEX;
	    this.PHONE = PHONE;
	    this.EMAIL = EMAIL;
	    this.FOXNUM = FOXNUM;
	    this.ADDRESS = ADDRESS;
	    this.POSTNUM = POSTNUM;
	    this.DESTEXT = DESTEXT;
	    this.RULEID = RULEID;
	    this.WEIXINID = WEIXINID;
	}
	
}
