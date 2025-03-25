  # Movie Database Application
Try at http://popcorn.toshibadatacenter.site/

This project is a Spring Boot application that allows users to search for movies using two different data sources:

1.  **IMDb Data:** Search through a local PostgreSQL database populated with IMDb data (from IMDb Non-Commercial Datasets found online).
2.  **TMDb API:** Fetch movie data directly from the The Movie Database (TMDb) API and enhance it with data from the Open Movie Database (OMDb) API.

This application use Spring Boot, database interaction with JPA, API consumption, and Thymeleaf templating.


https://github.com/user-attachments/assets/71a752d3-6577-49e9-b2a5-3b202fcf4c9f





## Features

*   **User Authentication:** Secure registration and login functionality.
*   **IMDb Movie Search:**
    *   Search for movies by year, genre, minimum rating, and minimum votes.
    *   Sort search results by different columns.
    *   Displays search results in a user-friendly table with sort functionality
*   **TMDb Movie Search:**
    *   Search for movies by release year range, genre (using genre IDs), minimum rating, and minimum vote count.
    *   Displays movie details including poster, release date, ratings, and links.
    *  Fetching data from two external API's TMDb, and OMDb.
*   **Responsive Design:** UI components are responsive to different screen sizes.
*   **Interactive UI:**  Use of JavaScript for sorting tables client-side and displaying movie details on modal.
*   **Dockerized Application:** Easily deployable with Docker.

## Technologies Used

*   **Backend:**
    *   Java 17
    *   Spring Boot
    *   Spring Security
    *   Spring Data JPA
    *   PostgreSQL
    *   RestTemplate
    *   Lombok
    *   Thymeleaf
*   **Frontend:**
    *   HTML
    *   CSS
    *   JavaScript
*   **Build & Deployment:**
    *   Maven
    *   Docker
    *   Docker Compose

## Setup and Installation

### Prerequisites

*   Java Development Kit (JDK) 17 or higher
*   Maven
*   Docker and Docker Compose (optional, for containerized deployment)
*   PostgreSQL Database

### Steps

1.  **Clone the Repository:**
2.  
3.  **Create the database:**
    * Make sure you have PostgreSQL installed locally and that it is running.
     *  Create a database named `user_registration`. You can run the following SQL command in your database:
        ```sql
           CREATE DATABASE user_registration;
        ```
4.  **Configure Database:**
    *   Navigate to `src/main/resources` and update `application.properties` with your PostgreSQL username, password and other database connection properties.
    *  Add TMDb and OMDb api keys in this file too.
    *  Example:
    *  spring.application.name=auth
    *  spring.datasource.url=jdbc:postgresql://localhost:5432/user_registration
    *  spring.datasource.username=
    *  spring.datasource.password=
    *  spring.jpa.hibernate.ddl-auto=update
    *  spring.jpa.show-sql=true
    *  tmdb.api.key=
    *  omdb.api.key=
5.  **Import the IMDb database**
    * Download [this file](https://datasets.imdbws.com/title.basics.tsv.gz), unzip it and name it `title.basics.tsv`.
    * Download [this file](https://datasets.imdbws.com/title.ratings.tsv.gz), unzip it and name it `title.ratings.tsv`.
    *  Import both files into database using SQL queries, here are examples:
     ```sql
        CREATE TABLE title_basics (
            tconst VARCHAR(20) PRIMARY KEY,
            titleType VARCHAR(20),
            primaryTitle TEXT,
            originalTitle TEXT,
            isAdult BOOLEAN,
            startYear INTEGER,
            endYear INTEGER,
            runtimeMinutes INTEGER,
            genres VARCHAR(200)
        );

        COPY title_basics FROM '/path_to_your/title.basics.tsv' DELIMITER E'\t' CSV HEADER;

        CREATE TABLE title_ratings (
            tconst VARCHAR(20) PRIMARY KEY,
            averageRating DECIMAL(3,1),
            numVotes INTEGER
        );

        COPY title_ratings FROM '/path_to_your/title.ratings.tsv' DELIMITER E'\t' CSV HEADER;
     ```
6.  **Build the Application:**
    ```bash
    mvn clean install
    ```
7.  **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will be available at `http://localhost:8080`.

### Running with Docker

1.  **Build the Docker Image:**
    ```bash
    docker build -t movie-app .
    ```
2.  **Run with Docker Compose:**
    ```bash
    docker-compose up --build
    ```
   The application will be available at `http://localhost:8080`.
    * You'll need a SQL file (`init.sql`) that contains the necessary SQL commands import data into it (only import data, tables will be created from SpringBoot). This file should be placed in same directory where project is.
    *   You will also need command to import data. Remember that the path in command is from the database container's perspective:
                  psql -U postgres -d user_registration -f /docker-entrypoint-initdb.d/init.sql


## Usage

1.  **Register/Login:**
    *   Visit `/register` to create a new user account.
    *   Visit `/login` to log in.

2.  **Movie Searches:**

    *   After log in, use the main menu to navigate to desired search function.
    *   For IMDb movie search, you will be redirected to `/imdb/search`, where you will fill required input fields and get table with results after submitting.
    *   For TMDB API movie search, you will be redirected to `/movies` and enter search data on the form provided, results will be displayed bellow.
