package com.tvapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tvapp.model.Rating;
import com.tvapp.model.TvShow;
import com.tvapp.model.User;
import com.tvapp.repository.RatingRepository;
import com.tvapp.repository.TvShowRepository;
import com.tvapp.repository.UserRepository;

@Service
public class TvShowService {
    
    private final TvShowRepository tvShowRepository;
    private final OmdbService omdbService;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    
    public TvShowService(TvShowRepository tvShowRepository, OmdbService omdbService, UserRepository userRepository, RatingRepository ratingRepository) {
        this.tvShowRepository = tvShowRepository;
        this.omdbService = omdbService;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }
    
    @Transactional
    public TvShow addShowFromOmdb(String title) {
        List<TvShow> searchResults = omdbService.searchShows(title);
        if (!searchResults.isEmpty()) {
            TvShow show = searchResults.get(0);
            return tvShowRepository.findByImdbId(show.getImdbId())
                .orElseGet(() -> tvShowRepository.save(show));
        }
        return null;
    }
    
    public List<TvShow> getAllShows() {
        return tvShowRepository.findAll();
    }
    
    public Optional<TvShow> getShowById(Long id) {
        return tvShowRepository.findById(id);
    }
    
    public List<TvShow> searchShows(String query) {
        // Search directly in OMDB API
        return omdbService.searchShows(query);
    }
    
    public TvShow getShowDetailsByImdbId(String imdbId) {
        return omdbService.getShowDetails(imdbId);
    }
    
    @Transactional
    public TvShow updateShow(TvShow show) {
        return tvShowRepository.save(show);
    }
    
    @Transactional
    public TvShow saveShow(TvShow show) {
        return tvShowRepository.findByImdbId(show.getImdbId())
            .orElseGet(() -> tvShowRepository.save(show));
    }
    
    public void deleteShow(Long id) {
        tvShowRepository.deleteById(id);
    }
    
    public List<TvShow> getTrendingShows() {
        // For now, return shows with highest ratings
        return tvShowRepository.findTop10ByOrderByAverageRatingDesc();
    }
    
    public List<TvShow> getPopularShows() {
        // For now, return shows with most ratings
        return tvShowRepository.findTop10ByOrderByRatingCountDesc();
    }
    
    @Transactional
    public void addRating(Long showId, String username, Integer rating, String review) {
        TvShow show = tvShowRepository.findById(showId)
            .orElseThrow(() -> new RuntimeException("Show not found"));
            
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        Rating newRating = new Rating();
        newRating.setShow(show);
        newRating.setUser(user);
        newRating.setRating(rating);
        newRating.setReview(review);
        newRating.setCreatedAt(LocalDateTime.now());
        
        ratingRepository.save(newRating);
    }
} 