package com.example.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class TMDbMovie {
    private Long id;
    private String title;

    @JsonProperty("poster_path")
    private String posterPath;
    private String overview;

    @JsonProperty("vote_average")
    private double rating;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("genre_ids")
    private List<Integer> genreIds;

    @JsonProperty("imdb_id")
    private String imdbId;

    private String imdbRating;
    private String imdbVotes;

    @JsonProperty("external_ids")
    private ExternalIds externalIds;

    @Data
    public static class ExternalIds {
        @JsonProperty("imdb_id")
        private String imdbId;
    }

    // posteris gaunamas tik panaudojus sita metoda tiesiogiai is modelio
    public String getFullPosterPath() {
      
        if (posterPath != null && !posterPath.isEmpty()) {
            return "https://image.tmdb.org/t/p/w500" + posterPath;
        }
        return null; 
    }

    public String getLargePosterPath() {
        return posterPath != null && !posterPath.isEmpty()
                ? "https://image.tmdb.org/t/p/original" + posterPath
                : null;
    }
}