package lvote.mprezes.student.agh.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import lvote.mprezes.student.agh.edu.pl.domain.VotingAnswer;
import lvote.mprezes.student.agh.edu.pl.repository.VotingAnswerRepository;
import lvote.mprezes.student.agh.edu.pl.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VotingAnswer.
 */
@RestController
@RequestMapping("/api")
public class VotingAnswerResource {

    private final Logger log = LoggerFactory.getLogger(VotingAnswerResource.class);

    private static final String ENTITY_NAME = "votingAnswer";

    private final VotingAnswerRepository votingAnswerRepository;

    public VotingAnswerResource(VotingAnswerRepository votingAnswerRepository) {
        this.votingAnswerRepository = votingAnswerRepository;
    }

    /**
     * POST  /voting-answers : Create a new votingAnswer.
     *
     * @param votingAnswer
     * 		the votingAnswer to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new votingAnswer, or with status 400 (Bad Request) if the votingAnswer has already an ID
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/voting-answers")
    @Timed
    public ResponseEntity<VotingAnswer> createVotingAnswer(@Valid @RequestBody VotingAnswer votingAnswer) throws URISyntaxException {
        log.debug("REST request to save VotingAnswer : {}", votingAnswer);
        if (votingAnswer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new votingAnswer cannot already have an ID")).body(null);
        }
        VotingAnswer result = votingAnswerRepository.save(votingAnswer);
        return ResponseEntity.created(new URI("/api/voting-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /voting-answers : Updates an existing votingAnswer.
     *
     * @param votingAnswer
     * 		the votingAnswer to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated votingAnswer,
     * or with status 400 (Bad Request) if the votingAnswer is not valid,
     * or with status 500 (Internal Server Error) if the votingAnswer couldnt be updated
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/voting-answers")
    @Timed
    public ResponseEntity<VotingAnswer> updateVotingAnswer(@Valid @RequestBody VotingAnswer votingAnswer) throws URISyntaxException {
        log.debug("REST request to update VotingAnswer : {}", votingAnswer);
        if (votingAnswer.getId() == null) {
            return createVotingAnswer(votingAnswer);
        }
        VotingAnswer result = votingAnswerRepository.save(votingAnswer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, votingAnswer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /voting-answers : get all the votingAnswers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of votingAnswers in body
     */
    @GetMapping("/voting-answers")
    @Timed
    public List<VotingAnswer> getAllVotingAnswers() {
        log.debug("REST request to get all VotingAnswers");
        List<VotingAnswer> votingAnswers = votingAnswerRepository.findAll();
        return votingAnswers;
    }

    /**
     * GET  /voting-answers/:id : get the "id" votingAnswer.
     *
     * @param id
     * 		the id of the votingAnswer to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the votingAnswer, or with status 404 (Not Found)
     */
    @GetMapping("/voting-answers/{id}")
    @Timed
    public ResponseEntity<VotingAnswer> getVotingAnswer(@PathVariable Long id) {
        log.debug("REST request to get VotingAnswer : {}", id);
        VotingAnswer votingAnswer = votingAnswerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(votingAnswer));
    }

    /**
     * DELETE  /voting-answers/:id : delete the "id" votingAnswer.
     *
     * @param id
     * 		the id of the votingAnswer to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/voting-answers/{id}")
    @Timed
    public ResponseEntity<Void> deleteVotingAnswer(@PathVariable Long id) {
        log.debug("REST request to delete VotingAnswer : {}", id);
        votingAnswerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
