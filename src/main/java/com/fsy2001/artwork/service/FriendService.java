package com.fsy2001.artwork.service;

import com.fsy2001.artwork.exception.WebRequestException;
import com.fsy2001.artwork.model.Friend;
import com.fsy2001.artwork.model.Message;
import com.fsy2001.artwork.model.User;
import com.fsy2001.artwork.model.support.FriendDisplay;
import com.fsy2001.artwork.repository.FriendRepository;
import com.fsy2001.artwork.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository, MessageService messageService) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    public List<String> getFriendApplication(String username) { // 获取用户的好友申请
        return friendRepository.findBySelfAndApproved(username, false)
                .stream()
                .map(Friend::getFriend)
                .collect(Collectors.toList());
    }

    public List<FriendDisplay> getFriends(String username) { // 获取用户的好友
        return friendRepository.findBySelfAndApproved(username, true)
                .stream()
                .map(Friend::getFriend)
                .map(name -> {
                    /* 获取最近消息 */
                    List<Message> messageList = messageService.getMessageList(username, name);
                    String recentMessage = "";
                    if (!messageList.isEmpty()) {
                        recentMessage = messageList.get(messageList.size() - 1).getContent();
                        if (recentMessage.length() > 5)
                            recentMessage = recentMessage.substring(0, 5) + "...";
                    }

                    User user = userRepository.getUserByUsername(name);
                    return new FriendDisplay(name, user.getImg(), recentMessage);
                })
                .collect(Collectors.toList());
    }

    public void applyFriend(String username, String friend) { // 提交好友申请
        if (friend.equals(username))
            throw new WebRequestException("error");
        if (!userRepository.existsById(friend))
            throw new WebRequestException("user-not-exist");
        if (friendRepository.findBySelfAndFriend(friend, username) != null
                || friendRepository.findBySelfAndFriend(username, friend) != null)
            throw new WebRequestException("duplicate-application");

        Friend application = new Friend(friend, username, false);
        friendRepository.save(application);
    }

    public void acceptFriend(String username, String friend) { // 接受好友申请
        Friend application = friendRepository.findBySelfAndFriend(username, friend);
        if (application == null) throw new WebRequestException("application-not-exist");
        if (application.isApproved()) throw new WebRequestException("application-already-approved");

        application.setApproved(true);
        Friend dual = new Friend(friend, username, true);

        friendRepository.save(application);
        friendRepository.save(dual);
    }
}