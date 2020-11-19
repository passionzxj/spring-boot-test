package com.zhang.service;

import com.zhang.domain.Code2SessionResponse;

public interface WeChatService {

    /**
     * 用code换取openid
     *
     * @param code
     * @return
     */
    Code2SessionResponse code2Session(String code);


    /**
     * 获取凭证
     *
     * @return
     */
    String getAccessToken();


    /**
     * 获取凭证
     *
     * @param isForce
     * @return
     */
    String getAccessToken(boolean isForce);


    String getSessionKey(String openId);
}
