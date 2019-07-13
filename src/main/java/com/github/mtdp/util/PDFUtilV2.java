package com.github.mtdp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * 
 *
 * @Description itextpdf、xmlworker工具生成pdf
 * @author wangguoqing
 * @date 2017年8月16日下午3:50:10
 *
 */
public class PDFUtilV2 {
	
	private static Logger logger = LoggerFactory.getLogger(PDFUtilV2.class);
	
	/**
	 * 根据html内容生成pdf文档并转换成byte数组
	 * @param htmlContent
	 * @return
	 */
	public static byte[] createPDF(String htmlContent){
		byte[] byteArr = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayInputStream bis = new ByteArrayInputStream(htmlContent.getBytes());
		Document doc = new Document();
		try {
			PdfWriter pdf = PdfWriter.getInstance(doc, bos);
			//打开文档
			doc.open();
			//设置字体
			XMLWorkerFontProvider font = new XMLWorkerFontProvider();
			font.register(PDFUtilV2.class.getResource("/").getPath()+"/fonts/ARIALUNI.TTF");
			XMLWorkerHelper.getInstance().parseXHtml(pdf, doc, bis,Charset.forName("UTF-8"),font);
			//关闭文档(未关闭生成的pdf文档无效,字节为0)
			doc.close();
			byteArr = bos.toByteArray();
		} catch (DocumentException | IOException e) {
			logger.error("生成pdf出错",e);
		}finally{
			try{
				bis.close();
				bos.close();
			}catch(Exception e){
				logger.error("关闭生产pdf流出错",e);
			}
		}
		return byteArr;
	}
	
	public static void main(String[] args)  throws Exception{
		//注意html文件的编码需要是utf-8 跟生成pdf的编码一致
		String filePath = "c:/tmp/loan-new2.html";
		String outFilePath = "c:/tmp/loan-new2-v2.pdf";
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
