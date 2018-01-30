package com.prevostc.mediafury.projection;

import com.prevostc.mediafury.model.Category;
import com.prevostc.mediafury.model.Movie;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "categories", types = { Movie.class })
public interface MovieProjection {
    Long getId();
    String getTitle();
    String getDescription();
    Set<Category> getCategories();
}