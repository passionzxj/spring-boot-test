package com.zhang.jwt.util;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.*;


/**
 * 防止重复请求工具类
 */
@Slf4j

public class ReqDedupHelper {

    /**
     * @param reqJSON     请求的参数，这里通常是JSON
     * @param excludeKeys 请求参数里面要去除哪些字段再求摘要
     * @return 去除参数的MD5摘要
     */
    private static String dedupParamMD5(final String reqJSON, String... excludeKeys) {
        String decreptParam = reqJSON;

        TreeMap paramTreeMap = JSON.parseObject(decreptParam, TreeMap.class);
        if (excludeKeys != null) {
            List<String> dedupExcludeKeys = Arrays.asList(excludeKeys);
            if (!dedupExcludeKeys.isEmpty()) {
                for (String dedupExcludeKey : dedupExcludeKeys) {
                    paramTreeMap.remove(dedupExcludeKey);
                }
            }
        }

        String paramTreeMapJSON = JSON.toJSONString(paramTreeMap);
        String md5deDupParam = jdkMD5(paramTreeMapJSON);
        log.debug("md5deDupParam = {}, excludeKeys = {} {}", md5deDupParam, Arrays.deepToString(excludeKeys), paramTreeMapJSON);
        return md5deDupParam;
    }

    private static String jdkMD5(String src) {
        String res = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdBytes = messageDigest.digest(src.getBytes());
            res = DatatypeConverter.printHexBinary(mdBytes);
        } catch (Exception e) {
            log.error("", e);
        }
        return res;
    }

    /**
     * 验证是否重复请求
     *
     * @param req
     * @param userId
     * @param method      接口路径
     * @param excludeKeys 请求参数中需要排除的字段
     * @return
     */

    /*
    *   http://localhost:8080/rtsbiz/page/goto.do?view=/page/jsp/maintainence/meeting/index
    *
    *   String url = request.getRequestURI(); // 这个方法只能获得不包含参数的请求url，且只包含相对路径
        System.out.println("url="+url);
        StringBuffer url_buffer = request.getRequestURL();// 这个方法也只能获得不包含参数的请求url，但是绝对路径
        System.out.println("url_buffer="+url_buffer.toString());
        String queryString = request.getQueryString();;// 这个方法能获得url后面的参数串
        System.out.println("queryString="+queryString);

        输出如下：
        url=/rtsbiz/page/goto.do
        url_buffer=http://localhost:8080/rtsbiz/page/goto.do
        queryString=view=/page/jsp/maintainence/meeting/index
    *
    * */
    public static boolean verifyIsDedupReq(final String req, String userId, String method, StringRedisTemplate stringRedisTemplate, String... excludeKeys) {
//        String userId = "12345678";//用户
//        String method = "pay";//接口名
        String dedupMD5 = dedupParamMD5(req, excludeKeys);//计算请求参数摘要，其中剔除里面请求时间的干扰
        String KEY = "dedup:U=" + userId + "M=" + method + "P=" + dedupMD5;

        long expireTime = 1000;// 1000毫秒过期，1000ms内的重复请求会认为重复
        long expireAt = System.currentTimeMillis() + expireTime;
        String val = "expireAt@" + expireAt;

        //        redis key还存在的话要就认为请求是重复的
//        NOTE:直接SETNX不支持带过期时间，所以设置+过期不是原子操作，极端情况下可能设置了就不过期了，后面相同请求可能会误以为需要去重，所以这里使用底层API，保证SETNX+过期时间是原子操作
        Boolean firstSet = stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set(KEY.getBytes(), val.getBytes(), Expiration.milliseconds(expireTime),
                RedisStringCommands.SetOption.SET_IF_ABSENT));

        final boolean isConsiderDup;
        if (firstSet != null && firstSet) {// 第一次访问
            isConsiderDup = false;
        } else {// redis值已存在，认为是重复了
            isConsiderDup = true;
        }

        return isConsiderDup;

    }

    /**
     * 获取req中body的json参数
     *
     * @param request
     * @return
     * @throws IOException
     */
    /*public static Map<String, String> getJSON(HttpServletRequest request) throws IOException {

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        Map<String, String> params = JSON.parseObject(responseStrBuilder.toString(), Map.class);

        return params;
    }*/

    @Getter
    public enum PathEnum {
        ADD_MONEY("/add/money"),
        UPDATE_MONEY("/update/money");

        private String path;

        PathEnum(String path) {
            this.path = path;
        }
    }

    public static void main(String[] args) {
        //两个请求一样，但是请求时间差一秒
        String req = "{\n" +
                "\"requestTime\" :\"20190101120001\",\n" +
                "\"requestValue\" :\"1000\",\n" +
                "\"requestKey\" :\"key\"\n" +
                "}";

        String req2 = "{\n" +
                "\"requestTime\" :\"20190101120002\",\n" +
                "\"requestValue\" :\"1000\",\n" +
                "\"requestKey\" :\"key\"\n" +
                "}";


        //全参数比对，所以两个参数MD5不同
        String dedupMD5 = dedupParamMD5(req);
        String dedupMD52 = dedupParamMD5(req2);
        System.out.println("req1MD5 = " + dedupMD5 + " , req2MD5=" + dedupMD52);

        //去除时间参数比对，MD5相同
        String dedupMD53 = dedupParamMD5(req, "requestTime");
        String dedupMD54 = dedupParamMD5(req2, "requestTime");
        System.out.println("req1MD5 = " + dedupMD53 + " , req2MD5=" + dedupMD54);

    }


}

