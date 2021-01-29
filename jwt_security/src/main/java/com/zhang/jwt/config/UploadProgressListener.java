package com.zhang.jwt.config;

import cn.hutool.json.JSONUtil;
import com.zhang.jwt.conf.JwtTokenUtil;
import com.zhang.jwt.websocket.WebSocketServer;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class UploadProgressListener implements ProgressListener {
    private HttpSession session;
    public static final String HEADER_STRING = "Authorization";

    @Autowired
    private WebSocketServer webSocketServer;
 
    /*public void setSession(HttpSession session){
        this.session=session;
        ProgressEntity status = new ProgressEntity();
        session.setAttribute("status", status);
    }*/
    public void setSession(HttpServletRequest request){
        this.session=request.getSession();
        ProgressEntity status = new ProgressEntity();
        session.setAttribute("status", status);
        session.setAttribute("userInfo",getUserInfo(request).get("account"));
    }

    private Map<String,String> getUserInfo(HttpServletRequest request){
        String token = request.getHeader(HEADER_STRING);
        return JwtTokenUtil.getUsernameFromToken2(token);
    }

    /* pBytesRead  到目前为止读取文件的比特数
     * pContentLength 文件总大小
     * pItems 目前正在读取第几个文件
     */
    public void update(long pBytesRead, long pContentLength, int pItems) {
        ProgressEntity status = (ProgressEntity) session.getAttribute("status");
        status.setPBytesRead(pBytesRead);
        status.setPContentLength(pContentLength);
        status.setPItems(pItems);
        float tmp = (float) pBytesRead;
        float result = tmp / pContentLength * 100;
//        System.out.println("UploadProgressListener update ProgressEntity:  "+status.toString());
        webSocketServer.sendOneMessage(session.getAttribute("userInfo").toString(), JSONUtil.toJsonStr(status));
    }
}