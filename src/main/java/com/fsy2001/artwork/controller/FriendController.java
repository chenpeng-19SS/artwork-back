package com.fsy2001.artwork.controller;

import com.fsy2001.artwork.model.support.FriendDisplay;
import com.fsy2001.artwork.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/friend")
public class FriendController {
    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<FriendDisplay> getFriends(Principal principal) {
        return friendService.getFriends(principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/apply")
    public List<String> getApplications(Principal principal) {
        return friendService.getFriendApplication(principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public void addFriend(@RequestParam(value = "friend") String friend,
                          Principal principal) {
        friendService.applyFriend(principal.getName(), friend);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/accept")
    public void acceptFriend(@RequestParam(value = "friend") String friend,
                             Principal principal) {
        friendService.acceptFriend(principal.getName(), friend);
    }
}