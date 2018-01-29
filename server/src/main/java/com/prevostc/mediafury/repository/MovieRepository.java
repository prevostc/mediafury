package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Repository
@RepositoryRestResource
@CrossOrigin
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {

    Optional<Movie> findByTitleIgnoreCase(String title);

    @RestResource(path = "search", rel = "search")
    public Page findByTitleContainingIgnoreCase(@Param("q") String title, Pageable p);
}

