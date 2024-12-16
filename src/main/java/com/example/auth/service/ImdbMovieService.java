package com.example.auth.service;

import com.example.auth.dto.MovieSearchResult;
import com.example.auth.entity.ImdbTitleBasics;
import com.example.auth.entity.ImdbTitleRatings;
import com.example.auth.repository.ImdbTitleBasicsRepository;
import com.example.auth.repository.ImdbTitleRatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImdbMovieService {
    @Autowired
    private ImdbTitleBasicsRepository imdbTitleBasicsRepository;

    @Autowired
    private ImdbTitleRatingsRepository imdbTitleRatingsRepository;

    @Transactional(readOnly = true)
    public List<MovieSearchResult> searchMovies(Integer year, String genre, BigDecimal minRating, Integer minVotes) {
        /*
        Ši užklausa grąžina visus įrašus iš ImdbTitleRatings, kurių:
        Vidutinis įvertinimas (averageRating) yra didesnis nei minRating.
        Balsų skaičius (numVotes) yra didesnis nei minVotes.
         */
        List<ImdbTitleRatings> ratings = imdbTitleRatingsRepository
                .findByAverageRatingGreaterThanAndNumVotesGreaterThan(minRating, minVotes);

        //Sukuria (Map)  paieškai, naudojant tconst id pasiekti objekta.
        Map<String, ImdbTitleRatings> ratingsMap = ratings.stream()
                .collect(Collectors.toMap(ImdbTitleRatings::getTconst, rating -> rating));

        /*
        Iš ImdbTitleBasics lentelės paimami filmai, kurie atitinka šiuos kriterijus:
        startYear: Filmo išleidimo metai sutampa su year.
        genres: Filmo žanrai turi būti susiję su genre.
        tconst: Filmo ID yra tarp ID sąrašo, kuris buvo sugeneruotas iš ratingsMap.
         */
        List<ImdbTitleBasics> movies = imdbTitleBasicsRepository
                .findByStartYearAndGenresContainingAndTconstIn(
                        year, genre, new ArrayList<>(ratingsMap.keySet()));

       /*
       Naudojant stream, kiekvienas filmas sujungiamas su jo įvertinimų duomenimis:
        Iš ratingsMap pagal filmo tconst paimami atitinkami įvertinimai.
        Kuriamas naujas MovieSearchResult (DTO) objektas, kuris sujungia abu duomenų šaltinius.
        */
        return movies.stream()
                .map(movie -> {
                    ImdbTitleRatings rating = ratingsMap.get(movie.getTconst());
                    return new MovieSearchResult(
                            movie.getTconst(),
                            movie.getPrimaryTitle(),
                            movie.getStartYear(),
                            movie.getGenres(),
                            movie.getRuntimeMinutes(),
                            rating.getAverageRating(),
                            rating.getNumVotes(),
                            movie.getTitleType()
                    );
                })
                .collect(Collectors.toList());
    }
}