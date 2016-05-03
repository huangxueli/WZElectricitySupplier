package com.example.wzelectricitysupplier.setting;

public class Constants {
	
	public static boolean Debug = true;
	public static boolean AppInitializeDialogShown = false;
	
	// 登录信息
	public static String UserName = "姓名";
	public static String UserID = "";
	public static String Account = "";
	public static String Password = "";
	
	public static boolean NewUntiRight = false;
	public static boolean NewProjectRight = false;
	public static boolean CompleteProjectRight = false;
	
	// HTTP访问地址
	public static final String URL_HTTP = "http://192.168.1.5/oa/"; // http://218.75.26.57:6131/oa/   http://192.168.1.5/oa/
	// FTP地址、端口、账号、密码
	public static String FTPHost = "192.168.1.5"; // 218.75.26.57 	192.168.1.5
	public static int FTPPort = 2121;
	public static String FTPUser = "anonymous";
	public static String FTPPassword = "123456";
	// 本地图片存储路径
	public static final String PICTURE_DIR_NAME = "照片信息";
	// 导出根目录
	public static final String EXPROT_DIR_NAME = "导出文件";
	public static final String IMPROT_DIR_NAME = "导入文件";
	public static final String EXPROT_DIR_DEFECTNAME = "缺陷照片";
	public static final String EXPROT_DIR_NORMALNAME = "普通照片";
	// 离线地图
	public static final String OFFLINE_MAP_ROOT_DIR = "离线地图";// 根目录
	public static final String MAP_LOCAL_DIR1 = "矢量";
	public static final String MAP_LOCAL_DIR2 = "2.5D";
	public static final String MAP_LOCAL_DIR3 = "影像";
	public static String MAP_LOCAL_FILE1 = "矢量.tpk";
	public static String MAP_LOCAL_FILE2 = "2.5维.tpk";
	public static String MAP_LOCAL_FILE3 = "影像.tpk";
	// geodatabase
	public static final String GEODATABASE_PATH1 = "离线地图/geodatabase/泰顺地名.geodatabase";
	public static final String GEODATABASE_PATH2 = "离线地图/geodatabase/泰顺标注.geodatabase";
	// 外网地图路径
//	public static final String MAP_SERVICE_PATH1 = "http://218.75.26.56:6080/arcgis/rest/services/pyditu/MapServer";
	public static final String MAP_SERVICE_PATH1 = "http://www.go577.com/iserver/services/wzmap/wmts";
//	public static final String MAP_SERVICE_PATH1 = "http://218.75.26.56:6080/arcgis/rest/services/kfq_cgcs2000/shiliang_KFQ/MapServer"; 
	public static final String MAP_SERVICE_PATH2 = "http://218.75.26.56:6080/arcgis/rest/services/kfq_cgcs2000/erwuD_KFQ/MapServer";
	public static final String MAP_SERVICE_PATH3 = "http://218.75.26.56:6080/arcgis/rest/services/kfq_cgcs2000/yingxiang_KFQ/MapServer";
	
	public static final int MESSAGE_WHAT_BASE = 1;
	
	public static final int MESSAGE_WHAT_FTPCMD_CONNECT_S = 101;
	public static final int MESSAGE_WHAT_FTPCMD_CONNECT_F = 102;
	public static final int MESSAGE_WHAT_CREATE_NEWCASE_S = 103;
	public static final int MESSAGE_WHAT_CREATE_NEWCASE_F = 104;
	public static final int MESSAGE_WHAT_GET_CASETYPEID_S = 105;
	public static final int MESSAGE_WHAT_GET_CASETYPEID_F = 106;
	public static final int MESSAGE_WHAT_LOGIN_S = 107;
	public static final int MESSAGE_WHAT_LOGIN_F = 108;
	public static final int MESSAGE_WHAT_QUERY_CASE_S = 109;
	public static final int MESSAGE_WHAT_QUERY_CASE_F = 110;
	public static final int MESSAGE_WHAT_DELETE_CASE_S = 111;
	public static final int MESSAGE_WHAT_DELETE_CASE_F = 112;
	public static final int MESSAGE_WHAT_GET_PROCESS_S = 113;
	public static final int MESSAGE_WHAT_GET_PROCESS_F = 114;
	public static final int MESSAGE_WHAT_GET_NEWPRO_S = 115;
	public static final int MESSAGE_WHAT_GET_NEWPRO_F = 116;
	public static final int MESSAGE_WHAT_UPDATE_NEWCASE_S = 117;
	public static final int MESSAGE_WHAT_UPDATE_NEWCASE_F = 118;
	public static final int MESSAGE_WHAT_SAVE_NEWCASE_S = 119;
	public static final int MESSAGE_WHAT_SAVE_NEWCASE_F = 120;
	public static final int MESSAGE_WHAT_UPLOAD_REMARK_S = 121;
	public static final int MESSAGE_WHAT_UPLOAD_REMARK_F = 122;
	
	public static final int MESSAGE_WHAT_UPDATE_PROGRESS = 201;
	
}
