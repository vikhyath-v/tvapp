package com.tvapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OmdbResponse {
    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    private String Type;
    private String totalSeasons;
    private String Response;
    private String Error;
} 