package com.prevostc.mediafury.service;

import com.prevostc.mediafury.domain.Vote;
import com.prevostc.mediafury.repository.VoteRepository;
import com.prevostc.mediafury.service.dto.VoteDTO;
import com.prevostc.mediafury.service.mapper.VoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Vote.
 */
@Service
@Transactional
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    public VoteService(VoteRepository voteRepository, VoteMapper voteMapper) {
        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
    }

    /**
     * Save a vote.
     *
     * @param voteDTO the entity to save
     * @return the persisted entity
     */
    public VoteDTO save(VoteDTO voteDTO) {
        log.debug("Request to save Vote : {}", voteDTO);
        Vote vote = voteMapper.toEntity(voteDTO);
        vote = voteRepository.save(vote);
        return voteMapper.toDto(vote);
    }

    /**
     * Get all the votes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Votes");
        return voteRepository.findAll(pageable)
            .map(voteMapper::toDto);
    }

    /**
     * Get one vote by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public VoteDTO findOne(Long id) {
        log.debug("Request to get Vote : {}", id);
        Vote vote = voteRepository.findOne(id);
        return voteMapper.toDto(vote);
    }

    /**
     * Delete the vote by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Vote : {}", id);
        voteRepository.delete(id);
    }
}
