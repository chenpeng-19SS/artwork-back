package com.fsy2001.artwork.controller;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/api/chat")
public class ChatServer {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>(); // 记录所有的Session

    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(Session session, String content) { // 发送消息
        sessions.forEach((id, targetSession) -> {
            try {
                targetSession.getBasicRemote().sendText(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
