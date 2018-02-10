package com.prevostc.mediafury.service.mapper;

import com.prevostc.mediafury.domain.*;
import com.prevostc.mediafury.service.dto.MovieDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Movie and its DTO MovieDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, MoviePersonMapper.class})
public interface MovieMapper extends EntityMapper<MovieDTO, Movie> {

    @Mapping(target = "moviePeople", ignore = true)
    Movie toEntity(MovieDTO movieDTO);

    default Movie fromId(Long id) {
        if (id == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setId(id);
        return movie;
    }
}
