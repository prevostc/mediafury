package com.prevostc.mediafury.controller;

import com.prevostc.mediafury.model.Movie;
import com.prevostc.mediafury.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private MovieRepository movieRepository;

    public AdminController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @RequestMapping(value = "/movies", method = RequestMethod.GET, produces = "application/json")
    public Page<Movie> listAll() {
        return movieRepository.findAll(new PageRequest(0, 10));
    }
}