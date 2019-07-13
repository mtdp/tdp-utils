package  com.github.mtdp.util.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.ReflectionUtils;

/**
 * 
* @ClassName: ExportExcel 
* @Description: 数据导出excel工具类
* 				 配合 ExcelField注解使用 
* @author gqwang
* @date 2015年11月13日 下午2:55:52 
* 
* @param <T>
 */
public class ExportExcel<T> {

	Map<String, ExcelField> fieldMap = new HashMap<String, ExcelField>();
	Map<String, Integer> sortFieldMap = new HashMap<String, Integer>();

	private HSSFWorkbook hw = new HSSFWorkbook();
	private String sheetName = "default";
	private HSSFSheet sheet;
	private boolean merge = false;
	private boolean showHead = true;
	private Collection<T> data;

	private int rowi = 0;

	public ExportExcel(Collection<T> data) {
		this.data = data;
		sheet = hw.createSheet(sheetName);
	}

	private void export0() {
		if (null == data || data.isEmpty()) {
			return;
		}
		buildHead();
		buildData(null, data);
		if (merge) {
			int startRow = 0;
			if (showHead) {
				startRow++;
			}
			mergData(startRow, data);
		}
	}
	
	/**
	 * 创建表头
	 */
	private void buildHead() {
		Iterator<?> it = data.iterator();
		Object o = it.next();
		getHeader(o.getClass());
		if (showHead) {
			HashMap<String, Object> headMap = new HashMap<String, Object>();
			for (String key : fieldMap.keySet()) {
				ExcelField excelField = fieldMap.get(key);
				headMap.put(key, excelField.filedName());
			}
			createRow(headMap);
		}
	}

	private void buildData(Map<String, Object> parentVal,
			Collection<? extends Object> sonVal) {
		for (Object o : sonVal) {
			Map<String, Object> fieldValues = getFieldValues(o);
			Collection<Object> collectionValues = getCollectionValues(o);
			if (null != fieldValues && parentVal != null) {
				fieldValues.putAll(parentVal);
			}
			if (null == collectionValues) {
				createRow(fieldValues);
			} else {
				buildData(fieldValues, collectionValues);
			}
		}
	}

	private void mergData(int startRow, Collection<? extends Object> sonVal) {
		if (null == sonVal) {
			return;
		}
		for (Object o : sonVal) {
			int mergerow = getMergerow(o);
			if (mergerow > 1) {
				Map<String, Object> fieldValues = getFieldValues(o);
				for (String key : fieldValues.keySet()) {
					int col = sortFieldMap.get(key);
					CellRangeAddress region = new CellRangeAddress(startRow,
							startRow + mergerow - 1, col, col);
					sheet.addMergedRegion(region);
				}

			}
			Collection<Object> collectionValues = getCollectionValues(o);
			mergData(startRow, collectionValues);
			startRow = startRow + mergerow;
		}
	}

	private void createRow(Map<String, Object> rowVal) {
		HSSFRow dataRow = sheet.createRow(rowi++);
		for (String key : rowVal.keySet()) {
			HSSFCell cell = dataRow.createCell(sortFieldMap.get(key));
			Object value = rowVal.get(key);			
			String valueStr = getValue(key,value);
			cell.setCellValue(valueStr);
		}
	}

	private String getValue(String key, Object value) {
		if (value == null) {
			return "";
		}
		ExcelField excelField = fieldMap.get(key);
		if (FormatType.DATE.equals(excelField.formatType())
				&& value instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat(excelField.pattern());
			return sdf.format(value);
		} else if (FormatType.NUM.equals(excelField.formatType())) {
			return value.toString();
		} else {
			return value.toString();
		}
	}

	/**
	 * 设置表头
	 * @param clzz
	 */
	private void getHeader(Class<?> clzz) {
		Field[] fields = getFileds(clzz);
		for (Field field : fields) {
			ExcelField excelField = field.getAnnotation(ExcelField.class);
			if (excelField != null) {
				if (Type.FIELD.equals(excelField.type())) {
					String key = getExcelFieldKey(excelField);
					fieldMap.put(key, excelField);
				} else if (Type.COLLECTION.equals(excelField.type())) {
					getHeader((Class<?>) ((ParameterizedType) field
							.getGenericType()).getActualTypeArguments()[0]);
				} else if (Type.OBJECT.equals(excelField.type())) {
					getHeader(field.getType());
				}
			}
		}
		sortHeader();
	}

	/**
	 * 表头排序
	 */
	private void sortHeader() {
		List<ExcelField> excelFields = new ArrayList<ExcelField>();
		excelFields.addAll(fieldMap.values());
		for (int i = 0; i < excelFields.size(); i++) {
			for (int j = 0; j < excelFields.size() - 1; j++) {
				ExcelField excelField1 = excelFields.get(j);
				ExcelField excelField2 = excelFields.get(j + 1);
				if (excelField1.sort() > excelField2.sort()) {
					excelFields.set(j, excelField2);
					excelFields.set(j + 1, excelField1);
				}

			}
		}
		for (int i = 0; i < excelFields.size(); i++) {
			String key = getExcelFieldKey(excelFields.get(i));
			sortFieldMap.put(key, i);
		}
	}

	private Map<String, Object> getFieldValues(Object obj) {
		if (null == obj) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		Field[] fields = getFileds(obj.getClass());
		for (Field field : fields) {
			ExcelField excelField = field.getAnnotation(ExcelField.class);
			if (excelField != null) {
				String excelFiledKey = getExcelFieldKey(excelField);
				ReflectionUtils.makeAccessible(field);
				Object value = ReflectionUtils.getField(field, obj);
				if (Type.FIELD.equals(excelField.type())) {
					result.put(excelFiledKey, value);
				} else if (Type.OBJECT.equals(excelField.type())) {
					Map<String, Object> sunMap = getFieldValues(value);
					if (null != sunMap) {
						result.putAll(sunMap);
					}
				}
			}
		}
		return result;
	}

	private Field[] getFileds(Class<?> clzz){
		Field[] fields = clzz.getDeclaredFields();
		Class<?> parentClzz = clzz.getSuperclass();
		if(null != parentClzz){
			Field[] parentFields = getFileds(parentClzz);
			fields = (Field[]) ArrayUtils.addAll(fields, parentFields);
		}
		return fields;
	}
	
	@SuppressWarnings("unchecked")
	private Collection<Object> getCollectionValues(Object obj) {
		if (null == obj) {
			return null;
		}
		Collection<Object> collectionVal = null;
		Collection<Object> objectCollectionVal = null;
		Field[] fields = getFileds(obj.getClass());
		for (Field field : fields) {
			ExcelField excelField = field.getAnnotation(ExcelField.class);
			if (excelField != null) {
				ReflectionUtils.makeAccessible(field);
				Object value = ReflectionUtils.getField(field, obj);
				if (Type.COLLECTION.equals(excelField.type())) {
					collectionVal = (Collection<Object>) value;
				} else if (Type.OBJECT.equals(excelField.type())) {
					Collection<Object> tmp = getCollectionValues(value);
					if (tmp != null) {
						objectCollectionVal = tmp;
					}
				}
			}
		}
		if (collectionVal != null) {
			return collectionVal;
		} else {
			return objectCollectionVal;
		}
	}

	private int getMergerow(Object obj) {
		Collection<Object> collectionValues = getCollectionValues(obj);
		int result = 1;
		if (collectionValues == null || collectionValues.isEmpty()) {
			return result;
		} else {
			result = collectionValues.size();
			for (Object son : collectionValues) {
				int mergerow = getMergerow(son);
				if (mergerow > 1) {
					result = result + mergerow - 1;
				}
			}
		}
		return result;
	}

	private String getExcelFieldKey(ExcelField ExcelField) {
		return ExcelField.filedName() + ExcelField.sort() + ExcelField.isKey()
				+ ExcelField.type();
	}

	public void export(OutputStream os) throws IOException {
		export0();
		hw.write(os);
	}

	public void export(String file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		export(out);
		out.close();
	}

	public HSSFWorkbook getHw() {
		return hw;
	}

	public void setHw(HSSFWorkbook hw) {
		this.hw = hw;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public boolean isMerge() {
		return merge;
	}

	public void setMerge(boolean merge) {
		this.merge = merge;
	}

	public boolean isShowHead() {
		return showHead;
	}

	public void setShowHead(boolean showHead) {
		this.showHead = showHead;
	}

	public Collection<T> getData() {
		return data;
	}

	public void setData(Collection<T> data) {
		this.data = data;
	}

	public int getRowi() {
		return rowi;
	}

	public void setRowi(int rowi) {
		this.rowi = rowi;
	}
}
