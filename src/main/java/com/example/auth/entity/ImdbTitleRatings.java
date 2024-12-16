package com.example.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "title_ratings")
@Data
public class ImdbTitleRatings {
    @Id
    private String tconst;
    private BigDecimal averageRating;
    private Integer numVotes;
}
