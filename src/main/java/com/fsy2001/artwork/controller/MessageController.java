package com.fsy2001.artwork.controller;

import com.fsy2001.artwork.model.Message;
import com.fsy2001.artwork.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /* 获取私信列表 */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Message> getMessageList
            (@RequestParam(value = "friend") String friend, Principal principal) {
        return messageService.getMessageList(principal.getName(), friend);
    }

    /* 发私信 */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public void sendMessage(@RequestParam(value = "friend") String friend,
                            @RequestBody String content,
                            Principal principal) {
        messageService.sendMessage(principal.getName(), friend, content);
    }
}