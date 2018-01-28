package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.model.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {

    /**
     * Case insensitive movie search by title
     * @param title A title to match on
     * @return Optional<Movie> The matched movie
     */
    Optional<Movie> findByTitleIgnoreCase(String title);
}

