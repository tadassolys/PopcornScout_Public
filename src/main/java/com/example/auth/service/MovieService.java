package com.example.auth.service;

import com.example.auth.model.TMDbMovie;
import com.example.auth.model.TMDbResponse;
import com.example.auth.model.OMDbMovie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";
    private static final String OMDB_BASE_URL = "http://www.omdbapi.com/";
    private static final int MAX_PAGES = 5;

    public List<TMDbMovie> fetchMovies(String yearFrom, String yearTo, String genre, double rating, double votes) {
        String dateFrom = yearFrom + "-01-01";
        String dateTo = yearTo + "-12-31";
        List<TMDbMovie> allMovies = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        int currentPage = 1;

        try {
            while (currentPage <= MAX_PAGES) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL + "/discover/movie")
                        .queryParam("api_key", tmdbApiKey)
                        .queryParam("primary_release_date.gte", dateFrom)
                        .queryParam("primary_release_date.lte", dateTo)
                        .queryParam("vote_average.gte", rating)
                        .queryParam("vote_count.gte", votes)
                        .queryParam("with_genres", genre)
                        .queryParam("page", currentPage)
                        .queryParam("sort_by", "primary_release_date.desc");

                String url = builder.toUriString();
                logger.info("TMDb API Request URL: {}", url);

                TMDbResponse response = restTemplate.getForObject(url, TMDbResponse.class);
                if (response == null || response.getMovies() == null || response.getMovies().isEmpty()) {
                    break;
                }

                // Fetch IMDb details and ratings for each movie
                for (TMDbMovie movie : response.getMovies()) {
                    // Fetch IMDb ID first
                    String movieDetailsUrl = UriComponentsBuilder.fromHttpUrl(TMDB_BASE_URL + "/movie/" + movie.getId())
                            .queryParam("api_key", tmdbApiKey)
                            .queryParam("append_to_response", "external_ids")
                            .toUriString();

                    TMDbMovie fullMovieDetails = restTemplate.getForObject(movieDetailsUrl, TMDbMovie.class);
                    if (fullMovieDetails != null && fullMovieDetails.getExternalIds() != null) {
                        movie.setImdbId(fullMovieDetails.getExternalIds().getImdbId());

                        // Fetch additional details from OMDb
                        if (movie.getImdbId() != null) {
                            String omdbUrl = UriComponentsBuilder.fromHttpUrl(OMDB_BASE_URL)
                                    .queryParam("apikey", omdbApiKey)
                                    .queryParam("i", movie.getImdbId())
                                    .toUriString();

                            try {
                                OMDbMovie omdbMovie = restTemplate.getForObject(omdbUrl, OMDbMovie.class);
                                if (omdbMovie != null) {
                                    movie.setImdbRating(omdbMovie.getImdbRating());
                                    movie.setImdbVotes(omdbMovie.getImdbVotes());
                                }
                            } catch (RestClientException e) {
                                logger.warn("Could not fetch OMDb details for movie: {}", movie.getTitle());
                            }
                        }
                    }
                }

                allMovies.addAll(response.getMovies());

                if (currentPage >= response.getTotalPages()) {
                    break;
                }
                currentPage++;
            }
            return allMovies;
        } catch (RestClientException e) {
            logger.error("Error fetching movies from TMDb API", e);
            return Collections.emptyList();
        }
    }
}