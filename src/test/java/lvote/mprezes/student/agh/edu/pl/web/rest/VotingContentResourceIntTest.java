package lvote.mprezes.student.agh.edu.pl.web.rest;

import lvote.mprezes.student.agh.edu.pl.LvoteApp;

import lvote.mprezes.student.agh.edu.pl.domain.VotingContent;
import lvote.mprezes.student.agh.edu.pl.repository.VotingContentRepository;
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
 * Test class for the VotingContentResource REST controller.
 *
 * @see VotingContentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LvoteApp.class)
public class VotingContentResourceIntTest {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    @Autowired
    private VotingContentRepository votingContentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVotingContentMockMvc;

    private VotingContent votingContent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VotingContentResource votingContentResource = new VotingContentResource(votingContentRepository);
        this.restVotingContentMockMvc = MockMvcBuilders.standaloneSetup(votingContentResource)
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
    public static VotingContent createEntity(EntityManager em) {
        VotingContent votingContent = new VotingContent()
            .question(DEFAULT_QUESTION);
        return votingContent;
    }

    @Before
    public void initTest() {
        votingContent = createEntity(em);
    }

    @Test
    @Transactional
    public void createVotingContent() throws Exception {
        int databaseSizeBeforeCreate = votingContentRepository.findAll().size();

        // Create the VotingContent
        restVotingContentMockMvc.perform(post("/api/voting-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(votingContent)))
            .andExpect(status().isCreated());

        // Validate the VotingContent in the database
        List<VotingContent> votingContentList = votingContentRepository.findAll();
        assertThat(votingContentList).hasSize(databaseSizeBeforeCreate + 1);
        VotingContent testVotingContent = votingContentList.get(votingContentList.size() - 1);
        assertThat(testVotingContent.getQuestion()).isEqualTo(DEFAULT_QUESTION);
    }

    @Test
    @Transactional
    public void createVotingContentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = votingContentRepository.findAll().size();

        // Create the VotingContent with an existing ID
        votingContent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVotingContentMockMvc.perform(post("/api/voting-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(votingContent)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<VotingContent> votingContentList = votingContentRepository.findAll();
        assertThat(votingContentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = votingContentRepository.findAll().size();
        // set the field null
        votingContent.setQuestion(null);

        // Create the VotingContent, which fails.

        restVotingContentMockMvc.perform(post("/api/voting-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(votingContent)))
            .andExpect(status().isBadRequest());

        List<VotingContent> votingContentList = votingContentRepository.findAll();
        assertThat(votingContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVotingContents() throws Exception {
        // Initialize the database
        votingContentRepository.saveAndFlush(votingContent);

        // Get all the votingContentList
        restVotingContentMockMvc.perform(get("/api/voting-contents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(votingContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())));
    }

    @Test
    @Transactional
    public void getVotingContent() throws Exception {
        // Initialize the database
        votingContentRepository.saveAndFlush(votingContent);

        // Get the votingContent
        restVotingContentMockMvc.perform(get("/api/voting-contents/{id}", votingContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(votingContent.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVotingContent() throws Exception {
        // Get the votingContent
        restVotingContentMockMvc.perform(get("/api/voting-contents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVotingContent() throws Exception {
        // Initialize the database
        votingContentRepository.saveAndFlush(votingContent);
        int databaseSizeBeforeUpdate = votingContentRepository.findAll().size();

        // Update the votingContent
        VotingContent updatedVotingContent = votingContentRepository.findOne(votingContent.getId());
        updatedVotingContent
            .question(UPDATED_QUESTION);

        restVotingContentMockMvc.perform(put("/api/voting-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVotingContent)))
            .andExpect(status().isOk());

        // Validate the VotingContent in the database
        List<VotingContent> votingContentList = votingContentRepository.findAll();
        assertThat(votingContentList).hasSize(databaseSizeBeforeUpdate);
        VotingContent testVotingContent = votingContentList.get(votingContentList.size() - 1);
        assertThat(testVotingContent.getQuestion()).isEqualTo(UPDATED_QUESTION);
    }

    @Test
    @Transactional
    public void updateNonExistingVotingContent() throws Exception {
        int databaseSizeBeforeUpdate = votingContentRepository.findAll().size();

        // Create the VotingContent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVotingContentMockMvc.perform(put("/api/voting-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(votingContent)))
            .andExpect(status().isCreated());

        // Validate the VotingContent in the database
        List<VotingContent> votingContentList = votingContentRepository.findAll();
        assertThat(votingContentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVotingContent() throws Exception {
        // Initialize the database
        votingContentRepository.saveAndFlush(votingContent);
        int databaseSizeBeforeDelete = votingContentRepository.findAll().size();

        // Get the votingContent
        restVotingContentMockMvc.perform(delete("/api/voting-contents/{id}", votingContent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VotingContent> votingContentList = votingContentRepository.findAll();
        assertThat(votingContentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VotingContent.class);
        VotingContent votingContent1 = new VotingContent();
        votingContent1.setId(1L);
        VotingContent votingContent2 = new VotingContent();
        votingContent2.setId(votingContent1.getId());
        assertThat(votingContent1).isEqualTo(votingContent2);
        votingContent2.setId(2L);
        assertThat(votingContent1).isNotEqualTo(votingContent2);
        votingContent1.setId(null);
        assertThat(votingContent1).isNotEqualTo(votingContent2);
    }
}
