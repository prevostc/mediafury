package com.prevostc.mediafury.web.rest;

import com.prevostc.mediafury.MediafuryApp;

import com.prevostc.mediafury.domain.Vote;
import com.prevostc.mediafury.domain.Movie;
import com.prevostc.mediafury.domain.Movie;
import com.prevostc.mediafury.repository.VoteRepository;
import com.prevostc.mediafury.service.VoteService;
import com.prevostc.mediafury.service.dto.VoteDTO;
import com.prevostc.mediafury.service.mapper.VoteMapper;
import com.prevostc.mediafury.web.rest.errors.ExceptionTranslator;
import com.prevostc.mediafury.service.dto.VoteCriteria;
import com.prevostc.mediafury.service.VoteQueryService;

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
import java.util.List;

import static com.prevostc.mediafury.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VoteResource REST controller.
 *
 * @see VoteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MediafuryApp.class)
public class VoteResourceIntTest {

    private static final Integer DEFAULT_WINNER_ELO_DIFF = 1;
    private static final Integer UPDATED_WINNER_ELO_DIFF = 2;

    private static final Integer DEFAULT_LOSER_ELO_DIFF = 1;
    private static final Integer UPDATED_LOSER_ELO_DIFF = 2;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteQueryService voteQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVoteMockMvc;

    private Vote vote;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VoteResource voteResource = new VoteResource(voteService, voteQueryService);
        this.restVoteMockMvc = MockMvcBuilders.standaloneSetup(voteResource)
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
    public static Vote createEntity(EntityManager em) {
        Vote vote = new Vote()
            .winnerEloDiff(DEFAULT_WINNER_ELO_DIFF)
            .loserEloDiff(DEFAULT_LOSER_ELO_DIFF);
        // Add required entity
        Movie winner = MovieResourceIntTest.createEntity(em);
        em.persist(winner);
        em.flush();
        vote.setWinner(winner);
        // Add required entity
        Movie loser = MovieResourceIntTest.createEntity(em);
        em.persist(loser);
        em.flush();
        vote.setLoser(loser);
        return vote;
    }

    @Before
    public void initTest() {
        vote = createEntity(em);
    }

    @Test
    @Transactional
    public void createVote() throws Exception {
        int databaseSizeBeforeCreate = voteRepository.findAll().size();

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);
        restVoteMockMvc.perform(post("/api/votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteDTO)))
            .andExpect(status().isCreated());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeCreate + 1);
        Vote testVote = voteList.get(voteList.size() - 1);
        assertThat(testVote.getWinnerEloDiff()).isEqualTo(DEFAULT_WINNER_ELO_DIFF);
        assertThat(testVote.getLoserEloDiff()).isEqualTo(DEFAULT_LOSER_ELO_DIFF);
    }

    @Test
    @Transactional
    public void createVoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = voteRepository.findAll().size();

        // Create the Vote with an existing ID
        vote.setId(1L);
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoteMockMvc.perform(post("/api/votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVotes() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList
        restVoteMockMvc.perform(get("/api/votes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vote.getId().intValue())))
            .andExpect(jsonPath("$.[*].winnerEloDiff").value(hasItem(DEFAULT_WINNER_ELO_DIFF)))
            .andExpect(jsonPath("$.[*].loserEloDiff").value(hasItem(DEFAULT_LOSER_ELO_DIFF)));
    }

    @Test
    @Transactional
    public void getVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get the vote
        restVoteMockMvc.perform(get("/api/votes/{id}", vote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vote.getId().intValue()))
            .andExpect(jsonPath("$.winnerEloDiff").value(DEFAULT_WINNER_ELO_DIFF))
            .andExpect(jsonPath("$.loserEloDiff").value(DEFAULT_LOSER_ELO_DIFF));
    }

    @Test
    @Transactional
    public void getAllVotesByWinnerEloDiffIsEqualToSomething() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where winnerEloDiff equals to DEFAULT_WINNER_ELO_DIFF
        defaultVoteShouldBeFound("winnerEloDiff.equals=" + DEFAULT_WINNER_ELO_DIFF);

        // Get all the voteList where winnerEloDiff equals to UPDATED_WINNER_ELO_DIFF
        defaultVoteShouldNotBeFound("winnerEloDiff.equals=" + UPDATED_WINNER_ELO_DIFF);
    }

    @Test
    @Transactional
    public void getAllVotesByWinnerEloDiffIsInShouldWork() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where winnerEloDiff in DEFAULT_WINNER_ELO_DIFF or UPDATED_WINNER_ELO_DIFF
        defaultVoteShouldBeFound("winnerEloDiff.in=" + DEFAULT_WINNER_ELO_DIFF + "," + UPDATED_WINNER_ELO_DIFF);

        // Get all the voteList where winnerEloDiff equals to UPDATED_WINNER_ELO_DIFF
        defaultVoteShouldNotBeFound("winnerEloDiff.in=" + UPDATED_WINNER_ELO_DIFF);
    }

    @Test
    @Transactional
    public void getAllVotesByWinnerEloDiffIsNullOrNotNull() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where winnerEloDiff is not null
        defaultVoteShouldBeFound("winnerEloDiff.specified=true");

        // Get all the voteList where winnerEloDiff is null
        defaultVoteShouldNotBeFound("winnerEloDiff.specified=false");
    }

    @Test
    @Transactional
    public void getAllVotesByWinnerEloDiffIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where winnerEloDiff greater than or equals to DEFAULT_WINNER_ELO_DIFF
        defaultVoteShouldBeFound("winnerEloDiff.greaterOrEqualThan=" + DEFAULT_WINNER_ELO_DIFF);

        // Get all the voteList where winnerEloDiff greater than or equals to UPDATED_WINNER_ELO_DIFF
        defaultVoteShouldNotBeFound("winnerEloDiff.greaterOrEqualThan=" + UPDATED_WINNER_ELO_DIFF);
    }

    @Test
    @Transactional
    public void getAllVotesByWinnerEloDiffIsLessThanSomething() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where winnerEloDiff less than or equals to DEFAULT_WINNER_ELO_DIFF
        defaultVoteShouldNotBeFound("winnerEloDiff.lessThan=" + DEFAULT_WINNER_ELO_DIFF);

        // Get all the voteList where winnerEloDiff less than or equals to UPDATED_WINNER_ELO_DIFF
        defaultVoteShouldBeFound("winnerEloDiff.lessThan=" + UPDATED_WINNER_ELO_DIFF);
    }


    @Test
    @Transactional
    public void getAllVotesByLoserEloDiffIsEqualToSomething() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where loserEloDiff equals to DEFAULT_LOSER_ELO_DIFF
        defaultVoteShouldBeFound("loserEloDiff.equals=" + DEFAULT_LOSER_ELO_DIFF);

        // Get all the voteList where loserEloDiff equals to UPDATED_LOSER_ELO_DIFF
        defaultVoteShouldNotBeFound("loserEloDiff.equals=" + UPDATED_LOSER_ELO_DIFF);
    }

    @Test
    @Transactional
    public void getAllVotesByLoserEloDiffIsInShouldWork() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where loserEloDiff in DEFAULT_LOSER_ELO_DIFF or UPDATED_LOSER_ELO_DIFF
        defaultVoteShouldBeFound("loserEloDiff.in=" + DEFAULT_LOSER_ELO_DIFF + "," + UPDATED_LOSER_ELO_DIFF);

        // Get all the voteList where loserEloDiff equals to UPDATED_LOSER_ELO_DIFF
        defaultVoteShouldNotBeFound("loserEloDiff.in=" + UPDATED_LOSER_ELO_DIFF);
    }

    @Test
    @Transactional
    public void getAllVotesByLoserEloDiffIsNullOrNotNull() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where loserEloDiff is not null
        defaultVoteShouldBeFound("loserEloDiff.specified=true");

        // Get all the voteList where loserEloDiff is null
        defaultVoteShouldNotBeFound("loserEloDiff.specified=false");
    }

    @Test
    @Transactional
    public void getAllVotesByLoserEloDiffIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where loserEloDiff greater than or equals to DEFAULT_LOSER_ELO_DIFF
        defaultVoteShouldBeFound("loserEloDiff.greaterOrEqualThan=" + DEFAULT_LOSER_ELO_DIFF);

        // Get all the voteList where loserEloDiff greater than or equals to UPDATED_LOSER_ELO_DIFF
        defaultVoteShouldNotBeFound("loserEloDiff.greaterOrEqualThan=" + UPDATED_LOSER_ELO_DIFF);
    }

    @Test
    @Transactional
    public void getAllVotesByLoserEloDiffIsLessThanSomething() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList where loserEloDiff less than or equals to DEFAULT_LOSER_ELO_DIFF
        defaultVoteShouldNotBeFound("loserEloDiff.lessThan=" + DEFAULT_LOSER_ELO_DIFF);

        // Get all the voteList where loserEloDiff less than or equals to UPDATED_LOSER_ELO_DIFF
        defaultVoteShouldBeFound("loserEloDiff.lessThan=" + UPDATED_LOSER_ELO_DIFF);
    }


    @Test
    @Transactional
    public void getAllVotesByWinnerIsEqualToSomething() throws Exception {
        // Initialize the database
        Movie winner = MovieResourceIntTest.createEntity(em);
        em.persist(winner);
        em.flush();
        vote.setWinner(winner);
        voteRepository.saveAndFlush(vote);
        Long winnerId = winner.getId();

        // Get all the voteList where winner equals to winnerId
        defaultVoteShouldBeFound("winnerId.equals=" + winnerId);

        // Get all the voteList where winner equals to winnerId + 1
        defaultVoteShouldNotBeFound("winnerId.equals=" + (winnerId + 1));
    }


    @Test
    @Transactional
    public void getAllVotesByLoserIsEqualToSomething() throws Exception {
        // Initialize the database
        Movie loser = MovieResourceIntTest.createEntity(em);
        em.persist(loser);
        em.flush();
        vote.setLoser(loser);
        voteRepository.saveAndFlush(vote);
        Long loserId = loser.getId();

        // Get all the voteList where loser equals to loserId
        defaultVoteShouldBeFound("loserId.equals=" + loserId);

        // Get all the voteList where loser equals to loserId + 1
        defaultVoteShouldNotBeFound("loserId.equals=" + (loserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultVoteShouldBeFound(String filter) throws Exception {
        restVoteMockMvc.perform(get("/api/votes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vote.getId().intValue())))
            .andExpect(jsonPath("$.[*].winnerEloDiff").value(hasItem(DEFAULT_WINNER_ELO_DIFF)))
            .andExpect(jsonPath("$.[*].loserEloDiff").value(hasItem(DEFAULT_LOSER_ELO_DIFF)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultVoteShouldNotBeFound(String filter) throws Exception {
        restVoteMockMvc.perform(get("/api/votes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingVote() throws Exception {
        // Get the vote
        restVoteMockMvc.perform(get("/api/votes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();

        // Update the vote
        Vote updatedVote = voteRepository.findOne(vote.getId());
        // Disconnect from session so that the updates on updatedVote are not directly saved in db
        em.detach(updatedVote);
        updatedVote
            .winnerEloDiff(UPDATED_WINNER_ELO_DIFF)
            .loserEloDiff(UPDATED_LOSER_ELO_DIFF);
        VoteDTO voteDTO = voteMapper.toDto(updatedVote);

        restVoteMockMvc.perform(put("/api/votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteDTO)))
            .andExpect(status().isOk());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
        Vote testVote = voteList.get(voteList.size() - 1);
        assertThat(testVote.getWinnerEloDiff()).isEqualTo(UPDATED_WINNER_ELO_DIFF);
        assertThat(testVote.getLoserEloDiff()).isEqualTo(UPDATED_LOSER_ELO_DIFF);
    }

    @Test
    @Transactional
    public void updateNonExistingVote() throws Exception {
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();

        // Create the Vote
        VoteDTO voteDTO = voteMapper.toDto(vote);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVoteMockMvc.perform(put("/api/votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteDTO)))
            .andExpect(status().isCreated());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);
        int databaseSizeBeforeDelete = voteRepository.findAll().size();

        // Get the vote
        restVoteMockMvc.perform(delete("/api/votes/{id}", vote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vote.class);
        Vote vote1 = new Vote();
        vote1.setId(1L);
        Vote vote2 = new Vote();
        vote2.setId(vote1.getId());
        assertThat(vote1).isEqualTo(vote2);
        vote2.setId(2L);
        assertThat(vote1).isNotEqualTo(vote2);
        vote1.setId(null);
        assertThat(vote1).isNotEqualTo(vote2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoteDTO.class);
        VoteDTO voteDTO1 = new VoteDTO();
        voteDTO1.setId(1L);
        VoteDTO voteDTO2 = new VoteDTO();
        assertThat(voteDTO1).isNotEqualTo(voteDTO2);
        voteDTO2.setId(voteDTO1.getId());
        assertThat(voteDTO1).isEqualTo(voteDTO2);
        voteDTO2.setId(2L);
        assertThat(voteDTO1).isNotEqualTo(voteDTO2);
        voteDTO1.setId(null);
        assertThat(voteDTO1).isNotEqualTo(voteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(voteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(voteMapper.fromId(null)).isNull();
    }
}
