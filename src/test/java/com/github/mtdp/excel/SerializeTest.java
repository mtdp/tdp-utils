package com.github.mtdp.excel;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 
 *
 * @Description 序列化工具测试
 * @author wangguoqing
 * @date 2016年8月31日上午10:02:36
 *
 */
public class SerializeTest {
	
	public static void main(String[] args) {
		Goo g = new Goo(1,"goo");
		String json = JSON.toJSONString(g);
		System.out.println(json);
		Goo gg = JSON.parseObject(json, Goo.class);
		System.out.println(gg);
		
		
		
		Goo g2 = new Goo(2,"goo2");
		List<Goo> list = new ArrayList<Goo>();
		list.add(g);
		list.add(g2);
		
		json = JSON.toJSONString(list);
		System.out.println(json);
		list = JSON.parseArray(json, Goo.class);
		System.out.println("list:"+list.get(0));
		for(Goo goo : list){
			System.out.println(goo);
		}
	}

}

class Goo{
	private int id;
	private String name;
	public Goo(){}
	public Goo(int id,String name){
		this.id=id;
		this.name=name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
