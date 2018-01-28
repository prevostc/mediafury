package com.prevostc.mediafury.controller;

import com.prevostc.mediafury.repository.MovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.prevostc.mediafury.model.Movie;

import java.util.Optional;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MovieController movieController;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MovieRepository movieRepository;

    @Test
    public void finds_movieByTitle() throws Exception {

        //Arrange
        Optional<Movie> movie = Optional.of(new Movie("Kung", "Fury"));
        when(movieRepository.findByTitleIgnoreCase(anyString())).thenReturn(movie);

        //Act
        ResultActions result = mvc.perform(get("/movie").param("title", "kung"));

        //Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("title").value("Kung"))
                .andExpect(jsonPath("description").value("Fury"));
    }


    @Test
    public void finds_nothing() throws Exception {

        //Arrange
        Optional<Movie> movie = Optional.empty();
        when(movieRepository.findByTitleIgnoreCase(anyString())).thenReturn(movie);

        //Act
        ResultActions result = mvc.perform(get("/movie").param("title", "kung"));

        //Assert
        result.andExpect(status().isOk())
                .andExpect(content().string(is("null")));
    }
}
