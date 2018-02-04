package com.prevostc.mediafury.service.mapper;

import com.prevostc.mediafury.domain.*;
import com.prevostc.mediafury.service.dto.MoviePersonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MoviePerson and its DTO MoviePersonDTO.
 */
@Mapper(componentModel = "spring", uses = {MovieMapper.class, PersonMapper.class})
public interface MoviePersonMapper extends EntityMapper<MoviePersonDTO, MoviePerson> {

    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "movie.title", target = "movieTitle")
    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "person.name", target = "personName")
    MoviePersonDTO toDto(MoviePerson moviePerson);

    @Mapping(source = "movieId", target = "movie")
    @Mapping(source = "personId", target = "person")
    MoviePerson toEntity(MoviePersonDTO moviePersonDTO);

    default MoviePerson fromId(Long id) {
        if (id == null) {
            return null;
        }
        MoviePerson moviePerson = new MoviePerson();
        moviePerson.setId(id);
        return moviePerson;
    }
}
