package com.prevostc.mediafury.web.rest;

import com.prevostc.mediafury.MediafuryApp;

import com.prevostc.mediafury.domain.MoviePerson;
import com.prevostc.mediafury.domain.Movie;
import com.prevostc.mediafury.domain.Person;
import com.prevostc.mediafury.repository.MoviePersonRepository;
import com.prevostc.mediafury.repository.MovieRepository;
import com.prevostc.mediafury.repository.PersonRepository;
import com.prevostc.mediafury.service.MoviePersonService;
import com.prevostc.mediafury.service.dto.MoviePersonDTO;
import com.prevostc.mediafury.service.mapper.MoviePersonMapper;
import com.prevostc.mediafury.web.rest.errors.ExceptionTranslator;
import com.prevostc.mediafury.service.dto.MoviePersonCriteria;
import com.prevostc.mediafury.service.MoviePersonQueryService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.prevostc.mediafury.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prevostc.mediafury.domain.enumeration.PersonRole;
/**
 * Test class for the MoviePersonResource REST controller.
 *
 * @see MoviePersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MediafuryApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MoviePersonResourceIntTest {

    public static final PersonRole DEFAULT_ROLE = PersonRole.WRITER;
    private static final PersonRole UPDATED_ROLE = PersonRole.ACTOR;

    @Autowired
    private MoviePersonRepository moviePersonRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MoviePersonMapper moviePersonMapper;

    @Autowired
    private MoviePersonService moviePersonService;

    @Autowired
    private MoviePersonQueryService moviePersonQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMoviePersonMockMvc;

    private MoviePerson moviePerson;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MoviePersonResource moviePersonResource = new MoviePersonResource(moviePersonService, moviePersonQueryService);
        this.restMoviePersonMockMvc = MockMvcBuilders.standaloneSetup(moviePersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoviePerson createEntity(EntityManager em) {
        MoviePerson moviePerson = new MoviePerson()
            .role(DEFAULT_ROLE);
        // Add required entity
        Movie movie = MovieResourceIntTest.createEntity(em);
        em.persist(movie);
        em.flush();
        moviePerson.setMovie(movie);
        // Add required entity
        Person person = PersonResourceIntTest.createEntity(em);
        em.persist(person);
        em.flush();
        moviePerson.setPerson(person);
        return moviePerson;
    }

    @Before
    public void initTest() {
        moviePerson = createEntity(em);
    }

    @Test
    @Transactional
    public void createMoviePerson() throws Exception {
        int databaseSizeBeforeCreate = moviePersonRepository.findAll().size();

        // Create the MoviePerson
        MoviePersonDTO moviePersonDTO = moviePersonMapper.toDto(moviePerson);
        restMoviePersonMockMvc.perform(post("/api/movie-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moviePersonDTO)))
            .andExpect(status().isCreated());

        // Validate the MoviePerson in the database
        List<MoviePerson> moviePersonList = moviePersonRepository.findAll();
        assertThat(moviePersonList).hasSize(databaseSizeBeforeCreate + 1);
        MoviePerson testMoviePerson = moviePersonList.get(moviePersonList.size() - 1);
        assertThat(testMoviePerson.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createMoviePersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = moviePersonRepository.findAll().size();

        // Create the MoviePerson with an existing ID
        moviePerson.setId(1L);
        MoviePersonDTO moviePersonDTO = moviePersonMapper.toDto(moviePerson);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoviePersonMockMvc.perform(post("/api/movie-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moviePersonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MoviePerson in the database
        List<MoviePerson> moviePersonList = moviePersonRepository.findAll();
        assertThat(moviePersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = moviePersonRepository.findAll().size();
        // set the field null
        moviePerson.setRole(null);

        // Create the MoviePerson, which fails.
        MoviePersonDTO moviePersonDTO = moviePersonMapper.toDto(moviePerson);

        restMoviePersonMockMvc.perform(post("/api/movie-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moviePersonDTO)))
            .andExpect(status().isBadRequest());

        List<MoviePerson> moviePersonList = moviePersonRepository.findAll();
        assertThat(moviePersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMoviePeople() throws Exception {
        // Initialize the database
        moviePersonRepository.saveAndFlush(moviePerson);

        // Get all the moviePersonList
        restMoviePersonMockMvc.perform(get("/api/movie-people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moviePerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    @Transactional
    public void getMoviePerson() throws Exception {
        // Initialize the database
        moviePersonRepository.saveAndFlush(moviePerson);

        // Get the moviePerson
        restMoviePersonMockMvc.perform(get("/api/movie-people/{id}", moviePerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(moviePerson.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getAllMoviePeopleByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        moviePersonRepository.saveAndFlush(moviePerson);

        // Get all the moviePersonList where role equals to DEFAULT_ROLE
        defaultMoviePersonShouldBeFound("role.equals=" + DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void getAllMoviePeopleByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        moviePersonRepository.saveAndFlush(moviePerson);

        // Get all the moviePersonList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultMoviePersonShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllMoviePeopleByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        moviePersonRepository.saveAndFlush(moviePerson);

        // Get all the moviePersonList where role is not null
        defaultMoviePersonShouldBeFound("role.specified=true");

        // Get all the moviePersonList where role is null
        defaultMoviePersonShouldNotBeFound("role.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviePeopleByMovieIsEqualToSomething() throws Exception {
        // Initialize the database
        Movie movie = MovieResourceIntTest.createEntity(em);
        em.persist(movie);
        em.flush();
        moviePerson.setMovie(movie);
        moviePersonRepository.saveAndFlush(moviePerson);
        Long movieId = movie.getId();

        // Get all the moviePersonList where movie equals to movieId
        defaultMoviePersonShouldBeFound("movieId.equals=" + movieId);

        // Get all the moviePersonList where movie equals to movieId + 1
        defaultMoviePersonShouldNotBeFound("movieId.equals=" + (movieId + 1));
    }


    @Test
    @Transactional
    public void getAllMoviePeopleByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        Person person = PersonResourceIntTest.createEntity(em);
        em.persist(person);
        em.flush();
        moviePerson.setPerson(person);
        moviePersonRepository.saveAndFlush(moviePerson);
        Long personId = person.getId();

        // Get all the moviePersonList where person equals to personId
        defaultMoviePersonShouldBeFound("personId.equals=" + personId);

        // Get all the moviePersonList where person equals to personId + 1
        defaultMoviePersonShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMoviePersonShouldBeFound(String filter) throws Exception {
        restMoviePersonMockMvc.perform(get("/api/movie-people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moviePerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMoviePersonShouldNotBeFound(String filter) throws Exception {
        restMoviePersonMockMvc.perform(get("/api/movie-people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMoviePerson() throws Exception {
        // Get the moviePerson
        restMoviePersonMockMvc.perform(get("/api/movie-people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMoviePerson() throws Exception {
        // Initialize the database
        moviePersonRepository.saveAndFlush(moviePerson);
        int databaseSizeBeforeUpdate = moviePersonRepository.findAll().size();

        // Update the moviePerson
        MoviePerson updatedMoviePerson = moviePersonRepository.findOne(moviePerson.getId());
        // Disconnect from session so that the updates on updatedMoviePerson are not directly saved in db
        em.detach(updatedMoviePerson);
        updatedMoviePerson
            .role(UPDATED_ROLE);
        MoviePersonDTO moviePersonDTO = moviePersonMapper.toDto(updatedMoviePerson);

        restMoviePersonMockMvc.perform(put("/api/movie-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moviePersonDTO)))
            .andExpect(status().isOk());

        // Validate the MoviePerson in the database
        List<MoviePerson> moviePersonList = moviePersonRepository.findAll();
        assertThat(moviePersonList).hasSize(databaseSizeBeforeUpdate);
        MoviePerson testMoviePerson = moviePersonList.get(moviePersonList.size() - 1);
        assertThat(testMoviePerson.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingMoviePerson() throws Exception {
        int databaseSizeBeforeUpdate = moviePersonRepository.findAll().size();

        // Create the MoviePerson
        MoviePersonDTO moviePersonDTO = moviePersonMapper.toDto(moviePerson);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMoviePersonMockMvc.perform(put("/api/movie-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moviePersonDTO)))
            .andExpect(status().isCreated());

        // Validate the MoviePerson in the database
        List<MoviePerson> moviePersonList = moviePersonRepository.findAll();
        assertThat(moviePersonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMoviePerson() throws Exception {
        // Initialize the database
        moviePersonRepository.saveAndFlush(moviePerson);
        int databaseSizeBeforeDelete = moviePersonRepository.findAll().size();

        // Get the moviePerson
        restMoviePersonMockMvc.perform(delete("/api/movie-people/{id}", moviePerson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MoviePerson> moviePersonList = moviePersonRepository.findAll();
        assertThat(moviePersonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoviePerson.class);
        MoviePerson moviePerson1 = new MoviePerson();
        moviePerson1.setId(1L);
        MoviePerson moviePerson2 = new MoviePerson();
        moviePerson2.setId(moviePerson1.getId());
        assertThat(moviePerson1).isEqualTo(moviePerson2);
        moviePerson2.setId(2L);
        assertThat(moviePerson1).isNotEqualTo(moviePerson2);
        moviePerson1.setId(null);
        assertThat(moviePerson1).isNotEqualTo(moviePerson2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoviePersonDTO.class);
        MoviePersonDTO moviePersonDTO1 = new MoviePersonDTO();
        moviePersonDTO1.setId(1L);
        MoviePersonDTO moviePersonDTO2 = new MoviePersonDTO();
        assertThat(moviePersonDTO1).isNotEqualTo(moviePersonDTO2);
        moviePersonDTO2.setId(moviePersonDTO1.getId());
        assertThat(moviePersonDTO1).isEqualTo(moviePersonDTO2);
        moviePersonDTO2.setId(2L);
        assertThat(moviePersonDTO1).isNotEqualTo(moviePersonDTO2);
        moviePersonDTO1.setId(null);
        assertThat(moviePersonDTO1).isNotEqualTo(moviePersonDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(moviePersonMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(moviePersonMapper.fromId(null)).isNull();
    }
}
