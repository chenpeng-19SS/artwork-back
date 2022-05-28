package com.fsy2001.artwork.service;

import com.fsy2001.artwork.exception.WebRequestException;
import com.fsy2001.artwork.model.User;
import com.fsy2001.artwork.repository.FileSystemRepository;
import com.fsy2001.artwork.repository.UserRepository;
import com.fsy2001.artwork.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final FileSystemRepository fileSystemRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder encoder,
                       FileSystemRepository fileSystemRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.fileSystemRepository = fileSystemRepository;
    }

    public void register(User user) {
        /* 检查用户信息 */
        if (userRepository.existsById(user.getUsername()))
            throw new WebRequestException("conflict-username");
        if (userRepository.getUserByEmail(user.getEmail()) != null)
            throw new WebRequestException("conflict-email");


        user.password_DEBUG = user.getPassword(); // FIXME: 密码明码
        user.setRole(UserRole.USER);
        user.setPassword(encoder.encode(user.getPassword())); // 哈希加盐
        user.setBalance(0); // 初始余额
        userRepository.save(user);
    }

    public User getInfo(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new WebRequestException("user-not-exist"));
    }

    public void recharge(Integer val, String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new WebRequestException("user-not-exist"));
        user.addBalance(val);
        userRepository.save(user);
    }

    public void setAvatar(MultipartFile avatar, String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new WebRequestException("user-not-exist"));

        try {
            /* 存储文件 */
            String filename = avatar.getOriginalFilename();
            if (filename == null)
                throw new WebRequestException("illegal-filename");
             String extension = Objects.requireNonNull(avatar.getContentType()).split("/")[1]; // 从 MIME 获取扩展名，例：image/png
             String path = fileSystemRepository.save(avatar.getBytes(), username + "." + extension); // 存储图片
            if (user.getImg().startsWith("image/")) { // 删除旧头像
                fileSystemRepository.delete(user.getImg());
            }

             /* 修改用户头像路径 */
             user.setImg(path);
             userRepository.save(user);

        } catch (IOException e) {
            throw new WebRequestException("io-exception");
        }
    }
}
