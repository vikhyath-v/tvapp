package com.tvapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tvapp.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
} 