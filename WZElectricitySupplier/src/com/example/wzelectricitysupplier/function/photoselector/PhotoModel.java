package com.example.wzelectricitysupplier.function.photoselector;

import java.io.Serializable;

public class PhotoModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String originalPath;
	private boolean isChecked;

	public PhotoModel(String originalPath, boolean isChecked) {
		super();
		this.originalPath = originalPath;
		this.isChecked = isChecked;
	}

	public PhotoModel(String name, String originalPath) {
		this.name = name;
		this.originalPath = originalPath;
	}

	public PhotoModel() {
	}

	public String getName() {
		return name;
	}
	
	public String getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
