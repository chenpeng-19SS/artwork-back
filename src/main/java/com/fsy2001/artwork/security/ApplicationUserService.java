package com.fsy2001.artwork.security;

import com.fsy2001.artwork.model.User;
import com.fsy2001.artwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public ApplicationUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String credential)
            throws UsernameNotFoundException {

        /* 判断是用户名还是邮箱 */
        User user = credential.contains("@") ?
                repository.getUserByEmail(credential) :
                repository.getUserByUsername(credential);

        if (user == null)
            throw new UsernameNotFoundException(String.format("Could not find user %s", credential));
        return new ApplicationUserDetail(user);
    }
}
