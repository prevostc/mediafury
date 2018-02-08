package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.domain.MoviePerson;
import com.prevostc.mediafury.domain.enumeration.PersonRole;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the MoviePerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoviePersonRepository extends JpaRepository<MoviePerson, Long>, JpaSpecificationExecutor<MoviePerson> {
    Optional<MoviePerson> findOneByRoleAndMovieIdAndPersonId(PersonRole role, Long movieId, Long personId);
}
