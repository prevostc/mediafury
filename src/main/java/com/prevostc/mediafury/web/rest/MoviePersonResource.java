package com.prevostc.mediafury.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.prevostc.mediafury.service.MoviePersonService;
import com.prevostc.mediafury.web.rest.errors.BadRequestAlertException;
import com.prevostc.mediafury.web.rest.util.HeaderUtil;
import com.prevostc.mediafury.web.rest.util.PaginationUtil;
import com.prevostc.mediafury.service.dto.MoviePersonDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MoviePerson.
 */
@RestController
@RequestMapping("/api")
public class MoviePersonResource {

    private final Logger log = LoggerFactory.getLogger(MoviePersonResource.class);

    private static final String ENTITY_NAME = "moviePerson";

    private final MoviePersonService moviePersonService;

    public MoviePersonResource(MoviePersonService moviePersonService) {
        this.moviePersonService = moviePersonService;
    }

    /**
     * POST  /movie-people : Create a new moviePerson.
     *
     * @param moviePersonDTO the moviePersonDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new moviePersonDTO, or with status 400 (Bad Request) if the moviePerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/movie-people")
    @Timed
    public ResponseEntity<MoviePersonDTO> createMoviePerson(@Valid @RequestBody MoviePersonDTO moviePersonDTO) throws URISyntaxException {
        log.debug("REST request to save MoviePerson : {}", moviePersonDTO);
        if (moviePersonDTO.getId() != null) {
            throw new BadRequestAlertException("A new moviePerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MoviePersonDTO result = moviePersonService.save(moviePersonDTO);
        return ResponseEntity.created(new URI("/api/movie-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movie-people : Updates an existing moviePerson.
     *
     * @param moviePersonDTO the moviePersonDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated moviePersonDTO,
     * or with status 400 (Bad Request) if the moviePersonDTO is not valid,
     * or with status 500 (Internal Server Error) if the moviePersonDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/movie-people")
    @Timed
    public ResponseEntity<MoviePersonDTO> updateMoviePerson(@Valid @RequestBody MoviePersonDTO moviePersonDTO) throws URISyntaxException {
        log.debug("REST request to update MoviePerson : {}", moviePersonDTO);
        if (moviePersonDTO.getId() == null) {
            return createMoviePerson(moviePersonDTO);
        }
        MoviePersonDTO result = moviePersonService.save(moviePersonDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, moviePersonDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movie-people : get all the moviePeople.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of moviePeople in body
     */
    @GetMapping("/movie-people")
    @Timed
    public ResponseEntity<List<MoviePersonDTO>> getAllMoviePeople(Pageable pageable) {
        log.debug("REST request to get a page of MoviePeople");
        Page<MoviePersonDTO> page = moviePersonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/movie-people");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /movie-people/:id : get the "id" moviePerson.
     *
     * @param id the id of the moviePersonDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the moviePersonDTO, or with status 404 (Not Found)
     */
    @GetMapping("/movie-people/{id}")
    @Timed
    public ResponseEntity<MoviePersonDTO> getMoviePerson(@PathVariable Long id) {
        log.debug("REST request to get MoviePerson : {}", id);
        MoviePersonDTO moviePersonDTO = moviePersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(moviePersonDTO));
    }

    /**
     * DELETE  /movie-people/:id : delete the "id" moviePerson.
     *
     * @param id the id of the moviePersonDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/movie-people/{id}")
    @Timed
    public ResponseEntity<Void> deleteMoviePerson(@PathVariable Long id) {
        log.debug("REST request to delete MoviePerson : {}", id);
        moviePersonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
