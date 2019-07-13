package com.github.mtdp.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mtdp.TdpUtilConstants;

/**
 * 
 *
 * @Description apache http 工具包
 * @author gqwang
 * @date 2016年1月26日下午3:21:17
 *
 */
public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private static String CHARSET = "UTF-8";
	
	/**
	 * content-type 默认 text/html
	 * @param url
	 * @param params
	 * @return
	 */
	public static String httpPostDefault(String url,Map<String,Object> params){
		Map<String,String> headerParams = new HashMap<String, String>();
		headerParams.put("Content-Type","text/html;charset="+CHARSET);
		return httpPost(url, params,headerParams);
	}
	
	/**
	 * content-type 默认 text/xml<br/>
	 * 默认连接及读超时时间都是15s<br/>
	 * @param url
	 * @param params
	 * @return
	 */
	public static String httpPost(String url,Map<String,Object> params){
	  Map<String,String> headerParams = new HashMap<String, String>();
	  headerParams.put("Content-Type","text/xml;charset="+CHARSET);
	  return httpPost(url, params,headerParams);
	}
	
	/**
     * 用httpclient 发送post 请求<br/>
     * 如果参数为null,会转化成""<br/>
     * 默认连接及读超时时间都是15s<br/>
     * @param url
     * @param params
     * @return
     */
	public static String httpPost(String url,Map<String,Object> params,Map<String,String> headerParams){
		String result = null;
		HttpClient client = getHttpClient();
		PostMethod post = new PostMethod(url);
		if(headerParams != null && !headerParams.isEmpty()){
			for(Map.Entry<String, String> en : headerParams.entrySet()){
				String k = en.getKey();
				String v = en.getValue();
				if(!StringUtil.isNotBlank(v)){
					v = "";
				}
				post.addRequestHeader(k, v);
			}
		}
		if(params != null && !params.isEmpty()){
			for(Map.Entry<String,Object> en : params.entrySet()){
				String key = en.getKey();
				String value = en.getValue().toString();
				if(!StringUtil.isNotBlank(value)){
					value = "";
				}
				NameValuePair v = new NameValuePair(key, value);
				post.addParameter(v);
			}
		}
		try {
			client.executeMethod(post);
			if(post.getStatusCode() == HttpStatus.SC_OK){
				result = post.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			logger.error("httpclient send post error",e);
		} catch (IOException e) {
			logger.error("httpclient send post error",e);
		}finally{
			post.releaseConnection();
		}
		return result;
	}
	
	/**
	 *用httpclient 发送post 请求<br/>
     * 如果参数为null,会转化成""<br/>
     * 默认连接及读超时时间都是15s<br/>
     * 没有设置head信息,默认charset为UTF-8
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String httpPost(String url,Map<String,Object> params,String charset){
	  String result = null;
	  HttpClient client = getHttpClient();
	  PostMethod post = new PostMethod(url);
	  //如果编码为空设置默认utf-8编码
	  if(StringUtil.isNotBlank(charset)){
	    post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CHARSET);
	  }
	  if(params != null && !params.isEmpty()){
	    for(Map.Entry<String,Object> en : params.entrySet()){
	      String key = en.getKey();
	      String value = en.getValue().toString();
	      if(!StringUtil.isNotBlank(value)){
	        value = "";
	      }
	      NameValuePair v = new NameValuePair(key, value);
	      post.addParameter(v);
	    }
	  }
	  try {
	    client.executeMethod(post);
	    if(post.getStatusCode() == HttpStatus.SC_OK){
	      result = post.getResponseBodyAsString();
	    }
	  } catch (HttpException e) {
	    logger.error("httpclient send post error",e);
	  } catch (IOException e) {
	    logger.error("httpclient send post error",e);
	  }finally{
	    post.releaseConnection();
	  }
	  return result;
	}
	
	/**
	 * 向url发送json格式的post请求
	 * @param url
	 * @param obj
	 * @param headerParams
	 * @return
	 */
	public static String httpPostJsonData(String url,String jsonString,Map<String,String> headerParams){
	  HttpClient client = getHttpClient();
      PostMethod post = new PostMethod(url);
      //设置返回内容的格式编码
      post.getParams().setContentCharset(CHARSET);
      post.addRequestHeader("Content-Type", "application/json;charset="+CHARSET);
      if(headerParams != null && !headerParams.isEmpty()){
        for(Map.Entry<String, String> en : headerParams.entrySet()){
            String k = en.getKey();
            String v = en.getValue();
            if(!StringUtil.isNotBlank(v)){
                v = "";
            }
            post.addRequestHeader(k, v);
        }
      }
      try {
        post.setRequestEntity(new StringRequestEntity(jsonString,"application/json",CHARSET));
        client.executeMethod(post);
        if(post.getStatusCode() == HttpStatus.SC_OK){
            return post.getResponseBodyAsString();
        }
      } catch (HttpException e) {
          logger.error("httpclient send post error",e);
      } catch (IOException e) {
          logger.error("httpclient send post error",e);
      }finally{
          post.releaseConnection();
      }
      return null;
	}
	
	public static String httpGet(String url){
		return httpGet(url,null);
	}
	
	/**
	 * 发送get请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String httpGet(String url,Map<String,String> params){
		String result = null;
		HttpClient client = getHttpClient();
		StringBuffer sb = new StringBuffer(url);
		if(params != null && !params.isEmpty()){
		    sb.append("?");
			for(Map.Entry<String,String> en : params.entrySet()){
				String key = en.getKey();
				String value = en.getValue();
				if(!StringUtil.isNotBlank(value)){
					value = "";
				}else{
					try {
						value = URLEncoder.encode(value,CHARSET);
					} catch (UnsupportedEncodingException e) {
						logger.error("set http get param error",e);
						sb.append(key).append("=").append(value).append("&");
						continue;
					}
				}
				sb.append(key).append("=").append(value).append("&");
			}
			//删除最后一个 & 符号
			sb.deleteCharAt(sb.length()-1);
		}
		GetMethod get = new GetMethod(sb.toString());
		get.addRequestHeader("Content-Type","text/html;charset="+CHARSET);
		try {
			client.executeMethod(get);
			if(get.getStatusCode() == HttpStatus.SC_OK){
				result = get.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			logger.error("httpclient send get error",e);
		} catch (IOException e) {
			logger.error("httpclient send get error",e);
		}finally{
			get.releaseConnection();
		}
		return result;
	}
	
	/**
	 * 返回responseBoby和responseHeader
	 * @param url
	 * @param params
	 * @return
	 */
	public static Map<String,Object> httpGetResultMap(String url,Map<String,String> params){
		Map<String, Object> result = new HashMap<String,Object>();
		HttpClient client = getHttpClient();
		StringBuffer sb = new StringBuffer(url);
		sb.append("?");
		if(params != null && !params.isEmpty()){
			for(Map.Entry<String,String> en : params.entrySet()){
				String key = en.getKey();
				String value = en.getValue();
				if(!StringUtil.isNotBlank(value)){
					value = "";
				}else{
					try {
						value = URLEncoder.encode(value,CHARSET);
					} catch (UnsupportedEncodingException e) {
						logger.error("set http get param error",e);
						sb.append(key).append("=").append(value).append("&");
						continue;
					}
				}
				sb.append(key).append("=").append(value).append("&");
			}
			//删除最后一个 & 符号
			sb.deleteCharAt(sb.length()-1);
		}
		GetMethod get = new GetMethod(sb.toString());
		get.addRequestHeader("Content-Type","text/html;charset="+CHARSET);
		try {
			client.executeMethod(get);
			if(get.getStatusCode() == HttpStatus.SC_OK){
				String respBody = get.getResponseBodyAsString();
				Header[] respHeader = get.getResponseHeaders();
				result.put("respBody", respBody);
				result.put("respHeader", respHeader);
			}
		} catch (HttpException e) {
			logger.error("httpclient send get error",e);
		} catch (IOException e) {
			logger.error("httpclient send get error",e);
		}finally{
			get.releaseConnection();
		}
		return result;
	}
	
	
	/**
	 * 获取一个httpclient<br/>
	 * 默认连接及读超时时间都是15s<br/>
	 * @return
	 */
	public static HttpClient getHttpClient(){
//	  HttpClient client = new HttpClient(); 经测试，这样连接完成后其实也没有关闭
	  HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
	  //设置连接及读超时
      client.getHttpConnectionManager().getParams().setConnectionTimeout(TdpUtilConstants.HTTP_CONNECT_TIMEOUT * 1000);
      client.getHttpConnectionManager().getParams().setSoTimeout(TdpUtilConstants.HTTP_READ_TIEMOUT * 1000);
	  return client;
	}

}
