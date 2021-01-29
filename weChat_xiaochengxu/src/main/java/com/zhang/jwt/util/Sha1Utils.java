package com.zhang.jwt.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

public class Sha1Utils {
  private static Logger log = LoggerFactory.getLogger(Sha1Utils.class);
  public static String decryptWXAppletInfo(String sessionKey, String encryptedData, String iv) {
    String result = null;
    try {
      byte[] encrypData = Base64.decodeBase64(encryptedData);
      byte[] ivData = Base64.decodeBase64(iv);
      byte[] sessionKeyB = Base64.decodeBase64(sessionKey);
 
      AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivData);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec keySpec = new SecretKeySpec(sessionKeyB, "AES");
      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
      byte[] doFinal = cipher.doFinal(encrypData);
      result = new String(doFinal);
      return result;
    } catch (Exception e) {
      //e.printStackTrace();
      log.error("decryptWXAppletInfo error",e);
    }
    return null;
  }
 
}