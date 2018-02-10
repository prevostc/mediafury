package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Movie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie>  {
    @Query("select distinct movie from Movie movie left join fetch movie.categories")
    List<Movie> findAllWithEagerRelationships();

    @Query("select movie from Movie movie left join fetch movie.categories categories left join fetch movie.moviePeople moviePeople left join fetch moviePeople.person where movie.id =:id")
    Movie findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Movie> findOneByTitleAndYear(String title, Integer year);

    Page<Movie> findAllByImageUrlNotNull(Pageable pageable);

    Long countByImageUrlNotNull();
}
