package com.zhang.jwt.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhang.jwt.constant.WechatConstant;
import com.zhang.jwt.domain.AccessTokenResponse;
import com.zhang.jwt.domain.Code2SessionResponse;
import com.zhang.jwt.service.WeChatService;
import com.zhang.jwt.util.HttpClientUtil;
import com.zhang.jwt.util.RedisCacheKeys;
import com.zhang.jwt.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class WeChatServiceImpl implements WeChatService {

  private static Logger log = LoggerFactory.getLogger(WeChatServiceImpl.class);
 
  //获取配置文件数据
//  @Value("${wechat.miniprogram.id}")
  private String appId;
 
//  @Value("${wechat.miniprogram.secret}")
  private String appSecret;
 

 
  @Override
  public Code2SessionResponse code2Session(String code) {
    String rawResponse = HttpClientUtil
        .get(String.format(WechatConstant.URL_CODE2SESSION, appId, appSecret, code));
    log.info("rawResponse====={}", rawResponse);
    Code2SessionResponse response = JSON.parseObject(rawResponse, Code2SessionResponse.class);
    if (response.isSuccess()) {
      cacheSessionKey(response);
    }
    return response;
  }
 
  private void cacheSessionKey(Code2SessionResponse response) {
    RedisUtils redisCache = RedisUtils.INSTANCE;
    String key = RedisCacheKeys.getWxSessionKeyKey(response.getOpenId());
    redisCache.set(key, "", response.getSessionKey());
  }
 
  @Override
  public String getAccessToken() {
    return getAccessToken(false);
  }
 
  @Override
  public String getAccessToken(boolean isForce) {
    RedisUtils redisCache = RedisUtils.INSTANCE;
    String accessToken = null;
    if (!isForce) {
      accessToken = redisCache.get(RedisCacheKeys.getWxAccessTokenKey(appId));
    }
    if (!StringUtils.isEmpty(accessToken)) {
      return accessToken;
    }
    String rawResponse = HttpClientUtil
        .get(String.format(WechatConstant.URL_GET_ACCESS_TOKEN, appId, appSecret));
    AccessTokenResponse response = JSON.parseObject(rawResponse, AccessTokenResponse.class);
    log.info("getAccessToken:response={}", response);
    if (response.isSuccess()) {
      redisCache.set(RedisCacheKeys.getWxAccessTokenKey(appId), "", response.getAcessToken());
      return response.getAcessToken();
    }
    return null;
  }


  @Override
  public String getSessionKey(String openId) {
    RedisUtils redisCache = RedisUtils.INSTANCE;
    String key = RedisCacheKeys.getWxSessionKeyKey(openId);
    return redisCache.get(key);
  }
}