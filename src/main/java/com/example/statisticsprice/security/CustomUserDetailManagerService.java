package com.example.statisticsprice.security;

import com.example.statisticsprice.entity.UserEntity;
import com.example.statisticsprice.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class CustomUserDetailManagerService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElse(null);
    }

    @Transactional
    public void addUser(String username, String password) {
        userRepo.save(new UserEntity(username, passwordEncoder.encode(password)));
    }

    public boolean isUserExist(String username) {
        return userRepo.findByUsername(username).isPresent();
    }
}
