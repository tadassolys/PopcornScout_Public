package com.example.auth.entity;

import lombok.Data;

@Data
public class Movie {
    private String title;
    private String genre;
    private double rating;
    private double votes;
    private String releaseYear;
}
