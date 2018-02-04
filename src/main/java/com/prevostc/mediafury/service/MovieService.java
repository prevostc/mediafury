package com.prevostc.mediafury.service;

import com.prevostc.mediafury.domain.Movie;
import com.prevostc.mediafury.repository.MovieRepository;
import com.prevostc.mediafury.service.dto.MovieDTO;
import com.prevostc.mediafury.service.mapper.MovieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Movie.
 */
@Service
@Transactional
public class MovieService {

    private final Logger log = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    /**
     * Save a movie.
     *
     * @param movieDTO the entity to save
     * @return the persisted entity
     */
    public MovieDTO save(MovieDTO movieDTO) {
        log.debug("Request to save Movie : {}", movieDTO);
        Movie movie = movieMapper.toEntity(movieDTO);
        movie = movieRepository.save(movie);
        return movieMapper.toDto(movie);
    }

    /**
     * Get all the movies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MovieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Movies");
        return movieRepository.findAll(pageable)
            .map(movieMapper::toDto);
    }

    /**
     * Get one movie by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MovieDTO findOne(Long id) {
        log.debug("Request to get Movie : {}", id);
        Movie movie = movieRepository.findOneWithEagerRelationships(id);
        return movieMapper.toDto(movie);
    }

    /**
     * Delete the movie by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Movie : {}", id);
        movieRepository.delete(id);
    }
}