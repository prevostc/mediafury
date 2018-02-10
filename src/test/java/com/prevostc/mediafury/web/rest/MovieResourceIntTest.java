package com.prevostc.mediafury.web.rest;

import com.prevostc.mediafury.MediafuryApp;

import com.prevostc.mediafury.domain.Movie;
import com.prevostc.mediafury.domain.MoviePerson;
import com.prevostc.mediafury.domain.Category;
import com.prevostc.mediafury.repository.MovieRepository;
import com.prevostc.mediafury.service.MovieService;
import com.prevostc.mediafury.service.dto.MovieDTO;
import com.prevostc.mediafury.service.mapper.MovieMapper;
import com.prevostc.mediafury.web.rest.errors.ExceptionTranslator;
import com.prevostc.mediafury.service.dto.MovieCriteria;
import com.prevostc.mediafury.service.MovieQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.prevostc.mediafury.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MovieResource REST controller.
 *
 * @see MovieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MediafuryApp.class)
public class MovieResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1888;
    private static final Integer UPDATED_YEAR = 1889;

    private static final String DEFAULT_PLOT = "AAAAAAAAAA";
    private static final String UPDATED_PLOT = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ELO = 1;
    private static final Integer UPDATED_ELO = 2;

    private static final String DEFAULT_CATEGORY_NAME = "Category1";

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieQueryService movieQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMovieMockMvc;

    private Movie movie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MovieResource movieResource = new MovieResource(movieService, movieQueryService);
        this.restMovieMockMvc = MockMvcBuilders.standaloneSetup(movieResource)
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
    public static Movie createEntity(EntityManager em) {
        Movie movie = new Movie()
            .title(DEFAULT_TITLE)
            .year(DEFAULT_YEAR)
            .plot(DEFAULT_PLOT)
            .imageUrl(DEFAULT_IMAGE_URL)
            .elo(DEFAULT_ELO);

        movie.setCategories(new HashSet<>(Arrays.asList(new Category(DEFAULT_CATEGORY_NAME))));
        return movie;
    }

    @Before
    public void initTest() {
        movie = createEntity(em);
    }

    @Test
    @Transactional
    public void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testMovie.getPlot()).isEqualTo(DEFAULT_PLOT);
        assertThat(testMovie.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testMovie.getElo()).isEqualTo(DEFAULT_ELO);
    }

    @Test
    @Transactional
    public void createMovieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie with an existing ID
        movie.setId(1L);
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setTitle(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].plot").value(hasItem(DEFAULT_PLOT.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].elo").value(hasItem(DEFAULT_ELO)));
    }

    @Test
    @Transactional
    public void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", movie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.plot").value(DEFAULT_PLOT.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.elo").value(DEFAULT_ELO))
            .andExpect(jsonPath("$.categories").isArray())
            .andExpect(jsonPath("$.categories").value("{}"))
        ;
    }

    @Test
    @Transactional
    public void getRandomMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/random"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(header().string("Cache-Control", "no-cache"))
        ;
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title equals to DEFAULT_TITLE
        defaultMovieShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMovieShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title is not null
        defaultMovieShouldBeFound("title.specified=true");

        // Get all the movieList where title is null
        defaultMovieShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year equals to DEFAULT_YEAR
        defaultMovieShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the movieList where year equals to UPDATED_YEAR
        defaultMovieShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultMovieShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the movieList where year equals to UPDATED_YEAR
        defaultMovieShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year is not null
        defaultMovieShouldBeFound("year.specified=true");

        // Get all the movieList where year is null
        defaultMovieShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year greater than or equals to DEFAULT_YEAR
        defaultMovieShouldBeFound("year.greaterOrEqualThan=" + DEFAULT_YEAR);

        // Get all the movieList where year greater than or equals to (DEFAULT_YEAR + 1)
        defaultMovieShouldNotBeFound("year.greaterOrEqualThan=" + (DEFAULT_YEAR + 1));
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year less than or equals to DEFAULT_YEAR
        defaultMovieShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the movieList where year less than or equals to (DEFAULT_YEAR + 1)
        defaultMovieShouldBeFound("year.lessThan=" + (DEFAULT_YEAR + 1));
    }


    @Test
    @Transactional
    public void getAllMoviesByPlotIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where plot equals to DEFAULT_PLOT
        defaultMovieShouldBeFound("plot.equals=" + DEFAULT_PLOT);

        // Get all the movieList where plot equals to UPDATED_PLOT
        defaultMovieShouldNotBeFound("plot.equals=" + UPDATED_PLOT);
    }

    @Test
    @Transactional
    public void getAllMoviesByPlotIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where plot in DEFAULT_PLOT or UPDATED_PLOT
        defaultMovieShouldBeFound("plot.in=" + DEFAULT_PLOT + "," + UPDATED_PLOT);

        // Get all the movieList where plot equals to UPDATED_PLOT
        defaultMovieShouldNotBeFound("plot.in=" + UPDATED_PLOT);
    }

    @Test
    @Transactional
    public void getAllMoviesByPlotIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where plot is not null
        defaultMovieShouldBeFound("plot.specified=true");

        // Get all the movieList where plot is null
        defaultMovieShouldNotBeFound("plot.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultMovieShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the movieList where imageUrl equals to UPDATED_IMAGE_URL
        defaultMovieShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllMoviesByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultMovieShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the movieList where imageUrl equals to UPDATED_IMAGE_URL
        defaultMovieShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllMoviesByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where imageUrl is not null
        defaultMovieShouldBeFound("imageUrl.specified=true");

        // Get all the movieList where imageUrl is null
        defaultMovieShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByEloIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where elo equals to DEFAULT_ELO
        defaultMovieShouldBeFound("elo.equals=" + DEFAULT_ELO);

        // Get all the movieList where elo equals to UPDATED_ELO
        defaultMovieShouldNotBeFound("elo.equals=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    public void getAllMoviesByEloIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where elo in DEFAULT_ELO or UPDATED_ELO
        defaultMovieShouldBeFound("elo.in=" + DEFAULT_ELO + "," + UPDATED_ELO);

        // Get all the movieList where elo equals to UPDATED_ELO
        defaultMovieShouldNotBeFound("elo.in=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    public void getAllMoviesByEloIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where elo is not null
        defaultMovieShouldBeFound("elo.specified=true");

        // Get all the movieList where elo is null
        defaultMovieShouldNotBeFound("elo.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByEloIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where elo greater than or equals to DEFAULT_ELO
        defaultMovieShouldBeFound("elo.greaterOrEqualThan=" + DEFAULT_ELO);

        // Get all the movieList where elo greater than or equals to UPDATED_ELO
        defaultMovieShouldNotBeFound("elo.greaterOrEqualThan=" + UPDATED_ELO);
    }

    @Test
    @Transactional
    public void getAllMoviesByEloIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where elo less than or equals to DEFAULT_ELO
        defaultMovieShouldNotBeFound("elo.lessThan=" + DEFAULT_ELO);

        // Get all the movieList where elo less than or equals to UPDATED_ELO
        defaultMovieShouldBeFound("elo.lessThan=" + UPDATED_ELO);
    }


    @Test
    @Transactional
    public void getAllMoviesByMoviePersonIsEqualToSomething() throws Exception {
        // Initialize the database
        MoviePerson moviePerson = MoviePersonResourceIntTest.createEntity(em);
        em.persist(moviePerson);
        em.flush();
        movie.addMoviePerson(moviePerson);
        movieRepository.saveAndFlush(movie);
        Long moviePersonId = moviePerson.getId();

        // Get all the movieList where moviePerson equals to moviePersonId
        defaultMovieShouldBeFound("moviePersonId.equals=" + moviePersonId);

        // Get all the movieList where moviePerson equals to moviePersonId + 1
        defaultMovieShouldNotBeFound("moviePersonId.equals=" + (moviePersonId + 1));
    }


    @Test
    @Transactional
    public void getAllMoviesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        movie.addCategory(category);
        movieRepository.saveAndFlush(movie);
        Long categoryId = category.getId();

        // Get all the movieList where category equals to categoryId
        defaultMovieShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the movieList where category equals to categoryId + 1
        defaultMovieShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMovieShouldBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].plot").value(hasItem(DEFAULT_PLOT.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].elo").value(hasItem(DEFAULT_ELO)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMovieShouldNotBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        Movie updatedMovie = movieRepository.findOne(movie.getId());
        // Disconnect from session so that the updates on updatedMovie are not directly saved in db
        em.detach(updatedMovie);
        updatedMovie
            .title(UPDATED_TITLE)
            .year(UPDATED_YEAR)
            .plot(UPDATED_PLOT)
            .imageUrl(UPDATED_IMAGE_URL)
            .elo(UPDATED_ELO);
        MovieDTO movieDTO = movieMapper.toDto(updatedMovie);

        restMovieMockMvc.perform(put("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testMovie.getPlot()).isEqualTo(UPDATED_PLOT);
        assertThat(testMovie.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testMovie.getElo()).isEqualTo(UPDATED_ELO);
    }

    @Test
    @Transactional
    public void updateNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMovieMockMvc.perform(put("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);
        int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Get the movie
        restMovieMockMvc.perform(delete("/api/movies/{id}", movie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Movie.class);
        Movie movie1 = new Movie();
        movie1.setId(1L);
        Movie movie2 = new Movie();
        movie2.setId(movie1.getId());
        assertThat(movie1).isEqualTo(movie2);
        movie2.setId(2L);
        assertThat(movie1).isNotEqualTo(movie2);
        movie1.setId(null);
        assertThat(movie1).isNotEqualTo(movie2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovieDTO.class);
        MovieDTO movieDTO1 = new MovieDTO();
        movieDTO1.setId(1L);
        MovieDTO movieDTO2 = new MovieDTO();
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
        movieDTO2.setId(movieDTO1.getId());
        assertThat(movieDTO1).isEqualTo(movieDTO2);
        movieDTO2.setId(2L);
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
        movieDTO1.setId(null);
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(movieMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(movieMapper.fromId(null)).isNull();
    }
}
