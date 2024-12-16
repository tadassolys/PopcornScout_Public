package com.example.auth.repository;

import com.example.auth.entity.ImdbTitleRatings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ImdbTitleRatingsRepository extends JpaRepository<ImdbTitleRatings, String> {
    List<ImdbTitleRatings> findByAverageRatingGreaterThanAndNumVotesGreaterThan(
            BigDecimal averageRating, Integer numVotes
    );
}
