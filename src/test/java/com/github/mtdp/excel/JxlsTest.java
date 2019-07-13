package com.github.mtdp.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.github.mtdp.util.excel.jxls.JxlsUtils;

/**
 * 测试模版是t.xlsx文件
* @ClassName: JxlsTest 
* @Description: TODO 
* @author gqwang
* @date 2015年4月28日 上午10:57:07 
*
 */
public class JxlsTest {
	
	public static void main(String[] args) {
		//准备模版
		String template = "C:/tmp/1/t.xlsx";
//		String dir = System.getProperty("user.dir");
//		template = JxlsTest.class.getClass().getResource("/").getPath()+"t.xlsx";
		//准备数据
		Map<String,Object> data = new HashMap<String,Object>();
		List<JxlsTestBean> cList = new ArrayList<JxlsTestBean>();
		//导出的bean不能是内部类
		for(int i = 0; i < 10; i++){
			JxlsTestBean tj = new JxlsTestBean();
			tj.settId(new Random(i).nextLong()+"");
			tj.settContent("导出测试内容:"+i);
			cList.add(tj);
		}
		//设置导出的循环数据  模版中用 <jx:forEach> 循环输出
		data.put("bindData", cList);
		//可以设置其他数据
		data.put("name", "jxls导出测试");
		//导出数据文件的路径
		String descPath = "C:/tmp/1/test.xlsx";
		JxlsUtils.buildExcelExport(descPath, template, data);
	}
}
