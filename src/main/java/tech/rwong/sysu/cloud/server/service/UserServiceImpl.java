package tech.rwong.sysu.cloud.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.rwong.sysu.cloud.server.model.User;
import tech.rwong.sysu.cloud.server.repository.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(name);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        return user;
    }
}
