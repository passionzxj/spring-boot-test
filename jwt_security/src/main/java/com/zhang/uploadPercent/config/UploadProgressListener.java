package com.zhang.uploadPercent.config;

import cn.hutool.json.JSONUtil;
import com.zhang.uploadPercent.websocket.WebSocketServer;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class UploadProgressListener implements ProgressListener {
    private HttpSession session;
    @Autowired
    private WebSocketServer webSocketServer;
 
    public void setSession(HttpSession session){
        this.session=session;
        ProgressEntity status = new ProgressEntity();
        session.setAttribute("status", status);
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
        webSocketServer.sendOneMessage("111111", JSONUtil.toJsonStr(status));
    }
}