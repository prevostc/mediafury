package com.prevostc.mediafury.commandline;

import com.prevostc.mediafury.model.Movie;
import com.prevostc.mediafury.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class MovieCommandLineRunner implements CommandLineRunner {

    private final MovieRepository movieRepository;

    public MovieCommandLineRunner(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) {
        // from https://github.com/fivethirtyeight/data/blob/master/bechdel/movies.csv
        Stream.of(
            "Annie Hall",
            "Close Encounters of the Third Kind",
            "Eraserhead",
            "High Anxiety",
            "Julia",
            "Star Wars",
            "Assault on Precinct 13",
            "Family Plot",
            "Logan's Run",
            "Network",
            "Rocky",
            "Silent Movie",
            "Sparkle",
            "Taxi Driver",
            "Barry Lyndon",
            "Jaws",
            "Monty Python and the Holy Grail",
            "The Rocky Horror Picture Show",
            "Black Christmas",
            "Blazing Saddles",
            "The Conversation",
            "The Godfather: Part II",
            "The Texas Chain Saw Massacre",
            "The Towering Inferno",
            "Young Frankenstein",
            "American Graffiti",
            "High Plains Drifter",
            "Sleeper",
            "The Exorcist",
            "The Sting",
            "Pink Flamingos",
            "The Godfather",
            "Escape from the Planet of the Apes",
            "Shaft",
            "Straw Dogs",
            "The French Connection",
            "Beyond the Valley of the Dolls"
        ).forEach((String title) -> {
            movieRepository.save(new Movie(title, "Good movie, but it's not Kung fury"));
        });
        movieRepository.save(new Movie("Kung Fury", "Yeah"));
    }
}