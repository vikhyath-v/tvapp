package com.tvapp.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvapp.model.TvShow;

@Service
public class OmdbService {
    private static final Logger logger = LoggerFactory.getLogger(OmdbService.class);
    private final String apiKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String OMDB_API_URL = "http://www.omdbapi.com";

    public OmdbService(@Value("${omdb.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        logger.info("Initialized OmdbService with API key: {}", apiKey);
    }

    public List<TvShow> searchShows(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String url = String.format("%s?apikey=%s&s=%s&type=series", OMDB_API_URL, apiKey, encodedQuery);
            logger.debug("Searching shows with URL: {}", url);

            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            List<TvShow> shows = new ArrayList<>();

            if (response == null) {
                logger.error("Received null response from OMDB API");
                throw new RuntimeException("No response from OMDB API");
            }

            logger.debug("OMDB API Response: {}", response.toString());

            if (response.has("Error")) {
                String error = response.get("Error").asText();
                logger.error("OMDB API Error: {}", error);
                throw new RuntimeException("OMDB API Error: " + error);
            }

            if (response.has("Search")) {
                JsonNode searchResults = response.get("Search");
                logger.info("Found {} TV series results for query: {}", searchResults.size(), query);

                for (JsonNode result : searchResults) {
                    String type = result.get("Type").asText();
                    String title = result.get("Title").asText();
                    String year = result.get("Year").asText();
                    logger.debug("Found: {} ({}) - Type: {}", title, year, type);
                    
                    if (!"series".equals(type)) {
                        logger.warn("Skipping non-series result: {} - Type: {}", title, type);
                        continue;
                    }

                    String imdbId = result.get("imdbID").asText();
                    logger.debug("Fetching details for TV series: {} with IMDB ID: {}", title, imdbId);
                    TvShow show = getShowDetails(imdbId);
                    if (show != null) {
                        shows.add(show);
                    }
                }
            }

            return shows;
        } catch (RestClientException e) {
            logger.error("Error connecting to OMDB API", e);
            throw new RuntimeException("Error connecting to OMDB API: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error searching shows", e);
            throw new RuntimeException("Error searching shows: " + e.getMessage());
        }
    }

    public TvShow getShowDetails(String imdbId) {
        try {
            String url = String.format("%s?apikey=%s&i=%s&type=series", OMDB_API_URL, apiKey, imdbId);
            logger.debug("Fetching show details with URL: {}", url);

            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response == null) {
                logger.error("Received null response from OMDB API for IMDB ID: {}", imdbId);
                return null;
            }

            logger.debug("OMDB API Response for {}: {}", imdbId, response.toString());

            if (response.has("Error")) {
                String error = response.get("Error").asText();
                logger.error("OMDB API Error for IMDB ID {}: {}", imdbId, error);
                return null;
            }

            if ("False".equals(response.get("Response").asText())) {
                logger.error("OMDB API returned False response for IMDB ID: {}", imdbId);
                return null;
            }

            TvShow show = new TvShow(
                response.get("Title").asText(),
                response.get("Plot").asText(),
                response.get("Genre").asText(),
                response.get("Year").asText(),
                response.get("Poster").asText(),
                imdbId,
                parseImdbRating(response.get("imdbRating").asText())
            );

            logger.info("Successfully retrieved show details for: {}", show.getTitle());
            return show;

        } catch (RestClientException e) {
            logger.error("Error connecting to OMDB API for IMDB ID: {}", imdbId, e);
            return null;
        } catch (Exception e) {
            logger.error("Error getting show details for IMDB ID: {}", imdbId, e);
            return null;
        }
    }

    private Double parseImdbRating(String rating) {
        try {
            return "N/A".equals(rating) ? null : Double.parseDouble(rating);
        } catch (NumberFormatException e) {
            logger.warn("Invalid IMDB rating format: {}", rating);
            return null;
        }
    }
} 