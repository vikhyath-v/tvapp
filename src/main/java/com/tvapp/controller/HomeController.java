package com.tvapp.controller;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tvapp.model.TvShow;
import com.tvapp.service.OmdbService;
import com.tvapp.service.TvShowService;
import com.tvapp.service.UserService;

@Controller
public class HomeController {
    private final OmdbService omdbService;
    private final UserService userService;
    private final TvShowService tvShowService;

    public HomeController(OmdbService omdbService, UserService userService, TvShowService tvShowService) {
        this.omdbService = omdbService;
        this.userService = userService;
        this.tvShowService = tvShowService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            model.addAttribute("watchlistCount", userService.getWatchlistCount(username));
            
            // Add trending and popular shows for logged-in users
            model.addAttribute("trendingShows", tvShowService.getTrendingShows());
            model.addAttribute("popularShows", tvShowService.getPopularShows());
        }
        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Set<TvShow> watchlist = userService.getWatchlist(username);

        model.addAttribute("username", username);
        model.addAttribute("watchlist", watchlist);
        model.addAttribute("watchlistCount", watchlist.size());

        return "profile";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model, Authentication authentication) {
        List<TvShow> searchResults = omdbService.searchShows(query);
        model.addAttribute("shows", searchResults);
        
        if (authentication != null) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            model.addAttribute("watchlistCount", userService.getWatchlistCount(username));
        }
        
        return "search";
    }

    @PostMapping("/watchlist/add")
    public String addToWatchlist(@RequestParam String imdbId, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        TvShow show = omdbService.getShowDetails(imdbId);
        if (show != null) {
            userService.addToWatchlist(username, show);
        }

        return "redirect:/profile";
    }

    @PostMapping("/watchlist/remove")
    public String removeFromWatchlist(@RequestParam Long showId, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        userService.removeFromWatchlist(username, showId);

        return "redirect:/profile";
    }
} 