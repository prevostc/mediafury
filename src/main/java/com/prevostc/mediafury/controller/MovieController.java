package com.prevostc.mediafury.controller;

import com.prevostc.mediafury.model.Movie;
import com.prevostc.mediafury.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MovieController {

    private MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("movie")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Optional<Movie> search(@RequestParam(value="title") Optional<String> title) {
        Optional<Movie> res;
        if (title.isPresent()) {
            res = movieRepository.findByTitleIgnoreCase(title.get());
        } else {
            res = Optional.empty();
        }
        return res;
    }

}
