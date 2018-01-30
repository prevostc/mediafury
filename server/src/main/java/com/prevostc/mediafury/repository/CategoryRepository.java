package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.model.Category;
import com.prevostc.mediafury.projection.CategoryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@RepositoryRestResource(excerptProjection = CategoryProjection.class)
@CrossOrigin
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
    @RestResource(path = "search", rel = "search")
    public Page findByNameContainingIgnoreCase(@Param("q") String name, Pageable p);
}

