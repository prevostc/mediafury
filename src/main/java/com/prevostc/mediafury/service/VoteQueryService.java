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

import com.prevostc.mediafury.domain.Vote;
import com.prevostc.mediafury.domain.*; // for static metamodels
import com.prevostc.mediafury.repository.VoteRepository;
import com.prevostc.mediafury.service.dto.VoteCriteria;

import com.prevostc.mediafury.service.dto.VoteDTO;
import com.prevostc.mediafury.service.mapper.VoteMapper;

/**
 * Service for executing complex queries for Vote entities in the database.
 * The main input is a {@link VoteCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VoteDTO} or a {@link Page} of {@link VoteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VoteQueryService extends QueryService<Vote> {

    private final Logger log = LoggerFactory.getLogger(VoteQueryService.class);


    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    public VoteQueryService(VoteRepository voteRepository, VoteMapper voteMapper) {
        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
    }

    /**
     * Return a {@link List} of {@link VoteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VoteDTO> findByCriteria(VoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Vote> specification = createSpecification(criteria);
        return voteMapper.toDto(voteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VoteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VoteDTO> findByCriteria(VoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Vote> specification = createSpecification(criteria);
        final Page<Vote> result = voteRepository.findAll(specification, page);
        return result.map(voteMapper::toDto);
    }

    /**
     * Function to convert VoteCriteria to a {@link Specifications}
     */
    private Specifications<Vote> createSpecification(VoteCriteria criteria) {
        Specifications<Vote> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Vote_.id));
            }
            if (criteria.getWinnerEloDiff() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWinnerEloDiff(), Vote_.winnerEloDiff));
            }
            if (criteria.getLoserEloDiff() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLoserEloDiff(), Vote_.loserEloDiff));
            }
            if (criteria.getWinnerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWinnerId(), Vote_.winner, Movie_.id));
            }
            if (criteria.getLoserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLoserId(), Vote_.loser, Movie_.id));
            }
        }
        return specification;
    }

}
