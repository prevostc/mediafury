package com.prevostc.mediafury.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.prevostc.mediafury.domain.Movie;
import com.prevostc.mediafury.domain.*; // for static metamodels
import com.prevostc.mediafury.repository.MovieRepository;
import com.prevostc.mediafury.service.dto.MovieCriteria;

import com.prevostc.mediafury.service.dto.MovieDTO;
import com.prevostc.mediafury.service.mapper.MovieMapper;

/**
 * Service for executing complex queries for Movie entities in the database.
 * The main input is a {@link MovieCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MovieDTO} or a {@link Page} of {@link MovieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovieQueryService extends QueryService<Movie> {

    private final Logger log = LoggerFactory.getLogger(MovieQueryService.class);


    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    public MovieQueryService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    /**
     * Return a {@link List} of {@link MovieDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MovieDTO> findByCriteria(MovieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Movie> specification = createSpecification(criteria);
        return movieMapper.toDto(movieRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MovieDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MovieDTO> findByCriteria(MovieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Movie> specification = createSpecification(criteria);
        final Page<Movie> result = movieRepository.findAll(specification, page);
        return result.map(movieMapper::toDto);
    }

    /**
     * Function to convert MovieCriteria to a {@link Specifications}
     */
    private Specifications<Movie> createSpecification(MovieCriteria criteria) {
        Specifications<Movie> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Movie_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Movie_.title));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), Movie_.year));
            }
            if (criteria.getPlot() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlot(), Movie_.plot));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), Movie_.imageUrl));
            }
            if (criteria.getElo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getElo(), Movie_.elo));
            }
            if (criteria.getMoviePersonId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMoviePersonId(), Movie_.moviePeople, MoviePerson_.id));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCategoryId(), Movie_.categories, Category_.id));
            }
        }
        return specification;
    }

}
