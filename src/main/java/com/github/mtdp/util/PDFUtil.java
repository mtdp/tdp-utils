package com.github.mtdp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

/**
 * 
 *
 * @Description pdf生成工具类<br/>
 * 注意:<br/>
 * a.不能有freemark标签标记 如:<#list ls  as l > </#list>
 * 后续需要添加的内容:<br/>
 * a.添加pdf加密,签名功能<br/>
 * b.pdf可以编辑并保存功能<br/>
 * 参考资料：http://blog.csdn.net/shanliangliuxing/article/details/6833471 <br/>
 * http://hao.jobbole.com/category/java/java-pdf/
 * @author wangguoqing
 * @date 2016年7月18日下午5:45:41
 *
 */
public class PDFUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PDFUtil.class);
	
	
	/**
	 * 根据html内容、字体文件生成pdf
	 * @param htmlPath 需要生成pdf文件的html路径
	 * @param pdfPath 生成的pdf路径
	 * @param fontPath html页面所有文字使用的字体路径
	 */
	public static void createPDF(String htmlPath,String pdfPath,String fontPath){
		ITextRenderer renderer = new ITextRenderer();
		OutputStream os = null;
		try {
			renderer.setDocument(new File(htmlPath).toURI().toURL().toString());
			//解决中文支持问题 页面上使用的字体需要与fontPath字体一直
			ITextFontResolver fontResolver = renderer.getFontResolver();
			//字体路径
			fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);     
			renderer.layout();
			os = new FileOutputStream(pdfPath);
			renderer.createPDF(os);
		} catch (MalformedURLException e) {
			logger.error("pdf设置document出错",e);
			throw new RuntimeException("pdf设置document出错");
		} catch (DocumentException e) {
			logger.error("pdf设置支持中文出错",e);
			throw new RuntimeException("pdf设置支持中文出错");
		} catch (IOException e) {
			logger.error("pdf设置支持中文出错",e);
			throw new RuntimeException("pdf设置支持中文出错");
		}finally{
			try {
				os.close();
			} catch (IOException e) {
			}
		}
		// 解决图片的相对路径问题     
        // renderer.getSharedContext().setBaseURL("file:/D:/Work/Demo2do/Yoda/branch/Yoda%20-%20All/conf/template/");  
	}
	
	
	/**
	 * 根据html内容生成pdf,html 所有字体默认使用font-family:'Arial Unicode MS'
	 * @param htmlPath 需要生成pdf文件的html路径
	 * @param pdfPath 生成的pdf路径
	 */
	public static void createPDF(String htmlPath,String pdfPath){
		//字体路径
		String fontPath = PDFUtil.class.getResource("/").getPath()+"/fonts/ARIALUNI.TTF";
		createPDF(htmlPath, pdfPath, fontPath);
	}
	
	/**
	 *  根据html内容生成pdf文件并返回文件的字节数组
	 * @param htmlContent  生成pdf文件的html字符串
	 * @param fontPath html页面所有文字使用的字体
	 * @return
	 */
	public static byte[] generatePDF(String htmlContent,String fontPath){ 
		byte[] byteArr = null;
		ITextRenderer renderer = new ITextRenderer();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		renderer.setDocumentFromString(htmlContent);
		//解决中文支持问题 页面上使用的字体需要与fontPath字体一直
		ITextFontResolver fontResolver = renderer.getFontResolver();
		try {
			fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);     
			renderer.layout();
			renderer.createPDF(bos);
			byteArr = bos.toByteArray();
		} catch (DocumentException e) {
			logger.error("生成pdf异常",e);
			throw new RuntimeException("生成pdf异常");
		} catch (IOException e) {
			logger.error("设置模版字体出错",e);
			throw new RuntimeException("设置模版字体出错");
		}finally{
			try {
				bos.close();
			} catch (IOException e) {
			}
		}
		return byteArr;
	}
	
	/**
	 * 根据html内容生成pdf文件并返回文件的字节数组,html 所有字体默认使用font-family:'Arial Unicode MS'
	 * @param htmlContent 生成pdf文件的html字符串
	 * @return 返回pdf文件字节数字
	 */
	public static byte[] createPDF(String htmlContent){
		//字体路径
		String fontPath = PDFUtil.class.getResource("/").getPath()+"/fonts/ARIALUNI.TTF";
		return generatePDF(htmlContent, fontPath);
	}
	
	
	public static void main(String[] args) throws Exception{
		//剔除freemark 标签可以生成
		String filePath = "c:/tmp/loan-new2.html";
		String outFilePath = "c:/tmp/loan-new2-v1.pdf";
//		createPDF(filePath, outFilePath);
		
		//测试生成pdf返回byte[]
		String htmlContent = FileUtil.readFile(filePath);
		Long startTime = System.currentTimeMillis();
		byte[] byteArr = createPDF(htmlContent);
		FileOutputStream fos = new FileOutputStream(new File(outFilePath));
		fos.write(byteArr);
		fos.close();
		Long endTime = System.currentTimeMillis();
		logger.info("pdf 已经生成,耗时={}ms",(endTime-startTime));
	}
	
}
