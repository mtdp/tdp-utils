package com.github.mtdp.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.github.mtdp.util.excel.ExcelUtils;
/**
 * 
 *
 * @Description excel导入测试
 * @author wangguoqing
 * @date 2016年7月13日下午1:49:09
 *
 */
public class EcxelImportTest {
	
	public static void main(String[] args) throws FileNotFoundException, Exception {
		String fileName = "C:/Users/wangguoqing/Desktop/f/scv.xls";
		String saveFilePath = "C:/Users/wangguoqing/Desktop/face/";
		ExcelUtils e = new ExcelUtils();
		e.setSheetName("scv");
		List<String[]> list = e.read(new FileInputStream(new File(fileName)));
		int count = 0;
		int end = 200;
		for(String[] strArr : list){
			count ++;
			if(count == 1){
				continue;
			}
			if(count > end){
				break;
			}
			System.out.println(Arrays.toString(strArr));
			saveFile(saveFilePath, strArr);
		}
	}
	
	private static void saveFile(String saveFilePath,String[] files) throws IOException{
		System.out.println("保存文件");
		for(String file : files){
			String fileName = file.substring(file.lastIndexOf("/")+1);
			File f = new File(saveFilePath+fileName+".jpg");
			FileOutputStream fos = new FileOutputStream(f);
			URL url = new URL(file);
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    InputStream in = conn.getInputStream();
		    byte[] byteArr = new byte[8*1024];
		    int c = 0;
		    while((c = in.read(byteArr)) != -1){
		    	fos.write(byteArr,0,c);
		    }
			fos.flush();
			fos.close();
			break;
		}
	}

}
