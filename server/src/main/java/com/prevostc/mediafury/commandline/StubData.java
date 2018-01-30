package com.prevostc.mediafury.commandline;

import com.prevostc.mediafury.model.Movie;
import com.prevostc.mediafury.model.Category;
import com.prevostc.mediafury.repository.CategoryRepository;
import com.prevostc.mediafury.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class StubData implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;

    public StubData(MovieRepository movieRepository, CategoryRepository categoryRepository) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        Category awesomeCategory = new Category("Awesome");
        Category oldCategory = new Category("Before 1990");
        Category old2Category = new Category("Before 2000");
        categoryRepository.save(awesomeCategory);
        categoryRepository.save(oldCategory);
        categoryRepository.save(old2Category);

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
            Movie movie = new Movie(title, "Good movie, but it's not Kung fury");
            movie.addCategory(oldCategory);
            movie.addCategory(old2Category);
            movieRepository.save(movie);
        });
        Movie movie = new Movie("Kung Fury", "Yeah");
        movie.addCategory(awesomeCategory);
        movieRepository.save(movie);
    }
}