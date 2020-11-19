package com.zhang.constant;

public interface WechatConstant {
  Integer OK_STATUS      = 0;
  String URL_CODE2SESSION   = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
 
 
  String URL_GET_ACCESS_TOKEN   = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
 
 
  String URL_GET_IMAGE = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
   
   
  /**
   * 给公众号发送信息。参考https://mp.weixin.qq.com/advanced/tmplmsg?action=faq&token=708366329&lang=zh_CN
   */
  String URL_SEND_TO_CHANNEL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
  String URL_SEND_MESSAGE   = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
   
  /**
   * 发送模板消息。参考https://developers.weixin.qq.com/miniprogram/dev/api-backend/sendMiniTemplateMessage.html
   */
  String URL_SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=%s";
 
  String URL_QR_CODE_UNLIMTED = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";
   
  String URL_QR_CODE = "https://api.weixin.qq.com/wxa/getwxacode?access_token=%s";
 
  /**
   * 获取标签下粉丝列表
   */
  String URL_ALL_FANS_OPENID = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=%s";
  /**
   * 获取公众号已创建的标签
   */
  String URL_ALL_TAGS = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=%s";
 
}