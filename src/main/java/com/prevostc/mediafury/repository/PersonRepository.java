package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.domain.Person;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person>  {

    @Query("select person from Person person left join fetch person.moviePeople moviePeople left join moviePeople.movie movie where person.id =:id")
    Person findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Person> findOneByName(String name);
}
