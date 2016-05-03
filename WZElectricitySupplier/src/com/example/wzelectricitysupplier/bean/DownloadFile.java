package com.example.wzelectricitysupplier.bean;

import it.sauronsoftware.ftp4j.FTPFile;


public class DownloadFile{
	public FTPFile file;
	public String parentDirPath; 
	
	public DownloadFile(String parentDirPath, FTPFile file){
		this.parentDirPath = parentDirPath;
		this.file = file;
	}
}
