package com.example.auth.controller;

import com.example.auth.dto.MovieSearchResult;
import com.example.auth.entity.ImdbTitleBasics;
import com.example.auth.service.ImdbMovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Controller
public class ImdbMovieController {
    private static final Logger logger = LoggerFactory.getLogger(ImdbMovieController.class);

    @Autowired
    private ImdbMovieService imdbMovieService;

    @GetMapping("/imdb/search")
    public String showSearchPage(Model model) {
      //default values
        model.addAttribute("year", 2000);
        model.addAttribute("genre", "Drama");
        model.addAttribute("minRating", new BigDecimal("7.0"));
        model.addAttribute("minVotes", 1000);
        return "imdb-search";
    }

    @GetMapping("/imdb/results")
    public String searchMovies(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) Integer minVotes,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            Model model
    ) {
        try {
            List<MovieSearchResult> movies = imdbMovieService.searchMovies(year, genre, minRating, minVotes);

            model.addAttribute("movies", movies);
            model.addAttribute("searchPerformed", true);
            model.addAttribute("year", year);
            model.addAttribute("genre", genre);
            model.addAttribute("minRating", minRating);
            model.addAttribute("minVotes", minVotes);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortOrder", sortOrder);

            return "imdb-results";

        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "imdb-results";
        }
    }
}