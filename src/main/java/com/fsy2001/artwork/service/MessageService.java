package com.fsy2001.artwork.service;

import com.fsy2001.artwork.exception.WebRequestException;
import com.fsy2001.artwork.model.Message;
import com.fsy2001.artwork.model.User;
import com.fsy2001.artwork.repository.MessageRepository;
import com.fsy2001.artwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<Message> getMessageList(String selfName, String friendName) {

        User self = userRepository.findById(selfName)
                .orElseThrow(() -> new WebRequestException("user-not-exist"));
        User friend = userRepository.findById(friendName)
                .orElseThrow(() -> new WebRequestException("user-not-exist"));

        /* 发送和接受的消息 */
        List<Message> messageList = new LinkedList<>();
        messageList.addAll(messageRepository.findByFromAndTo(self, friend));
        messageList.addAll(messageRepository.findByFromAndTo(friend, self));

        messageList.sort(Comparator.comparing(Message::getDate)); // 排序
        return messageList;
    }

    public void sendMessage(String selfName, String friendName, String content) {

        User self = userRepository.findById(selfName)
                .orElseThrow(() -> new WebRequestException("user-not-exist"));
        User friend = userRepository.findById(friendName)
                .orElseThrow(() -> new WebRequestException("user-not-exist"));

        Message message = new Message(self, friend, content, new Date()); // 注入发送时间
        messageRepository.save(message);
    }
}