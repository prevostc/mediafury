package com.prevostc.mediafury.commandline;

import com.prevostc.mediafury.model.Movie;
import com.prevostc.mediafury.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MovieCommandLineRunner implements CommandLineRunner {

    private final MovieRepository movieRepository;

    public MovieCommandLineRunner(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) {
        movieRepository.save(new Movie("Kung", "Fury"));
    }
}