package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.model.Movie;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MovieRepositoryTest {

    @After
    public void tearDown() throws Exception {
        movieRepository.deleteAll();
    }

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void findByTitleIgnoreCase_shouldBeCaseInsensitive() {
        // Arrange
        movieRepository.save(new Movie("Kung", "Fury"));

        // Act
        Optional<Movie> movie = movieRepository.findByTitleIgnoreCase("kung");

        // Assert
        assertThat(movie.isPresent()).isTrue();
        assertThat(movie.get().getTitle()).isEqualTo("Kung");
    }
}
