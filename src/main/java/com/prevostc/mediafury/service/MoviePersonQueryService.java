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

import com.prevostc.mediafury.domain.MoviePerson;
import com.prevostc.mediafury.domain.*; // for static metamodels
import com.prevostc.mediafury.repository.MoviePersonRepository;
import com.prevostc.mediafury.service.dto.MoviePersonCriteria;

import com.prevostc.mediafury.service.dto.MoviePersonDTO;
import com.prevostc.mediafury.service.mapper.MoviePersonMapper;
import com.prevostc.mediafury.domain.enumeration.PersonRole;

/**
 * Service for executing complex queries for MoviePerson entities in the database.
 * The main input is a {@link MoviePersonCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MoviePersonDTO} or a {@link Page} of {@link MoviePersonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MoviePersonQueryService extends QueryService<MoviePerson> {

    private final Logger log = LoggerFactory.getLogger(MoviePersonQueryService.class);


    private final MoviePersonRepository moviePersonRepository;

    private final MoviePersonMapper moviePersonMapper;

    public MoviePersonQueryService(MoviePersonRepository moviePersonRepository, MoviePersonMapper moviePersonMapper) {
        this.moviePersonRepository = moviePersonRepository;
        this.moviePersonMapper = moviePersonMapper;
    }

    /**
     * Return a {@link List} of {@link MoviePersonDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MoviePersonDTO> findByCriteria(MoviePersonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MoviePerson> specification = createSpecification(criteria);
        return moviePersonMapper.toDto(moviePersonRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MoviePersonDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MoviePersonDTO> findByCriteria(MoviePersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MoviePerson> specification = createSpecification(criteria);
        final Page<MoviePerson> result = moviePersonRepository.findAll(specification, page);
        return result.map(moviePersonMapper::toDto);
    }

    /**
     * Function to convert MoviePersonCriteria to a {@link Specifications}
     */
    private Specifications<MoviePerson> createSpecification(MoviePersonCriteria criteria) {
        Specifications<MoviePerson> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MoviePerson_.id));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildSpecification(criteria.getRole(), MoviePerson_.role));
            }
            if (criteria.getMovieId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMovieId(), MoviePerson_.movie, Movie_.id));
            }
            if (criteria.getPersonId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPersonId(), MoviePerson_.person, Person_.id));
            }
        }
        return specification;
    }

}
