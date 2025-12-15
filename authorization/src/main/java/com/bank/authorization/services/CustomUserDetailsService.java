package com.bank.authorization.services;

import com.bank.authorization.entities.User;
import com.bank.authorization.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByProfileId(username);
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getProfileId()),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    private User findByProfileId(String profileId) {
        return userRepository.findByProfileId(Long.parseLong(profileId))
                .orElseThrow(() -> new UsernameNotFoundException(String
                        .format("Пользователь с логином %s не найден", profileId)));
    }
}
