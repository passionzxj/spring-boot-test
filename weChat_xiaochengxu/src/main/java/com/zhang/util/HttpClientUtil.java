package com.zhang.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {
 
  // utf-8字符编码
  public static final String            CHARSET_UTF_8     = "utf-8";
 
  // HTTP内容类型。
  public static final String            CONTENT_TYPE_TEXT_HTML = "text/xml";
 
  // HTTP内容类型。相当于form表单的形式，提交数据
  public static final String            CONTENT_TYPE_FORM_URL = "application/x-www-form-urlencoded";
 
  // HTTP内容类型。相当于form表单的形式，提交数据
  public static final String            CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";
 
  // 连接管理器
  private static PoolingHttpClientConnectionManager pool;
 
  // 请求配置
  private static volatile RequestConfig requestConfig;
 
  private static CloseableHttpClient getNewHttpClient() {
 
    CloseableHttpClient httpClient = HttpClients.custom()
      // 设置连接池管理
      .setConnectionManager(pool)
      // 设置请求配置
      .setDefaultRequestConfig(getRequestConfig())
      // 设置重试次数
      .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();
 
    return httpClient;
  }
 
  /**
   * 发送 post请求
   *
   * @param httpUrl
   *      地址
   */
  public static String post(String httpUrl) {
    // 创建httpPost
    HttpPost httpPost = new HttpPost(httpUrl);
    return request(httpPost);
  }
 
  public static byte[] postRaw(String httpUrl) {
    // 创建httpPost
    HttpPost httpPost = new HttpPost(httpUrl);
    return requestRaw(httpPost);
  }
 
  /**
   * 发送 get请求
   *
   * @param httpUrl
   */
  public static String get(String httpUrl) {
    // 创建get请求
    HttpGet httpGet = new HttpGet(httpUrl);
    return request(httpGet);
  }
 
  /**
   * 发送 post请求（带文件）
   *
   * @param httpUrl
   *      地址
   * @param maps
   *      参数
   * @param fileLists
   *      附件
   */
  public static String post(String httpUrl, Map<String, String> maps, List<File> fileLists,
                            String fileName) {
    HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
    MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
    if (maps != null) {
      for (String key : maps.keySet()) {
        meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
      }
    }
    if (fileLists != null) {
      for (File file : fileLists) {
        FileBody fileBody = new FileBody(file);
        meBuilder.addPart(fileName, fileBody);
      }
    }
    HttpEntity reqEntity = (HttpEntity) meBuilder.build();
    httpPost.setEntity(reqEntity);
    return request(httpPost);
  }
 
  public static String post(String httpUrl, Map<String, String> maps, List<File> fileLists) {
    return post(httpUrl, maps, fileLists, "file");
  }
 
  public static String post(String httpUrl, List<File> fileLists) {
    return post(httpUrl, Collections.emptyMap(), fileLists, "file");
  }
 
  /**
   * 发送 post请求
   *
   * @param httpUrl
   *      地址
   * @param params
   *      参数(格式:key1=value1&key2=value2)
   *
   */
  public static String post(String httpUrl, String params) {
    HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
    try {
      // 设置参数
      if (params != null && params.trim().length() > 0) {
        StringEntity stringEntity = new StringEntity(params, "UTF-8");
        stringEntity.setContentType(CONTENT_TYPE_FORM_URL);
        httpPost.setEntity(stringEntity);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return request(httpPost);
  }
 
  /**
   * 发送 post请求
   *
   * @param maps
   *      参数
   */
  public static String post(String httpUrl, Map<String, String> maps) {
    String param = convertStringParamter(maps);
    return post(httpUrl, param);
  }
 
 
 
  /**
   * 发送 post请求 发送json数据
   *
   * @param httpUrl
   *      地址
   * @param content
   *
   *
   */
  public static String post(String httpUrl, String content, String contentType) {
    //    HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
    //    try {
    //      // 设置参数
    //      if (StringUtils.isNotEmpty(content)) {
    //        StringEntity stringEntity = new StringEntity(content, "UTF-8");
    //        stringEntity.setContentType(contentType);
    //        httpPost.setEntity(stringEntity);
    //      }
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    }
    //    return request(httpPost);
    return new String(postRaw(httpUrl, content, contentType), StandardCharsets.UTF_8);
  }
 
  public static byte[] postRaw(String httpUrl, String content, String contentType) {
    HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
    try {
      // 设置参数
      if (StringUtils.isNotEmpty(content)) {
        StringEntity stringEntity = new StringEntity(content, "UTF-8");
        stringEntity.setContentType(contentType);
        httpPost.setEntity(stringEntity);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return requestRaw(httpPost);
  }
 
  /**
   * 发送 post请求 发送json数据
   *
   * @param httpUrl
   *      地址
   * @param paramsJson
   *      参数(格式 json)
   *
   */
  public static String postJson(String httpUrl, String paramsJson) {
    return post(httpUrl, paramsJson, CONTENT_TYPE_JSON_URL);
  }
 
  public static byte[] postJsonRaw(String httpUrl, String paramsJson) {
    return postRaw(httpUrl, paramsJson, CONTENT_TYPE_JSON_URL);
  }
 
  /**
   * 发送 post请求 发送xml数据
   *
   * @param url  地址
   * @param paramsXml 参数(格式 Xml)
   *
   */
  public static String postXml(String url, String paramsXml) {
    return post(url, paramsXml, CONTENT_TYPE_TEXT_HTML);
  }
 
  /**
   * 将map集合的键值对转化成：key1=value1&key2=value2 的形式
   *
   * @param parameterMap
   *      需要转化的键值对集合
   * @return 字符串
   */
  public static String convertStringParamter(Map parameterMap) {
    StringBuilder parameterBuffer = new StringBuilder();
    if (parameterMap != null) {
      Iterator iterator = parameterMap.keySet().iterator();
      String key = null;
      String value = null;
      while (iterator.hasNext()) {
        key = (String) iterator.next();
        if (parameterMap.get(key) != null) {
          value = (String) parameterMap.get(key);
        } else {
          value = "";
        }
        parameterBuffer.append(key).append("=").append(value);
        if (iterator.hasNext()) {
          parameterBuffer.append("&");
        }
      }
    }
    return parameterBuffer.toString();
  }
 
  /**
   * 发送请求
   *
   * @param request
   * @return
   */
  public static byte[] requestRaw(HttpRequestBase request) {
 
    CloseableHttpClient httpClient;
    CloseableHttpResponse response = null;
    // 响应内容
    //    String responseContent = null;
    byte[] rawResponse = null;
    try {
      // 创建默认的httpClient实例.
      httpClient = getNewHttpClient();
      // 配置请求信息
      request.setConfig(requestConfig);
      // 执行请求
      response = httpClient.execute(request);
      // 得到响应实例
      HttpEntity entity = response.getEntity();
 
      // 可以获得响应头
      // Header[] headers = response.getHeaders(HttpHeaders.CONTENT_TYPE);
      // for (Header header : headers) {
      // System.out.println(header.getName());
      // }
 
      // 得到响应类型
      // System.out.println(ContentType.getOrDefault(response.getEntity()).getMimeType());
 
      // 判断响应状态
      if (response.getStatusLine().getStatusCode() >= 300) {
        throw new Exception("HTTP Request is not success, Response code is "
                  + response.getStatusLine().getStatusCode());
      }
 
      if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
        rawResponse = EntityUtils.toByteArray(entity);
        //        responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
        EntityUtils.consume(entity);
      }
 
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        // 释放资源
        if (response != null) {
          response.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return rawResponse;
  }
 
  private static String request(HttpRequestBase req) {
    return new String(requestRaw(req), StandardCharsets.UTF_8);
  }
 
  private static RequestConfig getRequestConfig() {
 
    if (requestConfig == null) {
      synchronized (HttpClientUtil.class) {
        if (requestConfig == null) {
          try {
            //System.out.println("初始化HttpClientTest~~~开始");
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
              builder.build());
            // 配置同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
              .<ConnectionSocketFactory> create()
              .register("http", PlainConnectionSocketFactory.getSocketFactory())
              .register("https", sslsf).build();
            // 初始化连接管理器
            pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
            pool.setMaxTotal(200);
            // 设置最大路由
            pool.setDefaultMaxPerRoute(2);
            // 根据默认超时限制初始化requestConfig
            int socketTimeout = 10000;
            int connectTimeout = 10000;
            int connectionRequestTimeout = 10000;
            requestConfig = RequestConfig.custom()
              .setConnectionRequestTimeout(connectionRequestTimeout)
              .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
              .build();
 
          } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
          } catch (KeyStoreException e) {
            e.printStackTrace();
          } catch (KeyManagementException e) {
            e.printStackTrace();
          }
 
          // 设置请求超时时间
          requestConfig = RequestConfig.custom().setSocketTimeout(50000)
            .setConnectTimeout(50000).setConnectionRequestTimeout(50000).build();
        }
      }
    }
    return requestConfig;
  }
}
