package com.prevostc.mediafury.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prevostc.mediafury.security.AuthoritiesConstants;
import com.prevostc.mediafury.service.VoteService;
import com.prevostc.mediafury.web.rest.errors.BadRequestAlertException;
import com.prevostc.mediafury.web.rest.util.HeaderUtil;
import com.prevostc.mediafury.web.rest.util.PaginationUtil;
import com.prevostc.mediafury.service.dto.VoteDTO;
import com.prevostc.mediafury.service.dto.VoteCriteria;
import com.prevostc.mediafury.service.VoteQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Vote.
 */
@RestController
@RequestMapping("/api")
public class VoteResource {

    private final Logger log = LoggerFactory.getLogger(VoteResource.class);

    private static final String ENTITY_NAME = "vote";

    private final VoteService voteService;

    private final VoteQueryService voteQueryService;

    public VoteResource(VoteService voteService, VoteQueryService voteQueryService) {
        this.voteService = voteService;
        this.voteQueryService = voteQueryService;
    }

    /**
     * POST  /votes : Create a new vote.
     *
     * @param voteDTO the voteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new voteDTO, or with status 400 (Bad Request) if the vote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/votes")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<VoteDTO> createVote(@Valid @RequestBody VoteDTO voteDTO) throws URISyntaxException {
        log.debug("REST request to save Vote : {}", voteDTO);
        if (voteDTO.getId() != null) {
            throw new BadRequestAlertException("A new vote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoteDTO result = voteService.save(voteDTO);
        return ResponseEntity.created(new URI("/api/votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /votes : Updates an existing vote.
     *
     * @param voteDTO the voteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated voteDTO,
     * or with status 400 (Bad Request) if the voteDTO is not valid,
     * or with status 500 (Internal Server Error) if the voteDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/votes")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<VoteDTO> updateVote(@Valid @RequestBody VoteDTO voteDTO) throws URISyntaxException {
        log.debug("REST request to update Vote : {}", voteDTO);
        if (voteDTO.getId() == null) {
            return createVote(voteDTO);
        }
        VoteDTO result = voteService.save(voteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, voteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /votes : get all the votes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of votes in body
     */
    @GetMapping("/votes")
    @Timed
    public ResponseEntity<List<VoteDTO>> getAllVotes(VoteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Votes by criteria: {}", criteria);
        Page<VoteDTO> page = voteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/votes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /votes/:id : get the "id" vote.
     *
     * @param id the id of the voteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the voteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/votes/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<VoteDTO> getVote(@PathVariable Long id) {
        log.debug("REST request to get Vote : {}", id);
        VoteDTO voteDTO = voteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(voteDTO));
    }

    /**
     * DELETE  /votes/:id : delete the "id" vote.
     *
     * @param id the id of the voteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/votes/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteVote(@PathVariable Long id) {
        log.debug("REST request to delete Vote : {}", id);
        voteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /votes/fury : Create a vote, compute ELO diffs and apply them
     *
     * @param voteDTO the voteDTO to process
     * @return the ResponseEntity with status 201 (Created) and with body the new voteDTO, or with status 400 (Bad Request) if the vote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/votes/fury")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<VoteDTO> createFury(@Valid @RequestBody VoteDTO voteDTO) throws URISyntaxException {
        log.debug("REST request to fury: {}", voteDTO);

        VoteDTO result = voteService.computeEloDiffUpdateMoviesAndSave(voteDTO);

        return ResponseEntity.created(new URI("/api/votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
