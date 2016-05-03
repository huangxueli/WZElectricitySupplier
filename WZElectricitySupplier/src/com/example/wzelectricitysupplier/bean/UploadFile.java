package com.example.wzelectricitysupplier.bean;

public  class UploadFile {
	
	public String fileName;
	public String filePath;
	public FileType fileType;
	
	enum FileType {
		FILE , DIRECTORY;
	}
	
	public UploadFile(String filePath, String fileName, boolean isDirectory) {
		this.filePath = filePath;
		this.fileName = fileName;
		fileType = isDirectory ? FileType.DIRECTORY : FileType.FILE;
	}

	public boolean isDirectory(){
		if(fileType == FileType.DIRECTORY){
			return true ;
		}else{
			return false ;
		}
	}
	
	@Override
	public String toString() {
		return "FileInfo [fileType=" + fileType + ", fileName=" + fileName + ", filePath=" + filePath + "]";
	}
}