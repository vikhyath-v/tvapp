package com.tvapp.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tvapp.model.TvShow;
import com.tvapp.model.User;
import com.tvapp.repository.TvShowRepository;
import com.tvapp.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final TvShowRepository tvShowRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, TvShowRepository tvShowRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tvShowRepository = tvShowRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Transactional
    public void addToWatchlist(String username, TvShow show) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if show already exists in database
        TvShow existingShow = tvShowRepository.findByImdbId(show.getImdbId())
            .orElseGet(() -> tvShowRepository.save(show));

        user.addToWatchlist(existingShow);
        userRepository.save(user);
    }
    
    @Transactional
    public void removeFromWatchlist(String username, Long showId) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        tvShowRepository.findById(showId).ifPresent(show -> {
            user.removeFromWatchlist(show);
            userRepository.save(user);
        });
    }
    
    public Set<TvShow> getWatchlist(String username) {
        return findByUsername(username).getWatchlist();
    }
    
    public int getWatchlistCount(String username) {
        return getWatchlist(username).size();
    }
} 