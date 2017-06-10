package lvote.mprezes.student.agh.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import lvote.mprezes.student.agh.edu.pl.domain.VotingContent;
import lvote.mprezes.student.agh.edu.pl.repository.VotingContentRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing VotingContent.
 */
@RestController
@RequestMapping("/api")
public class VotingContentResource {

    private final Logger log = LoggerFactory.getLogger(VotingContentResource.class);

    private static final String ENTITY_NAME = "votingContent";

    private final VotingContentRepository votingContentRepository;

    public VotingContentResource(VotingContentRepository votingContentRepository) {
        this.votingContentRepository = votingContentRepository;
    }

    /**
     * POST  /voting-contents : Create a new votingContent.
     *
     * @param votingContent the votingContent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new votingContent, or with status 400 (Bad Request) if the votingContent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/voting-contents")
    @Timed
    public ResponseEntity<VotingContent> createVotingContent(@Valid @RequestBody VotingContent votingContent) throws URISyntaxException {
        log.debug("REST request to save VotingContent : {}", votingContent);
        if (votingContent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new votingContent cannot already have an ID")).body(null);
        }
        VotingContent result = votingContentRepository.save(votingContent);
        return ResponseEntity.created(new URI("/api/voting-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /voting-contents : Updates an existing votingContent.
     *
     * @param votingContent the votingContent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated votingContent,
     * or with status 400 (Bad Request) if the votingContent is not valid,
     * or with status 500 (Internal Server Error) if the votingContent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/voting-contents")
    @Timed
    public ResponseEntity<VotingContent> updateVotingContent(@Valid @RequestBody VotingContent votingContent) throws URISyntaxException {
        log.debug("REST request to update VotingContent : {}", votingContent);
        if (votingContent.getId() == null) {
            return createVotingContent(votingContent);
        }
        VotingContent result = votingContentRepository.save(votingContent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, votingContent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /voting-contents : get all the votingContents.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of votingContents in body
     */
    @GetMapping("/voting-contents")
    @Timed
    public List<VotingContent> getAllVotingContents(@RequestParam(required = false) String filter) {
        if ("voting-is-null".equals(filter)) {
            log.debug("REST request to get all VotingContents where voting is null");
            return StreamSupport
                .stream(votingContentRepository.findAll().spliterator(), false)
                .filter(votingContent -> votingContent.getVoting() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all VotingContents");
        List<VotingContent> votingContents = votingContentRepository.findAll();
        return votingContents;
    }

    /**
     * GET  /voting-contents/:id : get the "id" votingContent.
     *
     * @param id the id of the votingContent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the votingContent, or with status 404 (Not Found)
     */
    @GetMapping("/voting-contents/{id}")
    @Timed
    public ResponseEntity<VotingContent> getVotingContent(@PathVariable Long id) {
        log.debug("REST request to get VotingContent : {}", id);
        VotingContent votingContent = votingContentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(votingContent));
    }

    /**
     * DELETE  /voting-contents/:id : delete the "id" votingContent.
     *
     * @param id the id of the votingContent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/voting-contents/{id}")
    @Timed
    public ResponseEntity<Void> deleteVotingContent(@PathVariable Long id) {
        log.debug("REST request to delete VotingContent : {}", id);
        votingContentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
