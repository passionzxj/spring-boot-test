package com.zhang.uploadPercent.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint(value = "/webSocket/{userId}")
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。
    private static int onlineCount = 0;

    private Session session;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private static Map<String, Session> sessionPool = new HashMap<String, Session>();

    /**
     * @方法描述: 开启socket
     * @return: void
     * @Author: carry
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        int maxSize = 200 * 1024;
        //  可以缓冲的传入二进制消息的最大长度
        session.setMaxBinaryMessageBufferSize(maxSize);
        //  可以缓冲的传入文本消息的最大长度
        session.setMaxTextMessageBufferSize(maxSize);
        // 设置超时时间
        session.setMaxIdleTimeout(1000*60*30);
        this.session = session;
        //  加入set中
        webSockets.add(this);
        //  连接数加1
        addOnlineCount();
        //  把对应用户id的session放到sessionPool中，用于单点信息发送
        sessionPool.put(userId, session);
        log.info("【websocket消息】 有新连接加入！用户id" + userId + "，当前连接数为" + getOnlineCount());
    }

    /**
     * @方法描述: 关闭socket
     * @return: void
     * @Author: carry
     */
    @OnClose
    public void onClose() {
        webSockets.remove(this);
        subOnlineCount();           //在线数减1
        log.info("【websocket消息】 连接断开！当前连接数为" + getOnlineCount());
    }

    /**
     * @方法描述: 收到客户端消息
     * @return: void
     * @Author: carry
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("【websocket消息】收到客户端消息:" + message);
    }

    /**
     * @方法描述: 广播消息全体发送
     * @return: void
     * @Author: carry
     */
    public void sendAllMessage(String message) {
        for (WebSocketServer webSocket : webSockets) {
            log.info("【websocket消息】广播消息:" + message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @方法描述: 一对一单点消息
     * @return: void
     * @Author: carry
     */
    public void sendOneMessage(String userId, String message) {
        try {
            // 防止推送到客户端的信息太多导致弹窗太快
//            Thread.sleep(500);
            log.info(String.format("【websocket消息】单点消息:用户%s-->%s",userId,message));
            Session session = sessionPool.get(userId);
            if (session != null) {
                // getAsyncRemote是异步发送
                // 加锁防止上一个消息还未发完下一个消息又进入了此方法，防止多线程中同一个session多次被调用报错
                synchronized (session) {
                    session.getAsyncRemote().sendText(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @方法描述: 发生错误时调用
     * @return: void
     * @Author: carry
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("【websocket消息】发生错误");
        error.printStackTrace();
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}