package lvote.mprezes.student.agh.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import lvote.mprezes.student.agh.edu.pl.domain.EncryptionData;

import lvote.mprezes.student.agh.edu.pl.repository.EncryptionDataRepository;
import lvote.mprezes.student.agh.edu.pl.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EncryptionData.
 */
@RestController
@RequestMapping("/api")
public class EncryptionDataResource {

    private final Logger log = LoggerFactory.getLogger(EncryptionDataResource.class);

    private static final String ENTITY_NAME = "encryptionData";
        
    private final EncryptionDataRepository encryptionDataRepository;

    public EncryptionDataResource(EncryptionDataRepository encryptionDataRepository) {
        this.encryptionDataRepository = encryptionDataRepository;
    }

    /**
     * POST  /encryption-data : Create a new encryptionData.
     *
     * @param encryptionData the encryptionData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new encryptionData, or with status 400 (Bad Request) if the encryptionData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/encryption-data")
    @Timed
    public ResponseEntity<EncryptionData> createEncryptionData(@RequestBody EncryptionData encryptionData) throws URISyntaxException {
        log.debug("REST request to save EncryptionData : {}", encryptionData);
        if (encryptionData.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new encryptionData cannot already have an ID")).body(null);
        }
        EncryptionData result = encryptionDataRepository.save(encryptionData);
        return ResponseEntity.created(new URI("/api/encryption-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /encryption-data : Updates an existing encryptionData.
     *
     * @param encryptionData the encryptionData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated encryptionData,
     * or with status 400 (Bad Request) if the encryptionData is not valid,
     * or with status 500 (Internal Server Error) if the encryptionData couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/encryption-data")
    @Timed
    public ResponseEntity<EncryptionData> updateEncryptionData(@RequestBody EncryptionData encryptionData) throws URISyntaxException {
        log.debug("REST request to update EncryptionData : {}", encryptionData);
        if (encryptionData.getId() == null) {
            return createEncryptionData(encryptionData);
        }
        EncryptionData result = encryptionDataRepository.save(encryptionData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, encryptionData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /encryption-data : get all the encryptionData.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of encryptionData in body
     */
    @GetMapping("/encryption-data")
    @Timed
    public List<EncryptionData> getAllEncryptionData() {
        log.debug("REST request to get all EncryptionData");
        List<EncryptionData> encryptionData = encryptionDataRepository.findAll();
        return encryptionData;
    }

    /**
     * GET  /encryption-data/:id : get the "id" encryptionData.
     *
     * @param id the id of the encryptionData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the encryptionData, or with status 404 (Not Found)
     */
    @GetMapping("/encryption-data/{id}")
    @Timed
    public ResponseEntity<EncryptionData> getEncryptionData(@PathVariable Long id) {
        log.debug("REST request to get EncryptionData : {}", id);
        EncryptionData encryptionData = encryptionDataRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(encryptionData));
    }

    /**
     * DELETE  /encryption-data/:id : delete the "id" encryptionData.
     *
     * @param id the id of the encryptionData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/encryption-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteEncryptionData(@PathVariable Long id) {
        log.debug("REST request to delete EncryptionData : {}", id);
        encryptionDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
