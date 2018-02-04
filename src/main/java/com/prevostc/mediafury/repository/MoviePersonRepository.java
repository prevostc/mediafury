package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.domain.MoviePerson;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MoviePerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoviePersonRepository extends JpaRepository<MoviePerson, Long> {

}
