package com.example.wzelectricitysupplier.database;

public class DBSetting {
	
	/**
	 *  系统设置数据库名称
	 */
	public static final String DB_NAME_POIDB = "poi.sqlite";
	
	/**
	 *  地理信息数据库名称
	 */
	public static final String DB_NAME_DLJ = "dlj_database.sqlite";
	/**
	 *  数据库所属文件夹名称
	 */
	public static final String DIR_DATABASE = "数据库文件";
	/**
	 *  是否打印数据库语句
	 */
	public static final boolean DEBUG = true;
	/**
	 *  主键字段名
	 */
	public static final String ZD_PRIMERY = "PK_UID";
	/**
	 *  地理信息字段
	 */
	public static final String ZD_GEOMETRY = "geometry";
	/**
	 *  地图坐标系
	 */
	public static final int SRID = 4326;
	
}
