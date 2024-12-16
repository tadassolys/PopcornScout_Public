package com.example.auth.dto;

import lombok.Data;
import java.math.BigDecimal;

/*
MovieSearchResult klasė yra naudojama kaip konteineris, kuris leidžia tvarkingai saugoti duomenis apie kiekvieną filmą,
gautą iš paieškos rezultatų. Kiekvienas filmas, grąžintas paieškos metu, bus paverstas į MovieSearchResult objektą,
kuriame visi reikalingi duomenys bus sudėti į atitinkamus laukus.
 */
@Data
public class MovieSearchResult {
    private String tconst;
    private String primaryTitle;
    private Integer startYear;
    private String genres;
    private Integer runtimeMinutes;
    private BigDecimal averageRating;
    private Integer numVotes;
    private String titleType;


    // Kontruktorius objektui
    public MovieSearchResult(String tconst, String primaryTitle, Integer startYear,
                             String genres, Integer runtimeMinutes,
                             BigDecimal averageRating, Integer numVotes, String titleType) {
        this.tconst = tconst;
        this.primaryTitle = primaryTitle;
        this.startYear = startYear;
        this.genres = genres;
        this.runtimeMinutes = runtimeMinutes;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
        this.titleType =titleType;
    }
}
