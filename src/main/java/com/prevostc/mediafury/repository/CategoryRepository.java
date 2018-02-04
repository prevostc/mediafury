package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.domain.Category;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findOneByName(String name);
}
