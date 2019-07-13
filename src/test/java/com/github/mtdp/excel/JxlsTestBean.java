package com.github.mtdp.excel;
/**
 * 
 *
 * @Description jxls导出数据的bena必须是public bean
 * @author wangguoqing
 * @date 2016年7月13日下午1:47:08
 *
 */
public class JxlsTestBean {
	public String tId;
	public String tContent;
	
	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
	}
	public String gettContent() {
		return tContent;
	}
	public void settContent(String tContent) {
		this.tContent = tContent;
	}
}
