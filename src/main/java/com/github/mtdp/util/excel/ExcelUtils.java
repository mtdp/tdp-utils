package com.github.mtdp.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @Description 读取excel工具类
 * @author wangguoqing
 * @date 2016年7月8日下午5:01:46
 *
 */
public class ExcelUtils {
	
	private String sheetName;
	
	private  Workbook create(InputStream in) throws  IOException,InvalidFormatException {  
	    if (!in.markSupported()) {  
	        in = new PushbackInputStream(in, 8);  
	    }  
	    if (POIFSFileSystem.hasPOIFSHeader(in)) {  
	        return new HSSFWorkbook(in);  
	    }  
	    if (POIXMLDocument.hasOOXMLHeader(in)) {  
	        return new XSSFWorkbook(OPCPackage.open(in));  
	    }  
	    throw new IllegalArgumentException("你的excel版本目前poi解析不了"); 
	}
	
	/**
	 * 读取excel
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public List<String[]> read(InputStream inputStream) throws Exception{
		List<String[]> result=new ArrayList<String[]>();
		try {
			Workbook wb = create(inputStream);
			for(int sheetCount=0;sheetCount<wb.getNumberOfSheets();sheetCount++){
				Sheet sheet = wb.getSheetAt(sheetCount);
				if(sheetName == null){
					throw new Exception("Excel表单名为空!");
				}
				if(sheet.getSheetName() != null && !"".equals(sheet.getSheetName().trim())){
					//读取指定的sheet
					if(sheet.getSheetName().startsWith(sheetName)){
						//sheet.getPhysicalNumberOfRows() 不能读取所有的行  中间断了 
						//int rows = sheet.getPhysicalNumberOfRows();
						int rows = sheet.getLastRowNum();
						if(rows <= 1){
							throw new Exception("Excel表单中没数据!");
						}
						//格式话数据准备
						DecimalFormat df = new DecimalFormat("#");//科学计数发
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); //日期处理
						for (int r = 0; r <= rows; r++) {
							Row row = sheet.getRow(r);
							if (row == null) {
								 continue;
							}
							int cells = row.getLastCellNum();
							String[] excelCols = new String[cells];
							for (int c = 0; c < cells; c++) {
								Cell cell = row.getCell(c);
								if(cell==null){
									continue;
								}
								Object value=null;
								int type= cell.getCellType();
								switch (type) {
									case Cell.CELL_TYPE_NUMERIC:
										//如果为日期
										if(HSSFDateUtil.isCellDateFormatted(cell)){
											value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
										}else{
											value = df.format(cell.getNumericCellValue());
										}
										break;
									case Cell.CELL_TYPE_STRING:
										value=cell.getStringCellValue();
										break;
									case Cell.CELL_TYPE_BOOLEAN:
										value=cell.getBooleanCellValue();
										break;
									default:
										//TODO 未知类型提醒
										continue;
									}
								if(value==null){
									continue;
								}
								excelCols[c] = value.toString();
							}
							result.add(excelCols);
						}
					}
				}
			}
			return result;
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}catch (InvalidFormatException e){
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 读取有表头的excel
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public List<String[]> read4Header(InputStream inputStream) throws Exception{
		List<String[]> result=new ArrayList<String[]>();
		try {
			Workbook wb = create(inputStream);
			for(int sheetCount=0;sheetCount<wb.getNumberOfSheets();sheetCount++){
				Sheet sheet = wb.getSheetAt(sheetCount);
				if(sheetName == null){
					throw new Exception("Excel表单名为空!");
				}
				if(sheet.getSheetName() != null && !"".equals(sheet.getSheetName().trim())){
					//读取指定的sheet
					if(sheet.getSheetName().startsWith(sheetName)){
						//sheet.getPhysicalNumberOfRows() 不能读取所有的行  中间断了 
						//int rows = sheet.getPhysicalNumberOfRows();
						int rows = sheet.getLastRowNum();
						if(rows <= 1){
							throw new Exception("Excel表单中没数据!");
						}
						Row tabHeaderInExcel = sheet.getRow(0);
						if(tabHeaderInExcel == null){
							throw new Exception("Excel表单中没有找到表头!");
						}
						//int cells = tabHeaderInExcel.getPhysicalNumberOfCells();
						//格式话数据准备
						DecimalFormat df = new DecimalFormat("#");//科学计数发
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); //日期处理
						for (int r = 1; r <= rows; r++) {
							Row row = sheet.getRow(r);
							if (row == null) {
								continue;
							}
							int cells = row.getLastCellNum();
							String[] excelCols = new String[cells];
							for (int c = 0; c < cells; c++) {
								Cell cell = row.getCell(c);
								if(cell == null){
									continue;
								}	
								Object value=null;
								int type= cell.getCellType();
								switch (type) {
								case Cell.CELL_TYPE_NUMERIC:
									//如果为日期
									if(HSSFDateUtil.isCellDateFormatted(cell)){
										value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
									}else{
										value = df.format(cell.getNumericCellValue());
									}
									break;
								case Cell.CELL_TYPE_STRING:
									value=cell.getStringCellValue();
									break;
								case Cell.CELL_TYPE_BOOLEAN:
									value=cell.getBooleanCellValue();
									break;
								default:
									//TODO 未知类型提醒
									continue;
								}
								if(value==null){
									continue;
								}
								excelCols[c] = value.toString();
							}
							result.add(excelCols);
						}
					}
				}
			}
			return result;
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}catch (InvalidFormatException e){
			throw new Exception(e.getMessage());
		}
		
	}


	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		ExcelUtils excel = new ExcelUtils();
		excel.setSheetName("Sheet2");
//		List<String[]> list = excel.read(new FileInputStream(new File("C:/Users/gqwang/Desktop/1/report0414.xls")));
		List<String[]> list = excel.read(new FileInputStream(new File("C:/Users/wangguoqing/Downloads/2088221830311890-20160815-005448820-帐务组合查询.xls")));
		for(String[] strArr : list){
			System.out.println(Arrays.toString(strArr));
		}
		
		
	}
	
}
