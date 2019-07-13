package com.github.mtdp.excel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.DecoderException;

import com.github.mtdp.util.excel.ExcelField;
import com.github.mtdp.util.excel.ExportExcel;
import com.github.mtdp.util.excel.Type;

/**
 * 
 * @Description excel导出测试
 * @author wangguoqing
 * @date 2016年7月13日下午1:24:00
 *
 */
public class ExcelExportTest {
	public static void main(String[] args) throws DecoderException, UnsupportedEncodingException {
		List<Foo> list = new ArrayList<Foo>();
		list.add(new Foo(1,"01"));
		list.add(new Foo(2,"02"));
		
		ExportExcel<Foo> ee = new ExportExcel<Foo>(list);
		
		try {
			ee.export("d:/tmp/1/excel.xls");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

class Foo{
	@ExcelField(filedName="id",sort=1)
	private int id;
	@ExcelField(filedName="name",sort=2)
	private String name;
	@ExcelField(type=Type.OBJECT,sort=3)
	private Hoo hoo;
	
	public Foo(int id,String name){
		this.id = id;
		this.name = name;
		this.hoo = new Hoo(123);
	}
}

class Hoo{
	private int i;
	public Hoo(int i){
		this.i = i;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	
}
