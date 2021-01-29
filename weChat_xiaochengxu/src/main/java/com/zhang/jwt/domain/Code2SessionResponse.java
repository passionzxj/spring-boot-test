package com.zhang.jwt.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Code2SessionResponse implements Serializable {
  public static Integer RESPONSE_OK = 0;
 
  @JSONField(name = "openid")
  private String    openId;
  @JSONField(name = "session_key")
  private String    sessionKey;
  @JSONField(name = "unionid")
  private String    unionId;
  @JSONField(name = "errcode")
  private Integer   errCode;
  @JSONField(name = "errmsg")
  private String   errMsg;
 
 
 
  public boolean isSuccess() {
    return this.errCode == null || RESPONSE_OK.equals(this.errCode);
  }
}