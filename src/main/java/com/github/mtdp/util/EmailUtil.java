package com.github.mtdp.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @Description 发送邮件工具类
 * @author wangguoqing
 * @date 2017年8月16日上午10:30:33
 *
 */
public class EmailUtil {
	
	private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);
	
	/**发送邮件服务器名称**/
	private static String hostName = "smtp.qq.com";
	
	private static String userName = "";
	
	private static String password = "upefiyzfoepbbajc";
	
	private static String charset = "UTF-8";
	
	public static void main(String[] args) {
		
//		simpleSend("", "测试邮件标题", "测试邮件内容");
		
//		htmlSend("594566751@qq.com", "测试邮件标题", "<h1>测试邮件内容</h1>!");
		
		File file = new File("c:/tmp/domain.txt");
		
//		attachmentSend("594566751@qq.com", "测试邮件标题", "<h1>测试邮件内容</h1>!",file);
		
		String url = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
		
		attachmentSend("594566751@qq.com", "测试邮件标题", "<h1>测试邮件内容</h1>!",url);
	}

	/**
	 * 发送简单的文本邮件
	 * @param email
	 * @param subJect
	 * @param text
	 */
	public static void simpleSend(String email,String subJect,String text){
		try {
			SimpleEmail s = new SimpleEmail();
			s.setHostName(hostName);
			s.setSslSmtpPort("465");
			s.setSSLOnConnect(true);
			s.setAuthentication(userName,password);
			s.addTo(email);
			s.setFrom(userName);
			s.setCharset(charset);
			s.setSubject(subJect);
			s.setMsg(text);
			String r = s.send();
			logger.debug("发送邮件返回值r={}",r);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送html邮件
	 * @param email
	 * @param subJect
	 * @param text
	 */
	public static void htmlSend(String email,String subJect,String text){
		try{
			HtmlEmail html = new HtmlEmail();
			html.setHostName(hostName);
			html.setSslSmtpPort("465");
			html.setSSLOnConnect(true);
			html.setCharset(charset);
			html.setAuthentication(userName, password);
			html.setFrom(userName);
			html.addTo(email);
			html.setSubject(subJect);
			html.setHtmlMsg(text);
			String r = html.send();
			logger.debug("发送邮件返回值r={}",r);
			logger.debug("发送邮件地址={}",html.getToAddresses());
		}catch(EmailException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送本地附件的邮件
	 * @param email
	 * @param subJect
	 * @param text
	 * @param attchement
	 */
	public static void attachmentSend(String email,String subJect,String text,File attchement){
		try {
			MultiPartEmail mp = new MultiPartEmail();
			mp.setHostName(hostName);
			mp.setSslSmtpPort("465");
			mp.setSSLOnConnect(true);
			mp.setCharset(charset);
			mp.setAuthentication(userName, password);
			mp.setFrom(userName);
			mp.addTo(email);
			mp.setSubject(subJect);
			mp.attach(attchement);
			String r = mp.send();
			logger.debug("发送邮件返回值r={}",r);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送url附件的邮件
	 * @param email
	 * @param subJect
	 * @param text
	 * @param url
	 */
	public static void attachmentSend(String email,String subJect,String text,String url){
		try {
			MultiPartEmail mp = new MultiPartEmail();
			mp.setHostName(hostName);
			mp.setSslSmtpPort("465");
			mp.setSSLOnConnect(true);
			mp.setCharset(charset);
			mp.setAuthentication(userName, password);
			mp.setFrom(userName);
			mp.addTo(email);
			mp.setSubject(subJect);
			mp.attach(new URL(url), "测试名称", "附件");
			String r = mp.send();
			logger.debug("发送邮件返回值r={}",r);
		} catch (EmailException | MalformedURLException e) {
			e.printStackTrace();
		}
	}	
}
