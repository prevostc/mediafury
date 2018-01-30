package com.prevostc.mediafury.projection;

import com.prevostc.mediafury.model.Category;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "category", types = { Category.class })
public interface CategoryProjection {
    Long getId();
    String getName();
}