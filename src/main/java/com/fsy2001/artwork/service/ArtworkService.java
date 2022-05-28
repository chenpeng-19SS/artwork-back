package com.fsy2001.artwork.service;

import com.fsy2001.artwork.exception.WebRequestException;
import com.fsy2001.artwork.model.Artwork;
import com.fsy2001.artwork.model.BrowseHistory;
import com.fsy2001.artwork.model.User;
import com.fsy2001.artwork.repository.ArtworkRepository;
import com.fsy2001.artwork.repository.BrowseHistoryRepository;
import com.fsy2001.artwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtworkService {
    private final ArtworkRepository artworkRepository;
    private final BrowseHistoryRepository browseHistoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArtworkService(ArtworkRepository artworkRepository, BrowseHistoryRepository browseHistoryRepository, UserRepository userRepository) {
        this.artworkRepository = artworkRepository;
        this.browseHistoryRepository = browseHistoryRepository;
        this.userRepository = userRepository;
    }


    public Artwork findArtwork(Integer id, boolean browse, String username) { // 根据ID查找
        Artwork artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new WebRequestException("artwork-not-exist"));
        if (browse) { // 访问量增加
            artwork.addView();
            artworkRepository.save(artwork);
            if (username != null) updateBrowseHistory(username, artwork);
        }

        return artwork;
    }

    private void updateBrowseHistory(String username, Artwork artwork) { // 统计并更新浏览记录
        User user = userRepository.findById(username)
                .orElseThrow(() -> new WebRequestException("user-not-exist"));
        BrowseHistory currentRecord = new BrowseHistory(user, artwork);
        List<BrowseHistory> historyList = browseHistoryRepository.findByUserOrderByDate(user);

        for (BrowseHistory history : historyList) {
            if (history.equals(currentRecord)) { // 最近记录里有，则更新时间
                browseHistoryRepository.delete(history);
                browseHistoryRepository.save(currentRecord);
                break;
            }
        }

        // 最近记录里没有，则插入
        browseHistoryRepository.save(currentRecord);

        // 如果超出5个，则删除最早一个
        if (historyList.size() >= 5)
            browseHistoryRepository.delete(historyList.get(0)); // FIXME: 这是最早的一个吗
    }

    public List<Artwork> searchArtwork(String query, boolean isTitle) { // 根据作者或标题查找
        String fuzzyQuery = "%" + query + "%";
        return isTitle ? artworkRepository.findByTitleLike(fuzzyQuery) : artworkRepository.findByAuthorLike(fuzzyQuery);
    }

    public List<Artwork> findTopView() { // 查询访问量最高的3个艺术品
        return artworkRepository.findFirst3ByOrderByView();
    }

    public List<Artwork> viewHistory(String username) { // 查询用户最近浏览的艺术品
        User user = userRepository.findById(username)
                .orElseThrow(() -> new WebRequestException("user-not-exist"));
        List<BrowseHistory> histories = browseHistoryRepository.findByUserOrderByDate(user);
        return histories.stream().map(BrowseHistory::getArtwork).collect(Collectors.toList());
    }
}