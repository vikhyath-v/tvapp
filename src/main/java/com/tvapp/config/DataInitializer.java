package com.tvapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tvapp.model.Rating;
import com.tvapp.model.TvShow;
import com.tvapp.model.User;
import com.tvapp.repository.RatingRepository;
import com.tvapp.repository.TvShowRepository;
import com.tvapp.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            TvShowRepository tvShowRepository,
            UserRepository userRepository,
            RatingRepository ratingRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Only initialize if the database is empty
            if (userRepository.count() == 0 && tvShowRepository.count() == 0) {
                // Create sample users
                User user1 = new User();
                user1.setUsername("john_doe");
                user1.setEmail("john@example.com");
                user1.setPassword(passwordEncoder.encode("password123"));
                userRepository.save(user1);

                User user2 = new User();
                user2.setUsername("jane_smith");
                user2.setEmail("jane@example.com");
                user2.setPassword(passwordEncoder.encode("password123"));
                userRepository.save(user2);

                // Create sample TV shows
                TvShow show1 = new TvShow(
                    "Breaking Bad",
                    "A high school chemistry teacher turned methamphetamine manufacturer partners with a former student to secure his family's financial future as he battles terminal lung cancer.",
                    "Crime, Drama, Thriller",
                    "2008",
                    "https://m.media-amazon.com/images/M/MV5BYmQ4YWMxYjUtNjZmYi00MDQ1LWFjMjMtNjA5ZDdiYjdiODU5XkEyXkFqcGdeQXVyMTMzNDExODE5._V1_.jpg",
                    "tt0903747",
                    9.5
                );
                tvShowRepository.save(show1);

                TvShow show2 = new TvShow(
                    "Stranger Things",
                    "When a young boy disappears, his mother, a police chief and his friends must confront terrifying supernatural forces in order to get him back.",
                    "Drama, Fantasy, Horror",
                    "2016",
                    "https://m.media-amazon.com/images/M/MV5BMDZkYmVhNjMtNWU4MC00MDQxLWE3MjYtZGMzZWI1ZjhlOWJmXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg",
                    "tt4574334",
                    8.7
                );
                tvShowRepository.save(show2);

                TvShow show3 = new TvShow(
                    "The Crown",
                    "Follows the political rivalries and romance of Queen Elizabeth II's reign and the events that shaped the second half of the twentieth century.",
                    "Biography, Drama, History",
                    "2016",
                    "https://m.media-amazon.com/images/M/MV5BZmY0MzBlNjctNTRmNy00Njk3LWFjMzctMWQwZDAwMGJmY2MyXkEyXkFqcGdeQXVyMDM2NDM2MQ@@._V1_.jpg",
                    "tt4786824",
                    8.7
                );
                tvShowRepository.save(show3);

                // Add sample ratings
                Rating rating1 = new Rating();
                rating1.setUser(user1);
                rating1.setShow(show1);
                rating1.setRating(5);
                rating1.setReview("One of the best shows ever made!");
                ratingRepository.save(rating1);

                Rating rating2 = new Rating();
                rating2.setUser(user2);
                rating2.setShow(show1);
                rating2.setRating(5);
                rating2.setReview("Absolutely brilliant!");
                ratingRepository.save(rating2);

                Rating rating3 = new Rating();
                rating3.setUser(user1);
                rating3.setShow(show2);
                rating3.setRating(4);
                rating3.setReview("Great show with amazing characters!");
                ratingRepository.save(rating3);

                Rating rating4 = new Rating();
                rating4.setUser(user2);
                rating4.setShow(show2);
                rating4.setRating(4);
                rating4.setReview("Very entertaining!");
                ratingRepository.save(rating4);

                Rating rating5 = new Rating();
                rating5.setUser(user1);
                rating5.setShow(show3);
                rating5.setRating(4);
                rating5.setReview("Excellent production value!");
                ratingRepository.save(rating5);
            }
        };
    }
} 