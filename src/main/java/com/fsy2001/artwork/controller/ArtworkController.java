package com.fsy2001.artwork.controller;

import com.fsy2001.artwork.model.Artwork;
import com.fsy2001.artwork.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/artwork")
public class ArtworkController {
    private final ArtworkService artworkService;

    @Autowired
    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    /* 浏览，增加访问量 */
    @PreAuthorize("permitAll()")
    @GetMapping("/browse/{id}")
    public Artwork browseArtwork(@PathVariable(value = "id") Integer id, Principal principal) {
        String username = principal == null ? null : principal.getName();
        return artworkService.findArtwork(id, true, username);
    }

    /* 查看，不增加访问量 */
    @PreAuthorize("permitAll()")
    @GetMapping("{id}")
    public Artwork getDetail(@PathVariable(value = "id") Integer id) {
        return artworkService.findArtwork(id, false, null);
    }

    /* 搜索，二选一 */
    @PreAuthorize("permitAll()")
    @GetMapping("/search")
    public List<Artwork> searchArtwork(@RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "author", required = false) String author) {
        boolean isTitle = title != null;
        String query = isTitle ? title : author;
        return artworkService.searchArtwork(query, isTitle);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/top")
    public List<Artwork> findTopView() {
        return artworkService.findTopView();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history")
    public List<Artwork> viewHistory(Principal principal) {
        return artworkService.viewHistory(principal.getName());
    }
}