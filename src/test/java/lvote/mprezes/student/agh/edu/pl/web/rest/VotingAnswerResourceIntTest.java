package lvote.mprezes.student.agh.edu.pl.web.rest;

import lvote.mprezes.student.agh.edu.pl.LvoteApp;
import lvote.mprezes.student.agh.edu.pl.domain.VotingAnswer;
import lvote.mprezes.student.agh.edu.pl.repository.VotingAnswerRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VotingAnswerResource REST controller.
 *
 * @see VotingAnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LvoteApp.class)
public class VotingAnswerResourceIntTest {

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    @Autowired
    private VotingAnswerRepository votingAnswerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVotingAnswerMockMvc;

    private VotingAnswer votingAnswer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VotingAnswerResource votingAnswerResource = new VotingAnswerResource(votingAnswerRepository);
        this.restVotingAnswerMockMvc = MockMvcBuilders.standaloneSetup(votingAnswerResource)
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
    public static VotingAnswer createEntity(EntityManager em) {
        VotingAnswer votingAnswer = new VotingAnswer()
            .answer(DEFAULT_ANSWER);
        return votingAnswer;
    }

    @Before
    public void initTest() {
        votingAnswer = createEntity(em);
    }

    @Test
    @Transactional
    public void createVotingAnswer() throws Exception {
        int databaseSizeBeforeCreate = votingAnswerRepository.findAll().size();

        // Create the VotingAnswer
        restVotingAnswerMockMvc.perform(post("/api/voting-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(votingAnswer)))
            .andExpect(status().isCreated());

        // Validate the VotingAnswer in the database
        List<VotingAnswer> votingAnswerList = votingAnswerRepository.findAll();
        assertThat(votingAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        VotingAnswer testVotingAnswer = votingAnswerList.get(votingAnswerList.size() - 1);
        assertThat(testVotingAnswer.getAnswer()).isEqualTo(DEFAULT_ANSWER);
    }

    @Test
    @Transactional
    public void createVotingAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = votingAnswerRepository.findAll().size();

        // Create the VotingAnswer with an existing ID
        votingAnswer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVotingAnswerMockMvc.perform(post("/api/voting-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(votingAnswer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<VotingAnswer> votingAnswerList = votingAnswerRepository.findAll();
        assertThat(votingAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = votingAnswerRepository.findAll().size();
        // set the field null
        votingAnswer.setAnswer(null);

        // Create the VotingAnswer, which fails.

        restVotingAnswerMockMvc.perform(post("/api/voting-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(votingAnswer)))
            .andExpect(status().isBadRequest());

        List<VotingAnswer> votingAnswerList = votingAnswerRepository.findAll();
        assertThat(votingAnswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVotingAnswers() throws Exception {
        // Initialize the database
        votingAnswerRepository.saveAndFlush(votingAnswer);

        // Get all the votingAnswerList
        restVotingAnswerMockMvc.perform(get("/api/voting-answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(votingAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())));
    }

    @Test
    @Transactional
    public void getVotingAnswer() throws Exception {
        // Initialize the database
        votingAnswerRepository.saveAndFlush(votingAnswer);

        // Get the votingAnswer
        restVotingAnswerMockMvc.perform(get("/api/voting-answers/{id}", votingAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(votingAnswer.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVotingAnswer() throws Exception {
        // Get the votingAnswer
        restVotingAnswerMockMvc.perform(get("/api/voting-answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVotingAnswer() throws Exception {
        // Initialize the database
        votingAnswerRepository.saveAndFlush(votingAnswer);
        int databaseSizeBeforeUpdate = votingAnswerRepository.findAll().size();

        // Update the votingAnswer
        VotingAnswer updatedVotingAnswer = votingAnswerRepository.findOne(votingAnswer.getId());
        updatedVotingAnswer
            .answer(UPDATED_ANSWER);

        restVotingAnswerMockMvc.perform(put("/api/voting-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVotingAnswer)))
            .andExpect(status().isOk());

        // Validate the VotingAnswer in the database
        List<VotingAnswer> votingAnswerList = votingAnswerRepository.findAll();
        assertThat(votingAnswerList).hasSize(databaseSizeBeforeUpdate);
        VotingAnswer testVotingAnswer = votingAnswerList.get(votingAnswerList.size() - 1);
        assertThat(testVotingAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    @Transactional
    public void updateNonExistingVotingAnswer() throws Exception {
        int databaseSizeBeforeUpdate = votingAnswerRepository.findAll().size();

        // Create the VotingAnswer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVotingAnswerMockMvc.perform(put("/api/voting-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(votingAnswer)))
            .andExpect(status().isCreated());

        // Validate the VotingAnswer in the database
        List<VotingAnswer> votingAnswerList = votingAnswerRepository.findAll();
        assertThat(votingAnswerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVotingAnswer() throws Exception {
        // Initialize the database
        votingAnswerRepository.saveAndFlush(votingAnswer);
        int databaseSizeBeforeDelete = votingAnswerRepository.findAll().size();

        // Get the votingAnswer
        restVotingAnswerMockMvc.perform(delete("/api/voting-answers/{id}", votingAnswer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VotingAnswer> votingAnswerList = votingAnswerRepository.findAll();
        assertThat(votingAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VotingAnswer.class);
        VotingAnswer votingAnswer1 = new VotingAnswer();
        votingAnswer1.setId(1L);
        VotingAnswer votingAnswer2 = new VotingAnswer();
        votingAnswer2.setId(votingAnswer1.getId());
        assertThat(votingAnswer1).isEqualTo(votingAnswer2);
        votingAnswer2.setId(2L);
        assertThat(votingAnswer1).isNotEqualTo(votingAnswer2);
        votingAnswer1.setId(null);
        assertThat(votingAnswer1).isNotEqualTo(votingAnswer2);
    }
}
