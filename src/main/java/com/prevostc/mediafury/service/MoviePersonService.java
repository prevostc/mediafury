package com.prevostc.mediafury.service;

import com.prevostc.mediafury.domain.MoviePerson;
import com.prevostc.mediafury.repository.MoviePersonRepository;
import com.prevostc.mediafury.service.dto.MoviePersonDTO;
import com.prevostc.mediafury.service.mapper.MoviePersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MoviePerson.
 */
@Service
@Transactional
public class MoviePersonService {

    private final Logger log = LoggerFactory.getLogger(MoviePersonService.class);

    private final MoviePersonRepository moviePersonRepository;

    private final MoviePersonMapper moviePersonMapper;

    public MoviePersonService(MoviePersonRepository moviePersonRepository, MoviePersonMapper moviePersonMapper) {
        this.moviePersonRepository = moviePersonRepository;
        this.moviePersonMapper = moviePersonMapper;
    }

    /**
     * Save a moviePerson.
     *
     * @param moviePersonDTO the entity to save
     * @return the persisted entity
     */
    public MoviePersonDTO save(MoviePersonDTO moviePersonDTO) {
        log.debug("Request to save MoviePerson : {}", moviePersonDTO);
        MoviePerson moviePerson = moviePersonMapper.toEntity(moviePersonDTO);
        moviePerson = moviePersonRepository.save(moviePerson);
        return moviePersonMapper.toDto(moviePerson);
    }

    /**
     * Get all the moviePeople.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MoviePersonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MoviePeople");
        return moviePersonRepository.findAll(pageable)
            .map(moviePersonMapper::toDto);
    }

    /**
     * Get one moviePerson by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MoviePersonDTO findOne(Long id) {
        log.debug("Request to get MoviePerson : {}", id);
        MoviePerson moviePerson = moviePersonRepository.findOne(id);
        return moviePersonMapper.toDto(moviePerson);
    }

    /**
     * Delete the moviePerson by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MoviePerson : {}", id);
        moviePersonRepository.delete(id);
    }
}
