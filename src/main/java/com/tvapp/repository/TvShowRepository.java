package com.tvapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tvapp.model.TvShow;

@Repository
public interface TvShowRepository extends JpaRepository<TvShow, Long> {
    List<TvShow> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT t FROM TvShow t LEFT JOIN t.ratings r GROUP BY t ORDER BY AVG(r.rating) DESC NULLS LAST")
    List<TvShow> findTop10ByOrderByAverageRatingDesc();
    
    @Query("SELECT t FROM TvShow t LEFT JOIN t.ratings r GROUP BY t ORDER BY COUNT(r) DESC NULLS LAST")
    List<TvShow> findTop10ByOrderByRatingCountDesc();
    
    Optional<TvShow> findByImdbId(String imdbId);
    boolean existsByImdbId(String imdbId);
} 