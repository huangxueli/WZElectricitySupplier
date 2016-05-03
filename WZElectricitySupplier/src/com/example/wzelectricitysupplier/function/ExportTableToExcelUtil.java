package com.example.wzelectricitysupplier.function;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jsqlite.Database;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.ScriptStyle;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.example.wzelectricitysupplier.bean.BDSRecord;
import com.example.wzelectricitysupplier.bean.CircuitRecord;
import com.example.wzelectricitysupplier.bean.DXRecord;
import com.example.wzelectricitysupplier.bean.DefectRecord;
import com.example.wzelectricitysupplier.bean.DeviceRecord;
import com.example.wzelectricitysupplier.bean.GBRecord;
import com.example.wzelectricitysupplier.bean.GLRecord;
import com.example.wzelectricitysupplier.bean.GTRecord;
import com.example.wzelectricitysupplier.bean.GeometryRecord;
import com.example.wzelectricitysupplier.bean.HWRecord;
import com.example.wzelectricitysupplier.bean.KBRecord;
import com.example.wzelectricitysupplier.bean.LKRecord;
import com.example.wzelectricitysupplier.bean.NormalRecord;
import com.example.wzelectricitysupplier.bean.PDRecord;
import com.example.wzelectricitysupplier.bean.SwitchRecord;
import com.example.wzelectricitysupplier.bean.XBRecord;
import com.example.wzelectricitysupplier.database.BDSTable;
import com.example.wzelectricitysupplier.database.CircuitTable;
import com.example.wzelectricitysupplier.database.DBSetting;
import com.example.wzelectricitysupplier.database.DXTable;
import com.example.wzelectricitysupplier.database.DefectTable;
import com.example.wzelectricitysupplier.database.Field;
import com.example.wzelectricitysupplier.database.GBTable;
import com.example.wzelectricitysupplier.database.GLTable;
import com.example.wzelectricitysupplier.database.GTTable;
import com.example.wzelectricitysupplier.database.GeometryTable;
import com.example.wzelectricitysupplier.database.HWTable;
import com.example.wzelectricitysupplier.database.KBTable;
import com.example.wzelectricitysupplier.database.LKTable;
import com.example.wzelectricitysupplier.database.PDTable;
import com.example.wzelectricitysupplier.database.SwitchTable;
import com.example.wzelectricitysupplier.database.XBTable;
import com.example.wzelectricitysupplier.fragment.MainFragment;
import com.example.wzelectricitysupplier.setting.AppUtil;
import com.example.wzelectricitysupplier.setting.AppUtil.DeviceType;
import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;

public class ExportTableToExcelUtil {
	
	private Database mDb;
	private MainFragment mMainFragment;
    private int lastRow;
    private int lastCol;
    private int sheetIdx;
    private String sheetName;
    private int imgColIdx;
    private int defectColIdx;
    private int isEditColIdx;
    private WritableSheet defectSheet;
    private int defectDeviceIdColIdx;
    private int defectDeviceTypeColIdx;
    private static final HashMap<String, String> TABLES = new HashMap<>();
    private static final int BEGIN_ROW = 1; // 起始行
    private static final int BEGIN_COL = 1; // 起始列
    private WritableCellFormat titleStyle = new WritableCellFormat();
    private WritableCellFormat valueStyle = new WritableCellFormat();
    private WritableCellFormat normalValueStyle = new WritableCellFormat();
    private WritableCellFormat editedValueStyle = new WritableCellFormat();
    private WritableCellFormat newAddedValueStyle = new WritableCellFormat();
    private WritableCellFormat hyperLinkStyle = new WritableCellFormat();

    private List<String> filter = new ArrayList<>();
    private List filterIdx = new ArrayList();
    private boolean remainDefect = true;
    private boolean[] remainImg = new boolean[]{true, true};
    private boolean remainGeometry = true;
    private File exportDir;
    public void setFilter(List<String> filter) {
        this.filter = filter;
        if(filter.contains(DeviceRecord.ZD_DEFECT_ID.NAME)){
            remainDefect = false;
        }
        else remainDefect = true;
        if(filter.contains(DeviceRecord.ZD_PICTURE.NAME)){
            remainImg[1] = false;
        }
        else remainImg[1] = true;
        if(filter.contains(DefectRecord.ZD_PICTURE.NAME)){
            remainImg[0] = false;
        }
        else remainImg[0] = true;
        if(filter.contains(DBSetting.ZD_GEOMETRY)){
            remainGeometry = false;
        }
        else  remainGeometry = true;
    }


    public ExportTableToExcelUtil(Database mDb, MainFragment mMainFragment) {
    	this.mDb = mDb;
    	this.mMainFragment = mMainFragment;
        /**
         * 添加要导出的数据表
         * key:数据库表名
         * value:中文表名(用作sheet表名)
         */
        TABLES.put(BDSTable.mTableName,DeviceType.DeviceBDS);
        TABLES.put(GBTable.mTableName,DeviceType.DeviceGB);
        TABLES.put(GLTable.mTableName,DeviceType.DeviceGL);
        TABLES.put(GTTable.mTableName,DeviceType.DeviceGT);
        TABLES.put(HWTable.mTableName,DeviceType.DeviceHW);
        TABLES.put(KBTable.mTableName,DeviceType.DeviceKG);
        TABLES.put(LKTable.mTableName,DeviceType.DeviceLK);
        TABLES.put(PDTable.mTableName,DeviceType.DevicePD);
        TABLES.put(SwitchTable.mTableName,DeviceType.DeviceSwitch);
        TABLES.put(XBTable.mTableName,DeviceType.DeviceXB);
        TABLES.put(DXTable.mTableName,DeviceType.Line);
        /**
         * 设置表格样式
         */
        setDocStyle();
    }
    public ExportTableToExcelUtil(Database mDb, MainFragment mMainFragment, List<String> filter) {
    	this.mDb = mDb;
    	this.mMainFragment = mMainFragment;
        setFilter(filter);
        /**
         * 添加要导出的数据表
         * key:数据库表名
         * value:中文表名
         */
        TABLES.put(BDSTable.mTableName,DeviceType.DeviceBDS);
        TABLES.put(GBTable.mTableName,DeviceType.DeviceGB);
        TABLES.put(GLTable.mTableName,DeviceType.DeviceGL);
        TABLES.put(GTTable.mTableName,DeviceType.DeviceGT);
        TABLES.put(HWTable.mTableName,DeviceType.DeviceHW);
        TABLES.put(KBTable.mTableName,DeviceType.DeviceKG);
        TABLES.put(LKTable.mTableName,DeviceType.DeviceLK);
        TABLES.put(PDTable.mTableName,DeviceType.DevicePD);
        TABLES.put(SwitchTable.mTableName,DeviceType.DeviceSwitch);
        TABLES.put(XBTable.mTableName,DeviceType.DeviceXB);
        TABLES.put(DXTable.mTableName,DeviceType.Line);
        /**
         * 设置表格样式
         */
        setDocStyle();
    }

    /**
     * 工具方法，设置表格样式
     */
    private void setDocStyle(){
        try {
            WritableFont titleFont = new WritableFont(
                    WritableFont.createFont("微软雅黑"), 10,
                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    Colour.BLACK);
            titleStyle.setAlignment(Alignment.CENTRE);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleStyle.setBorder(Border.ALL, BorderLineStyle.THICK);
            titleStyle.setFont(titleFont);
            normalValueStyle.setAlignment(Alignment.CENTRE);
            normalValueStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
            normalValueStyle.setBorder(Border.ALL, BorderLineStyle.THIN);
            valueStyle = normalValueStyle;
            WritableFont editedFont = new WritableFont(
                    WritableFont.createFont("微软雅黑"), 10,
                    WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    Colour.RED);
            editedValueStyle.setAlignment(Alignment.CENTRE);
            editedValueStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
            editedValueStyle.setBorder(Border.ALL, BorderLineStyle.THIN);
            editedValueStyle.setFont(editedFont);
            WritableFont newAddedFont = new WritableFont(
            		WritableFont.createFont("微软雅黑"), 10,
            		WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
            		Colour.ORANGE);
            newAddedValueStyle.setAlignment(Alignment.CENTRE);
            newAddedValueStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
            newAddedValueStyle.setBorder(Border.ALL, BorderLineStyle.THIN);
            newAddedValueStyle.setFont(newAddedFont);
            WritableFont linkFont = new WritableFont(
                    WritableFont.createFont("微软雅黑"), 10,
                    WritableFont.NO_BOLD, false, UnderlineStyle.SINGLE,
                    Colour.BLUE2, ScriptStyle.NORMAL_SCRIPT);
            hyperLinkStyle.setFont(linkFont);
            hyperLinkStyle.setAlignment(Alignment.CENTRE);
            hyperLinkStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
            hyperLinkStyle.setBorder(Border.ALL, BorderLineStyle.THIN);
        }
        catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 入口函数，根据线路id导出相应的excel表格
     * @param id 线路id
     */
    public void exportExlById(int id){
        sheetIdx = 0;

        CircuitTable circuitTable = mMainFragment.getCircuitTable();
        CircuitRecord circuitRecord = circuitTable.SelectCircuitRecord(id);
        String fileName = circuitRecord.getValueList().get(0);
        String path = Util.getExportDirRootPath();
        exportDir = new File(path + fileName+ "/");
        if(!exportDir.exists()){
            exportDir.mkdirs();
        }
        fileName = exportDir.getPath() + "/" + fileName + ".xls";
        WritableWorkbook excel = null;
        try {
            excel = Workbook.createWorkbook(new File(fileName));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Object[] tables = TABLES.values().toArray();
        Object[] defect = {"缺陷"};
        writeExl(defect,excel,id);
        writeExl(tables,excel,id);
        addHyperLinkFromDefectToDevice(excel);
        try {
            excel.write();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据tables来写出excel表格
     * @param tables 需要导出的表名数组
     * @param excel 导出的excel文件
     * @param id 线路id
     */
    private void writeExl(Object[] tables, WritableWorkbook excel, int id){
        List records = new ArrayList();
        
        for(int i=0;i<tables.length; i++){
            sheetName = (String)tables[i];
            filterIdx.clear();
            WritableSheet sheet = excel.createSheet(sheetName, sheetIdx);
            boolean haveGeometry = false;
            /**
             * 添加数据表实例，每个sheet对应数据库中一张表
             */
            switch (sheetName){
                case DeviceType.DeviceBDS:
                	BDSTable bdsTable = new BDSTable(mDb);
                    records = bdsTable.selectSubstations(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new BDSRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
                case DeviceType.DeviceGB:
                	GBTable gbTable = new GBTable(mDb);
                    records = gbTable.selectBarTypeVariables(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new GBRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
                case DeviceType.DeviceGL:
                	GLTable glTable = new GLTable(mDb);
                    records = glTable.selectDisconnectores(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new GLRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                	break;
                case DeviceType.DeviceGT:
                	GTTable gtTable = new GTTable(mDb);
                    records = gtTable.selectTowers(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new GTRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
                case DeviceType.DeviceHW:
                	HWTable hwTable = new HWTable(mDb);
                    records = hwTable.selectRingMainUnits(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new HWRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
                case DeviceType.DeviceKG:
                	KBTable kgTable = new KBTable(mDb);
                    records = kgTable.selectSwitchingStations(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new KBRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
                case DeviceType.DeviceLK:
                	LKTable lkTable = new LKTable(mDb);
                    records = lkTable.selectLineConnectors(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new LKRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
                case DeviceType.DevicePD:
                	PDTable pdTable = new PDTable(mDb);
                    records = pdTable.selectSwitchingRooms(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new PDRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
                case DeviceType.DeviceSwitch:
                	SwitchTable switchTable = new SwitchTable(mDb);
                    records = switchTable.selectSwitchs(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new SwitchRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
                case DeviceType.DeviceXB:
                	XBTable xbTable = new XBTable(mDb);
                    records = xbTable.selectBoxChanges(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new XBRecord() , true, haveGeometry, true);
                    writeSheet(sheet, records, excel, haveGeometry);
                	break;
                case DeviceType.Line:
                    DXTable dxTable = new DXTable(mDb);
                    records = dxTable.selectElectricWires(DeviceRecord.ZD_CIRCUIT.NAME+"='"+id+"'");
                    haveGeometry = true;
                    writeRow(sheet, new DXRecord() , true, haveGeometry, false);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
                case "缺陷":
                    DefectTable defectTable = new DefectTable(mDb);
                    records = defectTable.SelectDefects("where 1=1");
                    writeRow(sheet, new DefectRecord() , true, haveGeometry, false);
                    writeSheet(sheet, records, excel, haveGeometry);
                    break;
            }
            
        }
    }

    /**
     * 根据单张数据库表格查询到的records集合来生成相应Sheet
     * @param records 数据库单个表对应的数据集合
     * @param excel 需要导出的excel文件
     * @param haveGeometry 该表是否包含几何类型
     */
    private void writeSheet(WritableSheet sheet, List<NormalRecord> records,WritableWorkbook excel, 
    		boolean haveGeometry){
        for(int i=0;i<records.size();i++){
            writeRow(sheet, records.get(i), false, haveGeometry, false);
        }
        if(sheetName.equals("缺陷")){
            defectSheet = sheet;
        }
        sheetIdx++;
    }

    /**
     * 根据单条Record记录来写入相应Sheet的一行数据
     * @param sheet 相应的Sheet
     * @param record 写入excel一行所需的数据
     * @param isTitle 该行是否是标题行
     * @param haveGeometry 该行是否含有几何类型
     */
    private void writeRow(WritableSheet sheet, NormalRecord record, boolean isTitle, boolean haveGeometry, boolean isPoint){
        if(isTitle){
            lastCol = BEGIN_COL;
            lastRow = BEGIN_ROW;
            Label idLabelCell = new Label(lastCol, lastRow, "编号", titleStyle);
            try {
                sheet.addCell(idLabelCell);
                lastCol++;
            } catch (WriteException e) {
                e.printStackTrace();
            }
            List<Field> fields = record.getFieldList();
            if(filter.size()!=0){
                fields = filterLabel(fields);
            }
            for (int i = 0; i < fields.size(); i++) {
                Label cell = new Label(lastCol, lastRow, fields.get(i).NAME, titleStyle);
                if(fields.get(i).NAME.indexOf("照片")>=0){
                    imgColIdx = i;
                }
                if(fields.get(i).NAME.equals(DeviceRecord.ZD_DEFECT_ID.NAME)){
                    defectColIdx = i;
                }
                if(fields.get(i).NAME.equals(DeviceRecord.ZD_ISEDIT.NAME)){
                	isEditColIdx = i;
                }
                if(fields.get(i).NAME.equals(DefectRecord.ZD_BELONG_ID.NAME) && sheetName.equals("缺陷")){
                    defectDeviceIdColIdx = i;
                }
                if(fields.get(i).NAME.equals(DefectRecord.ZD_BELONG_DEVICE.NAME) && sheetName.equals("缺陷")){
                    defectDeviceTypeColIdx = i;
                }
                try {
                    sheet.addCell(cell);
                    lastCol++;
                    if(sheetName.equals("缺陷") && i == fields.size()-1){
                    	sheet.addCell(new Label(lastCol, lastRow, "设备名称", titleStyle));
                        lastCol++;
                    }
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
            if(haveGeometry&&remainGeometry){
                writeGeoTitle(isPoint, sheet);
            }
            lastRow++;
        }else{
            List<String> values = record.getValueList();
            if(filterIdx.size()!=0){
                values = filterValue(values);
            }
            if(values.get(isEditColIdx).equals("是") ){
            	valueStyle = editedValueStyle;
            }else if(values.get(isEditColIdx).equals("新增")){
            	valueStyle = newAddedValueStyle;
            }else{
            	valueStyle = normalValueStyle;
            }
            String[] defectIds = values.get(defectColIdx).split("&");
            int defectCount = defectIds.length;
            if (defectCount == 0){
                defectCount = 1;
            }
            for(int j=0; j<defectCount; j++){
                lastCol = BEGIN_COL;
                Label idCell = new Label(lastCol, lastRow, record.getId()+"", valueStyle);
                try {
                    sheet.addCell(idCell);
                    lastCol++;
                } catch (WriteException e) {
                    e.printStackTrace();
                }
                for(int i=0; i<values.size(); i++){

                    if((i==imgColIdx && !values.get(i).equals("") && sheetName.equals("缺陷") && remainImg[0])||
                       (i==imgColIdx && !values.get(i).equals("") && !sheetName.equals("缺陷") && remainImg[1])){
                        String folderName;
                        if(sheetName.equals("缺陷")){
                        	DefectRecord defectrecord = (DefectRecord)record;
                        	DeviceRecord devicerecord = AppUtil.getDeviceRecordByDefectRecord(mDb, Integer.valueOf(defectrecord.mBelongId), defectrecord.mBelongDevice);
                            folderName = Constants.EXPROT_DIR_DEFECTNAME + "/" + defectrecord.mBelongDevice + "/" + devicerecord.mName + "/" + defectrecord.mName;
                        }
                        else {
                            folderName = Constants.EXPROT_DIR_NORMALNAME + "/" + sheetName + "/" + ((DeviceRecord)record).mName;
                        }
                        writeImgsValue(sheet, folderName, lastCol, lastRow);
                        lastCol++;
                        continue;
                    }
                    if(i == defectColIdx && !defectIds[j].equals("") && !sheetName.equals("缺陷") && remainDefect){
                        writeDefectHyperLink(sheet, defectIds[j], lastCol, lastRow);
                        lastCol++;
                        continue;
                    }
                    Label cell = new Label(lastCol, lastRow, values.get(i), valueStyle);
                    try {
                        sheet.addCell(cell);
                        lastCol++;
                        if(sheetName.equals("缺陷") && i==values.size()-1){
                        	DefectRecord defectrecord = (DefectRecord)record;
                        	DeviceRecord devicerecord = AppUtil.getDeviceRecordByDefectRecord(mDb, Integer.valueOf(defectrecord.mBelongId), defectrecord.mBelongDevice);
                        	Label c = new Label(lastCol, lastRow, devicerecord.mName, valueStyle);
                        	sheet.addCell(c);
                        	lastCol++;
                        }
                    }
                    catch (WriteException e) {
                        e.printStackTrace();
                    }
                }
                if(haveGeometry&&remainGeometry){
                    writeGeoField(record, sheet);
                }
                lastRow++;
            }
            try {
                sheet.mergeCells(BEGIN_COL,lastRow-defectCount,BEGIN_COL,lastRow-1);
                for(int i=0; i<values.size(); i++){
                    if(i != defectColIdx){
                        sheet.mergeCells(i+1+BEGIN_COL,lastRow-defectCount,i+1+BEGIN_COL,lastRow-1);
                    }
                }
                if(haveGeometry&&remainGeometry){
                    sheet.mergeCells(values.size()+1+BEGIN_COL,lastRow-defectCount,values.size()+1+BEGIN_COL,lastRow-1);
                    sheet.mergeCells(values.size()+2+BEGIN_COL,lastRow-defectCount,values.size()+2+BEGIN_COL,lastRow-1);
                }
            } catch (WriteException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * 写设备Sheet中的缺陷超链接单元格
     * @param sheet 相应的设备Sheet
     * @param defectId 对应的缺陷编号
     * @param col 该单元格所在的列
     * @param row 该单元格所在的行
     */
    private void writeDefectHyperLink(WritableSheet sheet, String defectId, int col, int row){
        if(defectSheet != null){
            DefectTable defectTable = new DefectTable(mDb);
            List records = defectTable.SelectDefects("where 1=1");
            for(int i=1; i<=records.size(); i++){
                if(defectId.equals(defectSheet.getWritableCell(BEGIN_COL,i+BEGIN_ROW).getContents())){
                    WritableHyperlink hyperLink = new WritableHyperlink(col, row, col, row, defectId, defectSheet, BEGIN_COL, i+BEGIN_ROW, getSheetWidth(defectSheet)+BEGIN_COL, i+BEGIN_ROW);
                    try {
                        sheet.addHyperlink(hyperLink);
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                    WritableCell cell = sheet.getWritableCell(col, row);
                    cell.setCellFormat(hyperLinkStyle);
                    break;
                }
            }
        }
    }

    /**
     * 写入Sheet表中照片超链接单元格
     * @param sheet 相应的Sheet
     * @param dirName 照片文件夹路径
     * @param col 该单元格所在的列
     * @param row 该单元格所在的行
     */
    private void writeImgsValue(WritableSheet sheet, String dirName, int col, int row){
        String formu = "HYPERLINK(\"" + dirName + "\",\"" + "查看图片" + "\")";
        if(formu.contains("#")){
        	formu = formu.replaceAll("#", "%23");
        	String[] arr = formu.split("\"", 2);
        	formu = arr[0] + "\"file:///" + arr[1];
        }
        Formula formula = new Formula(col, row, formu, hyperLinkStyle);
        try {
            sheet.addCell(formula);
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 有几何类型时调用，写入几何类型描述作为标题
     * 如点类型，则写入经度，纬度
     * @param record 带几何类型的行数据
     * @param sheet 所在的Sheet
     */
    private void writeGeoTitle(boolean isPoint, WritableSheet sheet){
        if(isPoint){
            Label cellX = new Label(lastCol,lastRow,"经度", titleStyle);
            lastCol++;
            Label cellY = new Label(lastCol,lastRow,"纬度", titleStyle);
            lastCol++;
            try {
                sheet.addCell(cellX);
                sheet.addCell(cellY);
            }catch (WriteException e) {
                e.printStackTrace();
            }

        }
    }
    private void writeGeoTitle(GeometryTable table, WritableSheet sheet){
    	String type = table.getGeometryType();
    	if("POINT".equals(type)){
    		Label cellX = new Label(lastCol,lastRow,"经度", titleStyle);
    		lastCol++;
    		Label cellY = new Label(lastCol,lastRow,"纬度", titleStyle);
    		lastCol++;
    		try {
    			sheet.addCell(cellX);
    			sheet.addCell(cellY);
    		}catch (WriteException e) {
    			e.printStackTrace();
    		}
    		
    	}
    }

    /**
     * 有几何类型调用，写入几何类型具体描述的数值
     * 如点类型，则写入经度数值与纬度数值
     * @param record 带几何类型的行数据
     * @param sheet 所在的Sheet表
     */
    private void writeGeoField(NormalRecord record, WritableSheet sheet){
        GeometryRecord geometryRecord = (GeometryRecord)record;
        Geometry geometry = geometryRecord.getGeometry();
        if(geometry instanceof Point){
            Point point = (Point)geometry;
            Label cellX = new Label(lastCol,lastRow,point.getX()+"",valueStyle);
            lastCol++;
            Label cellY = new Label(lastCol,lastRow,point.getY()+"",valueStyle);
            lastCol++;
            try {
                sheet.addCell(cellX);

                sheet.addCell(cellY);

            }
            catch (WriteException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 添加缺陷表“所属编号”字段到设备表的超链接
     * @param excel 包含所有Sheet的整个excel表对象，即导出的excel表
     */
    private void addHyperLinkFromDefectToDevice(WritableWorkbook excel){
        DefectTable defectTable = new DefectTable(mDb);
        int defectNum = defectTable.SelectDefects("where 1=1").size();
        for(int i=1; i<=defectNum; i++){
            String id = defectSheet.getWritableCell(defectDeviceIdColIdx+1+BEGIN_COL,i+BEGIN_ROW).getContents();
            String type = defectSheet.getWritableCell(defectDeviceTypeColIdx+1+BEGIN_COL,i+BEGIN_ROW).getContents();
            WritableSheet deviceSheet = excel.getSheet(type);
            int width = getSheetWidth(deviceSheet);
            int row = 1+BEGIN_ROW;
            do{
                if(id.equals(deviceSheet.getWritableCell(BEGIN_COL,row).getContents())){

                    WritableHyperlink hyperLink = new WritableHyperlink(defectDeviceIdColIdx+1+BEGIN_COL, i+BEGIN_ROW, defectDeviceIdColIdx+1+BEGIN_COL, i+BEGIN_ROW, id, deviceSheet, BEGIN_COL, row, width+BEGIN_COL, row);
                    try {
                        defectSheet.addHyperlink(hyperLink);
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                    WritableCell cell = defectSheet.getWritableCell(defectDeviceIdColIdx+1+BEGIN_COL, i+BEGIN_ROW);
                    cell.setCellFormat(hyperLinkStyle);
                    break;
                }
                row ++;
            } while (!deviceSheet.getWritableCell(BEGIN_COL,row).getContents().equals(""));
        }
    }

    /**
     * 工具方法，获取相应Sheet的宽度
     * @param sheet 相应的Sheet
     * @return
     */
    private int getSheetWidth(WritableSheet sheet){
        int width = 0;
        while (!sheet.getWritableCell(width+BEGIN_COL,BEGIN_ROW).getContents().equals("")){
            width++;
        }
        return width-1;
    }

    /**
     * 根据过滤器过滤表的列名
     * @param fields 要素集合
     * @return
     */
    private List<Field> filterLabel(List<Field> fields){
        List<Field> returnFields = new ArrayList<>();
        for(int i=0; i<fields.size(); i++){
            if (filter.contains(fields.get(i).NAME)){
                filterIdx.add(i);
            }
            else {
                returnFields.add(fields.get(i));
            }
        }
        return returnFields;
    }

    /**
     * 过滤表相应的列内容，在过滤掉表列名之后调用
     * @param values 列内容集合
     * @return
     */
    private List<String> filterValue(List<String> values){
        List<String> returnValues = new ArrayList<>();
        for(int i=0; i<values.size(); i++){
            if(!filterIdx.contains(i)){
                returnValues.add(values.get(i));
            }
        }
        return returnValues;
    }


}