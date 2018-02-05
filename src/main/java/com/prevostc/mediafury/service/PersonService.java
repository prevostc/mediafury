package com.prevostc.mediafury.service;

import com.prevostc.mediafury.domain.Person;
import com.prevostc.mediafury.repository.PersonRepository;
import com.prevostc.mediafury.service.dto.PersonDTO;
import com.prevostc.mediafury.service.mapper.PersonMapper;
import com.prevostc.mediafury.service.util.ImportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Service Implementation for managing Person.
 */
@Service
@Transactional
public class PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    private final ImportUtil<PersonDTO, Person, Long> importUtil;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.importUtil = new ImportUtil<>(personRepository, personMapper, (PersonDTO dto) ->
            personRepository.findOneByName(dto.getName())
        );
    }

    /**
     * Imports person data, inserts the entity if it does not already exists
     * given the functional key [title, year]
     * @param personDTO the entity to save
     * @return the newly created entity
     */
    public PersonDTO importData(PersonDTO personDTO) {
        return this.importUtil.importData(personDTO);
    }

    /**
     * Save a person.
     *
     * @param personDTO the entity to save
     * @return the persisted entity
     */
    public PersonDTO save(PersonDTO personDTO) {
        log.debug("Request to save Person : {}", personDTO);
        Person person = personMapper.toEntity(personDTO);
        person = personRepository.save(person);
        return personMapper.toDto(person);
    }

    /**
     * Get all the people.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PersonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return personRepository.findAll(pageable)
            .map(personMapper::toDto);
    }

    /**
     * Get one person by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PersonDTO findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        Person person = personRepository.findOne(id);
        return personMapper.toDto(person);
    }

    /**
     * Delete the person by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.delete(id);
    }
}
