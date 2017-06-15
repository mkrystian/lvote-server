package lvote.mprezes.student.agh.edu.pl.web.rest;

import lvote.mprezes.student.agh.edu.pl.LvoteApp;
import lvote.mprezes.student.agh.edu.pl.domain.EncryptionData;
import lvote.mprezes.student.agh.edu.pl.repository.EncryptionDataRepository;
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
 * Test class for the EncryptionDataResource REST controller.
 *
 * @see EncryptionDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LvoteApp.class)
public class EncryptionDataResourceIntTest {

    private static final String DEFAULT_PRIVATE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PRIVATE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLIC_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PUBLIC_KEY = "BBBBBBBBBB";

    @Autowired
    private EncryptionDataRepository encryptionDataRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEncryptionDataMockMvc;

    private EncryptionData encryptionData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EncryptionDataResource encryptionDataResource = new EncryptionDataResource(encryptionDataRepository);
        this.restEncryptionDataMockMvc = MockMvcBuilders.standaloneSetup(encryptionDataResource)
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
    public static EncryptionData createEntity(EntityManager em) {
        EncryptionData encryptionData = new EncryptionData()
            .privateKey(DEFAULT_PRIVATE_KEY)
            .publicKey(DEFAULT_PUBLIC_KEY);
        return encryptionData;
    }

    @Before
    public void initTest() {
        encryptionData = createEntity(em);
    }

    @Test
    @Transactional
    public void createEncryptionData() throws Exception {
        int databaseSizeBeforeCreate = encryptionDataRepository.findAll().size();

        // Create the EncryptionData
        restEncryptionDataMockMvc.perform(post("/api/encryption-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(encryptionData)))
            .andExpect(status().isCreated());

        // Validate the EncryptionData in the database
        List<EncryptionData> encryptionDataList = encryptionDataRepository.findAll();
        assertThat(encryptionDataList).hasSize(databaseSizeBeforeCreate + 1);
        EncryptionData testEncryptionData = encryptionDataList.get(encryptionDataList.size() - 1);
        assertThat(testEncryptionData.getPrivateKey()).isEqualTo(DEFAULT_PRIVATE_KEY);
        assertThat(testEncryptionData.getPublicKey()).isEqualTo(DEFAULT_PUBLIC_KEY);
    }

    @Test
    @Transactional
    public void createEncryptionDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = encryptionDataRepository.findAll().size();

        // Create the EncryptionData with an existing ID
        encryptionData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEncryptionDataMockMvc.perform(post("/api/encryption-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(encryptionData)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EncryptionData> encryptionDataList = encryptionDataRepository.findAll();
        assertThat(encryptionDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEncryptionData() throws Exception {
        // Initialize the database
        encryptionDataRepository.saveAndFlush(encryptionData);

        // Get all the encryptionDataList
        restEncryptionDataMockMvc.perform(get("/api/encryption-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(encryptionData.getId().intValue())))
            .andExpect(jsonPath("$.[*].privateKey").value(hasItem(DEFAULT_PRIVATE_KEY.toString())))
            .andExpect(jsonPath("$.[*].publicKey").value(hasItem(DEFAULT_PUBLIC_KEY.toString())));
    }

    @Test
    @Transactional
    public void getEncryptionData() throws Exception {
        // Initialize the database
        encryptionDataRepository.saveAndFlush(encryptionData);

        // Get the encryptionData
        restEncryptionDataMockMvc.perform(get("/api/encryption-data/{id}", encryptionData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(encryptionData.getId().intValue()))
            .andExpect(jsonPath("$.privateKey").value(DEFAULT_PRIVATE_KEY.toString()))
            .andExpect(jsonPath("$.publicKey").value(DEFAULT_PUBLIC_KEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEncryptionData() throws Exception {
        // Get the encryptionData
        restEncryptionDataMockMvc.perform(get("/api/encryption-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEncryptionData() throws Exception {
        // Initialize the database
        encryptionDataRepository.saveAndFlush(encryptionData);
        int databaseSizeBeforeUpdate = encryptionDataRepository.findAll().size();

        // Update the encryptionData
        EncryptionData updatedEncryptionData = encryptionDataRepository.findOne(encryptionData.getId());
        updatedEncryptionData
            .privateKey(UPDATED_PRIVATE_KEY)
            .publicKey(UPDATED_PUBLIC_KEY);

        restEncryptionDataMockMvc.perform(put("/api/encryption-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEncryptionData)))
            .andExpect(status().isOk());

        // Validate the EncryptionData in the database
        List<EncryptionData> encryptionDataList = encryptionDataRepository.findAll();
        assertThat(encryptionDataList).hasSize(databaseSizeBeforeUpdate);
        EncryptionData testEncryptionData = encryptionDataList.get(encryptionDataList.size() - 1);
        assertThat(testEncryptionData.getPrivateKey()).isEqualTo(UPDATED_PRIVATE_KEY);
        assertThat(testEncryptionData.getPublicKey()).isEqualTo(UPDATED_PUBLIC_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingEncryptionData() throws Exception {
        int databaseSizeBeforeUpdate = encryptionDataRepository.findAll().size();

        // Create the EncryptionData

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEncryptionDataMockMvc.perform(put("/api/encryption-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(encryptionData)))
            .andExpect(status().isCreated());

        // Validate the EncryptionData in the database
        List<EncryptionData> encryptionDataList = encryptionDataRepository.findAll();
        assertThat(encryptionDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEncryptionData() throws Exception {
        // Initialize the database
        encryptionDataRepository.saveAndFlush(encryptionData);
        int databaseSizeBeforeDelete = encryptionDataRepository.findAll().size();

        // Get the encryptionData
        restEncryptionDataMockMvc.perform(delete("/api/encryption-data/{id}", encryptionData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EncryptionData> encryptionDataList = encryptionDataRepository.findAll();
        assertThat(encryptionDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EncryptionData.class);
        EncryptionData encryptionData1 = new EncryptionData();
        encryptionData1.setId(1L);
        EncryptionData encryptionData2 = new EncryptionData();
        encryptionData2.setId(encryptionData1.getId());
        assertThat(encryptionData1).isEqualTo(encryptionData2);
        encryptionData2.setId(2L);
        assertThat(encryptionData1).isNotEqualTo(encryptionData2);
        encryptionData1.setId(null);
        assertThat(encryptionData1).isNotEqualTo(encryptionData2);
    }
}
