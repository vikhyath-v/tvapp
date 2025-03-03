package com.tvapp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tvapp.model.TvShow;
import com.tvapp.service.TvShowService;

@Controller
@RequestMapping("/shows")
public class TvShowController {
    
    private final TvShowService tvShowService;
    
    public TvShowController(TvShowService tvShowService) {
        this.tvShowService = tvShowService;
    }
    
    @GetMapping
    public String listShows(Model model) {
        model.addAttribute("shows", tvShowService.getAllShows());
        return "shows/list";
    }
    
    @GetMapping("/search")
    public String searchShows(@RequestParam String query, Model model) {
        try {
            if (query.trim().length() < 2) {
                model.addAttribute("error", "Please enter at least 2 characters to search");
                model.addAttribute("shows", tvShowService.getAllShows());
                return "search";
            }

            List<TvShow> results = tvShowService.searchShows(query);
            
            // Save OMDB shows to database before displaying
            results = results.stream()
                .map(show -> {
                    if (show.getId() == null) {
                        return tvShowService.saveShow(show);
                    }
                    return show;
                })
                .collect(Collectors.toList());
            
            model.addAttribute("shows", results);
            model.addAttribute("searchQuery", query);
            
            if (results.isEmpty()) {
                model.addAttribute("message", "No shows found for: " + query);
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("OMDB API Error")) {
                model.addAttribute("error", errorMessage);
            } else {
                model.addAttribute("error", "An error occurred while searching. Please try again.");
            }
            model.addAttribute("shows", tvShowService.getAllShows());
        }
        return "search";
    }
    
    @GetMapping("/{imdbId}")
    public String showDetails(@PathVariable String imdbId, Model model, Authentication authentication) {
        try {
            TvShow show = tvShowService.getShowDetailsByImdbId(imdbId);
            if (show == null) {
                return "redirect:/shows";
            }

            model.addAttribute("show", show);
            
            if (authentication != null) {
                model.addAttribute("username", authentication.getName());
            }

            return "shows/details";
        } catch (Exception e) {
            return "redirect:/shows";
        }
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("show", new TvShow());
        return "shows/form";
    }
    
    @PostMapping("/add")
    public String addShow(@RequestParam String title) {
        tvShowService.addShowFromOmdb(title);
        return "redirect:/shows";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        tvShowService.getShowById(id).ifPresent(show -> model.addAttribute("show", show));
        return "shows/form";
    }
    
    @PostMapping("/{id}/edit")
    public String updateShow(@PathVariable Long id, @ModelAttribute TvShow show) {
        show.setId(id);
        tvShowService.updateShow(show);
        return "redirect:/shows";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteShow(@PathVariable Long id) {
        tvShowService.deleteShow(id);
        return "redirect:/shows";
    }

    @PostMapping("/{id}/rate")
    public String rateShow(@PathVariable Long id, 
                          @RequestParam Integer rating,
                          @RequestParam(required = false) String review,
                          Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        tvShowService.addRating(id, authentication.getName(), rating, review);
        return "redirect:/shows/" + id;
    }
} 