package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.domain.Person;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person>  {

    Optional<Person> findOneByName(String name);
}
