package com.github.mtdp.util.excel.jxls;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 用jxls导出excel比poi简单
 * 缺点要配置导出模版
* @ClassName: JxlsUtils 
* @Description: TODO 
* @author gqwang
* @date 2015年4月28日 上午9:37:25 
*
 */
public class JxlsUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JxlsUtils.class);
	
	
	/**
	 * 
	 * @param exportFilePath 导出文件的路径
	 * @param exportTemplateFilePath 导出文件的模版
	 * @param data 导出数据的map
	 * @return
	 * @throws CommonException 
	 * 如果导出的数据Object不能是内部类
	 */
	public static File buildExcelExport(String exportFilePath,String exportTemplateFilePath,Map<String,Object> data) throws RuntimeException{
		XLSTransformer transformer = new XLSTransformer();
		try {
			transformer.transformXLS(exportTemplateFilePath, data, exportFilePath);
			return new File(exportFilePath);
		} catch (ParsePropertyException e) {
			logger.error("解析数据出错",e);
			throw new RuntimeException("解析数据出错", e);
		} catch (InvalidFormatException e) {
			logger.error("校验数据出错",e);
			throw new RuntimeException("校验数据出错", e);
		} catch (IOException e) {
			logger.error("IO异常",e);
			throw new RuntimeException("解析数据出错", e);
		}
	}
	
	/**
	 * excel导出数据返回流
	 * @param in 导出的excel模版
	 * @param data 导出的数据
	 * @return
	 */
	public static void buildExcelExport(InputStream in,OutputStream os,Map<String,Object> data){
		XLSTransformer transformer = new XLSTransformer();
		try {
			Workbook wb = transformer.transformXLS(in, data);
			wb.write(os);
		} catch (ParsePropertyException | InvalidFormatException | IOException e) {
			logger.error("导出excel出错",e);
		}
	}

}
