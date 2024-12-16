package com.example.auth.repository;


import com.example.auth.entity.ImdbTitleBasics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImdbTitleBasicsRepository extends JpaRepository<ImdbTitleBasics, String> {
    List<ImdbTitleBasics> findByStartYearAndGenresContainingAndTconstIn(
            Integer startYear, String genre, List<String> tconsts
    );
}
