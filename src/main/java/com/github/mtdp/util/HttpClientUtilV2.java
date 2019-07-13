package com.github.mtdp.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * HTTP 请求工具类
 */
public class HttpClientUtilV2 {
  
  private static final Logger logger = LoggerFactory.getLogger(HttpClientUtilV2.class);
  
  private static PoolingHttpClientConnectionManager connMgr;
  private static RequestConfig requestConfig;
  private static final int MAX_TIMEOUT = 15000;
  
  private static String CHARSET = "UTF-8";

  static {
    // 设置连接池
    connMgr = new PoolingHttpClientConnectionManager();
    // 设置连接池大小
    connMgr.setMaxTotal(100);
    connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

    RequestConfig.Builder configBuilder = RequestConfig.custom();
    // 设置连接超时
    configBuilder.setConnectTimeout(MAX_TIMEOUT);
    // 设置读取超时
    configBuilder.setSocketTimeout(MAX_TIMEOUT);
    // 设置从连接池获取连接实例的超时
    configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
    // 在提交请求之前 测试连接是否可用
    configBuilder.setStaleConnectionCheckEnabled(true);
    requestConfig = configBuilder.build();
  }

  /**
   * 发送 GET 请求（HTTP），不带输入数据<br>
   * @param url
   * @return
   */
  public static String httpGet(String url) {
    return httpGet(url, new HashMap<String, Object>());
  }

  /**
   * 发送 GET 请求（HTTP），K-V形式<br/>
   * 
   * @param url
   * @param params
   * @return
   */
  public static String httpGet(String url, Map<String, Object> params) {
    String result = null;
    StringBuffer param = new StringBuffer();
    int i = 0;
    if(params != null){
      for (String key : params.keySet()) {
        if (i == 0)
          param.append("?");
        else
          param.append("&");
        param.append(key).append("=").append(params.get(key));
        i++;
      }
      url += param;
    }
    HttpClient httpclient = getHttpClient();
    try {
      HttpGet httpGet = new HttpGet(url);
      //设置请求参数
      httpGet.setConfig(requestConfig);
      HttpResponse response = httpclient.execute(httpGet);
      int statusCode = response.getStatusLine().getStatusCode();
      logger.info("返回的状态码:{}", statusCode);
      HttpEntity entity = response.getEntity();
      //200
      if(statusCode == HttpStatus.SC_OK && entity != null){
         result = EntityUtils.toString(entity, CHARSET);
      }
    } catch (IOException e) {
      logger.error("http get send error",e);
    }
    return result;
  }

  /**
   * 发送 POST 请求（HTTP），不带输入数据<br/>
   * 设置默认的头信息"Content-Type","text/xml;charset=UTF-8"
   * @param url
   * @return
   */
  public static String httpPost(String url) {
    Map<String,Object> headerParams = new HashMap<String, Object>();
    headerParams.put("Content-Type","text/xml;charset="+CHARSET);
    return httpPost(url, new HashMap<String, Object>(),headerParams);
  }

  /**
   * 发送 POST 请求（HTTP），K-V形式
   * 
   * @param apiUrl API接口URL
   * @param params 参数map
   * @return
   */
  public static String httpPost(String url, Map<String, Object> params,Map<String, Object> headerParams) {
    HttpClient httpClient = getHttpClient();
    String result = null;
    HttpPost httpPost = new HttpPost(url);

    try {
      //设置头信息
      if(headerParams != null && !headerParams.isEmpty()){
        for(Map.Entry<String, Object> en : headerParams.entrySet()){
          String k = en.getKey();
          String v = en.getValue().toString();
          if(!StringUtil.isNotBlank(v)){
              v = "";
          }
          httpPost.addHeader(k, v);
        }
      }
      httpPost.setConfig(requestConfig);
      if(params != null && !params.isEmpty()){
        List<NameValuePair> pairList = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
          NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
          pairList.add(pair);
        }
        httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(CHARSET)));
      }
      HttpResponse response = httpClient.execute(httpPost);
      int statusCode = response.getStatusLine().getStatusCode();
      logger.info("返回的状态码:{}", statusCode);
      HttpEntity entity = response.getEntity();
      if(HttpStatus.SC_OK == statusCode && entity != null){
        result = EntityUtils.toString(entity, CHARSET);
      }
    } catch (IOException e) {
      logger.error("http post send error",e);
    } finally {
      httpPost.releaseConnection();
    }
    return result;
  }

  /**
   * 发送 POST 请求（HTTP），JSON形式<br/>
   * 
   * @param url
   * @param json 字符串
   * @return
   */
  public static String httpPostJsonData(String url, String json) {
    HttpClient httpClient = getHttpClient();
    String result = null;
    HttpPost httpPost = new HttpPost(url);
    try {
      httpPost.setConfig(requestConfig);
      StringEntity stringEntity = new StringEntity(json, CHARSET);// 解决中文乱码问题
      stringEntity.setContentEncoding(CHARSET);
      stringEntity.setContentType("application/json");
      httpPost.setEntity(stringEntity);
      HttpResponse response = httpClient.execute(httpPost);
      int statusCode = response.getStatusLine().getStatusCode();
      logger.info("返回的状态码:{}", statusCode);
      HttpEntity entity = response.getEntity();
      if(HttpStatus.SC_OK == statusCode){
        result = EntityUtils.toString(entity, CHARSET);
      }
    } catch (IOException e) {
      logger.error("http post josn send error",e);
    } finally {
      httpPost.releaseConnection();
    }
    return result;
  }

  /**
   * 发送 SSL POST 请求（HTTPS），K-V形式<br/>
   * 
   * @param url
   * @param params 参数map
   * @return
   */
  public static String httpPostSSL(String url, Map<String, Object> params) {
    HttpClient httpClient = getHttpsClient();
    HttpPost httpPost = new HttpPost(url);
    String result = null;

    try {
      httpPost.setConfig(requestConfig);
      if(params != null){
        List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
          NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
          pairList.add(pair);
        }
        httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(CHARSET)));
      }
      HttpResponse response = httpClient.execute(httpPost);
      int statusCode = response.getStatusLine().getStatusCode();
      logger.info("返回的状态码:{}", statusCode);
      HttpEntity entity = response.getEntity();
      if (statusCode == HttpStatus.SC_OK && entity != null) {
        result = EntityUtils.toString(entity, CHARSET);
      }
    } catch (Exception e) {
      logger.error("https post send error",e);
    } finally {
       httpPost.releaseConnection();
    }
    return result;
  }

  /**
   * 发送 SSL POST 请求（HTTPS），JSON形式<br/>
   * 
   * @param url API接口URL
   * @param json JSON对象
   * @return
   */
  public static String httpPostSSLJsonData(String url, String json) {
    HttpClient httpClient = getHttpsClient();
    HttpPost httpPost = new HttpPost(url);
    String result = null;

    try {
      httpPost.setConfig(requestConfig);
      StringEntity stringEntity = new StringEntity(json, CHARSET);// 解决中文乱码问题
      stringEntity.setContentEncoding(CHARSET);
      stringEntity.setContentType("application/json");
      httpPost.setEntity(stringEntity);
      HttpResponse response = httpClient.execute(httpPost);
      int statusCode = response.getStatusLine().getStatusCode();
      logger.info("返回的状态码:{}", statusCode);
      HttpEntity entity = response.getEntity();
      if (statusCode == HttpStatus.SC_OK && entity != null) {
        result = EntityUtils.toString(entity, CHARSET);
      }
    } catch (Exception e) {
      logger.error("https post send error",e);
    } finally {
      httpPost.releaseConnection();
    }
    return result;
  }

  /**
   * 创建SSL安全连接
   * 
   * @return
   */
  private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
    SSLConnectionSocketFactory sslsf = null;
    try {
      SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

        public boolean isTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
          return true;
        }
      }).build();
      sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

        @Override
        public boolean verify(String arg0, SSLSession arg1) {
          return true;
        }

        @Override
        public void verify(String host, SSLSocket ssl) throws IOException {}

        @Override
        public void verify(String host, X509Certificate cert) throws SSLException {}

        @Override
        public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {}
      });
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
    }
    return sslsf;
  }

  /**
   * 获取httpclient
   * @return
   */
  public static HttpClient getHttpClient(){
    HttpClient client = HttpClients.createDefault();
    return client;
  }
  
  public static HttpClient getHttpsClient(){
    HttpClient client = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).build();
    return client;
  }

  /**
   * 测试方法
   * 
   * @param args
   */
  public static void main(String[] args) throws Exception {
    String url = "https://www.baidu.com";
    String result = httpPostJsonData(url,"{}");
    logger.info("result={}",result);
  }

}
