package lvote.mprezes.student.agh.edu.pl.web.rest;

import lvote.mprezes.student.agh.edu.pl.LvoteApp;
import lvote.mprezes.student.agh.edu.pl.domain.Voting;
import lvote.mprezes.student.agh.edu.pl.repository.VotingRepository;
import lvote.mprezes.student.agh.edu.pl.web.rest.errors.ExceptionTranslator;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VotingResource REST controller.
 *
 * @see VotingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LvoteApp.class)
public class VotingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private VotingRepository votingRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVotingMockMvc;

    private Voting voting;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VotingResource votingResource = new VotingResource(votingRepository);
        this.restVotingMockMvc = MockMvcBuilders.standaloneSetup(votingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voting createEntity(EntityManager em) {
        Voting voting = new Voting()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return voting;
    }

    @Before
    public void initTest() {
        voting = createEntity(em);
    }

    @Test
    @Transactional
    public void createVoting() throws Exception {
        int databaseSizeBeforeCreate = votingRepository.findAll().size();

        // Create the Voting
        restVotingMockMvc.perform(post("/api/votings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voting)))
            .andExpect(status().isCreated());

        // Validate the Voting in the database
        List<Voting> votingList = votingRepository.findAll();
        assertThat(votingList).hasSize(databaseSizeBeforeCreate + 1);
        Voting testVoting = votingList.get(votingList.size() - 1);
        assertThat(testVoting.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVoting.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testVoting.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createVotingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = votingRepository.findAll().size();

        // Create the Voting with an existing ID
        voting.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVotingMockMvc.perform(post("/api/votings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voting)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Voting> votingList = votingRepository.findAll();
        assertThat(votingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = votingRepository.findAll().size();
        // set the field null
        voting.setStartDate(null);

        // Create the Voting, which fails.

        restVotingMockMvc.perform(post("/api/votings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voting)))
            .andExpect(status().isBadRequest());

        List<Voting> votingList = votingRepository.findAll();
        assertThat(votingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = votingRepository.findAll().size();
        // set the field null
        voting.setEndDate(null);

        // Create the Voting, which fails.

        restVotingMockMvc.perform(post("/api/votings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voting)))
            .andExpect(status().isBadRequest());

        List<Voting> votingList = votingRepository.findAll();
        assertThat(votingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVotings() throws Exception {
        // Initialize the database
        votingRepository.saveAndFlush(voting);

        // Get all the votingList
        restVotingMockMvc.perform(get("/api/votings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voting.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getVoting() throws Exception {
        // Initialize the database
        votingRepository.saveAndFlush(voting);

        // Get the voting
        restVotingMockMvc.perform(get("/api/votings/{id}", voting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(voting.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVoting() throws Exception {
        // Get the voting
        restVotingMockMvc.perform(get("/api/votings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVoting() throws Exception {
        // Initialize the database
        votingRepository.saveAndFlush(voting);
        int databaseSizeBeforeUpdate = votingRepository.findAll().size();

        // Update the voting
        Voting updatedVoting = votingRepository.findOne(voting.getId());
        updatedVoting
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restVotingMockMvc.perform(put("/api/votings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVoting)))
            .andExpect(status().isOk());

        // Validate the Voting in the database
        List<Voting> votingList = votingRepository.findAll();
        assertThat(votingList).hasSize(databaseSizeBeforeUpdate);
        Voting testVoting = votingList.get(votingList.size() - 1);
        assertThat(testVoting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVoting.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testVoting.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVoting() throws Exception {
        int databaseSizeBeforeUpdate = votingRepository.findAll().size();

        // Create the Voting

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVotingMockMvc.perform(put("/api/votings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voting)))
            .andExpect(status().isCreated());

        // Validate the Voting in the database
        List<Voting> votingList = votingRepository.findAll();
        assertThat(votingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVoting() throws Exception {
        // Initialize the database
        votingRepository.saveAndFlush(voting);
        int databaseSizeBeforeDelete = votingRepository.findAll().size();

        // Get the voting
        restVotingMockMvc.perform(delete("/api/votings/{id}", voting.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Voting> votingList = votingRepository.findAll();
        assertThat(votingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Voting.class);
        Voting voting1 = new Voting();
        voting1.setId(1L);
        Voting voting2 = new Voting();
        voting2.setId(voting1.getId());
        assertThat(voting1).isEqualTo(voting2);
        voting2.setId(2L);
        assertThat(voting1).isNotEqualTo(voting2);
        voting1.setId(null);
        assertThat(voting1).isNotEqualTo(voting2);
    }
}
