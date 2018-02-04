package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.domain.Movie;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Movie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select distinct movie from Movie movie left join fetch movie.categories")
    List<Movie> findAllWithEagerRelationships();

    @Query("select movie from Movie movie left join fetch movie.categories where movie.id =:id")
    Movie findOneWithEagerRelationships(@Param("id") Long id);

}
